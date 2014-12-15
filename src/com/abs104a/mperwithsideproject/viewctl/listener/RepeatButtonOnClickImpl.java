package com.abs104a.mperwithsideproject.viewctl.listener;

import com.abs104a.mperwithsideproject.R;
import com.abs104a.mperwithsideproject.music.MusicPlayerWithQueue;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.Toast;
/**
 * リピートボタンを押した時に呼ばれるリスナImpl
 * @author Kouki-Mobile
 *
 */
public final class RepeatButtonOnClickImpl implements OnClickListener {

	private final ImageButton _button;
	private final MusicPlayerWithQueue _mpwpl;

	/**
	 * インスタンスの生成
	 * @param mView 結果反映用View
	 * @param mpwpl 音楽コントロールクラス
	 */
	public RepeatButtonOnClickImpl(ImageButton repeatButton, MusicPlayerWithQueue mpwpl) {
		this._button = repeatButton;
		this._mpwpl = mpwpl;
	}

	/**
	 * タップされた時
	 * （1曲ループ）→ループなし→全曲ループ→1曲ループ
	 */
	@Override
	public void onClick(View v) {
		//現在の状態を取得する
		int loopState = _mpwpl.getLoopState();
		//LOOPしてない時
		if(loopState == MusicPlayerWithQueue.NOT_LOOP){
			loopState = MusicPlayerWithQueue.ALL_LOOP;
		}
		//全曲LOOPの時
		else if(loopState == MusicPlayerWithQueue.ALL_LOOP){
			loopState = MusicPlayerWithQueue.ONE_LOOP;
		}
		//1曲ループの時
		else if(loopState == MusicPlayerWithQueue.ONE_LOOP){
			loopState = MusicPlayerWithQueue.NOT_LOOP;
		}
		//LOOP状態の取得
		loopState = _mpwpl.setLoopState(loopState);
		final String message;
		switch(loopState){
			default:
			case MusicPlayerWithQueue.NOT_LOOP:
				message = v.getContext().getString(R.string.play_loop_state_notloop);
				break;
			case MusicPlayerWithQueue.ALL_LOOP:
				message = v.getContext().getString(R.string.play_loop_state_allloop);
				break;
			case MusicPlayerWithQueue.ONE_LOOP:
				message = v.getContext().getString(R.string.play_loop_state_oneloop);
		}
		Toast.makeText(v.getContext(), message, Toast.LENGTH_SHORT).show();
		//TODO Viewへの反映
		
		//_button.setBackground(hoge);
		
	}

}
