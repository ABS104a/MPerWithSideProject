package com.abs104a.mperwithsideproject.viewctl;

import com.abs104a.mperwithsideproject.music.MusicPlayerWithQueue;
import com.abs104a.mperwithsideproject.utl.MusicUtils;

import android.content.Context;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.ListView;

public class PagerHolder {
	public static final int QUEUE = 0;
	public static final int PLAYLIST = 1;
	public static final int ALBUM = 2;
	public static final int EQUALIZER = 3;
	
	public View getView(Context mContext,int index){
		MusicPlayerWithQueue mpwpl = MusicUtils.getMusicController(mContext);
		View mView = MusicViewCtl.getPlayerView();
		View view = null;
		switch(index){
		case QUEUE:	//Page1
			view = (ListView) getQueueView(mContext);
			break;
		case PLAYLIST:	//Page2
			view = (ExpandableListView) new ViewPagerForPlayListViewCtl().createView(mContext, mView, mpwpl);
			break;
		case ALBUM:	//Page3
			view = (ListView) new ViewPagerForAlbumViewCtl().createView(mContext,mView, mpwpl);
			break;
		case EQUALIZER:	//Page4
			view = new ViewPagerForEqualizerViewCtl().createView(mContext, mpwpl);
			break;
		}
		return view;
	}
	
	public View getQueueView(Context mContext){
		MusicPlayerWithQueue mpwpl = MusicUtils.getMusicController(mContext);
		View mView = MusicViewCtl.getPlayerView();
		return new ViewPagerForPlayingQueueViewCtl().createView(mContext, mView, mpwpl);
	}
	
	public View getPlayListView(Context mContext){
		MusicPlayerWithQueue mpwpl = MusicUtils.getMusicController(mContext);
		View mView = MusicViewCtl.getPlayerView();
		return new ViewPagerForPlayListViewCtl().createView(mContext, mView, mpwpl);
	}
	
	public View getAlbumView(Context mContext){
		MusicPlayerWithQueue mpwpl = MusicUtils.getMusicController(mContext);
		View mView = MusicViewCtl.getPlayerView();
		return new ViewPagerForAlbumViewCtl().createView(mContext, mView, mpwpl);
	}
}
