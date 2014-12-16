package com.abs104a.mperwithsideproject.music;

import java.io.Serializable;

import android.net.Uri;

/**
 * 曲の情報を保持するクラス
 * @author Kouki
 *
 */
public class Music implements Serializable,Comparable<Music>{

	/**
	 * 
	 */
	private static final long serialVersionUID = 5772453197876336892L;
	
	//MediaID
	private long id;
	//アーティスト情報
	private String artist;
	//タイトル
	private String title;
	//アルバム
	private String album;
	//トラック番号
	private int truck;
	//長さ
	private long duration;
	//ファイルのURI
	private String pass = null;
	//アルバムId（ジャケット画像取得用）
	private long albumId;
	
	private String albumUri;
	
	
	/**
	 * 初期値代入のためのコンストラクタ
	 * @param id		メディアID
	 * @param artist	アーティスト
	 * @param title		タイトル
	 * @param album		アルバム
	 * @param albumId   アルバムID
	 * @param truck		トラック番号
	 * @param duration	長さ
	 * @param album1Uri 
	 * @param pass 		データパス
	 */
	public Music(long id, String artist, String title, String album,
			long albumId, int truck, long duration, Uri album1Uri, String pass) {
		this.id = id;
		this.artist = artist;
		this.title = title;
		this.album = album;
		this.albumId = albumId;
		this.albumUri = album1Uri.toString();
		this.truck = truck;
		this.duration = duration;
		this.setPass(pass);
	}
	
	public final long getId() {
		return id;
	}
	public final void setId(long id) {
		this.id = id;
	}
	public final String getArtist() {
		return artist;
	}
	public final void setArtist(String artist) {
		this.artist = artist;
	}
	public final String getTitle() {
		return title;
	}
	public final void setTitle(String title) {
		this.title = title;
	}
	public final String getAlbum() {
		return album;
	}
	public final void setAlbum(String album) {
		this.album = album;
	}
	public final int getTruck() {
		return truck;
	}
	public final void setTruck(int truck) {
		this.truck = truck;
	}
	public final long getDuration() {
		return duration;
	}
	public final void setDuration(long duration) {
		this.duration = duration;
	}
	public String getPass() {
		return pass;
	}
	public void setPass(String pass) {
		this.pass = pass;
	}

	@Override
	public int compareTo(Music another) {
		if(another == null)return 1;
		Music item = (Music)another;
		int result = album.compareTo(item.album);
		if (result != 0) {
	        return result;
	    }
	    return truck - item.truck;
	}

	/**
	 * @return albumId
	 */
	public final long getAlbumId() {
		return albumId;
	}

	/**
	 * @param albumId セットする albumId
	 */
	public final void setAlbumId(long albumId) {
		this.albumId = albumId;
	}

	/**
	 * @return albumUri
	 */
	public final Uri getAlbumUri() {
		return Uri.parse(albumUri);
	}

	/**
	 * @param albumUri セットする albumUri
	 */
	public final void setAlbumUri(Uri albumUri) {
		this.albumUri = albumUri.toString();
	}

	/* (非 Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return getPass();
	}
	
}
