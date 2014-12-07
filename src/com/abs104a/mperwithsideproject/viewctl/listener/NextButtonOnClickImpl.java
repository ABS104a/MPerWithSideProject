package com.abs104a.mperwithsideproject.viewctl.listener;

import java.io.IOException;

import com.abs104a.mperwithsideproject.music.MusicPlayerWithPlayLists;

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
	private final MusicPlayerWithPlayLists _mpwpl;
	//サービスのコンテキスト（例外時のメッセージ処理など
	private final Service _service;

	/**
	 * インスタンスの生成
	 * @param _service　サービスのコンテキスト
	 * @param mpwpl　ミュージックコントロールクラス
	 */
	public NextButtonOnClickImpl(Service _service,MusicPlayerWithPlayLists mpwpl) {
		this._mpwpl = mpwpl;
		this._service = _service;
	}

	@Override
	public void onClick(View v) {
		try {
			//次への再生を行うs
			_mpwpl.playNext();
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
