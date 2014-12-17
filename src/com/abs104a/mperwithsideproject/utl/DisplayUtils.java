package com.abs104a.mperwithsideproject.utl;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.abs104a.mperwithsideproject.R;
import com.abs104a.mperwithsideproject.music.Music;
import com.abs104a.mperwithsideproject.music.MusicPlayerWithQueue;
import com.abs104a.mperwithsideproject.viewctl.listener.MusicOnClickImpl;
import com.abs104a.mperwithsideproject.viewctl.listener.PlayListAddOnClickImpl;
import com.abs104a.mperwithsideproject.viewctl.listener.PlayListDeleteOnClickImpl;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.PorterDuff.Mode;
import android.text.TextUtils;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * ディスプレイに関するユーティリティクラス
 * @author Kouki
 *
 */
public class DisplayUtils {
	
	//日付のフォーマット
	private final static SimpleDateFormat DFYS = new SimpleDateFormat("mm:ss");

	/**
	 * 画面の幅を取得するクラス
	 * @param mContext アプリケーションのコンテキスト
	 * @return 画面幅
	 */
	public static float getDisplayWidth(Context mContext){
		  //画面サイズ取得の準備
        WindowManager wm = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
        Display disp = wm.getDefaultDisplay();

        Point size = new Point();
        disp.getSize(size);
        final float width = size.x;

        return width;
	}
	
	/**
     * 画像を角丸にする
     * @param bm
     * @return
     */
    public static Bitmap RadiusImage(Bitmap bm){
        int width  = bm.getWidth();
        int height = bm.getHeight();
        int size = Math.min(width, height);
        Bitmap clipArea = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(clipArea);
        c.drawRoundRect(new RectF(0, 0, size, size), size/10, size/10, new Paint(Paint.ANTI_ALIAS_FLAG));
        Bitmap newImage = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(newImage);
        Paint paint = new Paint();
        canvas.drawBitmap(clipArea, 0, 0, paint);
        paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
        canvas.drawBitmap(bm, new Rect(0, 0, size, size), new Rect(0, 0, size, size), paint);
        bm.recycle();
        return newImage;
    }
	
	/**
	 * Longでの時間を，分:秒に変換する
	 * @param time
	 * @return 変換されたString
	 */
	public static final String long2TimeString(final long time){
		Date date = new Date(time);
		return DFYS.format(date);
	}
	
	public static final View getChildView(
			View convertView,
			Music item,
			Context context,
			boolean isDelete,
			View rootView,
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
					//holder.jacketImage.setImageURI(item.getAlbumUri());
				}else
					holder.jacketImage.setImageResource(android.R.drawable.ic_menu_search);
			}
			//ボタンを表示するかどうかの設定
			if(isDelete){
				PlayListDeleteOnClickImpl plimpl = new PlayListDeleteOnClickImpl(context,rootView, item, mpwpl);
				holder.addButton.setOnClickListener(plimpl);
				holder.addButton.setOnLongClickListener(plimpl);
			}
			else{
				PlayListAddOnClickImpl plimpl = new PlayListAddOnClickImpl(context,rootView, item, mpwpl);
				holder.addButton.setOnClickListener(plimpl);
				holder.addButton.setOnLongClickListener(plimpl);
			}
			convertView.setOnClickListener(new MusicOnClickImpl(context, rootView, item, mpwpl,!isDelete));
			
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
