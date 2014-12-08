package com.abs104a.mperwithsideproject.music.listener;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;

/**
 * プレイリストに追加するボタンが押された時の動作．
 * 
 * プレイリスト一覧を取得し表示
 * 指定したプレイリストの末尾に追加する．
 * 
 * @author Kouki
 *
 */
public final class PlayListAddOnClickImpl implements OnClickListener {

	//アプリケーションのコンテキスト
	private Context mContext;

	/**
	 * インスタンスの生成
	 * @param mContext	アプリケーションのコンテキスト
	 */
	public PlayListAddOnClickImpl(Context mContext) {
		this.mContext = mContext;
	}

	/**
	 * ボタンがクリックされたとき
	 * プレイリストへの追加を行う.
	 */
	@Override
	public void onClick(View v) {
		// TODO プレイリストへの追加を行う

	}

}
