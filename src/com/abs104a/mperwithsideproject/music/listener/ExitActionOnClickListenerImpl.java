package com.abs104a.mperwithsideproject.music.listener;

import android.app.Service;
import android.view.View;
import android.view.View.OnClickListener;

/**
 * �A�v���P�[�V�������I�������郊�X�i
 * @author Kouki
 *
 */
public final class ExitActionOnClickListenerImpl implements OnClickListener {

	private final Service mService;

	public ExitActionOnClickListenerImpl(Service mService) {
		this.mService = mService;
	}

	/**
	 * �A�v���P�[�V�������I������
	 */
	@Override
	public void onClick(View v) { 
		mService.stopSelf(); 
	}

}
