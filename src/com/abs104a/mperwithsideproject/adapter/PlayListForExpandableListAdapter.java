package com.abs104a.mperwithsideproject.adapter;

import java.util.ArrayList;

import com.abs104a.mperwithsideproject.R;
import com.abs104a.mperwithsideproject.music.Music;
import com.abs104a.mperwithsideproject.music.MusicPlayerWithQueue;
import com.abs104a.mperwithsideproject.music.PlayList;
import com.abs104a.mperwithsideproject.utl.DisplayUtils;
import com.abs104a.mperwithsideproject.utl.GetJacketImageTask;
import com.abs104a.mperwithsideproject.utl.ImageCache;
import com.abs104a.mperwithsideproject.viewctl.listener.MusicOnClickImpl;
import com.abs104a.mperwithsideproject.viewctl.listener.PlayListAddOnClickImpl;
import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
	
	//アプリケーションコンテキスト
	private final Context mContext;
	//プレイリスト
	private final ArrayList<PlayList> playLists;
	//MusicControllerClass
	private final MusicPlayerWithQueue mpwpl;
	//RootView
	private final View rootView;
	
	/**
	 * インスタンスの生成
	 * @param mContext　アプリケーションのコンテキスト
	 * @param playLists	プレイリスト
	 * @param rootView アプリケーションのView
	 * @param mpwpl アプリケーションの音楽コントロールインスタンス
	 */
	public PlayListForExpandableListAdapter(Context mContext,ArrayList<PlayList> playLists
			,View rootView,MusicPlayerWithQueue mpwpl){
		this.mContext = mContext;
		this.playLists = playLists;
		this.mpwpl = mpwpl;
		this.rootView = rootView;
		
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
		Music item = playLists.get(groupPosition).getMusics()[childPosition];
		ChildHolder holder;
		//ViewがNullの時は新しく生成する
		if(convertView == null){
			LayoutInflater layoutInflater = LayoutInflater.from(mContext);
			convertView = layoutInflater.inflate(R.layout.album_item_row, null);
			holder = new ChildHolder();
			holder.addButton   = (ImageButton) convertView.findViewById(R.id.imageButton_album_add);
			
			holder.albumText   = (TextView) convertView.findViewById(R.id.textView_album_album);
			holder.albumText.setEllipsize(TextUtils.TruncateAt.MARQUEE);
			holder.albumText.setSingleLine(true);
			holder.albumText.setMarqueeRepeatLimit(5);
			holder.albumText.setSelected(true);
			
			holder.artistText  = (TextView) convertView.findViewById(R.id.textView_album_artist);
			holder.jacketImage = (ImageView) convertView.findViewById(R.id.imageView_album_jacket);
			holder.timeText    = (TextView) convertView.findViewById(R.id.textView_album_time);
			holder.titleText   = (TextView) convertView.findViewById(R.id.textView_album_title);
			
			convertView.setTag(holder);
		}else{
			//取得したViewがNullでない時
			holder = (ChildHolder) convertView.getTag();
		}
		
		if(item != null){
			//テキスト情報の入力
			holder.albumText.setText(item.getAlbum());
			holder.artistText.setText(item.getArtist());
			holder.timeText.setText(
					DisplayUtils.long2TimeString(item.getDuration()));
			holder.titleText.setText(item.getTitle());
			
			//ジャケット画像 バックグラウンドに
			if(ImageCache.isCache(item.getAlbum())){
				//キャッシュがヒットすればそれを使う
				holder.jacketImage.setImageBitmap(ImageCache.getImage(item.getAlbum()));
			}else{
				//キャッシュにない場合は新たに取得
				if(item.getAlbumUri() != null){
					holder.jacketImage.setImageResource(android.R.drawable.ic_menu_search);
					new GetJacketImageTask(mContext, holder.titleText, holder.jacketImage, item).execute();
					//holder.jacketImage.setImageURI(item.getAlbumUri());
				}else
					holder.jacketImage.setImageResource(android.R.drawable.ic_menu_search);
			}
			//ボタンを表示するかどうかの設定
			PlayListAddOnClickImpl plimpl = new PlayListAddOnClickImpl(mContext,rootView, item, mpwpl);
			holder.addButton.setOnClickListener(plimpl);
			holder.addButton.setOnLongClickListener(plimpl);

			convertView.setOnClickListener(new MusicOnClickImpl(mContext, rootView, item, mpwpl,true));
			
			Music currentMusic = mpwpl.getNowPlayingMusic();
			if(currentMusic != null && item.equals(currentMusic)){
				//再生中の曲がカラムと同じ場合
				convertView.setBackgroundResource(R.color.listview_current_row);
			}else{
				//再生中の曲がカラムと違う場合
				convertView.setBackgroundResource(R.color.transparent);
			}
		}
		return convertView;
	}

	/**
	 * 指定したグループの子要素を取得
	 */
	@Override
	public int getChildrenCount(int groupPosition) {
		Music[] pLists = playLists.get(groupPosition).getMusics();
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
