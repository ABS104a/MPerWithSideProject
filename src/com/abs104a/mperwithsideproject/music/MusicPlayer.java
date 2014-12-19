package com.abs104a.mperwithsideproject.music;

import java.io.IOException;

import com.abs104a.mperwithsideproject.R;
import com.abs104a.mperwithsideproject.viewctl.listener.OnPlayCompletedImpl;

import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.audiofx.Equalizer;

/**
 * 再生を行う音楽リソースを扱うクラス．
 * MediaPlayerクラスのラッパ
 * @author Kouki-Mobile
 *
 */
public class MusicPlayer implements OnCompletionListener {
	
	/**===================================
	 * 定数
	 ===================================*/
	
	//データソースが存在しないとき
	public final static int NOSOURCE = -1;
	
	//再生中
	public final static int PLAYING = 2;
	
	//再生していない（ソース読み込み完了時）
	public final static int NOTPLAYING = 5;
	//ポーズ中
	public final static int PAUSEING = 6;
	//停止中
	public final static int STOPPING = 7;
	
	
	/**===================================
	 * 内部変数
	 ===================================*/
	
	//再生を行うMediaPlayerクラス
	private MediaPlayer mMediaPlayer = null;
	//Equalizerクラス
	private Equalizer mEqualizer = null;
	//Equalizerを有効にするかどうか
	private boolean isEqualizer = false;
	
	private EqualizerItem[] mEqualizerItem = null;
	//現在再生しているかどうか
	private int _status = NOSOURCE;
	
	/**===================================
	 * Impl
	 ===================================*/
	
	/**
	 * 再生が完了した時に呼ばれるリスナImpl
	 * @author Kouki
	 *
	 */
	public interface OnPlayCompletedListener{
		/**
		 * 再生が完了した時
		 */
		public void onPlayCompleted();
	}
	
	/**
	 * 再生が完了した時に呼ばれるリスナ
	 */
	private OnPlayCompletedImpl mOnPlayCompletedListener = null;
	
	/**
	 * 再生完了時に呼ばれるリスナを取得するメソッド
	 * @return
	 */
	public OnPlayCompletedImpl getOnPlayCompletedListener() {
		return mOnPlayCompletedListener;
	}

	/**
	 * 再生完了時に呼ばれるリスナを設定するメソッド
	 * @param onPlayCompletedImpl
	 */
	public void setOnPlayCompletedListener(OnPlayCompletedImpl onPlayCompletedImpl) {
		this.mOnPlayCompletedListener = onPlayCompletedImpl;
	}
	
	
	/**===================================
	 * コントロールメソッド
	 ===================================*/
	
	
	/**
	 * 再生状況を設定する．
	 * @param status
	 * @return
	 */
	protected final int setStatus(int status){
		switch(status){
		case NOSOURCE:
		case PLAYING:
		case NOTPLAYING:
		case PAUSEING:
		case STOPPING:
			this._status = status;
		default:
		}
		return this._status;
	}
	

	/**
	 * 再生状況を取得する
	 * @return
	 */
	public final int getStatus(){
		return _status;
	}
	
	/**
	 * AudioSessionIdを取得する
	 * Visualizer用
	 * @return
	 */
	public final int getMediaPlayerSessionId(){
		if(mMediaPlayer != null)
			return mMediaPlayer.getAudioSessionId();
		else return -1;
	}
	
	/**
	 * ループ状態を設定する．
	 * @param isLoop ループするかどうか
	 * @return ループされているか．MediaPlayerがNullの場合はnullを返す．
	 */
	public Boolean setLoop(boolean isLoop){
		if(mMediaPlayer != null){
			mMediaPlayer.setLooping(isLoop);
			return mMediaPlayer.isLooping();
		}
		return null;
	}
	
	/**
	 * ループ状況を確認する．
	 * @return　ループされているか．MediaPlayerがNullの場合はnullを返す．
	 */
	public Boolean isLoop(){
		if(mMediaPlayer != null)
			return mMediaPlayer.isLooping();
		else
			return null;
	}
	
	/**
	 * データソースを設定する
	 * 
	 * @param pass　音楽ファイルのURI　String
	 * @throws IllegalArgumentException
	 * @throws SecurityException
	 * @throws IllegalStateException
	 * @throws IOException
	 * @return 再生状態（正常出あればSTOPPINGが返る）
	 */
	protected final int setSource(String pass) throws IllegalArgumentException, SecurityException, IllegalStateException, IOException{
		//MediaPlayerのインスタンスを取得
		if(mMediaPlayer == null)
			mMediaPlayer =  new MediaPlayer();
		else{
			//再生の停止
			playStop();
			setStatus(NOSOURCE);
			//リソースを開放する．
			mMediaPlayer.release();
			mMediaPlayer = new MediaPlayer();
		}
		//Equalizerの設定
		initEqualizer(mMediaPlayer);
		
		//データソースの設定
		mMediaPlayer.setDataSource(pass);

		//データ読み込み
		mMediaPlayer.prepare();
		//状況の反映
		setStatus(STOPPING);
		return getStatus();
	}
	
	/**
	 * 現在の再生時間を返す
	 * @return
	 */
	public final int getDuration(){
		return mMediaPlayer != null ? mMediaPlayer.getDuration() : 0;
	}
	
	public final int getCurrentTime(){
		return getStatus() == PLAYING || getStatus() == PAUSEING ? mMediaPlayer.getCurrentPosition() : 0;
	}
	
	public final int setCurrentTime(int progress){
		if(mMediaPlayer == null)return getStatus();
		if(getStatus() == PLAYING || getStatus() == PAUSEING){
			//値の正当性を検証
			if(progress > 0 && progress <= mMediaPlayer.getDuration()){
				mMediaPlayer.seekTo(progress);//シークする
			}
		}
		return getStatus();
	}
	
	/**
	 * 再生する時の動作
	 * @return　現在の状況
	 * @throws IOException 
	 * @throws IllegalStateException 
	 * @throws SecurityException 
	 * @throws IllegalArgumentException 
	 */
	public int playStartAndPause() throws IllegalArgumentException, SecurityException, IllegalStateException, IOException{
		if(mMediaPlayer != null){
			//再生していない場合　（NOTPLAYING or PAUSEING or STOPPING）
			if(getStatus() >= NOTPLAYING){
				//再生する
				//サービスの優先度を設定
				android.os.Process.setThreadPriority(android.os.Process.THREAD_PRIORITY_DISPLAY);
				mMediaPlayer.start();
				mMediaPlayer.setOnCompletionListener(this);
				setStatus(PLAYING);
			}
			//再生中の場合　PLAYING
			else if(getStatus() == PLAYING){
				//ポーズ状態にする
				mMediaPlayer.pause();
				//サービスの優先度を設定
				android.os.Process.setThreadPriority(android.os.Process.THREAD_PRIORITY_DEFAULT);
				setStatus(PAUSEING);
			}
		}
		return getStatus();
	}
	
	/**
	 * ストップする時の動作
	 * @return　現在の状況
	 */
	public final int playStop(){
		//再生中　or　ポーズ中の時　（PLAYING or PAUSEING）
		if(mMediaPlayer != null && 
				(getStatus() == PLAYING || getStatus() == PAUSEING)){
			//再生を停止する．
			mMediaPlayer.stop();
			setStatus(STOPPING);
			//サービスの優先度を設定
			android.os.Process.setThreadPriority(android.os.Process.THREAD_PRIORITY_DEFAULT);
		}
		return getStatus();
	}

	/**
	 * 再生が完了した時
	 */
	@Override
	public void onCompletion(MediaPlayer mp) {
		setStatus(STOPPING);
		//サービスの優先度を設定
		android.os.Process.setThreadPriority(android.os.Process.THREAD_PRIORITY_DEFAULT);
		if(mOnPlayCompletedListener != null)
			mOnPlayCompletedListener.onPlayCompleted();
		
	}
	
	//イコライザの設定を行う
	/**
	 * Equalizerの初期化
	 * @param mediaPlayer
	 */
	public void initEqualizer(MediaPlayer mediaPlayer){
		
		mEqualizer = new Equalizer(0,mediaPlayer.getAudioSessionId());
		mEqualizer.setEnabled(isEqualizer);
		if(mEqualizerItem != null){
			for(EqualizerItem item : mEqualizerItem){
				android.util.Log.v("MusicPlayer", "EQ : " + item.getBand() + " / " + item.getLevel());
				mEqualizer.setBandLevel(item.getBand(), item.getLevel());
			}
		}	
	}
	
	/**
	 * Equalizerを取得する．
	 * @return
	 */
	public Equalizer getEqualizerInstance(){
		if(mEqualizer != null){
			return mEqualizer;
		}else{
			return new Equalizer(0, R.raw.sample);
		}
	}
	
	/**
	 * Equalizerを有効にするか設定する
	 * @param isEqualizer
	 * @return
	 */
	public boolean setEqualizer(boolean isEqualizer){
		return this.isEqualizer = isEqualizer ;
	}
	
	/**
	 * Equalizerが有効かどうか取得する．
	 * @return
	 */
	public boolean getEqualizer(){
		return isEqualizer;
	}
	
	/**
	 * @return mEqualizerItem
	 */
	public EqualizerItem[] getEqualizerItem() {
		return mEqualizerItem;
	}

	/**
	 * @param mEqualizerItem セットする mEqualizerItem
	 */
	public void setEqualizerItem(EqualizerItem[] mEqualizerItem) {
		this.mEqualizerItem = mEqualizerItem;
	}

	/**
	 * デストラクタ
	 * インスタンスが破棄される際にプレイヤーのリソースを開放する．
	 */
	@Override
	protected void finalize() throws Throwable {
		//再生している場合は停止する．
		if(getStatus() == PLAYING)
			mMediaPlayer.stop();
		//サービスの優先度を設定
		android.os.Process.setThreadPriority(android.os.Process.THREAD_PRIORITY_DEFAULT);
		setStatus(NOSOURCE);
		//リソースを開放する．
		mMediaPlayer.release();
		mMediaPlayer = null;
		super.finalize();
	}


	
}
