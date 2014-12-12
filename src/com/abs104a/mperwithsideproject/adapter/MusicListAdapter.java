package com.abs104a.mperwithsideproject.adapter;

import java.util.List;

import com.abs104a.mperwithsideproject.R;
import com.abs104a.mperwithsideproject.music.Music;
import com.abs104a.mperwithsideproject.music.MusicPlayerWithPlayLists;
import com.abs104a.mperwithsideproject.music.listener.PlayListAddOnClickImpl;
import com.abs104a.mperwithsideproject.utl.DisplayUtils;
import com.abs104a.mperwithsideproject.viewctl.listener.MusicOnClickListener;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
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
	private final MusicPlayerWithPlayLists mpwpl;
	//RootView
	private final View rootView;

	public MusicListAdapter(
			Context context,
			View rootView, List<Music> items,
			MusicPlayerWithPlayLists mpwpl) 
	{
		super(context, LAYOUT, items);
		this.mpwpl = mpwpl;
		this.rootView = rootView;
		
		
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
			
			//TODO ジャケット画像
			if(item.getAlbumUri() != null){
				android.util.Log.v("uri",item.getAlbumUri().toString());
				holder.jacketImage.setImageURI(item.getAlbumUri());
			}
			
			holder.addButton.setOnClickListener(new PlayListAddOnClickImpl(getContext()));
			convertView.setOnClickListener(new MusicOnClickListener(getContext(), rootView, item, mpwpl));
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
