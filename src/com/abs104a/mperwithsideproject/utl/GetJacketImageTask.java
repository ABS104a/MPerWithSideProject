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
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
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

	public synchronized Bitmap resizeBitmapToDisplaySize(Bitmap src,float newSize){
        int srcWidth = src.getWidth(); // 元画像のwidth
        int srcHeight = src.getHeight(); // 元画像のheight

        // 画面サイズを取得する
        Matrix matrix = new Matrix();
        float screenWidth = newSize;
        float screenHeight = newSize;

        float widthScale = screenWidth / srcWidth;
        float heightScale = screenHeight / srcHeight;
        if (widthScale > heightScale) {
            matrix.postScale(heightScale, heightScale);
        } else {
            matrix.postScale(widthScale, widthScale);
        }
        
        // リサイズ
        Bitmap bm = Bitmap.createBitmap(src, 0, 0, srcWidth, srcHeight, matrix, true);
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

	@Override
	protected Bitmap doInBackground(Void... params) {
		try{
		    ContentResolver cr = context.getContentResolver();
		    InputStream is = cr.openInputStream(uri);
		    return resizeBitmapToDisplaySize(
		    		BitmapFactory.decodeStream(is), 
		    		context.getResources().getDimension(R.dimen.album_item_row_jacket));
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
			if(result != null && titleText.getText().equals(confilmString)){
				jacketImage.setImageBitmap(result);
				ImageCache.setImage(album, result);
				//Animation anim = AnimationUtils.loadAnimation(context,android.R.anim.fade_in);
				//jacketImage.startAnimation(anim);
			}
		}catch(NullPointerException e){ }
		super.onPostExecute(result);
	}

}
