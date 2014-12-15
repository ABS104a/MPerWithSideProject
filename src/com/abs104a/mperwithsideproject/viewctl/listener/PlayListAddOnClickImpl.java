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
import android.widget.Toast;

/**
 * プレイリストに追加するボタンが押された時の動作．
 * 
 * プレイリスト一覧を取得し表示
 * 指定したプレイリストの末尾に追加する．
 * 
 * @author Kouki
 *
 */
public final class PlayListAddOnClickImpl implements OnClickListener, OnLongClickListener {

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
	public PlayListAddOnClickImpl(Context mContext, View rootView, Music item, MusicPlayerWithQueue mpwpl) {
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
		//Queueへの追加を行う
		mpwpl.addMusic(item);
		Toast.makeText(
				mContext, 
				item.getTitle() + " " + mContext.getString(R.string.add_to_queue),
				Toast.LENGTH_SHORT)
				.show();
		//ListViewの変更も行う
		ViewPager viewPager = (ViewPager)rootView.findViewById(R.id.player_list_part);
		if(viewPager != null)
			((MusicViewPagerAdapter)viewPager.getAdapter()).notifitionDataSetChagedForQueueView();
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
