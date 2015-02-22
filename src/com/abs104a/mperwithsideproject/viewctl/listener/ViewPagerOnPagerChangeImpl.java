package com.abs104a.mperwithsideproject.viewctl.listener;

import com.abs104a.mperwithsideproject.Column;
import com.abs104a.mperwithsideproject.adapter.MusicViewPagerAdapter;
import com.abs104a.mperwithsideproject.utl.VisualizerUtils;
import com.abs104a.mperwithsideproject.viewctl.MusicViewCtl;
import com.abs104a.mperwithsideproject.viewctl.ViewPagerForEqualizerViewCtl;

import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;

/**
 * ViewPagerのページが切り替わったときに呼び出されるリスナ．
 * @author Kouki
 *
 */
public final class ViewPagerOnPagerChangeImpl implements OnPageChangeListener {

	private final ViewPager mViewPager;

	public ViewPagerOnPagerChangeImpl(ViewPager mViewPager) {
		this.mViewPager = mViewPager;
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
		
		View currentView = mViewPager.getChildAt(index);
		
		if(index == Column.EQUALIZER){
			if(currentView != null){
				if(currentView.getTag() != null && currentView.getTag() instanceof VisualizerUtils)
					((VisualizerUtils)currentView.getTag()).createMusicVisualizer(mViewPager.getContext());
				else{
					VisualizerUtils utils = new VisualizerUtils();
					utils.createMusicVisualizer(currentView.getContext());
					currentView.setTag(utils);
				}
			}
		}else{
			if(currentView != null && currentView.getTag() != null){
				//EQUALIZERViewのとき
				((VisualizerUtils)currentView.getTag()).setIsVisualizer(false);
					
			}
		}
		System.gc();
	}

}
