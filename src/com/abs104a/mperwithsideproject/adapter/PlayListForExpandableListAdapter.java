package com.abs104a.mperwithsideproject.adapter;

import java.util.ArrayList;

import com.abs104a.mperwithsideproject.R;
import com.abs104a.mperwithsideproject.music.Music;
import com.abs104a.mperwithsideproject.music.MusicPlayerWithQueue;
import com.abs104a.mperwithsideproject.music.PlayList;
import com.abs104a.mperwithsideproject.utl.GetJacketImageTask;
import com.abs104a.mperwithsideproject.utl.ImageCache;
import com.abs104a.mperwithsideproject.viewctl.listener.EditOfPlayListOnLCImpl;
import com.abs104a.mperwithsideproject.viewctl.listener.PlayOfPlayListOnLCImpl;

import android.content.Context;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * PlayListを表示するためのExpandListView用Adapter
 * @author Kouki
 *
 */
public final class PlayListForExpandableListAdapter extends
	BaseExpandableListAdapter {
	
	
	//定数///////////////////////////////////////////////////////////
	public final static int QUEUE = MusicListAdapter.QUEUE;
	public final static int PLAYLIST = MusicListAdapter.PLAYLIST;
	public final static int ALBUM = MusicListAdapter.ALBUM;
	
	//ヘッダーの数
	public final static int HEADER_COUNT = 2;
	//フッターの数
	public final static int FOOTER_COUNT = 0;
	
	
	//変数///////////////////////////////////////////////////////////
	
	//アプリケーションコンテキスト
	private final Context mContext;
	//プレイリスト
	private final ArrayList<PlayList> playLists;
	//MusicControllerClass
	private final MusicPlayerWithQueue mpwpl;
	//RootView
	private final View rootView;
	//AdapterのColumn
	private final int column;
	
	/**
	 * インスタンスの生成
	 * @param mContext　アプリケーションのコンテキスト
	 * @param playLists	プレイリスト
	 * @param rootView アプリケーションのView
	 * @param mpwpl アプリケーションの音楽コントロールインスタンス
	 */
	public PlayListForExpandableListAdapter(Context mContext,ArrayList<PlayList> playLists
			,View rootView,MusicPlayerWithQueue mpwpl,int column){
		this.mContext = mContext;
		this.playLists = playLists;
		this.mpwpl = mpwpl;
		this.rootView = rootView;
		this.column = column;
	}

	/**
	 * 子要素を取得
	 */
	@Override
	public Object getChild(int groupPosition, int childPosition) {
		return playLists.get(groupPosition).getMusics()[childPosition];
	}

	@Override
	public long getChildId(int groupPosition, int childPosition) {
		return 1000 * groupPosition + childPosition;
	}

	/**
	 * 子要素のViewを生成する
	 */
	@Override
	public View getChildView(int groupPosition, int childPosition,
			boolean isLastChild, View convertView, ViewGroup parent) {
		//TODO 1番目はQueueに追加，2番目はQueueにセット，最後はプレイリストの消去
		if(column == PLAYLIST && childPosition == 0){
			//header1 play
			TextView playView = new TextView(mContext);
			playView.setText(R.string.play_to_playlist);
			playView.setLayoutParams(
					new LayoutParams(
							LayoutParams.MATCH_PARENT,
							mContext.getResources().getDimensionPixelSize(R.dimen.album_item_height)));
			playView.setGravity(Gravity.CENTER);
			//OnClickListenerの実装
			PlayOfPlayListOnLCImpl impl = new PlayOfPlayListOnLCImpl(playLists.get(groupPosition));
			playView.setOnClickListener(impl);
			playView.setOnLongClickListener(impl);
			
			return playView;
			
		}else if(column == PLAYLIST && childPosition == 1){
			//header2 Edit
			TextView editView = new TextView(mContext);
			editView.setText(R.string.edit_to_playlist);
			editView.setLayoutParams(
					new LayoutParams(
							LayoutParams.MATCH_PARENT,
							mContext.getResources().getDimensionPixelSize(R.dimen.album_item_height)));
			editView.setGravity(Gravity.CENTER);
			
			//OnClickListenerの実装　（名前の変更，消去）
			EditOfPlayListOnLCImpl impl = new EditOfPlayListOnLCImpl(groupPosition, playLists);
			editView.setOnClickListener(impl);
			editView.setOnLongClickListener(impl);
			
			return editView;
		}else{
			if(convertView instanceof TextView){
				//Header or Footerのrecycleは破棄
				convertView = null;
			}
			if(column == PLAYLIST){
				childPosition -= HEADER_COUNT;
			}
			//Viewの生成
			Music item = playLists.get(groupPosition).getMusics()[childPosition];
			return MusicListAdapter.getChildView(convertView, item, mContext, column, rootView,this, mpwpl);
		}
	}

	/**
	 * 指定したグループの子要素を取得
	 */
	@Override
	public int getChildrenCount(int groupPosition) {
		Music[] pLists = playLists.get(groupPosition).getMusics();
		if(column == PLAYLIST)
			return pLists == null ? 0 : pLists.length + HEADER_COUNT + FOOTER_COUNT;
		else
			return pLists == null ? 0 : pLists.length;
	}

	/**
	 * Groupのオブジェクトを取得
	 */
	@Override
	public Object getGroup(int groupPosition) {
		return playLists.get(groupPosition);
	}

	/**
	 * グループの数を取得する
	 */
	@Override
	public int getGroupCount() {
		return playLists.size();
	}

	/**
	 * GroupIdを取得
	 */
	@Override
	public long getGroupId(int groupPosition) {
		return groupPosition;
	}

	/**
	 * GroupViewの取得
	 */
	@Override
	public View getGroupView(int groupPosition, boolean isExpanded,
			View convertView, ViewGroup parent) {
		PlayList group = playLists.get(groupPosition);
		GroupHolder holder;
		
		//ViewがNullの時は新しく生成する
		if(convertView == null){
			LayoutInflater layoutInflater = LayoutInflater.from(mContext);
			convertView = layoutInflater.inflate(R.layout.item_row_group, null);
			holder = new GroupHolder();
			holder.albumText = (TextView) convertView.findViewById(R.id.textView_album_album);
			holder.jacketImage = (ImageView)convertView.findViewById(R.id.imageView_album_jacket);
			holder.artistView = (TextView)convertView.findViewById(R.id.textView_album_artist);
			holder.artistView.setEllipsize(TextUtils.TruncateAt.MARQUEE);
			holder.artistView.setSingleLine(true);
			holder.artistView.setMarqueeRepeatLimit(5);
			holder.artistView.setSelected(true);
			holder.expandButton = (ImageButton)convertView.findViewById(R.id.imageButton_album_add);
			holder.expandButton.setVisibility(View.GONE);
			convertView.setTag(holder);
		}else{
			holder = (GroupHolder) convertView.getTag();
		}
		
		//idの設定
		convertView.setId((int )getGroupId(groupPosition));
		
		if(group != null){
			//アルバム名の設定
			holder.albumText.setText(group.getAlbum());
			//アーティスト名の設定
			holder.artistView.setText(group.getArtist());
			//ジャケット画像 バックグラウンドに
			if(ImageCache.isCache(group.getAlbum())){
				//キャッシュがヒットすればそれを使う
				holder.jacketImage.setImageBitmap(ImageCache.getImage(group.getAlbum()));
			}else{
				//キャッシュにない場合は新たに取得
				if(group.getJacketUri() != null){
					holder.jacketImage.setImageResource(android.R.drawable.ic_menu_search);
					new GetJacketImageTask(
							mContext, 
							holder.albumText,
							holder.jacketImage, 
							group)
					.execute();
				}else
					holder.jacketImage.setImageResource(android.R.drawable.ic_menu_search);
			}
		}
		return convertView;
	}

	@Override
	public boolean hasStableIds() {
		// TODO 自動生成されたメソッド・スタブ
		return false;
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		return false;
	}

	/**
	 * ChildViewのタグを保存するホルダー
	 * @author Kouki
	 *
	 */
	public static class ChildHolder{
		//ジャケット用View
		public ImageView jacketImage = null;
		//タイトル用View
		public TextView  titleText   = null;
		//アーティスト用View
		public TextView  artistText  = null;
		//アルバム用View
		public TextView  albumText   = null;
		//曲時間用View
		public TextView  timeText    = null;
		//プレイリスト追加用View
		public ImageButton addButton = null;
	}
	
	/**
	 * GroupViewのタグを保持するホルダー
	 * @author Kouki
	 *
	 */
	public static class GroupHolder{
		//ジャケット用View
		public ImageView jacketImage = null;
		//アルバム名用View
		public TextView albumText = null;
		//要素展開用ボタン
		public ImageButton expandButton = null;
		//アーティスト用View
		public TextView artistView = null;
		
	}
}
