package com.abs104a.mperwithsideproject.viewctl.listener;

import com.abs104a.mperwithsideproject.R;
import com.abs104a.mperwithsideproject.adapter.MusicViewPagerAdapter;
import com.abs104a.mperwithsideproject.music.Music;
import com.abs104a.mperwithsideproject.music.MusicPlayerWithQueue;
import com.abs104a.mperwithsideproject.utl.DialogUtils;

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
	private final Context mContext;
	//rootView(MusicPlayerView)
	private final View rootView;
	//music of item
	private final Music item;
	//music controller class
	private final MusicPlayerWithQueue mpwpl;
	private final int column;

	/**
	 * インスタンスの生成
	 * @param mContext	アプリケーションのコンテキスト
	 * @param mpwpl 
	 * @param item 
	 * @param rootView 
	 */
	public PlayListAddOnClickImpl(Context mContext, View rootView, Music item, MusicPlayerWithQueue mpwpl,int column) {
		this.mContext = mContext;
		this.rootView = rootView;
		this.item = item;
		this.column = column;
		this.mpwpl = mpwpl;
	}

	/**
	 * ボタンがクリックされたとき
	 * プレイリストへの追加を行う.
	 */
	@Override
	public void onClick(View v) {
		new DialogUtils().createIfSelectPlayListDialog(mContext, item, column);
	}

	/**
	 * 長押しされたとき
	 */
	@Override
	public boolean onLongClick(View v) {
		//プレイリストへの追加を行う
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
		
		return true;
	}

}
