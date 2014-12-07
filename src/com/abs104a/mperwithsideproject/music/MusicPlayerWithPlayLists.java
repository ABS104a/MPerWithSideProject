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
public class MusicPlayerWithPlayLists extends MusicPlayer {

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
	private ArrayList<?> mPlayList = null;
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
	
	/**
	 * プレイリストをセットする
	 * @param playList　セットするプレイリスト
	 */
	public void setPlayList(ArrayList<?> playList){
		this.mPlayList = playList;
	}
	
	/**
	 * 次の曲へ移動する
	 * @return 次の曲のカーソル
	 * @throws IOException 
	 * @throws IllegalStateException 
	 * @throws SecurityException 
	 * @throws IllegalArgumentException 
	 */
	public int playNext() throws IllegalArgumentException, SecurityException, IllegalStateException, IOException{
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
		String nextMusic = (String) mPlayList.get(mCursor);
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
	 * @return
	 * @throws IOException 
	 * @throws IllegalStateException 
	 * @throws SecurityException 
	 * @throws IllegalArgumentException 
	 */
	public int playBack() throws IllegalArgumentException, SecurityException, IllegalStateException, IOException{
		//TODO 実装予定
		//次の曲情報を取得
		if(--mCursor < 0){
			mCursor = 0;
		}
		String nextMusic = (String) mPlayList.get(mCursor);
		//データをセットする
		setSource(nextMusic);
		//再生を開始
		playStartAndPause();
		return mCursor;
		
	}




	
}


