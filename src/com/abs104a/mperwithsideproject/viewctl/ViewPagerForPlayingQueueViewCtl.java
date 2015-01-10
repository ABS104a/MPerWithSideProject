package com.abs104a.mperwithsideproject.viewctl;

import java.util.ArrayList;

import android.content.Context;
import android.view.View;
import android.widget.ListView;

import com.abs104a.mperwithsideproject.Column;
import com.abs104a.mperwithsideproject.R;
import com.abs104a.mperwithsideproject.adapter.MusicListAdapter;
import com.abs104a.mperwithsideproject.music.Music;
import com.abs104a.mperwithsideproject.music.MusicPlayerWithQueue;

public class ViewPagerForPlayingQueueViewCtl {

	public final View createView(
			final Context mContext,
			View rootView, final MusicPlayerWithQueue mpwpl)
	{

		//Viewの生成 Listview
		final ListView mListView = new ListView(mContext);
		ArrayList<Music> items = mpwpl.getQueue();
		MusicListAdapter adapter = new MusicListAdapter(mContext,rootView,items, Column.QUEUE, mpwpl);
		mListView.setAdapter(adapter);
		mListView.setDividerHeight(
				mContext
				.getResources()
				.getDimensionPixelSize(R.dimen.listview_divider));
		mListView.setDrawingCacheEnabled(true);

		android.util.Log.v("Queue", "getQueue count : " + mListView.getAdapter().getCount()+"");

		return mListView;
	}
	
}
