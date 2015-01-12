package com.abs104a.mperwithsideproject.utl;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.LinkedList;

import com.abs104a.mperwithsideproject.R;
import com.abs104a.mperwithsideproject.music.Music;
import com.abs104a.mperwithsideproject.music.PlayList;

import android.content.ContentResolver;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * アルバムのジャケットを取得するメソッド
 * 現在使われていません．
 * @author Kouki
 *
 */
public final class GetJacketImageTask{
	
	private final static LinkedList<GetJacketImageItem> itemQueue = new LinkedList<GetJacketImageItem>();
	private static boolean taskFalg = false;
	private final Context mContext;
	
	public GetJacketImageTask(Context mContext){
		this.mContext = mContext;
	}

	public final void GetJacketImage(TextView titleText,
			ImageView jacketImage, Music item) {
		synchronized(itemQueue){
			itemQueue.offer(new GetJacketImageItem( titleText, jacketImage, item));
			if(!taskFalg){
				new GetTask().execute();
			}
		}
	}
    
    public final void GetJacketImage(TextView albumText,
			ImageView jacketImage, PlayList group) {
    	synchronized(itemQueue){
			itemQueue.offer(new GetJacketImageItem( albumText, jacketImage, group));
			if(!taskFalg){
				new GetTask().execute();
			}
		}
	}

    public final class GetTask extends AsyncTask<Void,Object,Void>{
    	
    	@Override
    	protected Void doInBackground(Void... params) {
    		
    		do{
    			GetJacketImageItem item;
    			synchronized(itemQueue){
    				item = itemQueue.poll();
    			}
    			Bitmap bitmap = null;
    			try{
    				ContentResolver cr = mContext.getContentResolver();
    				InputStream is = cr.openInputStream(item.getUri());
    				bitmap = DisplayUtils.resizeBitmap(
    						BitmapFactory.decodeStream(is), 
    						mContext.getResources().getDimensionPixelSize(R.dimen.album_item_row_jacket));
    			}catch(FileNotFoundException err){

    			}
    			publishProgress(bitmap,item);
    		}while(itemQueue.peek() != null);

    		return null;
    	}

    	/* (非 Javadoc)
    	 * @see android.os.AsyncTask#onPostExecute(java.lang.Object)
    	 */
    	@Override
    	protected void onPostExecute(Void result) {
    		super.onPostExecute(result);
    		taskFalg = false;
    	}

    	/* (非 Javadoc)
    	 * @see android.os.AsyncTask#onProgressUpdate(Progress[])
    	 */
    	@Override
    	protected void onProgressUpdate(Object... values) {
    		try{
    			Bitmap image = (Bitmap) values[0];
    			GetJacketImageItem item = (GetJacketImageItem) values[1];
    			if(item == null)return;
    			if(item.getTitleText().getText().equals(item.getConfilmString())){
    				if(image != null){
    					item.getJacketImage().setImageBitmap(image);
    					ImageCache.setImage(item.getAlbum(), image);
    				}else{
    					item.getJacketImage().setImageResource(R.drawable.no_image);
    				}
    			}
    		}catch(NullPointerException e){ }
    		super.onProgressUpdate(values);
    	}

		/* (非 Javadoc)
		 * @see android.os.AsyncTask#onPreExecute()
		 */
		@Override
		protected void onPreExecute() {
			taskFalg = true;
			super.onPreExecute();
		}
    }

}
