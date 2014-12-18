package com.abs104a.mperwithsideproject.viewctl.listener;

import com.abs104a.mperwithsideproject.utl.MusicUtils;
import android.view.View;
import android.view.View.OnClickListener;

/**
 * 次へボタンを押した時に呼ばれるリスナImpl
 * @author Kouki-Mobile
 *
 */
public final class NextButtonOnClickImpl implements OnClickListener {

	//rootView
	private View rootView;

	/**
	 * インスタンスの生成
	 * @param _service　サービスのコンテキスト
	 * @param mpwpl　ミュージックコントロールクラス
	 */
	public NextButtonOnClickImpl(View rootView) {
		this.rootView = rootView;
	}

	@Override
	public void onClick(View v) {
		try {
			//次への再生を行うs
			MusicUtils.playNextWithView(rootView);
		} catch (IllegalArgumentException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		} catch (SecurityException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		} catch (IllegalStateException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		}
	}

}
