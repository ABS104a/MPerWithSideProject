package com.abs104a.mperwithsideproject.viewctl.listener;

import com.abs104a.mperwithsideproject.utl.MusicUtils;

import android.view.View;
import android.view.View.OnClickListener;

/**
 * 再生ボタンを押した時に呼ばれるリスナImpl
 * @author Kouki-Mobile
 *
 */
public final class PlayButtonOnClickImpl implements OnClickListener {

	//プレイヤーコントロールインスタンス
	private final View rootView;

	public PlayButtonOnClickImpl(View rootView) {
		this.rootView = rootView;
	}

	@Override
	public void onClick(View v) {
		//再生動作を行う
		MusicUtils.playOrPauseWithView(rootView);
	}

}
