package com.abs104a.mperwithsideproject.viewctl.listener;

import com.abs104a.mperwithsideproject.music.MusicPlayerWithPlayLists;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
/**
 * リピートボタンを押した時に呼ばれるリスナImpl
 * @author Kouki-Mobile
 *
 */
public final class RepeatButtonOnClickImpl implements OnClickListener {

	private final Button _button;
	private final MusicPlayerWithPlayLists _mpwpl;

	/**
	 * インスタンスの生成
	 * @param mView 結果反映用View
	 * @param mpwpl 音楽コントロールクラス
	 */
	public RepeatButtonOnClickImpl(Button button, MusicPlayerWithPlayLists mpwpl) {
		this._button = button;
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
		if(loopState == MusicPlayerWithPlayLists.NOT_LOOP){
			loopState = MusicPlayerWithPlayLists.ALL_LOOP;
		}
		//全曲LOOPの時
		else if(loopState == MusicPlayerWithPlayLists.ALL_LOOP){
			loopState = MusicPlayerWithPlayLists.ONE_LOOP;
		}
		//1曲ループの時
		else if(loopState == MusicPlayerWithPlayLists.ONE_LOOP){
			loopState = MusicPlayerWithPlayLists.NOT_LOOP;
		}
		//LOOP状態の取得
		loopState = _mpwpl.setLoopState(loopState);

		//TODO Viewへの反映
		
		//_button.setBackground(hoge);
		
	}

}
