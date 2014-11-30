package com.abs104a.mperwithsideproject.music;

import java.io.Serializable;

/**
 * 曲の情報を保持するクラス
 * @author Kouki
 *
 */
public class Item implements Serializable,Comparable<Item>{

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
	
	
	/**
	 * 初期値代入のためのコンストラクタ
	 * @param id		メディアID
	 * @param artist	アーティスト
	 * @param title		タイトル
	 * @param album		アルバム
	 * @param truck		トラック番号
	 * @param duration	長さ
	 */
	public Item(long id, String artist, String title, String album,
			int truck, long duration) {
		this.id = id;
		this.artist = artist;
		this.title = title;
		this.album = album;
		this.truck = truck;
		this.duration = duration;
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

	@Override
	public int compareTo(Item another) {
		if(another == null)return 1;
		Item item = (Item)another;
		int result = album.compareTo(item.album);
		if (result != 0) {
	        return result;
	    }
	    return truck - item.truck;
	}
	
}
