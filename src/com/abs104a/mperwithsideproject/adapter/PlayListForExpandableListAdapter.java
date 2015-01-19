package com.abs104a.mperwithsideproject.adapter;

import java.util.ArrayList;

import com.abs104a.mperwithsideproject.Column;
import com.abs104a.mperwithsideproject.R;
import com.abs104a.mperwithsideproject.music.Music;
import com.abs104a.mperwithsideproject.music.MusicPlayerWithQueue;
import com.abs104a.mperwithsideproject.music.PlayList;
import com.abs104a.mperwithsideproject.utl.GetJacketImageTask;
import com.abs104a.mperwithsideproject.utl.ImageCache;
import com.abs104a.mperwithsideproject.utl.ItemViewFactory;
import com.abs104a.mperwithsideproject.viewctl.MusicViewCtl;
import com.abs104a.mperwithsideproject.viewctl.listener.AddOfPlayListOnLCImpl;
import com.abs104a.mperwithsideproject.viewctl.listener.DeletePlayListOnClickImpl;
import com.abs104a.mperwithsideproject.viewctl.listener.EditOfPlayListOnLCImpl;
import com.abs104a.mperwithsideproject.viewctl.listener.PlayOfPlayListOnLCImpl;
import com.abs104a.mperwithsideproject.viewctl.listener.UpDownForExpandOnClickImpl;

import android.content.Context;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

/**
 * PlayListを表示するためのExpandListView用Adapter
 * @author Kouki
 *
 */
public final class PlayListForExpandableListAdapter extends
	BaseExpandableListAdapter {
	
	//ヘッダーの数
	public final static int HEADER_COUNT = 1;
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
	//ジャケットイメージを取得するインスタンス
	public final GetJacketImageTask getImageTask;
	
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
		getImageTask = new GetJacketImageTask(mContext);
	}
	
	public void addPlayLists(ArrayList<PlayList> lists){
		playLists.addAll(lists);
		this.notifyDataSetChanged();
	}
	
	/**
	 * AdapterのColumnを返す
	 * @return
	 */
	public int getCuolumn(){
		return column;
	}
	
	/**
	 * プレイリストを取得する
	 * @return
	 */
	public ArrayList<PlayList> getPlayLists(){
		return this.playLists;
	}

	/**
	 * 子要素を取得
	 */
	@Override
	public Object getChild(int groupPosition, int childPosition) {
		return playLists.get(groupPosition).getMusics()[childPosition];
	}

	/**
	 * 子IDを取得する．
	 */
	@Override
	public long getChildId(int groupPosition, int childPosition) {
		return 1000 * (groupPosition + 1) + childPosition;
	}

	/**
	 * 子要素のViewを生成する
	 */
	@Override
	public View getChildView(int groupPosition, int childPosition,
			boolean isLastChild, View convertView, ViewGroup parent) {
		//1番目はQueueに追加，2番目はQueueにセット，最後はプレイリストの消去
		
		
		if((column == Column.PLAYLIST || column == Column.ALBUM )&& childPosition == 0){
			final int viewHeight = mContext.getResources().getDimensionPixelSize(R.dimen.album_item_height);
			
			LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(0, viewHeight, 1);
			params.gravity = Gravity.CENTER;
			
			LinearLayout mLayout = new LinearLayout(mContext);
			mLayout.setOrientation(LinearLayout.HORIZONTAL);
			mLayout.setLayoutParams(new ListView.LayoutParams(ListView.LayoutParams.MATCH_PARENT, viewHeight));
			
			//header1 play
			ImageButton playView = new ImageButton(mContext);
			playView.setImageResource(R.drawable.play);
			playView.setLayoutParams(params);
			playView.setBackgroundResource(R.drawable.button);
			//OnClickListenerの実装
			PlayOfPlayListOnLCImpl impl = new PlayOfPlayListOnLCImpl(playLists.get(groupPosition));
			playView.setOnClickListener(impl);
			playView.setOnLongClickListener(impl);
			mLayout.addView(playView);
			
			//header2 add
			ImageButton addView = new ImageButton(mContext);
			addView.setImageResource(R.drawable.add);
			addView.setLayoutParams(params);
			//OnClickListenerの実装
			AddOfPlayListOnLCImpl implp = new AddOfPlayListOnLCImpl(playLists.get(groupPosition));
			addView.setOnClickListener(implp);
			addView.setOnLongClickListener(implp);
			addView.setBackgroundResource(R.drawable.button);
			mLayout.addView(addView);
			
			//header3 Edit
			if(column == Column.PLAYLIST){
				ImageButton editView = new ImageButton(mContext);
				editView.setImageResource(android.R.drawable.ic_menu_preferences);
				editView.setLayoutParams(params);

				//OnClickListenerの実装　（名前の変更，消去）
				EditOfPlayListOnLCImpl eimpl = new EditOfPlayListOnLCImpl(groupPosition, playLists);
				editView.setOnClickListener(eimpl);
				editView.setOnLongClickListener(eimpl);
				editView.setBackgroundResource(R.drawable.button);
				mLayout.addView(editView);
			}
			
			return mLayout;
			
		}else{
			if(convertView instanceof LinearLayout){
				//Header or Footerのrecycleは破棄
				convertView = null;
			}
			if(column == Column.PLAYLIST || column == Column.ALBUM){
				childPosition -= HEADER_COUNT;
			}
			//Viewの生成
			Music item = playLists.get(groupPosition).getMusics()[childPosition];
			View view = ItemViewFactory.getChildView(convertView, item, mContext, column, rootView,this, mpwpl);
			
			//上へのボタンを上書き
			ImageButton upButton = (ImageButton)view.findViewById(R.id.imageButton_expand_up);
			if(upButton != null)
				upButton.setOnClickListener(new UpDownForExpandOnClickImpl(playLists,groupPosition,childPosition, true, false, this));
			
			//下へのボタン を上書き
			ImageButton downButton = (ImageButton)view.findViewById(R.id.imageButton_expand_down);
			if(downButton != null)
				downButton.setOnClickListener(new UpDownForExpandOnClickImpl(playLists,groupPosition,childPosition, false, true, this));
			
			//消去ボタンを上書き
			ImageButton deleteButton = (ImageButton)view.findViewById(R.id.imageButton_expand_delete);
			if(deleteButton != null){//PlayList中の曲を消去するようにする．
				deleteButton.setOnClickListener(new DeletePlayListOnClickImpl(mContext, playLists,groupPosition,childPosition));
				deleteButton.setOnLongClickListener((OnLongClickListener)null);
			}
			
			return view;
		}
	}

	/**
	 * 指定したグループの子要素を取得
	 */
	@Override
	public int getChildrenCount(int groupPosition) {
		Music[] pLists = playLists.get(groupPosition).getMusics();
		if(column == Column.PLAYLIST || column == Column.ALBUM)
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
			convertView = layoutInflater.inflate(R.layout.album_group_row, (ViewGroup) MusicViewCtl.getPlayerView(),false);
			convertView.setBackgroundResource(R.drawable.group_row);
			convertView.setLayoutParams(new AbsListView.LayoutParams(AbsListView.LayoutParams.MATCH_PARENT,AbsListView.LayoutParams.WRAP_CONTENT));
			holder = new GroupHolder();
			holder.albumText = (TextView) convertView.findViewById(R.id.textView_album_album);
			holder.jacketImage = (ImageView)convertView.findViewById(R.id.imageView_album_jacket);
			holder.artistView = (TextView)convertView.findViewById(R.id.textView_album_artist);
			holder.artistView.setEllipsize(TextUtils.TruncateAt.MARQUEE);
			holder.artistView.setSingleLine(true);
			holder.artistView.setMarqueeRepeatLimit(5);
			holder.artistView.setSelected(true);
			holder.expandIndicator = (ImageView)convertView.findViewById(R.id.imageView_album_add);
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
					getImageTask.GetJacketImage(
							holder.albumText,
							holder.jacketImage, 
							group);
				}else
					holder.jacketImage.setImageResource(android.R.drawable.ic_menu_search);
			}
		}
		
		//Viewが広がっているかどうか
		int padding = mContext.getResources().getDimensionPixelSize(R.dimen.album_expand_button_padding);
		if(isExpanded){
			//Expanded
			holder.expandIndicator.setImageResource(R.drawable.close);
			holder.expandIndicator.setPadding(padding, padding, padding,padding + mContext.getResources().getDimensionPixelSize(R.dimen.album_expand_button_top_padding));
		}else{
			//not Expanded
			holder.expandIndicator.setImageResource(R.drawable.open);
			holder.expandIndicator.setPadding(padding, padding + mContext.getResources().getDimensionPixelSize(R.dimen.album_expand_button_top_padding), padding, padding);
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
		public ImageView expandIndicator = null;
		//アーティスト用View
		public TextView artistView = null;
		
	}
}
