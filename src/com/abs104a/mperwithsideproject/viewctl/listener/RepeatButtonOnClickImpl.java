package com.abs104a.mperwithsideproject.viewctl.listener;

import com.abs104a.mperwithsideproject.utl.MusicUtils;

import android.view.View;
import android.view.View.OnClickListener;
/**
 * リピートボタンを押した時に呼ばれるリスナImpl
 * @author Kouki-Mobile
 *
 */
public final class RepeatButtonOnClickImpl implements OnClickListener {

	private final View rootView;

	/**
	 * インスタンスの生成
	 * @param mView 結果反映用View
	 * @param mpwpl 音楽コントロールクラス
	 */
	public RepeatButtonOnClickImpl(View rootView) {
		this.rootView = rootView;
	}

	/**
	 * タップされた時
	 * （1曲ループ）→ループなし→全曲ループ→1曲ループ
	 */
	@Override
	public void onClick(View v) {
		MusicUtils.changeRepeatState(rootView);
	}

}
