package com.abs104a.mperwithsideproject.viewctl.listener;

import com.abs104a.mperwithsideproject.music.MusicPlayerWithPlayLists;

import android.app.Service;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;

/**
 * PlayListViewの子要素が選択された時に呼び出されるリスナImpl
 * 選択されたPlayListを呼び出しその要素から再生を行う
 * @author Kouki-Mobile
 *
 */
public final class PlayListOnChildClickImpl implements OnChildClickListener {

	//親サービスのコンテキスト
	private final Service mService;
	//音楽プレーヤーのコントロールインスタンス
	private final MusicPlayerWithPlayLists mpwpl;

	/**
	 * インスタンス生成
	 * @param mService	親サービスのコンテキスト
	 * @param mpwpl		音楽プレーヤーのコントロールインスタンス
	 */
	public PlayListOnChildClickImpl(Service mService,
			MusicPlayerWithPlayLists mpwpl) {
		this.mService = mService;
		this.mpwpl = mpwpl;
	}

	/**
	 * 子要素が選択された時
	 */
	@Override
	public boolean onChildClick(ExpandableListView parent, View v,
			int groupPosition, int childPosition, long id) {
		// TODO 子要素が選択された時
		//選択されたグループ　=　プレイリスト　を読み込み，選択位置から再生する．
		return true;
	}

}
