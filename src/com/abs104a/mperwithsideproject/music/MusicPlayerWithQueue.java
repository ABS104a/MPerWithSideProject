package com.abs104a.mperwithsideproject.music;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

import com.abs104a.mperwithsideproject.utl.FileUtils;

import android.content.Context;

/**
 * 音楽プレーヤーにくわえてプレイリスト再生を実現する
 * ArrayList型のプレイリストを読み込むことで
 * 
 * ・リピート再生　1曲orリスト内
 * ・シャッフル再生　リスト内
 * ・前の曲への移動
 * ・後の曲への移動
 * 
 * を提供する．
 * 
 * @author Kouki
 *
 */
public final class MusicPlayerWithQueue extends MusicPlayer {

	/**===================================
	 * 定数
	 ===================================*/
	
	//ループが有効ではない
	public final static int NOT_LOOP = 0;
	//1曲ループ
	public final static int ONE_LOOP = 1;
	//全曲ループ
	public final static int ALL_LOOP = 2;
	
	/**===================================
	 * 内部変数
	 ===================================*/
	
	private MusicQueue mQueue = null;
	//アプリケーションのコンテキスト
	private Context mContext;
	
	
	/**===================================
	 * メソッド
	 ===================================*/
	
	public MusicPlayerWithQueue(Context mContext){
		//Queueを読み込む
		MusicQueue mQueue = FileUtils.readSerializableQueue(mContext);
		this.mQueue = mQueue;
		this.mContext = mContext;
	}
	
	@Override
	protected void finalize() throws Throwable {
		//Queueの内容を書き出す．
		FileUtils.writeSerializableQueue(mContext, mQueue);
		super.finalize();
	}
	
	public void writeQueue(){
		FileUtils.writeSerializableQueue(mContext, mQueue);
	}
	

	//===================================
	// ・ シャッフル機能について
	// ==================================
	
	/**
	 * シャッフル機能有効フラグの状態を取得する
	 * @return　シャッフル機能が有効かどうか
	 */
	public final boolean isShuffle() {
		return mQueue.isShuffle();
	}

	/**
	 * シャッフル機能を設定する．
	 * @param isShuffle　シャッフルが有効かどうか
	 */
	public final boolean setShuffle(boolean isShuffle) {
		mQueue.setShuffle(isShuffle);
		return mQueue.isShuffle();
	}
	
	//===================================
	// ・ シャッフル機能について
	// ==================================
	
	/**
	 * ループの状態を取得する
	 * @return ループ状態
	 */
	public final int getLoopState() {
		return mQueue.getRepeatState();
	}

	/**
	 * ループの状態を設定する
	 * 設定外の値は設定されない
	 * @param loopState ループ状態
	 */
	public final int setLoopState(int loopState) {
		if(loopState == NOT_LOOP || 
				loopState == ALL_LOOP || loopState == ONE_LOOP){
			mQueue.setRepeatState(loopState);
		}
		return mQueue.getRepeatState();
			
	}
	
	
	//===================================
	// ・再生コントロール機能
	// ==================================
	
	/* (非 Javadoc)
	 * @see com.abs104a.mperwithsideproject.music.MusicPlayer#playStartAndPause()
	 */
	@Override
	public int playStartAndPause()
			throws IndexOutOfBoundsException, 
			IllegalArgumentException, 
			SecurityException, 
			IllegalStateException, 
			IOException 
			{
		if(getStatus() == NOSOURCE){
			setSource(mQueue.getCursorMusic().getPass());
		}
		return super.playStartAndPause();
	}	
	
	/**
	 * プレイリストをセットする
	 * @param playList　セットするプレイリスト
	 * @throws IOException 
	 * @throws IllegalStateException 
	 * @throws SecurityException 
	 * @throws IllegalArgumentException 
	 */
	public final void setPlayList(ArrayList<Music> playList) 
			throws IllegalArgumentException, 
			SecurityException, 
			IllegalStateException, 
			IOException
			{
		ArrayList<Music> mPlayList = mQueue.getQueueMusics();
		mPlayList.clear();
		mPlayList.addAll(playList);
		setSource(playList.get(0).getPass());
		writeQueue();
	}
	
	/**
	 * プレイリストをセットする (indexの曲目から再生する)
	 * @param playList　セットするプレイリスト
	 * @throws IOException 
	 * @throws IllegalStateException 
	 * @throws SecurityException 
	 * @throws IllegalArgumentException 
	 * @throws IndexOutOfBoundsException
	 */
	public final void setPlayList(ArrayList<Music> playList,int index) throws IndexOutOfBoundsException ,IllegalArgumentException, SecurityException, IllegalStateException, IOException{
		ArrayList<Music> mPlayList = mQueue.getQueueMusics();
		mPlayList.clear();
		mPlayList.addAll(playList);
		mQueue.setCursor(index);
		setSource(playList.get(index).getPass());
		writeQueue();
	}
	
	/**
	 * 曲をセットする（新たなプレイリストを作成して曲をセットする．）
	 * @param music
	 * @throws IllegalArgumentException
	 * @throws SecurityException
	 * @throws IllegalStateException
	 * @throws IOException
	 */
	public final void setMusic(final Music music) throws IllegalArgumentException, SecurityException, IllegalStateException, IOException{
		ArrayList<Music> mPlayList = mQueue.getQueueMusics();
		mPlayList.clear();
		mPlayList.add(music);
		mQueue.setCursor(0);
		setSource(music.getPass());
		writeQueue();
	}
	
	/**
	 * 曲を現在のプレイリストに追加する．
	 * 
	 * プレイリストがない場合は新規に作成をして追加します．
	 * @param music　追加する音楽要素
	 */
	public final void addMusic(final Music music){
		mQueue.getQueueMusics().add(music);
		writeQueue();
	}
	
	
	
	/**
	 * 曲を現在のプレイリストの任意の場所に挿入します．
	 * 
	 * プレイリストがない場合は作成します．
	 * 
	 * @param music　挿入する音楽
	 * @param index	挿入箇所
	 * @throws IndexOutOfBoundsException　プレイリスト外のIndex指定があった場合
	 */
	public final void addMusic(final Music music ,final int index) throws IndexOutOfBoundsException{
		mQueue.getQueueMusics().add(index, music);
		writeQueue();
	}
	
	/**
	 * 再生Queueのなかから指定した音楽を消去します．
	 * @param music
	 */
	public final void removeMusic(final Music music){
		int index = mQueue.getQueueMusics().indexOf(music);
		if(index != -1){
			if(index <= mQueue.getCursor())
				mQueue.setCursor(Math.max(0, mQueue.getCursor() - 1));
			mQueue.getQueueMusics().remove(music);
			writeQueue();
		}
	}
	
	/**
	 * 再生Queueのなかから指定した音楽を消去します．
	 * @param music
	 */
	public final void removeMusic(final int index){
		if(index >= 0 && index < mQueue.getQueueMusics().size()){
			if(index <= mQueue.getCursor())
				mQueue.setCursor(Math.max(0, mQueue.getCursor() - 1));
			mQueue.getQueueMusics().remove(index);
			writeQueue();
		}
	}
	
	/**
	 * 現在再生しているMusic
	 * @return
	 */
	public final Music getNowPlayingMusic(){
		if(mQueue.getQueueMusics() != null && mQueue.getQueueMusics().size() > 0)
			return mQueue.getCursorMusic();
		else
			return null;
	}
	
	/**
	 * 再生Queueを取得します．
	 * @return　再生Queue
	 */
	public final ArrayList<Music> getQueue(){
		return mQueue.getQueueMusics();
	}
	
	/**
	 * Queueの任意のインデックスにシークする．
	 * @param index
	 * @return
	 * @throws IndexOutOfBoundsException
	 * @throws IllegalArgumentException
	 * @throws SecurityException
	 * @throws IllegalStateException
	 * @throws IOException
	 */
	public final int seekQueue(final int index) 
			throws IndexOutOfBoundsException, 
			IllegalArgumentException,
			SecurityException, 
			IllegalStateException, 
			IOException
	{
		mQueue.setCursor(index);
		return setSource(mQueue.getCursorMusic().getPass());
	}
	
	/**
	 * 次の曲へ移動する
	 * @return 次の曲のカーソル
	 * @throws IOException 
	 * @throws IllegalStateException 
	 * @throws SecurityException 
	 * @throws IllegalArgumentException 
	 */
	public final int playNext() 
			throws IllegalArgumentException, 
			SecurityException, 
			IllegalStateException, 
			IOException
			{
		//TODO List形式を実装する
		//シャッフル機能が有効の時
		if(isShuffle()){
			//ランダムにカーソル値を設定する．
			Random random = new Random();
			int oldCursor = mQueue.getCursor();
			mQueue.setCursor(random.nextInt(mQueue.getQueueMusics().size()));
			//前の曲と同じ値になった場合はその次の曲or前の曲にする．
			if(oldCursor == mQueue.getCursor())
				mQueue.setCursor(mQueue.getCursor() == (mQueue.getQueueMusics().size()-1) ? Math.max(0, mQueue.getCursor()-1) : mQueue.getCursor()+1);
		}
		//シャッフル機能が無効の時
		else{
			//再生を終了するかどうか設定するフラグ
			boolean flag = false;
			//ループの状態
			switch(mQueue.getRepeatState()){
			case NOT_LOOP: 	//ループ無しの時
				//終了フラグを立てる．
				flag = true;
			case ALL_LOOP: 	//全曲ループの時
				mQueue.setCursor(mQueue.getCursor()+1);	//カーソルを進める
				if(mQueue.getQueueMusics().size() == mQueue.getCursor()){
					//プレイリストの最後まで来たとき
					//カーソルを0に戻す
					mQueue.setCursor(0);
					//全曲ループでないときは再生を終了する．
					if(flag)return mQueue.getCursor();
				}
			case ONE_LOOP:	//1曲ループの時
			}
		}
		//次の曲情報を取得
		String nextMusic = mQueue.getCursorMusic().getPass();
		//データをセットする
		setSource(nextMusic);
		//再生を開始
		playStartAndPause();
		return mQueue.getCursor();
		
	}
	
	/**
	 * 前の曲へ移動する．
	 * 
	 * 再生秒数が1s未満であれば前の曲
	 * 再生秒数が1s以上であれば曲の先頭へシークする
	 * @return 再生を行うカーソル
	 * @throws IOException 
	 * @throws IllegalStateException 
	 * @throws SecurityException 
	 * @throws IllegalArgumentException 
	 */
	public final int playBack() 
			throws IllegalArgumentException, 
			SecurityException, 
			IllegalStateException, 
			IOException
			{
		//秒数が1秒以上なら前に戻らない
		if(getCurrentTime() > 1000){
			mQueue.setCursor(mQueue.getCursor()+1);
		}
		android.util.Log.v("playback", getCurrentTime() + "");
		
		//次の曲情報を取得
		mQueue.setCursor(mQueue.getCursor()-1);
		if(mQueue.getCursor() < 0){
			mQueue.setCursor(0);
		}
		
		String nextMusic = mQueue.getCursorMusic().getPass();
		//データをセットする
		setSource(nextMusic);
		//再生を開始
		playStartAndPause();
		return mQueue.getCursor();	
	}


}