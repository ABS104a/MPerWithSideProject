package com.abs104a.mperwithsideproject.viewctl;

import java.util.ArrayList;

import com.abs104a.mperwithsideproject.R;
import com.abs104a.mperwithsideproject.adapter.MusicListAdapter;
import com.abs104a.mperwithsideproject.adapter.PlayListForExpandableListAdapter;
import com.abs104a.mperwithsideproject.music.MusicPlayerWithQueue;
import com.abs104a.mperwithsideproject.music.PlayList;
import com.abs104a.mperwithsideproject.utl.FileUtils;
import com.abs104a.mperwithsideproject.viewctl.listener.PlayListOnChildClickImpl;

import android.app.Service;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.TextView;

/**
 * PlayListのViewをコントロールするクラス
 * TODO　空の場合にPlayListを作成するItemを作成する．
 * TODO　作成したプレイリストの作成，曲の消去を行えるようにする．
 * @author Kouki-Mobile
 *
 */
public final class ViewPagerForPlayListViewCtl {
	
	public static final String TAG = "ViewPagerForPlayListViewCtl";
	
	public static ExpandableListView mListView = null;
	
	/**
	 * プレイリストViewを生成する
	 * @param mServie	親となるサービスのコンテキスト
	 * @return	生成したView
	 */
	public final static ExpandableListView createView(Service mService,View rootView ,MusicPlayerWithQueue mpwpl){
		//TODO　ルーチンの実装
		mListView = new ExpandableListView(mService);
		//ここでPlayListを読み込む
		ArrayList<PlayList> pList = FileUtils.readSerializablePlayList(mService);
		android.util.Log.v(TAG , "ListNum : " + (pList != null ? pList.size() : "null"));
		mListView.setAdapter(new PlayListForExpandableListAdapter(mService, pList, rootView, mpwpl,MusicListAdapter.PLAYLIST));
		mListView.setOnChildClickListener(new PlayListOnChildClickImpl(mService,mpwpl));
		TextView textView = new TextView(mService);
		textView.setText(R.string.row_empty);
		mListView.setEmptyView(textView);

		return mListView;
	}
	
	public static void updateExpandableListViewItems(Service mService,View rootView ,MusicPlayerWithQueue mpwpl){
		//ここでPlayListを読み込む
		if(mListView == null)return;
		ArrayList<PlayList> pList = FileUtils.readSerializablePlayList(mService);
		android.util.Log.v(TAG + " Update" , "ListNum : " + (pList != null ? pList.size() : "null"));
		mListView.setAdapter(new PlayListForExpandableListAdapter(mService, pList, rootView, mpwpl,MusicListAdapter.PLAYLIST));
		mListView.setOnChildClickListener(new PlayListOnChildClickImpl(mService,mpwpl));
	}
	
}
