package com.abs104a.mperwithsideproject.viewctl.listener;

import java.util.ArrayList;

import com.abs104a.mperwithsideproject.R;
import com.abs104a.mperwithsideproject.adapter.MusicViewPagerAdapter;
import com.abs104a.mperwithsideproject.music.Music;
import com.abs104a.mperwithsideproject.music.PlayList;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;

public final class DeletePlayListOnClickImpl implements OnClickListener  {

	//アプリケーションのコンテキスト
	private Context mContext;
	//rootView(MusicPlayerView)
	private View rootView;
	//GroupPosition
	private int groupPosition;
	//ChildPosition
	private int childPosition;
	//Music PlayList
	private ArrayList<PlayList> playLists;

	/**
	 * インスタンスの生成
	 * @param mContext	アプリケーションのコンテキスト
	 * @param childPosition 
	 * @param groupPosition 
	 * @param rootView 
	 */
	public DeletePlayListOnClickImpl(Context mContext, View rootView,ArrayList<PlayList> playLists, int groupPosition, int childPosition) {
		this.mContext = mContext;
		this.rootView = rootView;
		this.groupPosition = groupPosition;
		this.childPosition = childPosition;
		this.playLists = playLists;
	}

	/**
	 * ボタンがクリックされたとき
	 * プレイリストへの追加を行う.
	 */
	@Override
	public void onClick(View v) {
		//TODO 確認画面
		//PlayList消去を行う
		Music result = playLists.get(groupPosition).removeMusic(childPosition);
		
		if(result != null){
			//通知する．
			Toast.makeText(mContext, result.getTitle() + " Removed.", Toast.LENGTH_SHORT).show();
		}else{
			Toast.makeText(mContext,  "Music is not found.", Toast.LENGTH_SHORT).show();
		}
		
		//ViewPager の設定
		ViewPager mViewPager = (ViewPager)rootView.findViewById(R.id.player_list_part);
		//Viewへの反映
		((MusicViewPagerAdapter)mViewPager.getAdapter()).notifitionDataSetChagedForQueueView();
	}
	
}
