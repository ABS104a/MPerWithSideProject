package com.abs104a.mperwithsideproject.viewctl.listener;

import java.io.IOException;

import com.abs104a.mperwithsideproject.music.MusicPlayerWithQueue;

import android.app.Service;
import android.view.View;
import android.view.View.OnClickListener;

/**
 * 戻るボタンを押した時に呼ばれるリスナImpl
 * @author Kouki-Mobile
 *
 */
public final class BackButtonOnClickImpl implements OnClickListener {

	//ミュージックコントロールクラスのインスタンス
	private final MusicPlayerWithQueue _mpwpl;
	//サービスのコンテキスト（例外時のメッセージ処理など
	private final Service _service;

	/**
	 * インスタンスの生成
	 * @param mService 親クラスのサービスコンテキスト
	 * @param mpwpl ミュージックコントロールクラス
	 */
	public BackButtonOnClickImpl(Service mService,
			MusicPlayerWithQueue mpwpl) {
		this._mpwpl = mpwpl;
		this._service = mService;
	}

	/**
	 * 戻るボタンがクリックされた時
	 */
	@Override
	public void onClick(View v) {
		try {
			//戻る動作を行う
			_mpwpl.playBack();
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
