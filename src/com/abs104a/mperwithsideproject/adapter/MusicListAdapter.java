package com.abs104a.mperwithsideproject.adapter;

import java.util.ArrayList;
import java.util.List;

import com.abs104a.mperwithsideproject.R;
import com.abs104a.mperwithsideproject.music.Music;
import com.abs104a.mperwithsideproject.music.MusicPlayerWithQueue;
import com.abs104a.mperwithsideproject.utl.DisplayUtils;
import com.abs104a.mperwithsideproject.utl.GetJacketImageTask;
import com.abs104a.mperwithsideproject.utl.ImageCache;
import com.abs104a.mperwithsideproject.viewctl.listener.MusicOnClickListener;
import com.abs104a.mperwithsideproject.viewctl.listener.PlayListAddOnClickImpl;
import com.abs104a.mperwithsideproject.viewctl.listener.PlayListDeleteOnClickImpl;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * アルバムのリスト管理をするAdapter
 * @author Kouki
 *
 */
public final class MusicListAdapter extends ArrayAdapter<Music> {

	//このリストのカスタムレイアウト
	private final static int LAYOUT = R.layout.album_item_row;
	//ミュージックプレイヤーコントロールインスタンス
	private final MusicPlayerWithQueue mpwpl;
	//RootView
	private final View rootView;
	//プレイリスト追加ボタンを消去ボタンにするかどうか
	private boolean isDelete = false;

	public MusicListAdapter(
			Context context,
			View rootView, ArrayList<Music> items,
			MusicPlayerWithQueue mpwpl) 
	{
		super(context, LAYOUT, items);
		this.mpwpl = mpwpl;
		this.rootView = rootView;
	}
	
	/**
	 * Viewのカラムにプレイリスト用のボタンを追加するかどうかの設定
	 * true = 消去
	 * false = Queueに追加
	 * default = false;
	 * @param isShow 
	 */
	public void setButtonForDelete(boolean isdelete) {
		this.isDelete = isdelete;
	}
	
	/**
	 * Viewのカラムにプレイリスト用のボタンを追加するかどうかの設定を取得
	 * @return
	 */
	public boolean isButtonForDelete(){
		return isDelete;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final Music item = getItem(position);
		ViewHolder holder;
		if(convertView == null){
			//取得したViewがNullの時
			LayoutInflater inflater = LayoutInflater.from(getContext());
			inflater = LayoutInflater.from(getContext());
			convertView = inflater.inflate(LAYOUT, null);
			holder = new ViewHolder();
			holder.addButton   = (ImageButton) convertView.findViewById(R.id.imageButton_album_add);
			
			holder.albumText   = (TextView) convertView.findViewById(R.id.textView_album_album);
			holder.albumText.setEllipsize(TextUtils.TruncateAt.MARQUEE);
			holder.albumText.setSingleLine(true);
			holder.albumText.setMarqueeRepeatLimit(5);
			holder.albumText.setSelected(true);
			
			if(isDelete){
				holder.addButton.setBackgroundResource(android.R.drawable.ic_input_delete);
			}else{
				holder.addButton.setBackgroundResource(android.R.drawable.ic_input_add);
			}
			
			holder.artistText  = (TextView) convertView.findViewById(R.id.textView_album_artist);
			holder.jacketImage = (ImageView) convertView.findViewById(R.id.imageView_album_jacket);
			holder.timeText    = (TextView) convertView.findViewById(R.id.textView_album_time);
			holder.titleText   = (TextView) convertView.findViewById(R.id.textView_album_title);
			
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
			
			//TODO ジャケット画像 バックグラウンドに
			if(ImageCache.isCache(item.getAlbum())){
				//キャッシュがヒットすればそれを使う
				holder.jacketImage.setImageBitmap(ImageCache.getImage(item.getAlbum()));
			}else{
				//キャッシュにない場合は新たに取得
				if(item.getAlbumUri() != null){
					holder.jacketImage.setImageResource(android.R.drawable.ic_menu_search);
					new GetJacketImageTask(getContext(), holder.titleText, holder.jacketImage, item).execute();
					//holder.jacketImage.setImageURI(item.getAlbumUri());
				}else
					holder.jacketImage.setImageResource(android.R.drawable.ic_menu_search);
			}
			//ボタンを表示するかどうかの設定
			if(isDelete){
				PlayListDeleteOnClickImpl plimpl = new PlayListDeleteOnClickImpl(getContext(),rootView, item, mpwpl);
				holder.addButton.setOnClickListener(plimpl);
				holder.addButton.setOnLongClickListener(plimpl);
			}
			else{
				PlayListAddOnClickImpl plimpl = new PlayListAddOnClickImpl(getContext(),rootView, item, mpwpl);
				holder.addButton.setOnClickListener(plimpl);
				holder.addButton.setOnLongClickListener(plimpl);
			}
			convertView.setOnClickListener(new MusicOnClickListener(getContext(), rootView, item, mpwpl,!isDelete));
			
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
	}

}
