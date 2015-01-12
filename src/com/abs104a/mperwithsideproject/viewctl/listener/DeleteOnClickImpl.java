package com.abs104a.mperwithsideproject.viewctl.listener;

import com.abs104a.mperwithsideproject.Column;
import com.abs104a.mperwithsideproject.R;
import com.abs104a.mperwithsideproject.adapter.MusicViewPagerAdapter;
import com.abs104a.mperwithsideproject.music.ExpandPosition;
import com.abs104a.mperwithsideproject.music.Music;
import com.abs104a.mperwithsideproject.music.MusicPlayerWithQueue;
import com.abs104a.mperwithsideproject.utl.MusicUtils;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;

public final class DeleteOnClickImpl implements OnClickListener, OnLongClickListener  {

	//rootView(MusicPlayerView)
	private final View rootView;
	//music of item
	private final Music item;
	//music controller class
	private final MusicPlayerWithQueue mpwpl;
	//Itemのcolumn
	private final int column;
	private final ExpandPosition expandposition;

	/**
	 * インスタンスの生成
	 * @param mContext	アプリケーションのコンテキスト
	 * @param expandposition 
	 * @param item 
	 * @param rootView 
	 */
	public DeleteOnClickImpl(Context mContext, View rootView,int column, Music item, ExpandPosition expandposition) {
		this.rootView = rootView;
		this.item = item;
		this.mpwpl = MusicUtils.getMusicController(mContext);;
		this.column = column;
		this.expandposition = expandposition;
	}

	/**
	 * ボタンがクリックされたとき
	 * プレイリストへの消去を行う.
	 */
	@Override
	public void onClick(View v) {
		if(column == Column.QUEUE){
			//Queueへの消去を行う
			mpwpl.removeMusic(item);
			expandposition.setExpandPosition(-1);
		}
		//ViewPager の設定
		ViewPager mViewPager = (ViewPager)rootView.findViewById(R.id.player_list_part);
		//Viewへの反映
		((MusicViewPagerAdapter)mViewPager.getAdapter()).notifitionDataSetChagedForQueueView();
	}

	/**
	 * 長押しされたとき
	 */
	@Override
	public boolean onLongClick(View v) {
		// TODO プレイリストへの追加を行う
		return true;
	}
	
}
