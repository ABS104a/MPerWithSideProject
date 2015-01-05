package com.abs104a.mperwithsideproject.viewctl;

import java.util.ArrayList;

import com.abs104a.mperwithsideproject.R;
import com.abs104a.mperwithsideproject.adapter.MusicListAdapter;
import com.abs104a.mperwithsideproject.adapter.PlayListForExpandableListAdapter;
import com.abs104a.mperwithsideproject.music.MusicPlayerWithQueue;
import com.abs104a.mperwithsideproject.music.PlayList;
import com.abs104a.mperwithsideproject.utl.DialogUtils;
import com.abs104a.mperwithsideproject.utl.FileUtils;

import android.app.Service;
import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ExpandableListView;
import android.widget.TextView;

/**
 * PlayListのViewをコントロールするクラス
 * TODO　空の場合にPlayListを作成するItemを作成する．
 * TODO　作成したプレイリストの作成，曲の消去を行えるようにする．
 * @author Kouki-Mobile
 *
 */
public final class ViewPagerForPlayListViewCtl{
	
	public static final String TAG = "ViewPagerForPlayListViewCtl";
	
	public static ExpandableListView mListView = null;

	//プレイリスト
	private static ArrayList<PlayList> pList = null;
	
	public static ArrayList<PlayList> getPlayList(Context mContext){
		if(pList == null){
			pList = FileUtils.readSerializablePlayList(mContext);	
		}
		return pList;
	}
	
	public static void writePlayList(Context con){
		if(pList != null)
			FileUtils.writeSerializablePlayList(con, pList);
	}
	
	/**
	 * プレイリストViewを生成する
	 * @param mServie	親となるサービスのコンテキスト
	 * @return	生成したView
	 */
	public final static ExpandableListView createView(final Service mService,View rootView ,MusicPlayerWithQueue mpwpl){
		//　ルーチンの実装
		mListView = new ExpandableListView(mService);
		//ここでPlayListを読み込む
		pList = getPlayList(mService);
		android.util.Log.v(TAG , "ListNum : " + (pList != null ? pList.size() : "null"));
		//adapterのセット
		mListView.setAdapter(new PlayListForExpandableListAdapter(mService, pList, rootView, mpwpl,MusicListAdapter.PLAYLIST));
		//EmptyViewのセット
		ExpandableListView.LayoutParams params = new ExpandableListView.LayoutParams(
				ExpandableListView.LayoutParams.MATCH_PARENT,
				mService.getResources().getDimensionPixelSize(R.dimen.album_item_height));
		
		TextView textView = new TextView(mService);
		textView.setText(R.string.row_empty);
		textView.setLayoutParams(params);
		textView.setGravity(Gravity.CENTER);
		mListView.setEmptyView(textView);
		
		TextView addView = new TextView(mService);
		addView.setText(R.string.create_playlist);
		addView.setLayoutParams(params);	
		addView.setGravity(Gravity.CENTER);
		mListView.addFooterView(addView);
		mListView.setDividerHeight(mService.getResources().getDimensionPixelSize(R.dimen.listview_divider));
	
		mListView.setOnItemClickListener(new OnItemClickListener(){

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				//Dialogの生成
				DialogUtils.createPlayListDialog(mService, null, pList);
				android.util.Log.v(TAG,"position : " + arg2);
			}
			
		});
		
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
		ArrayList<PlayList> pList = getPlayList(mService);
		android.util.Log.v(TAG + " Update" , "ListNum : " + (pList != null ? pList.size() : "null"));
		mListView.setAdapter(new PlayListForExpandableListAdapter(mService, pList, rootView, mpwpl,MusicListAdapter.PLAYLIST));
	}
	
}
