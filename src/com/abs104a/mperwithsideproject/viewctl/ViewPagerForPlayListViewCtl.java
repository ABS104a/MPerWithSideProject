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
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnGroupCollapseListener;
import android.widget.ExpandableListView.OnGroupExpandListener;
import android.widget.TextView;

/**
 * PlayListのViewをコントロールするクラス
 * TODO　空の場合にPlayListを作成するItemを作成する．
 * TODO　作成したプレイリストの作成，曲の消去を行えるようにする．
 * @author Kouki-Mobile
 *
 */
public final class ViewPagerForPlayListViewCtl implements OnGroupExpandListener, OnGroupCollapseListener {
	
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
		//adapterのセット
		mListView.setAdapter(new PlayListForExpandableListAdapter(mService, pList, rootView, mpwpl,MusicListAdapter.PLAYLIST));
		mListView.setOnChildClickListener(new PlayListOnChildClickImpl(mService,mpwpl));
		
		ViewPagerForPlayListViewCtl mInstance = new ViewPagerForPlayListViewCtl();
		mListView.setOnGroupExpandListener(mInstance);
		mListView.setOnGroupCollapseListener(mInstance);
		
		//EmptyViewのセット
		TextView textView = new TextView(mService);
		textView.setText(R.string.row_empty);
		mListView.setEmptyView(textView);
		
		TextView addView = new TextView(mService);
		addView.setText(R.string.create_playlist);
		addView.setLayoutParams(
				new LayoutParams(
						LayoutParams.MATCH_PARENT,
						mService.getResources().getDimensionPixelSize(R.dimen.album_item_height)));
		addView.setGravity(Gravity.CENTER);
		
		mListView.addFooterView(addView);

		return mListView;
	}
	
	/**
	 * ListViewを更新する．
	 * @param mService
	 * @param rootView
	 * @param mpwpl
	 */
	public static void updateExpandableListViewItems(Service mService,View rootView ,MusicPlayerWithQueue mpwpl){
		//ここでPlayListを読み込む
		if(mListView == null)return;
		ArrayList<PlayList> pList = FileUtils.readSerializablePlayList(mService);
		android.util.Log.v(TAG + " Update" , "ListNum : " + (pList != null ? pList.size() : "null"));
		mListView.setAdapter(new PlayListForExpandableListAdapter(mService, pList, rootView, mpwpl,MusicListAdapter.PLAYLIST));
		mListView.setOnChildClickListener(new PlayListOnChildClickImpl(mService,mpwpl));
	}

	//現在開いているグループのPosition
	private static int currentGroupPos = -1;
	
	/**
	 * 現在開いているグループPositionを取得する．
	 * @return
	 */
	public static final int getCurrentGroupPos(){
		return currentGroupPos;
	}

	@Override
	public void onGroupCollapse(int groupPosition) {
		currentGroupPos = -1;
	}

	@Override
	public void onGroupExpand(int groupPosition) {
		currentGroupPos  = groupPosition;
		android.util.Log.i(TAG, "onGroupExpanded Position : " + currentGroupPos);
	}
	
}
