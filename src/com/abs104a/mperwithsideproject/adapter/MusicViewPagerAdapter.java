package com.abs104a.mperwithsideproject.adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.ListView;

import com.abs104a.mperwithsideproject.Column;
import com.abs104a.mperwithsideproject.viewctl.PagerHolder;
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

	//定数/////////////////////////////////////////////
	
	//Classのタグ
	public final static String TAG = "MusicViewPagerAdapter";
	//ViewPagerのページ数
	public final static int PAGE_SIZE = 4;
	
	//変数/////////////////////////////////////////////
	
	//自身のサービスコンテキスト
	private final Context mContext;
	
	//View生成用の
	private final PagerHolder pageHolder;
	
	/**
	 * ViewのResourceを解放する．
	 */
	public void cleanUp(){
		ExpandableListView mAlbumView = (ExpandableListView) pageHolder.getIndexView(Column.ALBUM);
		ListView QueueView = (ListView) pageHolder.getIndexView(Column.QUEUE);
		if(mAlbumView != null){
			int firstVisiblePosition = mAlbumView.getFirstVisiblePosition();
			SharedPreferences sp = mContext.getSharedPreferences(PagerHolder.TAG, Context.MODE_PRIVATE);
			sp.edit().putInt("FIRST_VISIBLE", firstVisiblePosition).commit();
			mAlbumView.setVisibility(View.GONE);
			//DisplayUtils.cleanupImageView(mAlbumView);
		}
		if(QueueView != null){
			QueueView.setVisibility(View.GONE);
			//DisplayUtils.cleanupImageView(QueueView);
		}
	}

	
	/**
	 * ListViewの要素を更新する．
	 */
	public void notifitionDataSetChagedForQueueView(){
		ListView mQueueListView = (ListView) pageHolder.getIndexView(Column.QUEUE);
		ExpandableListView mPlayListsListView = (ExpandableListView) pageHolder.getIndexView(Column.PLAYLIST);
		ExpandableListView mAlbumView = (ExpandableListView) pageHolder.getIndexView(Column.ALBUM);
		
		//columnに応じた処理を行う．
		if(mQueueListView != null)
			((MusicListAdapter)mQueueListView.getAdapter()).notifyDataSetChanged();
		if(mPlayListsListView != null){
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
	public MusicViewPagerAdapter(Context mContext) {
		this.mContext = mContext;
		this.pageHolder  = new PagerHolder(getCount());
	}

	/**
	 * ページタイトルの取得
	 */
	@Override
	public CharSequence getPageTitle(int position) {
		return pageHolder.getString(mContext, position);
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
		View view = pageHolder.createView(mContext, position);;
		container.addView(view);
		return view;
	}
	
	/**
	 * Viewの消去を行う
	 */
	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		// ページの消去を行う際に呼ばれるメソッド
		pageHolder.removeView(position);
		((ViewPager) container).removeView((View) object);
	}

	@Override
	public boolean isViewFromObject(View view, Object org) {
		return view.equals((View)org);
	}

}
