package com.abs104a.mperwithsideproject.viewctl;

import com.abs104a.mperwithsideproject.adapter.PlayListForExpandableListAdapter;
import com.abs104a.mperwithsideproject.music.MusicPlayerWithPlayLists;
import com.abs104a.mperwithsideproject.viewctl.listener.PlayListOnChildClickImpl;

import android.app.Service;
import android.view.View;
import android.widget.ExpandableListView;

/**
 * PlayListのViewをコントロールするクラス
 * @author Kouki-Mobile
 *
 */
public final class ViewPagerForPlayListViewCtl {
	
	/**
	 * プレイリストViewを生成する
	 * @param mServie	親となるサービスのコンテキスト
	 * @return	生成したView
	 */
	public final static View createView(Service mService,MusicPlayerWithPlayLists mpwpl){
		//TODO　ルーチンの実装
		ExpandableListView mListView = new ExpandableListView(mService);
		//TODO ここでPlayListを読み込む
		mListView.setAdapter(new PlayListForExpandableListAdapter(mService, null));
		mListView.setOnChildClickListener(new PlayListOnChildClickImpl(mService,mpwpl));
		
		return mListView;
	}
	
}
