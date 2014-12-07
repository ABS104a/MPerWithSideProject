package com.abs104a.mperwithsideproject.viewctl.listener;

import com.abs104a.mperwithsideproject.music.MusicPlayerWithPlayLists;

import android.view.View;
import android.view.View.OnClickListener;

/**
 * 再生ボタンを押した時に呼ばれるリスナImpl
 * @author Kouki-Mobile
 *
 */
public final class PlayButtonOnClickImpl implements OnClickListener {

	//プレイヤーコントロールインスタンス
	private final MusicPlayerWithPlayLists _mpwpl;

	public PlayButtonOnClickImpl(MusicPlayerWithPlayLists mpwpl) {
		this._mpwpl = mpwpl;
	}

	@Override
	public void onClick(View v) {
		//再生動作を行う
		_mpwpl.playStartAndPause();
	}

}
