package com.abs104a.mperwithsideproject.viewctl;

import android.content.Context;
import android.media.audiofx.Equalizer;
import android.media.audiofx.Visualizer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
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
import com.abs104a.mperwithsideproject.utl.VisualizerUtil;
import com.abs104a.mperwithsideproject.view.FFTView;
import com.abs104a.mperwithsideproject.viewctl.listener.MyOnDataCaptureImpl;

/**
 * EqualizerViewの生成とコントロールを行うクラス
 * @author Kouki-Mobile
 *
 */
public class ViewPagerForEqualizerViewCtl {
	
	//private static Visualizer mVisualizer = null;
	//private static boolean isFFT = false;
	//private static boolean isWave = true;
	private static VisualizerUtil vutl = null;
	
	public static VisualizerUtil getVisualizerUtil(){
		return vutl;
	}
	
	//Tag
	public static final String TAG = "ViewPagerForEqualizerViewCtl";

	/**
	 * Viewの生成
	 * @param mService	親サービスのコンテキスト
	 * @param mpwpl		音楽playerのコントロール
	 * @return			生成したView
	 */
	public final View createView(
			final Context mContext,
			final MusicPlayerWithQueue mpwpl,
			final VisualizerUtil vutl) 
	{
		
		//TODO Visualizerの設定
		ViewPagerForEqualizerViewCtl.vutl = vutl;
		
		//Layoutの生成
		LayoutInflater layoutInflater = LayoutInflater.from(mContext);
		final ScrollView mView = (ScrollView)layoutInflater.inflate(R.layout.equalizer, (ViewGroup)MusicViewCtl.getPlayerView(),false);
		
		vutl.createMusicVisualizer(mContext);
		
		//EqualizerのView反映
		final Equalizer eq = setEqualizerForView(mView,vutl);
		
		
		//>-----チェックボックスの設定-----------------------------------------<//
		CheckBox chBox = (CheckBox)mView.findViewById(R.id.checkBox_equalizer);
		chBox.setChecked(mpwpl.getEqualizer());
		chBox.setOnCheckedChangeListener(new OnCheckedChangeListener(){

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				android.util.Log.v("Equalizer","isEnabled : " + isChecked);
				mpwpl.setEqualizer(isChecked);
				//反映する
				setEqualizerForView(mView,vutl);
			}
			
		});
		//>-----チェックボックスの設定終わり-------------------------------------<//
		
		//>-----Spinnerの設定------------------------------------------<//
		final Spinner mSpinner = (Spinner)mView.findViewById(R.id.spinner_equalizer);
		//Equalizerのプリセットをロード
		final short numOfPresets = eq.getNumberOfPresets();
		//現在のプリセット値
		short currentPreset = mpwpl.getEqualizerCursor();
		
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(mContext, android.R.layout.simple_spinner_item);
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
				setEqualizerForView(mView,vutl);
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {	}
			
		});
		
		return mView;
	}
	
	/**
	 * equalizerをViewに反映させるメソッド
	 * @param mView	生成したequalizerView
	 * @return	生成したequalizerインスタンス
	 */
	private final Equalizer setEqualizerForView(final View mView,final VisualizerUtil vutl){
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
				items[i].setBand((short) (eq.getCenterFreq(i) / 1000));
				items[i].setLevel((short) eq.getBandLevel(i));
			}
		}
		
		//>-----Equalizerのバーについての設定-----------------------------<//
		for(short i = 0;i < bands;i++){
			final int index = (int)i;
			final short band = (short) (eq.getCenterFreq(i) / 1000);
			//指定バンド幅の数だけViewを作成する．
			View eqView = layoutInflater.inflate(R.layout.equalizer_row, (ViewGroup)MusicViewCtl.getPlayerView(),false);
			final TextView currentText = (TextView)eqView.findViewById(R.id.textView_equalizer_row_current);
			SeekBar seekBar = (SeekBar)eqView.findViewById(R.id.seekBar_equalizer_row);
			TextView maxText = (TextView)eqView.findViewById(R.id.textView_equalizer_row_max);
			maxText.setText(String.valueOf(band) + "Hz");
			currentText.setText(eq.getBandLevel((short)index) / 100 + "");
			
			seekBar.setMax((int)(maxEQLevel - minEQLevel) / 100);
			seekBar.setProgress(((int) eq.getBandLevel((short)index) - minEQLevel) / 100);
			seekBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener(){

				@Override
				public void onProgressChanged(SeekBar seekBar, int progress,
						boolean fromUser) {
					if(fromUser){
						currentText.setText((progress * 100 + minEQLevel) / 100 + "");
						short newLevel = (short) ((progress * 100 + minEQLevel) / 100);
						Equalizer eqr = mpwpl.getEqualizerInstance();
						eqr.setBandLevel((short)index,(short) (newLevel * 100));
						items[index].setLevel((short) (newLevel * 100));
						
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
			
			layoutView.addView(eqView);
		}
		//>-----Equalizerのバーについての設定-----------------------------<//
		
		//>-----スイッチのバーについての設定-----------------------------<//
		final Button switchButton = (Button)mView.findViewById(R.id.button_equalizer_switch);
		switchButton.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				View mView = v.getRootView();
				//Visualizerの生成
				FFTView fftView = (FFTView)mView.findViewById(R.id.fftview_equalizer);
				if(vutl.getVisualizer() != null && fftView != null){
					vutl.getVisualizer().setEnabled(false);
					//各結果を反転させる．
					vutl.setWave(!vutl.isWave());
					vutl.setFFT(!vutl.isFFT());
					vutl.getVisualizer().setDataCaptureListener(
							new MyOnDataCaptureImpl(fftView),
							Visualizer.getMaxCaptureRate(),
							vutl.isWave(), 
							vutl.isFFT());
					vutl.getVisualizer().setEnabled(true);
				}
			}

		});
		
		//>-----スイッチのバーについての設定-----------------------------<//
		return eq;
	}
	
	//前に取得したジャケットの画像のアルバム名
	//private static String oldAlbumName = null;
	
	/**
	 * Visualizerを作成する．
	 * @param context
	 * @return Visualizer
	 */
	public final static Visualizer createMusicVisualizer(final Context context){
		return vutl.getVisualizer();
	}
	
	/**
	 * Visualizerを解放する
	 */
	
	public static void removeMusicVisualizer(){
		try{
			if(vutl != null){
				vutl.removeMusicVisualizer();
			}
		}catch(Exception e){

		}
	}
	
	public static void setIsVisualizer(boolean isEnabled){
		try{
			if(vutl != null){
				vutl.setIsVisualizer(isEnabled);
			}
		}catch(Exception e){

		}
	}

}
