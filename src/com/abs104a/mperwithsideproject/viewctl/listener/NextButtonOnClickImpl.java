package com.abs104a.mperwithsideproject.viewctl.listener;

import com.abs104a.mperwithsideproject.viewctl.MusicViewCtl;

import android.view.View;
import android.view.View.OnClickListener;

/**
 * 次へボタンを押した時に呼ばれるリスナImpl
 * @author Kouki-Mobile
 *
 */
public final class NextButtonOnClickImpl implements OnClickListener {


	@Override
	public void onClick(View v) {
		try {
			//次への再生を行うs
			MusicViewCtl.playNextWithView();
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
