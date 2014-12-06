package com.abs104a.mperwithsideproject.music;

import java.io.IOException;

import android.media.MediaPlayer;

/**
 * 再生を行う音楽リソースを扱うクラス．
 * MediaPlayerクラスのラッパ
 * @author Kouki-Mobile
 *
 */
public final class MusicPlayer {
	
	//再生を行うMediaPlayerクラス
	private MediaPlayer mMediaPlayer = null;
	//現在再生しているかどうか
	private boolean isPlaying = false;
	
	public final void setSource(String pass) throws IllegalArgumentException, SecurityException, IllegalStateException, IOException{
		mMediaPlayer =  new MediaPlayer();
		mMediaPlayer.setDataSource(pass);
		mMediaPlayer.prepare();
	}
	
}
