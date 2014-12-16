package com.abs104a.mperwithsideproject.music;

import java.io.Serializable;
import java.util.ArrayList;

public class MusicQueue implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1555613550875599291L;
	
	private ArrayList<Music> queueMusics = new ArrayList<Music>();
	private int mCursor = 0;
	private boolean isShuffle = false;
	private int repeatState = MusicPlayerWithQueue.NOT_LOOP;
	
	public MusicQueue(ArrayList<Music> queueMusics,int mCursor,boolean isShuffle,int repeatState){
		this.queueMusics = queueMusics;
		this.mCursor = mCursor;
		this.isShuffle = isShuffle;
		this.repeatState = repeatState;
	}
	
	/**
	 * @return queueMusics
	 */
	public ArrayList<Music> getQueueMusics() {
		return queueMusics;
	}
	/**
	 * @param queueMusics セットする queueMusics
	 */
	public void setQueueMusics(ArrayList<Music> queueMusics) {
		this.queueMusics = queueMusics;
	}
	/**
	 * @return mCursor
	 */
	public int getCursor() {
		return mCursor;
	}
	/**
	 * @param mCursor セットする mCursor
	 */
	public void setCursor(int mCursor) {
		this.mCursor = mCursor;
	}
	/**
	 * @return isShuffle
	 */
	public boolean isShuffle() {
		return isShuffle;
	}
	/**
	 * @param isShuffle セットする isShuffle
	 */
	public void setShuffle(boolean isShuffle) {
		this.isShuffle = isShuffle;
	}
	/**
	 * @return repeatState
	 */
	public int getRepeatState() {
		return repeatState;
	}
	/**
	 * @param repeatState セットする repeatState
	 */
	public void setRepeatState(int repeatState) {
		this.repeatState = repeatState;
	}
	
	/**
	 * 指定したインデックスのキューにある曲を返す
	 * @param index
	 * @return
	 */
	public Music getIndexMusic(int index){
		return this.queueMusics.get(index);
	}
	
	/**
	 * カーソルの位置の曲を返す
	 * @return
	 */
	public Music getCursorMusic(){
		return this.queueMusics.get(mCursor);
	}
	
}
