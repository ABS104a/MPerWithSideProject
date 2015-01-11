package com.abs104a.mperwithsideproject.viewctl.listener;

import com.abs104a.mperwithsideproject.MainService;
import com.abs104a.mperwithsideproject.music.MusicPlayerWithQueue;

import android.app.Service;
import android.view.View;
import android.view.View.OnClickListener;

/**
 * アプリケーションを終了させるリスナ
 * @author Kouki
 *
 */
public final class ExitButtonOnClickListenerImpl implements OnClickListener {

	//アプリケーションのコンテキスト
	private final Service mService;
	//ミュージックcontrollerクラス
	private final MusicPlayerWithQueue mpwpl;

	/**
	 * インスタンスの生成
	 * @param mService
	 * @param mpwpl
	 */
	public ExitButtonOnClickListenerImpl(Service mService,MusicPlayerWithQueue mpwpl) {
		this.mService = mService;
		this.mpwpl = mpwpl;
	}

	/**
	 * アプリケーションを終了する
	 */
	@Override
	public void onClick(View v) { 
		mpwpl.playStop();
		((MainService)mService).stopService();
	}

}
