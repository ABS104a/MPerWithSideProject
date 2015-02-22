package com.abs104a.mperwithsideproject.utl;

import android.content.Context;
import android.graphics.Bitmap;
import android.media.audiofx.Visualizer;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.abs104a.mperwithsideproject.R;
import com.abs104a.mperwithsideproject.music.MusicPlayerWithQueue;
import com.abs104a.mperwithsideproject.view.FFTView;
import com.abs104a.mperwithsideproject.viewctl.MusicViewCtl;
import com.abs104a.mperwithsideproject.viewctl.listener.MyOnDataCaptureImpl;


/**
 * Visualizerのを管理するUtils
 * @author ABS104a
 *
 */
public class VisualizerUtils {

	
	private Visualizer mVisualizer = null;
	private static boolean isFFT = false;
	private static boolean isWave = true;
	
	//Tag
	public static final String TAG = "VisualizerUtils";

	//前に取得したジャケットの画像のアルバム名
	private static String oldAlbumName = null;
	
	/**
	 * Visualizerを作成する．
	 * @param context
	 * @return Visualizer
	 */
	public final Visualizer createMusicVisualizer(final Context context){
		
		removeMusicVisualizer();
		
		View playerView = MusicViewCtl.getPlayerView();
		if(playerView == null) return null;
		FFTView fftView = (FFTView)playerView.findViewById(R.id.fftview_equalizer);
		if(fftView == null) return null;
		final int captureRate = Visualizer.getMaxCaptureRate();
		
		final MusicPlayerWithQueue mpwpl = MusicUtils.getMusicController(context);
		//再生している曲がない場合はViewを作成しない
		if(mpwpl.getMediaPlayerSessionId() == -1)return null;
		final Visualizer mVisualizer = new Visualizer(mpwpl.getMediaPlayerSessionId());
		mVisualizer.setEnabled(false);
		
		mVisualizer.setCaptureSize(Visualizer.getCaptureSizeRange()[1]);
		
		//キャプチャしたデータを定期的に取得するリスナーを設定
        mVisualizer.setDataCaptureListener(new MyOnDataCaptureImpl(fftView),
        captureRate, //キャプチャーデータの取得レート（ミリヘルツ）
        isWave,//waveFrom
        isFFT);//fftFlag
        
        (fftView).setSamplingRate(captureRate);
        if(mpwpl.getNowPlayingMusic() != null){
        	
        	final ImageView imageView = (ImageView)((ViewGroup)fftView.getParent()).findViewById(R.id.imageview_equalizer);
        	if(imageView != null && imageView.getDrawable() == null)
        		oldAlbumName = new String();
        	
        	if(oldAlbumName == null)
        		oldAlbumName = new String();
        	
        	if(!oldAlbumName.equals(mpwpl.getNowPlayingMusic().getAlbum())){
        		oldAlbumName = mpwpl.getNowPlayingMusic().getAlbum();
        		new GetImageTask(
        				context, 
        				context.getResources().getDimensionPixelSize(R.dimen.visualizer_view_height), 
        				new GetImageTask.OnGetImageListener() {

        			@Override
        			public void onGetImage(Bitmap image) {
        				try{
        					if(imageView != null){
        						//ジャケット画像を表示する．
        						Animation anim = AnimationUtils.loadAnimation(context, android.R.anim.fade_in);
        						imageView.setImageBitmap(image);
        						imageView.startAnimation(anim);
        					}else{
        						image.recycle();
        						image = null;
        					}
        				}catch(NullPointerException e){
        					android.util.Log.e(TAG,e.getMessage());
        				}
        			}
        		}).execute(mpwpl.getNowPlayingMusic().getAlbumUri());
        	}
        }
        mVisualizer.setEnabled(true);
        this.mVisualizer = mVisualizer;
		return mVisualizer;
	}
	
	/**
	 * Visualizerを解放する
	 */
	public void removeMusicVisualizer(){
		try{
			if(mVisualizer != null){
				mVisualizer.release();
				mVisualizer = null;
				System.gc();
			}
		}catch(Exception e){

		}
	}
	
	/**
	 * 動作モードの変更
	 */
	public void changeActionMode(Context mContext){
		isWave = !isWave;
		isFFT = !isFFT;
		if(mVisualizer != null)
			mVisualizer.setEnabled(false);
		this.createMusicVisualizer(mContext);
	}
	
	public void setIsVisualizer(boolean isEnabled){
		try{
			if(mVisualizer != null){
				mVisualizer.setEnabled(isEnabled);
			}
		}catch(Exception e){

		}
	}

	
}
