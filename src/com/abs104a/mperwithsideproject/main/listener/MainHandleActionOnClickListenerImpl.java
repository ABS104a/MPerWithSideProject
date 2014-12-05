package com.abs104a.mperwithsideproject.main.listener;

import com.abs104a.mperwithsideproject.music.MusicPlayerController;

import android.app.Service;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;

/**
 * メインビューのハンドル（取っ手）がクリックされた時に呼ばれるリスナ
 * @author Kouki
 *
 */
public final class MainHandleActionOnClickListenerImpl implements OnClickListener {

	private final Service mService;

	public MainHandleActionOnClickListenerImpl(Service mService) {
		this.mService = mService;
	}

	@Override
	public void onClick(View v) {
		//MusicPlayerViewの作成
		View mPlayerView = MusicPlayerController.createView(mService);
		((LinearLayout)v.getParent()).addView(mPlayerView);
	}

}
