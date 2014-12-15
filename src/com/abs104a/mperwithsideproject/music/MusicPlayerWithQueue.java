package com.abs104a.mperwithsideproject.music;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

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
	
	//再生曲のプレイリスト
	private final static ArrayList<Music> mPlayList = new ArrayList<Music>();
	//プレイリストの再生番号を管理するカーソル
	private int mCursor = 0;
	
	//ループ状態を記憶する変数
	private int loopState = NOT_LOOP;
	
	//シャッフル機能が有効かどうかのフラグ
	private boolean isShuffle = false;
	
	
	/**===================================
	 * メソッド
	 ===================================*/

	//===================================
	// ・ シャッフル機能について
	// ==================================
	
	/**
	 * シャッフル機能有効フラグの状態を取得する
	 * @return　シャッフル機能が有効かどうか
	 */
	public final boolean isShuffle() {
		return isShuffle;
	}

	/**
	 * シャッフル機能を設定する．
	 * @param isShuffle　シャッフルが有効かどうか
	 */
	public final boolean setShuffle(boolean isShuffle) {
		return this.isShuffle = isShuffle;
	}
	
	//===================================
	// ・ シャッフル機能について
	// ==================================
	
	/**
	 * ループの状態を取得する
	 * @return ループ状態
	 */
	public final int getLoopState() {
		return loopState;
	}

	/**
	 * ループの状態を設定する
	 * 設定外の値は設定されない
	 * @param loopState ループ状態
	 */
	public final int setLoopState(int loopState) {
		if(loopState == NOT_LOOP || 
				loopState == ALL_LOOP || loopState == ONE_LOOP)
			return this.loopState = loopState;
		else
			return this.loopState;
			
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
			setSource(mPlayList.get(mCursor).getPass());
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
		this.mPlayList.clear();
		this.mPlayList.addAll(playList);
		setSource(playList.get(0).getPass());
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
		this.mPlayList.clear();
		this.mPlayList.addAll(playList);
		mCursor = index;
		setSource(playList.get(index).getPass());
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
		mPlayList.clear();
		mPlayList.add(music);
		mCursor = 0;
		setSource(music.getPass());
	}
	
	/**
	 * 曲を現在のプレイリストに追加する．
	 * 
	 * プレイリストがない場合は新規に作成をして追加します．
	 * @param music　追加する音楽要素
	 */
	public final void addMusic(final Music music){
		mPlayList.add(music);
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
		mPlayList.add(index, music);
	}
	
	/**
	 * 再生Queueのなかから指定した音楽を消去します．
	 * @param music
	 */
	public final void removeMusic(final Music music){
		int index = mPlayList.indexOf(music);
		if(index != -1){
			if(index <= mCursor)
				mCursor = Math.max(0, mCursor - 1);
			mPlayList.remove(music);
		}
	}
	
	/**
	 * 再生Queueのなかから指定した音楽を消去します．
	 * @param music
	 */
	public final void removeMusic(final int index){
		if(index >= 0 && index < mPlayList.size()){
			if(index <= mCursor)
				mCursor = Math.max(0, mCursor - 1);
			mPlayList.remove(index);
		}
	}
	
	/**
	 * 現在再生しているMusic
	 * @return
	 */
	public final Music getNowPlayingMusic(){
		if(mPlayList != null && mPlayList.size() > 0)
			return mPlayList.get(mCursor);
		else
			return null;
	}
	
	/**
	 * 再生Queueを取得します．
	 * @return　再生Queue
	 */
	public final ArrayList<Music> getQueue(){
		return mPlayList;
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
		mCursor = index;
		return setSource(mPlayList.get(index).getPass());
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
			int oldCursor = mCursor;
			mCursor = random.nextInt(mPlayList.size()-1);
			//前の曲と同じ値になった場合はその次の曲or前の曲にする．
			if(oldCursor == mCursor)
				mCursor = mCursor == (mPlayList.size()-1) ? Math.max(0, --mCursor) : ++mCursor;
		}
		//シャッフル機能が無効の時
		else{
			//再生を終了するかどうか設定するフラグ
			boolean flag = false;
			//ループの状態
			switch(loopState){
			case NOT_LOOP: 	//ループ無しの時
				//終了フラグを立てる．
				flag = true;
			case ALL_LOOP: 	//全曲ループの時
				++mCursor;	//カーソルを進める
				if(mPlayList.size() == mCursor){
					//プレイリストの最後まで来たとき
					//カーソルを0に戻す
					mCursor = 0;
					//全曲ループでないときは再生を終了する．
					if(flag)return mCursor;
				}
			case ONE_LOOP:	//1曲ループの時
			}
		}
		//次の曲情報を取得
		String nextMusic = mPlayList.get(mCursor).getPass();
		//データをセットする
		setSource(nextMusic);
		//再生を開始
		playStartAndPause();
		return mCursor;
		
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
			++mCursor;
		}
		android.util.Log.v("playback", getCurrentTime() + "");
		
		//次の曲情報を取得
		if(--mCursor < 0){
			mCursor = 0;
		}
		
		String nextMusic = mPlayList.get(mCursor).getPass();
		//データをセットする
		setSource(nextMusic);
		//再生を開始
		playStartAndPause();
		return mCursor;	
	}


}