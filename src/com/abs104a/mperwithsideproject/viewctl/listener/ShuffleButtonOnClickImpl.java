package com.abs104a.mperwithsideproject.viewctl.listener;

import com.abs104a.mperwithsideproject.utl.MusicUtils;

import android.view.View;
import android.view.View.OnClickListener;

/**
 * シャッフルボタンが押された時に呼ばれるリスナImpl
 * @author Kouki-Mobile
 *
 */
public final class ShuffleButtonOnClickImpl implements OnClickListener {

	//rootのView
	private final View rootView;

	/**
	 * インスタンスの生成
	 * @param shuffleButton　シャッフルボタンのView
	 * @param mpwpl　ミュージッククラスのインスタンス
	 */
	public ShuffleButtonOnClickImpl(View rootView) {
		this.rootView = rootView;
	}

	/**
	 * クリックされた時
	 */
	@Override
	public void onClick(View v) {
		MusicUtils.changeShuffleState(rootView);
	}

}
