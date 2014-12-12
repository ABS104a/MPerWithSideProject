package com.abs104a.mperwithsideproject.viewctl;

import java.util.ArrayList;

import com.abs104a.mperwithsideproject.adapter.MusicListAdapter;
import com.abs104a.mperwithsideproject.music.Album;
import com.abs104a.mperwithsideproject.music.Music;
import com.abs104a.mperwithsideproject.music.MusicPlayerWithPlayLists;
import com.abs104a.mperwithsideproject.utl.MusicUtils;
import com.abs104a.mperwithsideproject.viewctl.listener.MusicItemOnClickListener;

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
	 * @param mView 
	 * @param mpwpl		playerのコントロール
	 * @return			作成したView
	 */
	public final static View createView(
			final Service mService,
			View rootView, final MusicPlayerWithPlayLists mpwpl)
	{
		//TODO Viewの生成 ExpandableListview
		final ListView mListView = new ListView(mService);
		ArrayList<Music> items = MusicUtils.getMusicList(mService);
		mListView.setAdapter(new MusicListAdapter(mService,items, mpwpl));
		mListView.setOnItemClickListener(new MusicItemOnClickListener(mService,rootView,items,mpwpl));
		
		android.util.Log.v("count", mListView.getAdapter().getCount()+"");
		//MusicUtilsからアルバム情報を取得
		//ArrayList<Album> list = MusicUtils.getMusicList(mService);
		
		return mListView;
	}
}
