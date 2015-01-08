package com.abs104a.mperwithsideproject.viewctl;

import java.util.ArrayList;

import com.abs104a.mperwithsideproject.R;
import com.abs104a.mperwithsideproject.adapter.MusicListAdapter;
import com.abs104a.mperwithsideproject.adapter.MusicViewPagerAdapter;
import com.abs104a.mperwithsideproject.adapter.PlayListForExpandableListAdapter;
import com.abs104a.mperwithsideproject.music.Album;
import com.abs104a.mperwithsideproject.music.MusicPlayerWithQueue;
import com.abs104a.mperwithsideproject.music.PlayList;
import com.abs104a.mperwithsideproject.utl.MusicUtils;
import android.content.Context;
import android.content.SharedPreferences;
import android.view.View;
import android.widget.ExpandableListView;

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
	public final View createView(
			final Context mContext,
			View rootView, final MusicPlayerWithQueue mpwpl)
	{
		//TODO　ルーチンの実装
		ExpandableListView mListView = new ExpandableListView(mContext);
		//TODO ここでPlayListを読み込む
		ArrayList<Album> list = MusicUtils.getMusicAlbumList(mContext);
		ArrayList<PlayList> pList = new ArrayList<PlayList>();
		for(Album album : list){
			PlayList mlist = new PlayList(album.getAlbum(), album.getArtist(), album.getAlbumId(), album.getJacketUri());
			mlist.setMusics(album.getMusics());
			pList.add(mlist);
		}
		mListView.setAdapter(new PlayListForExpandableListAdapter(mContext, pList, rootView, mpwpl,MusicListAdapter.ALBUM));
		SharedPreferences sp = mContext.getSharedPreferences(MusicViewPagerAdapter.TAG, Context.MODE_PRIVATE);
		int firstVisible = sp.getInt("FIRST_VISIBLE", 0);
		mListView.setSelectionFromTop(Math.min(firstVisible, pList.size() - 1), 0);
		
		mListView.setDividerHeight(mContext.getResources().getDimensionPixelSize(R.dimen.listview_divider));
		
		return mListView;
	}
}
