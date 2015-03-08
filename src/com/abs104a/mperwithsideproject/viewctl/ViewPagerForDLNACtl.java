package com.abs104a.mperwithsideproject.viewctl;

import java.util.ArrayList;

import com.abs104a.mperwithsideproject.R;
import com.abs104a.mperwithsideproject.adapter.DisplayItemAdapter;
import com.abs104a.mperwithsideproject.music.MusicPlayerWithQueue;
import com.abs104a.mperwithsideproject.upnp.DisplayItem;
import com.abs104a.mperwithsideproject.viewctl.listener.DLNAOnItemClickListener;

import android.content.Context;
import android.view.View;
import android.widget.ListView;

public class ViewPagerForDLNACtl {
	
	private final static ArrayList<DisplayItem> deviceList = new ArrayList<DisplayItem>();
	
	public final static void clearList(){
		deviceList.clear();
	}
	
	public final static void addList(DisplayItem item){
		if(deviceList.indexOf(item) == -1){
			deviceList.add(item);
			if(mListView != null && ((DisplayItemAdapter)mListView.getAdapter()) != null)
				((DisplayItemAdapter)mListView.getAdapter()).notifyDataSetChanged();
		}
	}
	
	public final static void removeList(DisplayItem item){
		deviceList.remove(item);
		if(mListView != null && ((DisplayItemAdapter)mListView.getAdapter()) != null)
			((DisplayItemAdapter)mListView.getAdapter()).notifyDataSetChanged();
	}
	
	private static ListView mListView = null;

	public View createView(Context mContext, MusicPlayerWithQueue mpwpl){
		
		mListView  = new ListView(mContext);
		mListView.setAdapter(new DisplayItemAdapter(mContext,R.layout.display_item , deviceList));
		mListView.setOnItemClickListener(new DLNAOnItemClickListener(mListView));
		return mListView;
	}
	
	public void initView(DisplayItem[] item){
		//TODO DLNAのAdapterを作成する．
	}
}
