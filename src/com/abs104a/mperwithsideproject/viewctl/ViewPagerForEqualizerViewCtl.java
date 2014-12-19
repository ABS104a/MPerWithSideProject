package com.abs104a.mperwithsideproject.viewctl;

import android.app.Service;
import android.content.Context;
import android.media.audiofx.Equalizer;
import android.media.audiofx.Visualizer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

import com.abs104a.mperwithsideproject.R;
import com.abs104a.mperwithsideproject.music.EqualizerItem;
import com.abs104a.mperwithsideproject.music.MusicPlayerWithQueue;
import com.abs104a.mperwithsideproject.utl.MusicUtils;

/**
 * EqualizerViewの生成とコントロールを行うクラス
 * @author Kouki-Mobile
 *
 */
public class ViewPagerForEqualizerViewCtl {
	
	private static Visualizer mVisualizer = null;

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
		LayoutInflater layoutInflater = LayoutInflater.from(mService);
		ScrollView mView = (ScrollView)layoutInflater.inflate(R.layout.equalizer, null);
		
		CheckBox chBox = (CheckBox)mView.findViewById(R.id.checkBox_equalizer);
		chBox.setOnCheckedChangeListener(new OnCheckedChangeListener(){

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				android.util.Log.v("Equalizer","NewLevel : " + isChecked);
				mpwpl.setEqualizer(isChecked);
			}
			
		});
		
		final LinearLayout layoutView = (LinearLayout)mView.findViewById(R.id.linearLayout_equalizer);
		final Equalizer eq = mpwpl.getEqualizerInstance();
		// 調整を行うことのできる中心周波数の数を確認
		final short bands = eq.getNumberOfBands();
		// 設定出来るレベルの下限、上限を確認
		final short minEQLevel = eq.getBandLevelRange()[0];
		final short maxEQLevel = eq.getBandLevelRange()[1];
		//中心レベルを計算
		final short centerEQLevel = (short) ((maxEQLevel - minEQLevel) / 2);
		
		final EqualizerItem[] items = new EqualizerItem[(int)bands];
		mpwpl.setEqualizerItem(items);
		
		for(short i = 0;i < bands;i++){
			final int index = (int)i;
			final short band = (short) eq.getCenterFreq(i);
			items[index] = new EqualizerItem((short) (eq.getCenterFreq(i)), (short) ((maxEQLevel - Math.abs(minEQLevel)) / 2));
			//指定バンド幅の数だけViewを作成する．
			
			View eqView = layoutInflater.inflate(R.layout.equalizer_row, null);
			final TextView currentText = (TextView)eqView.findViewById(R.id.textView_equalizer_row_current);
			SeekBar seekBar = (SeekBar)eqView.findViewById(R.id.seekBar_equalizer_row);
			TextView maxText = (TextView)eqView.findViewById(R.id.textView_equalizer_row_max);
			currentText.setText((maxEQLevel - centerEQLevel) + "");
			
			seekBar.setMax((int)(maxEQLevel - minEQLevel));
			seekBar.setProgress((int) centerEQLevel);
			seekBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener(){

				@Override
				public void onProgressChanged(SeekBar seekBar, int progress,
						boolean fromUser) {
					if(fromUser){
						currentText.setText((progress - centerEQLevel) + "");
						short newLevel = (short) (progress - centerEQLevel);
						//android.util.Log.v("Equalizer","NewLevel : " + newLevel);
						//eq.setBandLevel(band, newLevel);
						items[index].setLevel(newLevel);
					}
				}

				@Override
				public void onStartTrackingTouch(SeekBar seekBar) {	}

				@Override
				public void onStopTrackingTouch(SeekBar seekBar) {}
				
			});
			maxText.setText(String.valueOf(band / 1000));
			layoutView.addView(eqView);
		}
		
		return mView;
	}
	
	/**
	 * Visualizerを作成する．
	 * @param context
	 * @return Visualizer
	 */
	public static final Visualizer createMusicVisualizer(final Context context){
		final MusicPlayerWithQueue mpwpl = MusicUtils.getMusicController(context);
		//再生している曲がない場合はViewを作成しない
		if(mpwpl.getMediaPlayerSessionId() == -1)return null;
		final Visualizer mVisualizer = new Visualizer(mpwpl.getMediaPlayerSessionId());
		mVisualizer.setEnabled(false);
		
		mVisualizer.setCaptureSize(Visualizer.getCaptureSizeRange()[1]);
		
		//キャプチャしたデータを定期的に取得するリスナーを設定
        mVisualizer.setDataCaptureListener(new Visualizer.OnDataCaptureListener() {
            //Wave形式のキャプチャーデータ
            public void onWaveFormDataCapture(Visualizer visualizer, byte[] bytes,
                    int samplingRate) {
                //mVisualizerView.updateVisualizer(bytes);
            	android.util.Log.v("Visualizer","setVisualizer");
            }
 
            //高速フーリエ変換のキャプチャーデータ
            public void onFftDataCapture(Visualizer visualizer, byte[] bytes,
                    int samplingRate) {
            }
        },
        Visualizer.getMaxCaptureRate() / 2, //キャプチャーデータの取得レート（ミリヘルツ）
        true,//これがTrueだとonWaveFormDataCaptureにとんでくる
        false);//これがTrueだとonFftDataCaptureにとんでくる
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
