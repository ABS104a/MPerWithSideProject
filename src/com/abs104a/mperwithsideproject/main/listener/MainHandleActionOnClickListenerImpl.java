package com.abs104a.mperwithsideproject.main.listener;

import com.abs104a.mperwithsideproject.music.MusicPlayerController;

import android.app.Service;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;

/**
 * ���C���r���[�̃n���h���i�����j���N���b�N���ꂽ���ɌĂ΂�郊�X�i
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
		//MusicPlayerView�̍쐬
		View mPlayerView = MusicPlayerController.createView(mService);
		((LinearLayout)v.getParent()).addView(mPlayerView);
	}

}
