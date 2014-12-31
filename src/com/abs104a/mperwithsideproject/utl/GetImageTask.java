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
	private final int size;
	
	public interface OnGetImageListener{
		public void onGetImage(Bitmap image);
	}

	public GetImageTask(Context context,int picSize,OnGetImageListener listener){
		this.context = context;
		this.listener = listener;
		this.size = picSize;
	}
	
	@Override
	protected Bitmap doInBackground(Uri... params) {
		if(params == null || params.length == 0)return null;
		try{
		    ContentResolver cr = context.getContentResolver();
		    InputStream is = cr.openInputStream(params[0]);
		    return DisplayUtils.resizeBitmap(BitmapFactory.decodeStream(is), size);
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
