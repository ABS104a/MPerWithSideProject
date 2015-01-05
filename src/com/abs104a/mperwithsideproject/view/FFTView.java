package com.abs104a.mperwithsideproject.view;

import com.abs104a.mperwithsideproject.R;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

/**
 * FFTと生波形に関して，Visualizerとしての描画を行うView
 * @author Kouki
 *
 */
public class FFTView extends View {
	// ----------------------------
	// 定数
	// ----------------------------
	// ピーク値
	private static float FFT_PEAK_VALUE = (float) 181.019335984f;
	// デシベルの下限
	private static float DISPLAY_MINIMUM_DB = -35;
	// 最小周波数
	private static float DISPLAY_MINIMUM_HZ = 35;
	// 最大周波数
	private static float DISPLAY_MAXIMUM_HZ = 20000;
	// バンド表示の最小周波数
	private static float BAND_MINIMUM_HZ = 40;
	// バンド表示の最大周波数
	private static float BAND_MAXIMUM_HZ = 18000;
	// バンドのデフォルト数
	private static int BAND_DEFAULT_NUMBER = 20;
	// バンドの内側の表示オフセット
	private static float BAND_INNER_OFFSET = 4;
	
	private static float FFT_BAND_SCALE = 1.5f;
	// FFTデータの描画色ID
	private static int FFT_DATA_SHADER_START_COLOR_ID = R.color.Denim;
	private static int FFT_DATA_SHADER_END_COLOR_ID = android.R.color.holo_blue_bright;
	// 対数グリッドの色ID
	private static int LOG_GRID_COLOR_ID = R.color.Denim;
	
	// ----------------------------
	// 変数
	// ----------------------------
	// Viewのサイズ
	private int currentWidth_ = -1;
	private int currentHeight_ = -1;
	// FFTデータ
	private byte[] fftData_;
	// FFTデータの色
	private Paint fftDataPaint_;

	// 対数グリッドの色
	private Paint waveLinePaint_;
	// サンプリングレート
	private int samplingRate_;
	// 表示するバンドの数
	private int bandNumber_;
	// バンドの矩形
	private RectF[] bandRects_;

	// 対数の区間あたりの幅 (e.g. 10^1から10^2と，10^2から10^3の描画幅は一緒)
	private float logBlockWidth_;
	// X方向の表示オフセット
	private float logOffsetX_;
	// バンド全体のX方向の表示域
	private int bandRegionMinX_;
	private int bandRegionMaxX_;
	// 個々のバンドの幅
	private int bandWidth_;
	// バンドのデータ
	private float[] bandFftData_;
	// データ表示用のシェーダ
	private LinearGradient fftDataShader_;
	
	// Waveデータ
	private byte[] waveData_;
	//最大データ数
	private float maxLevel_;
 
	// コンストラクタ
	public FFTView(Context context) {
		super(context);		
		init();
	}
	public FFTView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}
	public FFTView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		init();
	}
 
	// 初期化
	private void init() {
		// 変数設定
		bandNumber_ = BAND_DEFAULT_NUMBER;
		// バーの領域確保
		bandRects_ = new RectF[bandNumber_];
		for(int i = 0; i < bandNumber_; ++i){
			bandRects_[i] = new RectF();
		}
		// データを格納する配列
		bandFftData_ = new float[bandNumber_];
		// ペイントの設定
		fftDataPaint_ = new Paint();
		fftDataPaint_.setStrokeWidth(2f);
		fftDataPaint_.setAntiAlias(true);
		
		waveLinePaint_ = new Paint();
		waveLinePaint_.setStrokeWidth(3f);
		waveLinePaint_.setAntiAlias(true);
		waveLinePaint_.setColor(getResources().getColor(LOG_GRID_COLOR_ID));
		waveLinePaint_.setStyle(Paint.Style.STROKE);
		
		maxLevel_ = 0;
	}
 
	@Override
	public void onWindowFocusChanged(boolean hasWindowFocus) {
		super.onWindowFocusChanged(hasWindowFocus);
		// Viewの高さ，幅が取れるのでそれらに依存した計算を行う
		calculateViewSizeDependedData();
	}
	
	// Viewのサイズを基に対数グリッドとバーの座標を計算
	private void calculateViewSizeDependedData() {
		//画面の大きさを取得
		currentHeight_ = getHeight();
		currentWidth_ = getWidth();
	
		// 対数の区間あたりの幅
		logBlockWidth_ = (float) (getWidth() / (Math.log10(DISPLAY_MAXIMUM_HZ) - Math.log10(DISPLAY_MINIMUM_HZ)));
		// X方向の表示オフセット
		logOffsetX_ = (float) (Math.log10(DISPLAY_MINIMUM_HZ) * logBlockWidth_);
 
		int top = getTop();
		
		// 各々のバンドの座標を計算
		bandRegionMinX_ = (int) (Math.log10(BAND_MINIMUM_HZ) * logBlockWidth_ - logOffsetX_);
		bandRegionMaxX_ = (int) (Math.log10(BAND_MAXIMUM_HZ) * logBlockWidth_ - logOffsetX_);
		bandWidth_ = (int) (bandRegionMaxX_ - bandRegionMinX_) / bandNumber_;
		int bottom = getBottom();
		for(int i = 0; i < bandNumber_; ++i){
			bandRects_[i].bottom = bottom;
			bandRects_[i].top = bottom;
			bandRects_[i].left = bandRegionMinX_ + (bandWidth_ * i) + BAND_INNER_OFFSET;
			bandRects_[i].right = bandRects_[i].left + bandWidth_ - BAND_INNER_OFFSET;
		}
		
		// シェーダーを設定
		int color0 = getResources().getColor(FFT_DATA_SHADER_START_COLOR_ID);
		int color1 = getResources().getColor(FFT_DATA_SHADER_END_COLOR_ID);
		fftDataShader_ = new LinearGradient(0, bottom, 0, top, color0, color1, android.graphics.Shader.TileMode.CLAMP);
		fftDataPaint_.setShader(fftDataShader_);
		
		android.util.Log.v("calculateViewSizeDependedData", currentWidth_ + " / " + currentHeight_);
	}
	
	// サンプリングレート
	public void setSamplingRate(int samplingRateInMilliHz) {
		samplingRate_ = samplingRateInMilliHz / 1000;
	}
	public int getSamplingRate() {
		return samplingRate_;
	}
 
	// 更新
	public void updateFFT(byte[] bytes) {
		fftData_ = bytes;
		waveData_ = null;
		invalidate();
	}
	
	// 更新
		public void updateWave(byte[] bytes) {
			waveData_ = bytes;
			fftData_ = null;
			invalidate();
		}
 
	// 描画
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		// Viewのサイズ変更があった場合，再計算
		if(currentWidth_ != getWidth() || currentHeight_ != getHeight()){
			calculateViewSizeDependedData();
		}
		// 波形データがない場合には処理を行わない
		if (fftData_ == null && waveData_ == null){
			return;
		}else if(fftData_ != null){
			// FFTデータの描画
			drawFft(canvas);
		}else{
			drawWave(canvas);
		}
		
		
	}
	
	// FFTの内容を描画
	private void drawFft(Canvas canvas) {
		// Viewのサイズ情報
		int top = getTop();
		int height = getHeight();
		// データの個数
		int fftNum = fftData_.length / 2;
		// データをバンドに加工して表示
		// データの初期化
		for(int i = 0; i < bandNumber_; ++i){
			bandFftData_[i] = 0;
		}
		// データを順に見ていく
		for(int i = 1; i < fftNum; i++){
			// 注目しているデータの周波数
			float frequency = (float) (i * samplingRate_ / 2) / fftNum;
			// 表示位置から対応するバンドのインデックスを計算
			float x = (float) (Math.log10(frequency) * logBlockWidth_) + logOffsetX_;
			//android.util.Log.v("x",x+" / " + logOffsetX_);
			int index = (int) (x - bandRegionMinX_) / bandWidth_;
			if(index >= 0 && index < bandNumber_){
				// 振幅スペクトルを計算
				float amplitude = FFT_BAND_SCALE * (float)Math.sqrt(Math.pow((float)fftData_[i * 2], 2) + Math.pow((float)fftData_[i * 2 + 1], 2));
				if(amplitude > 0 ){
					// 対応する区間で一番大きい値を取っておく
					if(bandFftData_[index] < amplitude) {
						bandFftData_[index] = amplitude;
					}
				}
			}
		}
		// バーの高さを計算して描画
		for(int i = 0; i < bandNumber_; ++i){
			float db = (float) (20.0f * Math.log10(bandFftData_[i]/FFT_PEAK_VALUE));
			float y = (float) (top - db / -DISPLAY_MINIMUM_DB * height );
			bandRects_[i].top = y;
			canvas.drawRect(bandRects_[i], fftDataPaint_);
		}
	}
	
	/**
	 * 生波形を描画する．
	 */
	private void drawWave(Canvas canvas){
		//画面の高さ
		int height = getHeight();
		//画面の幅
		int width = getWidth();
		
		//縦の中心位置を計算
		int center_vartical = (height / 2);
		//データの個数
		int dataNum = waveData_.length;
		
		//描画するpathを作成
		Path path = new Path();
		//初期位置に移動
		path.moveTo(0, center_vartical);
		
		for(int pos = 0;pos < dataNum;pos += 8){
			
			float data = (float)waveData_[pos];
			
			if(data > maxLevel_)
				maxLevel_ = data;
			
			if(data >= 0){
				data -= maxLevel_;
			}else{
				data += maxLevel_;
			}

			float draw_x = ((float)width / (float)dataNum) * pos;
			float draw_y = center_vartical + (data / maxLevel_) * center_vartical;

			path.lineTo(draw_x, draw_y);
		}
		canvas.drawPath(path, waveLinePaint_);
		
	}
}
