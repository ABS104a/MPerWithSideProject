package com.abs104a.mperwithsideproject.viewctl;

import java.util.ArrayList;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.TextView;

import com.abs104a.mperwithsideproject.Column;
import com.abs104a.mperwithsideproject.R;
import com.abs104a.mperwithsideproject.adapter.MusicListAdapter;
import com.abs104a.mperwithsideproject.music.Music;
import com.abs104a.mperwithsideproject.music.MusicPlayerWithQueue;
import com.abs104a.mperwithsideproject.utl.DialogUtils;

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
		
		//Footerの設定
		TextView footerView = new TextView(mContext);
		footerView.setText(R.string.play_to_playlist);
		footerView.setLayoutParams(new AbsListView.LayoutParams(AbsListView.LayoutParams.MATCH_PARENT, AbsListView.LayoutParams.MATCH_PARENT));
		footerView.setGravity(Gravity.CENTER);
		footerView.setBackgroundResource(R.drawable.button);
		footerView.setText(R.string.playlist_to_queue);
		
		footerView.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				new DialogUtils().createPlayListWithQueueDialog(mContext);
			}
			
		});
		
		mListView.addFooterView(footerView);
		android.util.Log.v("Queue", "getQueue count : " + mListView.getAdapter().getCount()+"");

		return mListView;
	}
	
}
