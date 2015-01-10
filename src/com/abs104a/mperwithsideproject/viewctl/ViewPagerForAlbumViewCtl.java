package com.abs104a.mperwithsideproject.viewctl;

import java.util.ArrayList;

import com.abs104a.mperwithsideproject.Column;
import com.abs104a.mperwithsideproject.R;
import com.abs104a.mperwithsideproject.adapter.PlayListForExpandableListAdapter;
import com.abs104a.mperwithsideproject.music.Album;
import com.abs104a.mperwithsideproject.music.MusicPlayerWithQueue;
import com.abs104a.mperwithsideproject.music.PlayList;
import com.abs104a.mperwithsideproject.utl.MusicUtils;
import android.content.Context;
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
		//ListViewの生成
		ExpandableListView mListView = new ExpandableListView(mContext);
		//Albumの読み込み
		ArrayList<Album> list = MusicUtils.getMusicAlbumList(mContext);
		ArrayList<PlayList> pList = new ArrayList<PlayList>();
		for(Album album : list){
			PlayList mlist = new PlayList(album.getAlbum(), album.getArtist(), album.getAlbumId(), album.getJacketUri());
			mlist.setMusics(album.getMusics());
			pList.add(mlist);
		}
		//Adapterの設定
		mListView.setAdapter(new PlayListForExpandableListAdapter(mContext, pList, rootView, mpwpl,Column.ALBUM));
		//パラメーター調整
		mListView.setDividerHeight(mContext.getResources().getDimensionPixelSize(R.dimen.listview_divider));
		
		return mListView;
	}
}
