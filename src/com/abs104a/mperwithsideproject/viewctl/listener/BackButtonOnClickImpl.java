package com.abs104a.mperwithsideproject.viewctl.listener;

import com.abs104a.mperwithsideproject.utl.MusicUtils;
import android.view.View;
import android.view.View.OnClickListener;

/**
 * 戻るボタンを押した時に呼ばれるリスナImpl
 * @author Kouki-Mobile
 *
 */
public final class BackButtonOnClickImpl implements OnClickListener {
	/**
	 * 戻るボタンがクリックされた時
	 */
	@Override
	public void onClick(View v) {
		try {
			MusicUtils.playBackWithView();
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
