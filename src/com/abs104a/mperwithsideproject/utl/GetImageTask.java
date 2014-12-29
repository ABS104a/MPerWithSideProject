package com.abs104a.mperwithsideproject.utl;

import java.io.FileNotFoundException;
import java.io.InputStream;

import android.content.ContentResolver;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;

/**
 * Imageを取得するリスナ
 * @author Kouki-Mobile
 *
 */
public class GetImageTask extends AsyncTask<Uri, Void, Bitmap> {
	
	private final Context context;
	private final OnGetImageListener listener;
	
	public interface OnGetImageListener{
		public void onGetImage(Bitmap image);
	}

	public GetImageTask(Context context,OnGetImageListener listener){
		this.context = context;
		this.listener = listener;
	}
	
	@Override
	protected Bitmap doInBackground(Uri... params) {
		if(params == null || params.length == 0)return null;
		try{
		    ContentResolver cr = context.getContentResolver();
		    InputStream is = cr.openInputStream(params[0]);
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
		if(listener != null){
			listener.onGetImage(result);
		}
		super.onPostExecute(result);
	}

}
