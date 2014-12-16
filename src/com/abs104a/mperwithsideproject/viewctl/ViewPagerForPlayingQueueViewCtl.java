package com.abs104a.mperwithsideproject.viewctl;

import java.util.ArrayList;

import android.app.Service;
import android.view.View;
import android.widget.ListView;

import com.abs104a.mperwithsideproject.R;
import com.abs104a.mperwithsideproject.adapter.MusicListAdapter;
import com.abs104a.mperwithsideproject.music.Music;
import com.abs104a.mperwithsideproject.music.MusicPlayerWithQueue;

public class ViewPagerForPlayingQueueViewCtl {

	public final static View createView(
			final Service mService,
			View rootView, final MusicPlayerWithQueue mpwpl)
	{
		//Viewの生成 Listview
		final ListView mListView = new ListView(mService);
		ArrayList<Music> items = mpwpl.getQueue();
		MusicListAdapter adapter = new MusicListAdapter(mService,rootView,items, mpwpl);
		adapter.setButtonForDelete(true);
		mListView.setAdapter(adapter);
		mListView.setDividerHeight(
				mService
				.getResources()
				.getDimensionPixelSize(R.dimen.listview_divider));
		mListView.setDrawingCacheEnabled(true);
		
		android.util.Log.v("Queue", "getQueue count : " + mListView.getAdapter().getCount()+"");
		
		return mListView;
	}
	
}
