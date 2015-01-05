package com.abs104a.mperwithsideproject.viewctl.listener;

import com.abs104a.mperwithsideproject.viewctl.MusicViewCtl;

import android.view.View;
import android.view.View.OnClickListener;

/**
 * シャッフルボタンが押された時に呼ばれるリスナImpl
 * @author Kouki-Mobile
 *
 */
public final class ShuffleButtonOnClickImpl implements OnClickListener {

	/**
	 * クリックされた時
	 */
	@Override
	public void onClick(View v) {
		MusicViewCtl.changeShuffleState();
	}

}
