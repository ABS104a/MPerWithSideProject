package com.abs104a.mperwithsideproject.upnp;

import com.abs104a.mperwithsideproject.R;
import com.abs104a.mperwithsideproject.utl.DisplayUtils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;

/**
 * Imageを取得するリスナ
 * @author Kouki-Mobile
 *
 */
public class GetHTTPImageTask extends AsyncTask<String, Void, Bitmap> {
	
	//アプリケーションのコンテキスト
	private final Context context;
	//イメージを取得した後のリスナ
	private final OnGetImageListener listener;
	
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
	public GetHTTPImageTask(Context context,OnGetImageListener listener){
		this.context = context;
		this.listener = listener;
	}
	
	@Override
	protected Bitmap doInBackground(String... params) {
		Bitmap result = null;
		if(params != null && params.length > 0){
			try{
				result = DisplayUtils.resizeBitmap(HttpClient.getImage(params[0]),context.getResources().getDimensionPixelSize(R.dimen.album_item_row_jacket));
			}catch(Exception e){}
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
