package com.abs104a.mperwithsideproject.viewctl.listener;

import com.abs104a.mperwithsideproject.viewctl.MusicViewCtl;

import android.view.View;
import android.view.View.OnClickListener;

/**
 * 再生ボタンを押した時に呼ばれるリスナImpl
 * @author Kouki-Mobile
 *
 */
public final class PlayButtonOnClickImpl implements OnClickListener {


	@Override
	public void onClick(View v) {
		//再生動作を行う
		MusicViewCtl.playOrPauseWithView();
	}

}
