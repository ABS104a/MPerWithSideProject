package com.abs104a.mperwithsideproject.utl;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AbsListView;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.abs104a.mperwithsideproject.Column;
import com.abs104a.mperwithsideproject.R;
import com.abs104a.mperwithsideproject.music.Music;
import com.abs104a.mperwithsideproject.music.MusicPlayerWithQueue;
import com.abs104a.mperwithsideproject.viewctl.MusicViewCtl;
import com.abs104a.mperwithsideproject.viewctl.listener.DeleteOnClickImpl;
import com.abs104a.mperwithsideproject.viewctl.listener.ExpandActionOnClickImpl;
import com.abs104a.mperwithsideproject.viewctl.listener.ItemOnClickImpl;
import com.abs104a.mperwithsideproject.viewctl.listener.PlayListAddOnClickImpl;
import com.abs104a.mperwithsideproject.viewctl.listener.UpDownButtonOnClickImpl;

public class ItemViewFactory {
	
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
		if(convertView == null || convertView instanceof LinearLayout){
			convertView = null;
			//取得したViewがNullの時
			LayoutInflater inflater = LayoutInflater.from(context);
			inflater = LayoutInflater.from(context);
			convertView = inflater.inflate(R.layout.album_item_row, (ViewGroup) MusicViewCtl.getPlayerView(),false);
			convertView.setLayoutParams(new AbsListView.LayoutParams(AbsListView.LayoutParams.MATCH_PARENT,AbsListView.LayoutParams.WRAP_CONTENT));
			holder = new ViewHolder();
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
			if(column == Column.QUEUE || column == Column.PLAYLIST){
				//Expandする．
				ExpandActionOnClickImpl plimpl = new ExpandActionOnClickImpl(adapter, item);
				holder.addButton.setOnClickListener(plimpl);
				holder.addButton.setOnLongClickListener(plimpl);
				holder.addButton.setImageResource(R.drawable.button_open);
			}
			else{
				//Expandしない．
				PlayListAddOnClickImpl plimpl = new PlayListAddOnClickImpl(context,rootView, item, mpwpl,column);
				holder.addButton.setOnClickListener(plimpl);
				holder.addButton.setOnLongClickListener(plimpl);
				holder.addButton.setImageResource(R.drawable.button_add);
			}
			convertView.setOnClickListener(new ItemOnClickImpl(item));
			
			//ExpandViewがすでに展開されている場合は消去する．
			if(!item.isExpandView() && holder.framelayout.getChildCount() > 0){
				holder.framelayout.removeAllViews();
			}else if(item.isExpandView() && holder.framelayout.getChildCount() == 0){
				
				//Indicatorの変更
				holder.addButton.setImageResource(R.drawable.button_close);
				
				//ExpandViewの生成
				final LayoutInflater layoutInflater = LayoutInflater.from(context);
				final ViewGroup expandView = (ViewGroup)layoutInflater.inflate(R.layout.expand_album_item, (ViewGroup) MusicViewCtl.getPlayerView(),false);

				//Viewを追加する．
				Animation showAnimation = 
						AnimationUtils.loadAnimation(context, android.R.anim.fade_in);
				holder.framelayout.addView(expandView);
				holder.framelayout.startAnimation(showAnimation);
				
				//消去ボタン
				ImageButton deleteButton = (ImageButton)holder.framelayout.findViewById(R.id.imageButton_expand_delete);
				deleteButton.setOnClickListener(new DeleteOnClickImpl(context, rootView,column, item, mpwpl));
				
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
						new DialogUtils().createIfSelectPlayListDialog(context, item, column);
						item.setExpandView(false);
					}
					
				});
				
			}
			
			Music currentMusic = mpwpl.getNowPlayingMusic();
			if(currentMusic != null && item.equals(currentMusic)){
				//再生中の曲がカラムと同じ場合　Childのいろを変える．
				convertView.setBackgroundResource(R.drawable.child_current_row);
			}else if(column == Column.ALBUM || column == Column.PLAYLIST){
				convertView.setBackgroundResource(R.drawable.child_row);
			}else{
				convertView.setBackgroundResource(R.drawable.group_row);
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
