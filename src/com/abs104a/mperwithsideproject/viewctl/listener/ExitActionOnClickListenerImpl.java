package com.abs104a.mperwithsideproject.viewctl.listener;

import android.app.Service;
import android.view.View;
import android.view.View.OnClickListener;

/**
 * アプリケーションを終了させるリスナ
 * @author Kouki
 *
 */
public final class ExitActionOnClickListenerImpl implements OnClickListener {

	private final Service mService;

	public ExitActionOnClickListenerImpl(Service mService) {
		this.mService = mService;
	}

	/**
	 * アプリケーションを終了する
	 */
	@Override
	public void onClick(View v) { 
		mService.stopSelf(); 
	}

}
