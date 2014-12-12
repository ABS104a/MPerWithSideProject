package com.abs104a.mperwithsideproject.adapter;

import com.abs104a.mperwithsideproject.R;
import com.abs104a.mperwithsideproject.music.MusicPlayerWithPlayLists;
import com.abs104a.mperwithsideproject.viewctl.ViewPagerForAlbumViewCtl;
import android.app.Service;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
/**
 * MusicPlayerクラスでのViewPagerのView設定を行うクラス
 * 主に提供する機能として
 * ・ページ1	プレイリスト
 * ・ページ2	検索メディア
 * ・ページ3	イコライザAndビジュアライザ
 * とする．
 * @author Kouki-Mobile
 *
 */
public final class MusicViewPagerAdapter extends PagerAdapter {

	//ViewPagerのページ数
	public final static int PAGE_SIZE = 3;
	
	//自身のサービスコンテキスト
	private final Service mService;
	//playerのコントロールクラス
	private final MusicPlayerWithPlayLists mpwpl;
	//rootView
	private final View mView;
	
	/**
	 * 自身のインスタンス生成
	 * @param mService
	 * @param mView 
	 * @param mpwpl
	 */
	public MusicViewPagerAdapter(Service mService,
			View mView, MusicPlayerWithPlayLists mpwpl) {
		this.mService = mService;
		this.mpwpl = mpwpl;
		this.mView = mView;
	}

	/**
	 * ページタイトルの取得
	 */
	@Override
	public CharSequence getPageTitle(int position) {
		switch(position){
		case 0:
			return mService.getText(R.string.viewpager_page_playlist); //プレイリスト
		case 1:
			return mService.getText(R.string.viewpager_page_album); //アルバム
		case 2:
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
		ListView view = null;
		switch(position){
		case 0:	//Page1
			view = new ListView(mService);//(ListView) ViewPagerForPlayListViewCtl.createView(mService, mpwpl);
			break;
		case 1:	//Page2
			view = (ListView) ViewPagerForAlbumViewCtl.createView(mService,mView, mpwpl);
			break;
		case 2:	//Page3
			view = new ListView(mService);//(ListView) ViewPagerForEqualizerViewCtl.createView(mService, mpwpl);
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
		((ViewPager) container).removeView((View) object);
	}

	@Override
	public boolean isViewFromObject(View view, Object org) {
		return view.equals((View)org);
	}

}
