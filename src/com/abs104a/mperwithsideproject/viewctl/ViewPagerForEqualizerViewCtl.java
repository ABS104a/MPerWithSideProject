package com.abs104a.mperwithsideproject.viewctl;

import android.app.Service;
import android.content.Context;
import android.media.audiofx.Equalizer;
import android.media.audiofx.Visualizer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.Spinner;
import android.widget.TextView;

import com.abs104a.mperwithsideproject.R;
import com.abs104a.mperwithsideproject.music.EqualizerItem;
import com.abs104a.mperwithsideproject.music.MusicPlayerWithQueue;
import com.abs104a.mperwithsideproject.utl.MusicUtils;
import com.abs104a.mperwithsideproject.view.FFTView;
import com.abs104a.mperwithsideproject.viewctl.listener.MyOnDataCaptureImpl;

/**
 * EqualizerViewの生成とコントロールを行うクラス
 * @author Kouki-Mobile
 *
 */
public class ViewPagerForEqualizerViewCtl {
	
	private static Visualizer mVisualizer = null;
	private static boolean isFFT = false;
	private static boolean isWave = true;
	
	private static FFTView fftView = null;

	/**
	 * Viewの生成
	 * @param mService	親サービスのコンテキスト
	 * @param mpwpl		音楽playerのコントロール
	 * @return			生成したView
	 */
	public static final View createView(
			final Service mService,
			final MusicPlayerWithQueue mpwpl) 
	{
		// TODO 動作の登録
		//TODO Visualizerの設定
		//データの初期化
		fftView = null;
		
		//Layoutの生成
		LayoutInflater layoutInflater = LayoutInflater.from(mService);
		final ScrollView mView = (ScrollView)layoutInflater.inflate(R.layout.equalizer, null);
		
		//Visualizerの生成
		fftView  = (FFTView)mView.findViewById(R.id.fftview_equalizer);
		createMusicVisualizer(mService);
		
		//EqualizerのView反映
		final Equalizer eq = setEqualizerForView(mView);
		
		
		//>-----チェックボックスの設定-----------------------------------------<//
		CheckBox chBox = (CheckBox)mView.findViewById(R.id.checkBox_equalizer);
		chBox.setChecked(mpwpl.getEqualizer());
		chBox.setOnCheckedChangeListener(new OnCheckedChangeListener(){

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				android.util.Log.v("Equalizer","isEnabled : " + isChecked);
				mpwpl.setEqualizer(isChecked);
				//TODO 反映する
				setEqualizerForView(mView);
			}
			
		});
		//>-----チェックボックスの設定終わり-------------------------------------<//
		
		//>-----Spinnerの設定------------------------------------------<//
		final Spinner mSpinner = (Spinner)mView.findViewById(R.id.spinner_equalizer);
		//Equalizerのプリセットをロード
		final short numOfPresets = eq.getNumberOfPresets();
		//現在のプリセット値
		short currentPreset = mpwpl.getEqualizerCursor();
		
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(mService, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		
		for(short i = 0;i < numOfPresets;i++){
			final short presetIndex = i;
			String presetName = eq.getPresetName(presetIndex);
	        // アイテムを追加します
	        adapter.add(presetName);
		}
		adapter.add(mView.getContext().getString(R.string.equalizer_custom));
		//adapterの設定
		mSpinner.setAdapter(adapter);
		mSpinner.setSelection(currentPreset);
		
		mSpinner.setOnItemSelectedListener(new OnItemSelectedListener(){

			@Override
			public void onItemSelected(AdapterView<?> arg0, View view,
					int position, long id) {
				if(numOfPresets != position)
					mpwpl.getEqualizerInstance().usePreset((short)position);
				mpwpl.setEqualizerCursor((short)position);
				//バーの反映する
				setEqualizerForView(mView);
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {	}
			
		});
		
		return mView;
	}
	
	private static final Equalizer setEqualizerForView(final View mView){
		final LinearLayout layoutView = (LinearLayout)mView.findViewById(R.id.linearLayout_equalizer_child);
		//Viewのクリア
		layoutView.removeAllViews();
		LayoutInflater layoutInflater = LayoutInflater.from(mView.getContext());
		final MusicPlayerWithQueue mpwpl = MusicUtils.getMusicController(mView.getContext());
		final Equalizer eq = mpwpl.getEqualizerInstance();
		// 調整を行うことのできる中心周波数の数を確認
		final short bands = eq.getNumberOfBands();
		// 設定出来るレベルの下限、上限を確認
		final short minEQLevel = eq.getBandLevelRange()[0];
		final short maxEQLevel = eq.getBandLevelRange()[1];
		//中心レベルを計算
		//final short centerEQLevel = (short) ((maxEQLevel - minEQLevel) / 2);
		
		final EqualizerItem[] items;
		if(mpwpl.getEqualizerItem() == null || mpwpl.getEqualizerItem().length != bands){
			items = new EqualizerItem[(int)bands];
			for(short i = 0;i < bands;i++)
				items[i] = new EqualizerItem((short) (eq.getCenterFreq(i)), (short) 0);
			mpwpl.setEqualizerItem(items);
		}else{
			items = mpwpl.getEqualizerItem();
		}
		
		//customEqualizerの場合
		if(mpwpl.getEqualizerCursor() >= eq.getNumberOfPresets()){
			for(short i = 0;i < bands;i++){
				eq.setBandLevel(i, items[i].getLevel());
			}
		}else{
			eq.usePreset(mpwpl.getEqualizerCursor());
			for(short i = 0;i < bands;i++){
				items[i].setBand((short) eq.getCenterFreq(i));
				items[i].setLevel((short) eq.getBandLevel(i));
			}
		}
		
		//>-----Equalizerのバーについての設定-----------------------------<//
		for(short i = 0;i < bands;i++){
			final int index = (int)i;
			final short band = (short) eq.getCenterFreq(i);
			//指定バンド幅の数だけViewを作成する．
			View eqView = layoutInflater.inflate(R.layout.equalizer_row, null);
			final TextView currentText = (TextView)eqView.findViewById(R.id.textView_equalizer_row_current);
			SeekBar seekBar = (SeekBar)eqView.findViewById(R.id.seekBar_equalizer_row);
			TextView maxText = (TextView)eqView.findViewById(R.id.textView_equalizer_row_max);
			currentText.setText(eq.getBandLevel((short)index) + "");
			
			seekBar.setMax((int)(maxEQLevel - minEQLevel));
			seekBar.setProgress((int) eq.getBandLevel((short)index) - minEQLevel);
			seekBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener(){

				@Override
				public void onProgressChanged(SeekBar seekBar, int progress,
						boolean fromUser) {
					if(fromUser){
						currentText.setText((progress + minEQLevel) + "");
						short newLevel = (short) (progress + minEQLevel);
						//android.util.Log.v("Equalizer","NewLevel : " + newLevel);
						Equalizer eqr = mpwpl.getEqualizerInstance();
						eqr.setBandLevel((short)index, newLevel);
						items[index].setLevel(newLevel);
						
						final Spinner mSpinner = (Spinner)mView.findViewById(R.id.spinner_equalizer);
						if(mSpinner != null && 
								mSpinner.getAdapter() != null &&
								mSpinner.getAdapter().getCount() >= eq.getNumberOfPresets()){
							mSpinner.setSelection(eq.getNumberOfPresets());
							mpwpl.setEqualizerCursor(eq.getNumberOfPresets());
						}
					}
				}

				@Override
				public void onStartTrackingTouch(SeekBar seekBar) {	}

				@Override
				public void onStopTrackingTouch(SeekBar seekBar) {
					mpwpl.writeQueue();
				}
				
			});
			maxText.setText(String.valueOf(band));
			layoutView.addView(eqView);
		}
		//>-----Equalizerのバーについての設定-----------------------------<//
		
		//>-----スイッチのバーについての設定-----------------------------<//
		final Button switchButton = (Button)mView.findViewById(R.id.button_equalizer_switch);
		switchButton.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				if(mVisualizer != null && fftView != null){
					mVisualizer.setEnabled(false);
					//各結果を反転させる．
					isWave = !isWave;
					isFFT = !isFFT;
					mVisualizer.setDataCaptureListener(
							new MyOnDataCaptureImpl(fftView),
							Visualizer.getMaxCaptureRate(),
							isWave, 
							isFFT);
					mVisualizer.setEnabled(true);
				}
			}

		});
		
		//>-----スイッチのバーについての設定-----------------------------<//
		return eq;
	}
	
	/**
	 * Visualizerを作成する．
	 * @param context
	 * @return Visualizer
	 */
	public static final Visualizer createMusicVisualizer(final Context context){
		removeMusicVisualizer();
		
		final int captureRate = Visualizer.getMaxCaptureRate();
		
		final MusicPlayerWithQueue mpwpl = MusicUtils.getMusicController(context);
		//再生している曲がない場合はViewを作成しない
		if(mpwpl.getMediaPlayerSessionId() == -1)return null;
		final Visualizer mVisualizer = new Visualizer(mpwpl.getMediaPlayerSessionId());
		mVisualizer.setEnabled(false);
		
		mVisualizer.setCaptureSize(Visualizer.getCaptureSizeRange()[1]);
		
		//キャプチャしたデータを定期的に取得するリスナーを設定
        mVisualizer.setDataCaptureListener(new MyOnDataCaptureImpl(fftView),
        captureRate, //キャプチャーデータの取得レート（ミリヘルツ）
        isWave,//waveFrom
        isFFT);//fftFlag
        
        ((FFTView)fftView).setSamplingRate(captureRate);
        mVisualizer.setEnabled(true);
        ViewPagerForEqualizerViewCtl.mVisualizer = mVisualizer;
		return mVisualizer;
	}
	
	/**
	 * Visualizerを解放する
	 */
	public static void removeMusicVisualizer(){
		if(mVisualizer != null){
			mVisualizer.release();
			mVisualizer = null;
		}
	}

}
