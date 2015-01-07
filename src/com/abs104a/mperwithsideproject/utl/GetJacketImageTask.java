package com.abs104a.mperwithsideproject.utl;

import java.io.FileNotFoundException;
import java.io.InputStream;

import com.abs104a.mperwithsideproject.R;
import com.abs104a.mperwithsideproject.music.Music;
import com.abs104a.mperwithsideproject.music.PlayList;

import android.content.ContentResolver;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * アルバムのジャケットを取得するメソッド
 * 現在使われていません．
 * @author Kouki
 *
 */
public final class GetJacketImageTask extends AsyncTask<Void,Void,Bitmap>{

	private final Context context;
	private final Uri uri;
	private final String album;
	private final String confilmString;
	private final ImageView jacketImage;
	private final TextView titleText;

	public GetJacketImageTask(Context context, TextView titleText,
			ImageView jacketImage, Music item) {
		this.context = context;
		this.uri = item.getAlbumUri();
		this.confilmString = item.getTitle();
		this.jacketImage = jacketImage;
		this.titleText = titleText;
		this.album = item.getAlbum();
	}
    
    public GetJacketImageTask(Context mContext, TextView albumText,
			ImageView jacketImage, PlayList group) {
    	this.context = mContext;
		this.uri = group.getJacketUri();
		this.confilmString = group.getAlbum();
		this.jacketImage = jacketImage;
		this.titleText = albumText;
		this.album = group.getAlbum();
	}

	@Override
	protected Bitmap doInBackground(Void... params) {
		try{
		    ContentResolver cr = context.getContentResolver();
		    InputStream is = cr.openInputStream(uri);
		    return DisplayUtils.resizeBitmap(
		    		BitmapFactory.decodeStream(is), 
		    		context.getResources().getDimensionPixelSize(R.dimen.album_item_row_jacket));
		}catch(FileNotFoundException err){

		}
		return null;
	}

	/* (非 Javadoc)
	 * @see android.os.AsyncTask#onPostExecute(java.lang.Object)
	 */
	@Override
	protected void onPostExecute(Bitmap result) {
		try{
			if(titleText.getText().equals(confilmString)){
				if(result != null){
					jacketImage.setImageBitmap(result);
					ImageCache.setImage(album, result);
				}else{
					jacketImage.setImageResource(R.drawable.no_image);
				}
			}
		}catch(NullPointerException e){ }
		super.onPostExecute(result);
	}

}
