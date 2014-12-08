package com.abs104a.mperwithsideproject.viewctl;

import com.abs104a.mperwithsideproject.music.MusicPlayerWithPlayLists;

import android.app.Service;
import android.view.View;
import android.widget.ListView;

/**
 * アルバム表示部を作成し，管理するView
 * @author Kouki-Mobile
 *
 */
public final class AlbumViewController {

	/**
	 * AlbumViewの作成
	 * @param mService	サービスのコンテキスト
	 * @param mpwpl		playerのコントロール
	 * @return			作成したView
	 */
	public final static View createView(
			final Service mService,
			final MusicPlayerWithPlayLists mpwpl)
	{
		//TODO Viewの生成
		ListView mListView = new ListView(mService);
		return mListView;
	}
}
