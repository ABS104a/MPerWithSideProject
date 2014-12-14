package com.abs104a.mperwithsideproject.viewctl.listener;

import com.abs104a.mperwithsideproject.R;
import com.abs104a.mperwithsideproject.adapter.MusicViewPagerAdapter;
import com.abs104a.mperwithsideproject.music.Music;
import com.abs104a.mperwithsideproject.music.MusicPlayerWithQueue;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;

public final class PlayListDeleteOnClickImpl implements OnClickListener, OnLongClickListener  {

	//アプリケーションのコンテキスト
	private Context mContext;
	//rootView(MusicPlayerView)
	private View rootView;
	//music of item
	private Music item;
	//music controller class
	private MusicPlayerWithQueue mpwpl;

	/**
	 * インスタンスの生成
	 * @param mContext	アプリケーションのコンテキスト
	 * @param mpwpl 
	 * @param item 
	 * @param rootView 
	 */
	public PlayListDeleteOnClickImpl(Context mContext, View rootView, Music item, MusicPlayerWithQueue mpwpl) {
		this.mContext = mContext;
		this.rootView = rootView;
		this.item = item;
		this.mpwpl = mpwpl;
	}

	/**
	 * ボタンがクリックされたとき
	 * プレイリストへの追加を行う.
	 */
	@Override
	public void onClick(View v) {
		//Queueへの消去を行う
		mpwpl.removeMusic(item);
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
