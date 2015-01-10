package com.abs104a.mperwithsideproject.utl;

import java.io.FileNotFoundException;
import java.io.InputStream;

import com.abs104a.mperwithsideproject.R;

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
	
	//アプリケーションのコンテキスト
	private final Context context;
	//イメージを取得した後のリスナ
	private final OnGetImageListener listener;
	//イメージ画像のサイズ
	private final int size;
	
	/**
	 * イメージを取得したときに呼ばれるコールバックInterface
	 * imageが取得されなかったときは呼ばれない．
	 * @author Kouki
	 *
	 */
	public interface OnGetImageListener{
		/**
		 * 画像を取得したとき
		 * @param image	取得した画像
		 */
		public void onGetImage(Bitmap image);
	}

	/**
	 * インスタンスの生成
	 * @param context
	 * @param picSize
	 * @param listener
	 */
	public GetImageTask(Context context,int picSize,OnGetImageListener listener){
		this.context = context;
		this.listener = listener;
		this.size = picSize;
	}
	
	@Override
	protected Bitmap doInBackground(Uri... params) {
		Bitmap result = null;
		if(params != null && params.length > 0){
			try{
				ContentResolver cr = context.getContentResolver();
				InputStream is = cr.openInputStream(params[0]);
				result =  DisplayUtils.resizeBitmap(BitmapFactory.decodeStream(is), size);
			}catch(FileNotFoundException err){

			}
		}
		//イメージが取得できなかった場合
		if(result == null){
			result = BitmapFactory.decodeResource(context.getResources(), R.drawable.no_image);
		}
		return result;
	}

	/* (非 Javadoc)
	 * @see android.os.AsyncTask#onPostExecute(java.lang.Object)
	 */
	@Override
	protected void onPostExecute(Bitmap result) {
		if(listener != null){
			//画像が取得できていればコールバックを呼ぶ
			listener.onGetImage(result);
		}
		super.onPostExecute(result);
	}

}
