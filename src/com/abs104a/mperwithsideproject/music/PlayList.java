package com.abs104a.mperwithsideproject.music;

import java.util.ArrayList;

import android.net.Uri;

public final class PlayList extends Album {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7865838533199336968L;

	public PlayList(String albumName, String artist, long albumId, Uri jacketUri) {
		super(albumName, artist, albumId, jacketUri);
	}
	
	private static ArrayList<PlayList> mPlayLists = null;
	
	public static final ArrayList<PlayList> readPlayLists(){
		//TODO プレイリストを読み取るリスト部分の実装
		return null;
	}
	
	public static final boolean writePlayLists(){
		//PlayListを書き込む部分の実装
		return false;
	}

	/**
	 * プレイリストを取得する．
	 * getter
	 * @return mPlayLists
	 */
	public static ArrayList<PlayList> getPlayLists() {
		return mPlayLists;
	}
}
