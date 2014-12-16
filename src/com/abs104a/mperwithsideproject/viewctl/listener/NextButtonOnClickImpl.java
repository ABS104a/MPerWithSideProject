package com.abs104a.mperwithsideproject.viewctl.listener;

import java.io.IOException;

import com.abs104a.mperwithsideproject.music.MusicPlayerWithQueue;
import com.abs104a.mperwithsideproject.viewctl.MusicPlayerViewController;

import android.app.Service;
import android.view.View;
import android.view.View.OnClickListener;

/**
 * 次へボタンを押した時に呼ばれるリスナImpl
 * @author Kouki-Mobile
 *
 */
public final class NextButtonOnClickImpl implements OnClickListener {

	//ミュージックコントロールインスタンス
	private final MusicPlayerWithQueue _mpwpl;
	//サービスのコンテキスト（例外時のメッセージ処理など
	private final Service _service;
	//rootView
	private View mView;

	/**
	 * インスタンスの生成
	 * @param _service　サービスのコンテキスト
	 * @param mpwpl　ミュージックコントロールクラス
	 */
	public NextButtonOnClickImpl(Service _service,MusicPlayerWithQueue mpwpl,View mView) {
		this._mpwpl = mpwpl;
		this._service = _service;
		this.mView = mView;
	}

	@Override
	public void onClick(View v) {
		try {
			//次への再生を行うs
			_mpwpl.playNext();
			MusicPlayerViewController.setPartOfPlayerView(_service, mView, _mpwpl.getNowPlayingMusic(), _mpwpl);
		} catch (IllegalArgumentException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		} catch (SecurityException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		} catch (IllegalStateException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		} catch (IOException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		}
	}

}
