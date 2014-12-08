package com.abs104a.mperwithsideproject.viewctl;

import java.util.ArrayList;

import com.abs104a.mperwithsideproject.adapter.AlbumListAdapter;
import com.abs104a.mperwithsideproject.music.Item;
import com.abs104a.mperwithsideproject.music.MusicPlayerWithPlayLists;
import com.abs104a.mperwithsideproject.utl.MusicUtils;

import android.app.Service;
import android.view.View;
import android.widget.ListView;

/**
 * アルバム表示部を作成し，管理するViewController
 * @author Kouki-Mobile
 *
 */
public final class ViewPagerForAlbumViewCtl {

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
		ArrayList<Item> items = MusicUtils.getMusicList(mService);
		mListView.setAdapter(new AlbumListAdapter(mService,items, mpwpl));
		return mListView;
	}
}
