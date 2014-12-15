package com.abs104a.mperwithsideproject.utl;

import java.io.FileNotFoundException;
import java.io.InputStream;

import com.abs104a.mperwithsideproject.music.Music;

import android.R;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
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
	private final Music item;
	private final ImageView jacketImage;
	private final TextView titleText;

	public GetJacketImageTask(Context context, TextView titleText,
			ImageView jacketImage, Music item) {
		this.context = context;
		this.item = item;
		this.jacketImage = jacketImage;
		this.titleText = titleText;
	}

	@Override
	protected Bitmap doInBackground(Void... params) {
		Uri albumArtUri = Uri.parse(
		        "content://media/external/audio/albumart");
		Uri album1Uri = ContentUris.withAppendedId(albumArtUri, item.getAlbumId());
		try{
		    ContentResolver cr = context.getContentResolver();
		    InputStream is = cr.openInputStream(album1Uri);
		    return BitmapFactory.decodeStream(is);
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
			if(titleText.getText().equals(item.getTitle())){
				jacketImage.setImageBitmap(result);
				Animation anim = AnimationUtils.loadAnimation(context,R.anim.fade_in);
				jacketImage.startAnimation(anim);
			}
		}catch(NullPointerException e){ }
		super.onPostExecute(result);
	}

}
