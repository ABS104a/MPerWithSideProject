package com.abs104a.mperwithsideproject.music.list;

import com.abs104a.mperwithsideproject.music.MusicPlayerWithPlayLists;

import android.app.Service;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
public class MusicViewPagerAdapter extends PagerAdapter {

	//ViewPagerのページ数
	public final static int PAGE_SIZE = 3;
	
	//自身のサービスコンテキスト
	private Service mService;
	//playerのコントロールクラス
	private MusicPlayerWithPlayLists mpwpl;
	
	/**
	 * 自身のインスタンス生成
	 * @param mService
	 * @param mpwpl
	 */
	public MusicViewPagerAdapter(Service mService,
			MusicPlayerWithPlayLists mpwpl) {
		this.mService = mService;
		this.mpwpl = mpwpl;
	}

	/**
	 * ページタイトルの取得
	 */
	@Override
	public CharSequence getPageTitle(int position) {
		switch(position){
		case 0:
			return "page1"; //TODO
		case 1:
			return "page2"; //TODO
		case 2:
			return "page3"; //TODO
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
		// TODO ページごとのViewを生成する
		LayoutInflater inflater = LayoutInflater.from(mService);
		
		switch(position){
		case 0:	//Page1
			break;
		case 1:	//Page2
			break;
		case 2:	//Page3
			break;
		}
		return super.instantiateItem(container, position);
	}
	
	/**
	 * Viewの消去を行う
	 */
	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		// TODO ページの消去を行う際に呼ばれるメソッド
		super.destroyItem(container, position, object);
	}

	@Override
	public boolean isViewFromObject(View view, Object org) {
		// TODO 自動生成されたメソッド・スタブ
		return false;
	}

}
