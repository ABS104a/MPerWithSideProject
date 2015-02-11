package com.abs104a.mperwithsideproject.viewctl.listener;

import com.abs104a.mperwithsideproject.Column;
import com.abs104a.mperwithsideproject.adapter.MusicViewPagerAdapter;
import com.abs104a.mperwithsideproject.utl.VisualizerUtil;
import com.abs104a.mperwithsideproject.viewctl.MusicViewCtl;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;

/**
 * ViewPagerのページが切り替わったときに呼び出されるリスナ．
 * @author Kouki
 *
 */
public final class ViewPagerOnPagerChangeImpl implements OnPageChangeListener {

	private final ViewPager mViewPager;
	private final VisualizerUtil util;

	public ViewPagerOnPagerChangeImpl(ViewPager mViewPager,VisualizerUtil util) {
		this.mViewPager = mViewPager;
		this.util = util;
	}

	@Override
	public void onPageScrollStateChanged(int arg0) {
		// TODO 自動生成されたメソッド・スタブ

	}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {
		// TODO 自動生成されたメソッド・スタブ

	}

	/**
	 * ページが切り替わったとき
	 */
	@Override
	public void onPageSelected(int index) {
		//android.util.Log.v("ViewPager Count",mViewPager.getChildCount()+"");
		MusicViewPagerAdapter adapter = (MusicViewPagerAdapter)mViewPager.getAdapter();
		adapter.notifitionDataSetChagedForQueueView();
		MusicViewCtl.setPageCount(index);
		if(index == Column.EQUALIZER){
			//EQUALIZERViewのとき
			util.createMusicVisualizer(mViewPager.getContext());
		}else{
			util.setIsVisualizer(false);
		}
		System.gc();
	}

}
