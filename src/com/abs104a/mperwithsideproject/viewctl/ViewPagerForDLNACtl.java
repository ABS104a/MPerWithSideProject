package com.abs104a.mperwithsideproject.viewctl;

import com.abs104a.mperwithsideproject.music.MusicPlayerWithQueue;

import android.content.Context;
import android.view.View;
import android.widget.ListView;

public class ViewPagerForDLNACtl {
	
	
	public View createView(Context mContext, MusicPlayerWithQueue mpwpl){
		
		ListView mListView = new ListView(mContext);
		return mListView;
	}
}
