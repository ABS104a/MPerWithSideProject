package com.abs104a.mperwithsideproject.adapter;

import android.app.Service;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.ListView;

import com.abs104a.mperwithsideproject.R;
import com.abs104a.mperwithsideproject.utl.DisplayUtils;
import com.abs104a.mperwithsideproject.viewctl.PagerHolder;
import com.abs104a.mperwithsideproject.viewctl.ViewPagerForEqualizerViewCtl;
/**
 * MusicPlayerクラスでのViewPagerのView設定を行うクラス
 * 主に提供する機能として
 * ・ページ1	キュー
 * ・ページ2	プレイリスト
 * ・ページ3	検索メディア
 * ・ページ4	イコライザAndビジュアライザ
 * とする．
 * @author Kouki-Mobile
 *
 */
public final class MusicViewPagerAdapter extends PagerAdapter {

	public static final int QUEUE = 0;
	public static final int PLAYLIST = 1;
	public static final int ALBUM = 2;
	public static final int EQUALIZER = 3;
	
	
	//ViewPagerのページ数
	public final static int PAGE_SIZE = 4;
	//自身のサービスコンテキスト
	private final Service mService;
	
	//Queue用のListView;
	private ListView mQueueListView = null;
	//ExpandableListView用のListView;
	private ExpandableListView mPlayListsListView = null;
	
	private ExpandableListView mAlbumView = null;
	
	
	private PagerHolder pageHolder = null;

	
	/**
	 * ListViewの要素を更新する．
	 */
	public void notifitionDataSetChagedForQueueView(){
		if(mQueueListView != null)
			((MusicListAdapter)mQueueListView.getAdapter()).notifyDataSetChanged();
		if(mPlayListsListView != null){
			//ViewPagerForPlayListViewCtl.updateExpandableListViewItems(mService, mView, mpwpl);
			PlayListForExpandableListAdapter adapter = ((PlayListForExpandableListAdapter)mPlayListsListView.getExpandableListAdapter());
			adapter.notifyDataSetChanged();	
		}
		if(mAlbumView != null){
			PlayListForExpandableListAdapter adapter = ((PlayListForExpandableListAdapter)mAlbumView.getExpandableListAdapter());
			adapter.notifyDataSetChanged();	
		}
	}
	
	/**
	 * 自身のインスタンス生成
	 * @param mService
	 * @param mView 
	 * @param mpwpl
	 */
	public MusicViewPagerAdapter(Service mService) {
		this.mService = mService;
		this.pageHolder  = new PagerHolder();
	}

	/**
	 * ページタイトルの取得
	 */
	@Override
	public CharSequence getPageTitle(int position) {
		switch(position){
		case QUEUE:
			return mService.getText(R.string.viewpager_page_playing_queue); //プレイリスト
		case PLAYLIST:
			return mService.getText(R.string.viewpager_page_playlist); //プレイリスト
		case ALBUM:
			return mService.getText(R.string.viewpager_page_album); //アルバム
		case EQUALIZER:
			return mService.getText(R.string.viewpager_page_equalizer); //イコライザ・ビジュアライザ-
		}
		return super.getPageTitle(position);
	}

	/**
	 * ViewPagerのページ数を取得
	 */
	@Override
	public int getCount() {
		return PAGE_SIZE;
	}

	/**
	 * Viewの生成を行う
	 */
	@Override
	public Object instantiateItem(ViewGroup container, int position) {
		//ページごとのViewを生成する
		View view = null;
		switch(position){
		case QUEUE:	//Page1
			view = (ListView) pageHolder.getView(mService, position);
			mQueueListView = (ListView) view;
			break;
		case PLAYLIST:	//Page2
			view = (ExpandableListView) pageHolder.getView(mService, position);
			mPlayListsListView = (ExpandableListView) view;
			break;
		case ALBUM:	//Page3
			view = (ExpandableListView) pageHolder.getView(mService, position);
			mAlbumView = (ExpandableListView) view;
			break;
		case EQUALIZER:	//Page4
			view = pageHolder.getView(mService, position);
			break;
		}
		container.addView(view);
		return view;
	}
	
	/**
	 * Viewの消去を行う
	 */
	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		// ページの消去を行う際に呼ばれるメソッド
		if(position == QUEUE)
			mQueueListView = null;
		else if(position == PLAYLIST)
			mPlayListsListView = null;
		else if(position == ALBUM)
			mAlbumView = null;
		if(position == EQUALIZER){
			ViewPagerForEqualizerViewCtl.removeMusicVisualizer();
		}
		
		DisplayUtils.cleanupView((View) object);
		((ViewPager) container).removeView((View) object);
	}

	@Override
	public boolean isViewFromObject(View view, Object org) {
		return view.equals((View)org);
	}

}
