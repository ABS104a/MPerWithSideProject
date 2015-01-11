package com.abs104a.mperwithsideproject;

import com.abs104a.mperwithsideproject.music.Music;
import com.abs104a.mperwithsideproject.IPlayServiceCallback;

interface IPlayerService{
//TODO このService にQueueを実装させるかどうか

	boolean stopService();
	
	/*
	int play();
	
	int playIndex(int index);
	
	int seek(int index);
	
	int pause();
	
	int stop();
	
	int previous();
	
	int next();
	
	int getCursor();
	
	int addQueue(inout Music music);
	
	Music nowPlayingMusic();
	
	Music[] getQueueMusics();
	
	int removeQueue(int index);
	
	*/
	 /**
     * コールバック登録。
     * @param callback 登録するコールバック。
     */
    //oneway void registerCallback(IPlayServiceCallback callback);
    
    /**
     * コールバック解除。
     * @param callback 解除するコールバック。
     */
    //oneway void unregisterCallback(IPlayServiceCallback callback);
    
	
}