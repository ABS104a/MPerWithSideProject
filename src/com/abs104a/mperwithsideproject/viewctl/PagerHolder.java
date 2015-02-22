package com.abs104a.mperwithsideproject.viewctl;

import com.abs104a.mperwithsideproject.Column;
import com.abs104a.mperwithsideproject.R;
import com.abs104a.mperwithsideproject.music.MusicPlayerWithQueue;
import com.abs104a.mperwithsideproject.utl.MusicUtils;
import com.abs104a.mperwithsideproject.utl.VisualizerUtils;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.ListView;

/**
 * ViewPagerのおのおののViewを管理，生成するヘルパーメソッド
 * @author Kouki
 *
 */
public class PagerHolder {
	
	//Tag
	public static final String TAG = "PagerHolder";
	
	//Viewを管理するための配列（Viewpagerの各Viewが入る．）
	private final View[] rowView;
	
	/**
	 * インスタンス生成
	 * @param viewCount　ViewPagerのページ数
	 */
	public PagerHolder(int viewCount){
		//データの初期化
		this.rowView = new View[viewCount];
		for(int i = 0;i < rowView.length;i++)
			rowView[i] = null;
	}
	
	/**
	 * ページ番号のViewを取得する
	 * Viewが破棄されているのならNullが返る
	 * @param index	ページ番号
	 * @return	ページのView
	 */
	public View getIndexView(int index){
		return rowView[index];
	}
	
	/**
	 * ViewをClearする．
	 * 主にViewPagerを破棄するときに使う．
	 */
	public void clearView(){
		for(int i = 0;i < rowView.length;i++)
			removeView(i);
	}
	
	/**
	 * ViewPagerのページタイトルとページ数を対応づける．
	 * @param mContext	アプリケーションのコンテキスト
	 * @param index	ページ数
	 * @return	ページタイトル．
	 */
	public CharSequence getString(Context mContext ,int index){
		switch(index){
		case Column.QUEUE:
			return mContext.getText(R.string.viewpager_page_playing_queue); //プレイリスト
		case Column.PLAYLIST:
			return mContext.getText(R.string.viewpager_page_playlist); //プレイリスト
		case Column.ALBUM:
			return mContext.getText(R.string.viewpager_page_album); //アルバム
		case Column.EQUALIZER:
			return mContext.getText(R.string.viewpager_page_equalizer); //イコライザ・ビジュアライザ-
		}
		return new String();
	}
	
	/**
	 * 指定したページ数のViewを消去する．
	 * @param index
	 */
	public void removeView(int index){
		ExpandableListView mAlbumView = (ExpandableListView) rowView[Column.ALBUM];
		if(mAlbumView != null){
			int firstVisiblePosition = mAlbumView.getFirstVisiblePosition();
			SharedPreferences sp = mAlbumView.getContext().getSharedPreferences(TAG, Context.MODE_PRIVATE);
			sp.edit().putInt("FIRST_VISIBLE", firstVisiblePosition).commit();
		}
		if(index == Column.EQUALIZER){
			if(rowView[index] != null && rowView[index].getTag() != null)
				((VisualizerUtils)rowView[index].getTag()).removeMusicVisualizer();
			//ViewPagerForEqualizerViewCtl.removeMusicVisualizer();
		}
		rowView[index] = null;
	}
	
	
	/**
	 * 指定したページ番号に対応するViewを生成する．
	 * @param mContext	アプリケーションのコンテキスト
	 * @param index		ページ番号
	 * @return			生成したView
	 */
	public View createView(Context mContext,int index){
		View view = null;
		switch(index){
		case Column.QUEUE:	//Page1
			view = (ListView) createQueueView(mContext);
			break;
		case Column.PLAYLIST:	//Page2
			view = (ExpandableListView) createPlayListView(mContext);
			break;
		case Column.ALBUM:	//Page3
			view = (ListView) createAlbumView(mContext);
			break;
		case Column.EQUALIZER:	//Page4
			view = createEqualizerView(mContext);
			break;
		}
		rowView[index] = view;
		return view;
	}
	
	/**
	 * QueueのViewを生成する
	 * @param mContext	アプリケーションのコンテキスト
	 * @return			QueueのView
	 */
	public View createQueueView(Context mContext){
		MusicPlayerWithQueue mpwpl = MusicUtils.getMusicController(mContext);
		View mView = MusicViewCtl.getPlayerView();
		return new ViewPagerForPlayingQueueViewCtl().createView(mContext, mView, mpwpl);
	}
	
	/**
	 * PlayListのViewを生成する
	 * @param mContext	アプリケーションのコンテキスト
	 * @return			PlayListのView
	 */
	public View createPlayListView(Context mContext){
		MusicPlayerWithQueue mpwpl = MusicUtils.getMusicController(mContext);
		View mView = MusicViewCtl.getPlayerView();
		return new ViewPagerForPlayListViewCtl().createView(mContext, mView, mpwpl);
	}
	
	/**
	 * AlbumのViewを生成する．
	 * @param mContext	アプリケーションのコンテキスト
	 * @return			AlbumのView
	 */
	public View createAlbumView(Context mContext){
		MusicPlayerWithQueue mpwpl = MusicUtils.getMusicController(mContext);
		View mView = MusicViewCtl.getPlayerView();
		ListView mListView = (ListView) new ViewPagerForAlbumViewCtl().createView(mContext, mView, mpwpl);
		return mListView;
	}
	
	/**
	 * EqualizerのViewを生成する
	 * @param mContext	アプリケーションのコンテキスト
	 * @return			EqualizerのView
	 */
	public View createEqualizerView(Context mContext){
		MusicPlayerWithQueue mpwpl = MusicUtils.getMusicController(mContext);
		View mView = new ViewPagerForEqualizerViewCtl().createView(mContext, mpwpl);
		
		VisualizerUtils mVisualizerUtils = new VisualizerUtils();
		mVisualizerUtils.createMusicVisualizer(mContext);
		
		mView.setTag(mVisualizerUtils);
		
		//ViewPagerForEqualizerViewCtl.createMusicVisualizer(mContext);
		return mView;
	}
}
