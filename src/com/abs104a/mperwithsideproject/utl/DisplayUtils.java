package com.abs104a.mperwithsideproject.utl;

import java.text.SimpleDateFormat;
import java.util.Date;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.PorterDuff.Mode;
import android.os.Debug;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;

/**
 * ディスプレイに関するユーティリティクラス
 * @author Kouki
 *
 */
public class DisplayUtils {

	
	//日付のフォーマット
	@SuppressLint("SimpleDateFormat") 
	private final static SimpleDateFormat DFYS = new SimpleDateFormat("m:ss");

	public static int px2dp(Context con,float px){
		// WindowManager から取得する (要 Activity)
		DisplayMetrics metrics = new DisplayMetrics();  
		WindowManager mWindowManager = (WindowManager) con.getSystemService(Context.WINDOW_SERVICE);
		mWindowManager.getDefaultDisplay().getMetrics(metrics);  
		 
		float density = con.getResources().getDisplayMetrics().density;
		//  px を dp に変換する ( pixel ÷ density + 0.5f（四捨五入) )
		int dp = (int) (px / density + 0.5f);
		return dp;
	}
	
	public static int dp2px(Context con,int dp){
		// WindowManager から取得する (要 Activity)
		DisplayMetrics metrics = new DisplayMetrics();  
		WindowManager mWindowManager = (WindowManager) con.getSystemService(Context.WINDOW_SERVICE);
		mWindowManager.getDefaultDisplay().getMetrics(metrics);  

		float density = con.getResources().getDisplayMetrics().density;
		
		int px = (int) ((float)dp * density + 0.5f);
		return px;
	}
	
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
	 * 使用ヒープサイズをPrintするメソッド
	 * 現在使用中のヒープサイズと最大ヒープサイズをトレースする．
	 */
	public static final void printHeapSize(){
		// 全体ネイティブサイズ
		long nativeHeap = Debug.getNativeHeapSize() / 1024;
		 
		// 使用中ネイティブヒープサイズ
		long usedNativeHeap = Debug.getNativeHeapAllocatedSize() / 1024;
		 
		// 空きネイティブヒープサイズ
		long emptyNativeHeap = Debug.getNativeHeapFreeSize() / 1024;
		
		// 確保されたDalvikヒープサイズ
		long dalvikHeap = Runtime.getRuntime().totalMemory() / 1024;
		 
		// メモリが足りない場合に確保しようとする最大のDalvikヒープサイズ
		long maxDalvikHeap = Runtime.getRuntime().maxMemory() / 1024;
		 
		// 空きDalvikヒープサイズ
		long emptyDalvikHeap = Runtime.getRuntime().freeMemory() / 1024;
		 
		// 使用中Dalvikヒープサイズ
		long usedDalvikHeap = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
		
		android.util.Log.v("NativeHeap", "Total : " + nativeHeap + "KB / Used : " + usedNativeHeap + "KB / Empty : " + emptyNativeHeap + "KB");
		android.util.Log.v("DalvikHeap", "Total : " + dalvikHeap + "KB / Max : " + maxDalvikHeap + "KB / Used : " + usedDalvikHeap + "KB / Empty : " + emptyDalvikHeap + "KB");
	}
	
	/**
     * 画像を角丸にする
     * @param bm
     * @return
     */
    public synchronized static Bitmap RadiusImage(Bitmap bm){
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
     * 画像をリサイズする
     * リサイズ前のSOURCEは破棄する．
     * @param src	アプリケーションのコンテキスト
     * @param newSize	新しいサイズ
     * @return	リサイズしたBitmap
     */
    public synchronized static Bitmap resizeBitmap(Bitmap src,float newSize){
    	 int srcWidth = src.getWidth(); // 元画像のwidth
         int srcHeight = src.getHeight(); // 元画像のheight

         // 画面サイズを取得する
         Matrix matrix = new Matrix();
         float screenWidth = newSize;
         float screenHeight = newSize;

         //新しいサイズを計算する．
         float widthScale = screenWidth / srcWidth;
         float heightScale = screenHeight / srcHeight;
         if (widthScale > heightScale) {
             matrix.postScale(heightScale, heightScale);
         } else {
             matrix.postScale(widthScale, widthScale);
         }
         
         Bitmap result = Bitmap.createBitmap(src, 0, 0, srcWidth, srcHeight, matrix, true);
         if(!result.equals(src)){
        	 src.recycle();
        	 src = null;
         }
         
         // リサイズ
         return result;
    }
    
    /**
     * 画像を指定したサイズでファイルから読み込む
     * @param filePath	画像のファイルパス
     * @param reqWidth	読み込む画像幅
     * @param reqHeight	読み込む画像高さ
     * @return	読み込んだ画像
     */
    public static Bitmap decodeSampledBitmapFromFile(String filePath, int reqWidth, int reqHeight) {  
    	  
        // inJustDecodeBounds=true で画像のサイズをチェック  
        final BitmapFactory.Options options = new BitmapFactory.Options();  
        options.inJustDecodeBounds = true;  
        BitmapFactory.decodeFile(filePath, options);  
      
        // inSampleSize を計算  
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);  
      
        // inSampleSize をセットしてデコード  
        options.inJustDecodeBounds = false;  
        return BitmapFactory.decodeFile(filePath, options);  
    }  
    
    /**
     * 画像を読み込む際に読み込む画像のスケールを取得する．
     * {@link DisplayUtils#decodeSampledBitmapFromFile(String, int, int)}
     * とセットで使用する．
     * 
     * @param options	BitmapOption
     * @param reqWidth	要求幅
     * @param reqHeight	要求高さ
     * @return	スケール
     */
    private static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {  
    	  
        // 画像の元サイズ  
        final int height = options.outHeight;  
        final int width = options.outWidth;  
        int inSampleSize = 1;  
      
        if (height > reqHeight || width > reqWidth) {  
            if (width > height) {  
                inSampleSize = Math.round((float)height / (float)reqHeight);  
            } else {  
                inSampleSize = Math.round((float)width / (float)reqWidth);  
            }  
        }  
        return inSampleSize;  
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
	
	/**
	 * IMEを表示する．
	 * @param context
	 * @param view
	 */
	public static void showInputMethodEditor(Context context, View view) {
		InputMethodManager inputMethodManager = (InputMethodManager) context
				.getSystemService(Context.INPUT_METHOD_SERVICE);
		inputMethodManager.showSoftInput(view, InputMethodManager.SHOW_FORCED);
	}
	
	/**
	 * IMEを非表示にする．
	 * @param context
	 * @param view
	 */
	public static void hideInputMethodEditor(Context context, View view) {
		InputMethodManager inputMethodManager = (InputMethodManager) context
				.getSystemService(Context.INPUT_METHOD_SERVICE);
		inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
	}
	
	/** 
	 * 指定したビュー階層内のドローワブルをクリアする。 
	 * （ドローワブルをのコールバックメソッドによるアクティビティのリークを防ぐため） 
	 * @param view 
	 */ 
	@SuppressWarnings("deprecation")
	public static final void cleanupView(View view) { 
		if(view instanceof ImageButton) { 
			ImageButton ib = (ImageButton)view; 
			ib.setImageDrawable(null); 
		} else if(view instanceof ImageView) { 
			ImageView iv = (ImageView)view; 
			iv.setImageDrawable(null); 
		} else if(view instanceof SeekBar) { 
			SeekBar sb = (SeekBar)view; 
			sb.setProgressDrawable(null); 
			sb.setThumb(null); 
			// } else if(view instanceof( xxxx )) {  -- 他にもDrawableを使用するUIコンポーネントがあれば追加 
		} 
		view.setBackgroundDrawable(null); 
		if(view instanceof ViewGroup) { 
			ViewGroup vg = (ViewGroup)view; 
			int size = vg.getChildCount(); 
			for(int i = 0; i < size; i++) { 
				cleanupView(vg.getChildAt(i)); 
			} 
		}
		System.gc();
	}
	
	public static final void cleanupImageView(View view) { 
		if(view instanceof ImageView) { 
			ImageView iv = (ImageView)view; 
			iv.setImageDrawable(null); 
			iv.setVisibility(View.GONE);
		} 
		if(view instanceof ViewGroup) { 
			ViewGroup vg = (ViewGroup)view; 
			int size = vg.getChildCount(); 
			for(int i = 0; i < size; i++) { 
				cleanupView(vg.getChildAt(i)); 
			} 
		}
	}
	

	
}
