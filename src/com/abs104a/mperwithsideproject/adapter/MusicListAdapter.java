package com.abs104a.mperwithsideproject.adapter;

import java.util.ArrayList;
import com.abs104a.mperwithsideproject.R;
import com.abs104a.mperwithsideproject.music.Music;
import com.abs104a.mperwithsideproject.music.MusicPlayerWithQueue;
import com.abs104a.mperwithsideproject.utl.DialogUtils;
import com.abs104a.mperwithsideproject.utl.DisplayUtils;
import com.abs104a.mperwithsideproject.utl.GetJacketImageTask;
import com.abs104a.mperwithsideproject.utl.ImageCache;
import com.abs104a.mperwithsideproject.viewctl.listener.DeleteOnClickImpl;
import com.abs104a.mperwithsideproject.viewctl.listener.ExpandActionOnClickImpl;
import com.abs104a.mperwithsideproject.viewctl.listener.MusicOnClickImpl;
import com.abs104a.mperwithsideproject.viewctl.listener.PlayListAddOnClickImpl;
import com.abs104a.mperwithsideproject.viewctl.listener.UpDownButtonOnClickImpl;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.TextView;

/**
 * アルバムのリスト管理をするAdapter
 * @author Kouki
 *
 */
public final class MusicListAdapter extends ArrayAdapter<Music> {

	public static final int QUEUE = 0;
	public static final int PLAYLIST = 1;
	public static final int ALBUM = 2;
	public static final int EQUALIZER = 3;
	
	//ミュージックプレイヤーコントロールインスタンス
	private final MusicPlayerWithQueue mpwpl;
	//RootView
	private final View rootView;
	
	private ListAdapter adapter = this;
	private final int column;

	public MusicListAdapter(
			Context context,
			View rootView, ArrayList<Music> items,
			int column,
			MusicPlayerWithQueue mpwpl) 
	{
		super(context, R.layout.album_item_row, items);
		this.mpwpl = mpwpl;
		this.rootView = rootView;
		this.column = column;
	}

	/**
	 * Viewの生成
	 */
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final Music item = getItem(position);
		return getChildView(convertView, item, getContext(), column, rootView, adapter, mpwpl);
	}
	
	
	/**
	 * ListViewのChild用View生成method
	 * @param convertView	母体となるView
	 * @param item			適用するMusicインスタンス
	 * @param context		アプリケーションのコンテキスト
	 * @param isQueue		Queueのカラムかどうか？
	 * @param rootView		アプリケーションのView
	 * @param mpwpl			ミュージックコントロールインスタンス
	 * @return				生成したView
	 */
	public static final View getChildView(
			View convertView,
			final Music item,
			final Context context,
			final int column,
			View rootView,
			Object adapter,
			MusicPlayerWithQueue mpwpl){
		ViewHolder holder;
		if(convertView == null){
			//取得したViewがNullの時
			LayoutInflater inflater = LayoutInflater.from(context);
			inflater = LayoutInflater.from(context);
			convertView = inflater.inflate(R.layout.album_item_row, null);
			holder = new ViewHolder();
			holder.addButton   = (ImageButton) convertView.findViewById(R.id.imageButton_album_add);
			
			holder.albumText   = (TextView) convertView.findViewById(R.id.textView_album_album);
			holder.albumText.setEllipsize(TextUtils.TruncateAt.MARQUEE);
			holder.albumText.setSingleLine(true);
			holder.albumText.setMarqueeRepeatLimit(5);
			holder.albumText.setSelected(true);
			
			holder.addButton.setBackgroundResource(android.R.drawable.ic_input_add);
			
			holder.artistText  = (TextView) convertView.findViewById(R.id.textView_album_artist);
			holder.jacketImage = (ImageView) convertView.findViewById(R.id.imageView_album_jacket);
			holder.timeText    = (TextView) convertView.findViewById(R.id.textView_album_time);
			holder.titleText   = (TextView) convertView.findViewById(R.id.textView_album_title);
			holder.framelayout = (FrameLayout)convertView.findViewById(R.id.framelayout_album);
			
			convertView.setTag(holder);
		}else{
			//取得したViewがNullでない時
			holder = (ViewHolder) convertView.getTag();
		}
		//ItemがNullでないとき，値をViewに格納する．
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
				android.util.Log.v("ImageCache","hitImage!!" + item.getAlbum());
				holder.jacketImage.setImageBitmap(ImageCache.getImage(item.getAlbum()));
			}else{
				//キャッシュにない場合は新たに取得
				if(item.getAlbumUri() != null){
					holder.jacketImage.setImageResource(android.R.drawable.ic_menu_search);
					new GetJacketImageTask(context, holder.titleText, holder.jacketImage, item).execute();
				}else
					holder.jacketImage.setImageResource(android.R.drawable.ic_menu_search);
			}
			//ボタンを表示するかどうかの設定
			if(column == QUEUE || column == PLAYLIST){
				ExpandActionOnClickImpl plimpl = new ExpandActionOnClickImpl(adapter, item);
				holder.addButton.setOnClickListener(plimpl);
				holder.addButton.setOnLongClickListener(plimpl);
			}
			else{
				PlayListAddOnClickImpl plimpl = new PlayListAddOnClickImpl(context,rootView, item, mpwpl,column);
				holder.addButton.setOnClickListener(plimpl);
				holder.addButton.setOnLongClickListener(plimpl);
			}
			convertView.setOnClickListener(new MusicOnClickImpl(rootView, item));
			
			//ExpandViewがすでに展開されている場合は消去する．
			if(!item.isExpandView() && holder.framelayout.getChildCount() > 0){
				holder.framelayout.removeAllViews();
			}else if(item.isExpandView() && holder.framelayout.getChildCount() == 0){
				//ExpandViewの生成
				final LayoutInflater layoutInflater = LayoutInflater.from(context);
				final ViewGroup expandView = (ViewGroup)layoutInflater.inflate(R.layout.expand_album_item, null);

				//Viewを追加する．
				Animation showAnimation = 
						AnimationUtils.loadAnimation(context, android.R.anim.fade_in);
				holder.framelayout.addView(expandView);
				holder.framelayout.startAnimation(showAnimation);

				//TODO columnによって動作を分岐させる．
				
				//消去ボタン
				ImageButton deleteButton = (ImageButton)holder.framelayout.findViewById(R.id.imageButton_expand_delete);
				deleteButton.setOnClickListener(new DeleteOnClickImpl(context, rootView, item, mpwpl));
				
				//上へのボタン
				ImageButton upButton = (ImageButton)holder.framelayout.findViewById(R.id.imageButton_expand_up);
				upButton.setOnClickListener(new UpDownButtonOnClickImpl(context, item, true, false, adapter));
				
				//下へのボタン 
				ImageButton downButton = (ImageButton)holder.framelayout.findViewById(R.id.imageButton_expand_down);
				downButton.setOnClickListener(new UpDownButtonOnClickImpl(context, item, false, true, adapter));
				
				//追加ボタン
				ImageButton addButton = (ImageButton)holder.framelayout.findViewById(R.id.imageButton_expand_add);
				addButton.setOnClickListener(new OnClickListener(){

					@Override
					public void onClick(View v) {
						DialogUtils.createIfSelectPlayListDialog(context, item, column);
					}
					
				});
				
			}
			
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
	 * Viewの行データ保持用クラス
	 * @author Kouki
	 *
	 */
	public final static class ViewHolder{
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
		//ExpandView用Layout
		public FrameLayout framelayout = null;
	}
	


}
