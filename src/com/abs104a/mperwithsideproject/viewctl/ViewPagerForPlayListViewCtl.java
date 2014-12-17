package com.abs104a.mperwithsideproject.viewctl;

import java.util.ArrayList;

import com.abs104a.mperwithsideproject.R;
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
 * @author Kouki-Mobile
 *
 */
public final class ViewPagerForPlayListViewCtl {
	
	/**
	 * プレイリストViewを生成する
	 * @param mServie	親となるサービスのコンテキスト
	 * @return	生成したView
	 */
	public final static ExpandableListView createView(Service mService,View rootView ,MusicPlayerWithQueue mpwpl){
		//TODO　ルーチンの実装
		ExpandableListView mListView = new ExpandableListView(mService);
		//TODO ここでPlayListを読み込む
		ArrayList<PlayList> pList = FileUtils.readSerializablePlayList(mService);
		mListView.setAdapter(new PlayListForExpandableListAdapter(mService, pList, rootView, mpwpl));
		mListView.setOnChildClickListener(new PlayListOnChildClickImpl(mService,mpwpl));
		TextView textView = new TextView(mService);
		textView.setText(R.string.row_empty);
		mListView.setEmptyView(textView);

		return mListView;
	}
	
}
