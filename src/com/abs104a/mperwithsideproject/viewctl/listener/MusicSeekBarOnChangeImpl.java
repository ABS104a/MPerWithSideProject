package com.abs104a.mperwithsideproject.viewctl.listener;

import com.abs104a.mperwithsideproject.music.MusicPlayerWithQueue;
import com.abs104a.mperwithsideproject.utl.DisplayUtils;

import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

/**
 * 音楽のシークバーが操作された時のリスナImpl
 * @author Kouki
 *
 */
public class MusicSeekBarOnChangeImpl implements OnSeekBarChangeListener {

	//テキスト表示の時間を表わすView
	private TextView currentTimeView;
	//ミュージックコントロールクラス
	private MusicPlayerWithQueue mpwpl;

	public MusicSeekBarOnChangeImpl(TextView currentTime,
			MusicPlayerWithQueue mpwpl) {
		this.currentTimeView = currentTime;
		this.mpwpl = mpwpl;
	}

	//プログレスバーが変わったとき
	@Override
	public void onProgressChanged(SeekBar seekBar, int progress,
			boolean fromUser) {
		//操作がユーザーの時のみシークを変更する
		if(fromUser){
			mpwpl.setCurrentTime(progress);
			currentTimeView.setText(DisplayUtils.long2TimeString(mpwpl.getCurrentTime()));
		}
	}

	@Override
	public void onStartTrackingTouch(SeekBar seekBar) {
		// TODO 自動生成されたメソッド・スタブ

	}

	@Override
	public void onStopTrackingTouch(SeekBar seekBar) {
		// TODO 自動生成されたメソッド・スタブ

	}

}
