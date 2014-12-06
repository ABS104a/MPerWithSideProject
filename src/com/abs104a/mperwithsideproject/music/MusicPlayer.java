package com.abs104a.mperwithsideproject.music;

import java.io.IOException;

import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;

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
	protected MediaPlayer mMediaPlayer = null;
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
	private OnPlayCompletedListener mOnPlayCompletedListener = null;
	
	/**
	 * 再生完了時に呼ばれるリスナを取得するメソッド
	 * @return
	 */
	public OnPlayCompletedListener getOnPlayCompletedListener() {
		return mOnPlayCompletedListener;
	}

	/**
	 * 再生完了時に呼ばれるリスナを設定するメソッド
	 * @param mOnPlayCompletedListener
	 */
	public void setOnPlayCompletedListener(OnPlayCompletedListener mOnPlayCompletedListener) {
		this.mOnPlayCompletedListener = mOnPlayCompletedListener;
	}
	
	
	/**===================================
	 * コントロールメソッド
	 ===================================*/
	
	
	/**
	 * 再生状況を設定する．
	 * @param status
	 * @return
	 */
	protected int setStatus(int status){
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
	public int getStatus(){
		return _status;
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
	public final int setSource(String pass) throws IllegalArgumentException, SecurityException, IllegalStateException, IOException{
		//MediaPlayerのインスタンスを取得
		if(mMediaPlayer == null)
			mMediaPlayer =  new MediaPlayer();
		//データソースの設定
		mMediaPlayer.setDataSource(pass);
		//データ読み込み
		mMediaPlayer.prepare();
		//状況の反映
		setStatus(STOPPING);
		return getStatus();
	}
	
	/**
	 * 再生する時の動作
	 * @return　現在の状況
	 */
	public final int playStartAndPause(){
		if(mMediaPlayer != null){
			//再生していない場合　（NOTPLAYING or PAUSEING or STOPPING）
			if(getStatus() >= NOTPLAYING){
				//再生する
				mMediaPlayer.start();
				mMediaPlayer.setOnCompletionListener(this);
				setStatus(PLAYING);
			}
			//再生中の場合　PLAYING
			else if(getStatus() == PLAYING){
				//ポーズ状態にする
				mMediaPlayer.pause();
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
		}
		return getStatus();
	}

	/**
	 * 再生が完了した時
	 */
	@Override
	public void onCompletion(MediaPlayer mp) {
		setStatus(STOPPING);
		if(mOnPlayCompletedListener != null)
			mOnPlayCompletedListener.onPlayCompleted();
		
	}


	
}
