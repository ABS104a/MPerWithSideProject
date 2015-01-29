package com.abs104a.mperwithsideproject.viewctl;

import java.util.ArrayList;

import com.abs104a.mperwithsideproject.Column;
import com.abs104a.mperwithsideproject.R;
import com.abs104a.mperwithsideproject.adapter.PlayListForExpandableListAdapter;
import com.abs104a.mperwithsideproject.music.Album;
import com.abs104a.mperwithsideproject.music.MusicPlayerWithQueue;
import com.abs104a.mperwithsideproject.music.PlayList;
import com.abs104a.mperwithsideproject.utl.MusicUtils;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ExpandableListView;

/**
 * アルバム表示部を作成し，管理するViewController
 * @author Kouki-Mobile
 *
 */
public final class ViewPagerForAlbumViewCtl {

	/**
	 * AlbumViewの作成
	 * @param mService	サービスのコンテキスト
	 * @param mView 
	 * @param mpwpl		playerのコントロール
	 * @return			作成したView
	 */
	public final View createView(
			final Context mContext,
			final View rootView, final MusicPlayerWithQueue mpwpl)
	{
		//ListViewの生成
		final ExpandableListView mListView = new ExpandableListView(mContext);
		
		//LoadingViewの実装
		// Viewからインフレータを作成する
		LayoutInflater layoutInflater = LayoutInflater.from(mContext);
		final View loadingView = layoutInflater.inflate(R.layout.loading_row, (ViewGroup)rootView ,false);
		loadingView.setLayoutParams(new AbsListView.LayoutParams(AbsListView.LayoutParams.MATCH_PARENT, AbsListView.LayoutParams.WRAP_CONTENT));
		ArrayList<PlayList> pList = new ArrayList<PlayList>();
		final PlayListForExpandableListAdapter adapter = new PlayListForExpandableListAdapter(mContext, pList, rootView, mpwpl,Column.ALBUM);
		
		mListView.addHeaderView(loadingView);
		
		mListView.setAdapter(adapter);
		
		new AsyncTask<Void,Void,ArrayList<PlayList>>(){

			@Override
			protected ArrayList<PlayList> doInBackground(Void... params) {
				//ここから非同期
				//Albumの読み込み
				ArrayList<Album> list = MusicUtils.getMusicAlbumList(mContext);
				ArrayList<PlayList> pList = new ArrayList<PlayList>();
				for(Album album : list){
					PlayList mlist = new PlayList(album.getAlbum(), album.getArtist(), album.getAlbumId(), album.getJacketUri());
					mlist.setMusics(album.getMusics());
					pList.add(mlist);
				}
				return pList;
			}

			/* (非 Javadoc)
			 * @see android.os.AsyncTask#onPostExecute(java.lang.Object)
			 */
			@Override
			protected void onPostExecute(ArrayList<PlayList> result) {
				//Footerの消去
				mListView.removeHeaderView(loadingView);
				
				//adapterの設定
				adapter.addPlayLists(result);
				//前回のリストViewのスクロール位置を記憶する．
				SharedPreferences sp = mContext.getSharedPreferences(PagerHolder.TAG, Context.MODE_PRIVATE);
				int firstVisible = sp.getInt("FIRST_VISIBLE", 0);
				mListView.setSelectionFromTop(firstVisible, 0);
				super.onPostExecute(result);
			}
			
		}.execute();
		//ここまで非同期

		
		//パラメーター調整
		mListView.setDividerHeight(mContext.getResources().getDimensionPixelSize(R.dimen.listview_divider));
		
		return mListView;
	}
}
