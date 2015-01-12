package com.abs104a.mperwithsideproject.music;

import java.io.Serializable;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * 曲の情報を保持するクラス
 * @author Kouki
 *
 */
public class Music implements Serializable,Comparable<Music>,Parcelable {

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
		if(album1Uri != null)
			this.albumUri = album1Uri.toString();
		else
			this.albumUri = null;
		this.truck = truck;
		this.duration = duration;
		this.setPass(pass);
	}
	
	public Music(Parcel in) {
		readFromParcel(in);
	}
	
	public void readFromParcel(Parcel in) {
		this.id = in.readLong();
		this.artist = in.readString();
		this.title = in.readString();
		this.album = in.readString();
		this.truck = in.readInt();
		this.duration = in.readLong();
		this.setPass(in.readString());
		this.albumId = in.readLong();
		this.albumUri = in.readString();
	}
	
	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel out, int flags) {
		out.writeLong(id);
		out.writeString(artist);
		out.writeString(title);
		out.writeString(album);
		out.writeInt(truck);
		out.writeLong(duration);
		out.writeString(pass);
		out.writeLong(albumId);
		out.writeString(albumUri);
	}
	
	public static final Parcelable.Creator<Music> CREATOR  
	= new Parcelable.Creator<Music>() {  
		public Music createFromParcel(Parcel in) {  
			return new Music(in);  
		}  

		public Music[] newArray(int size) {  
			return new Music[size];  
		}  
	}; 
	
	
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
	
	/* (非 Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object o) {
		if(o instanceof Music){
			return ((Music)o).id == this.id;
		}else{
			return false;
		}
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
		if(albumUri != null)
			return Uri.parse(albumUri);
		else 
			return null;
	}

	/**
	 * @param albumUri セットする albumUri
	 */
	public final void setAlbumUri(Uri albumUri) {
		if(albumUri != null)
			this.albumUri = albumUri.toString();
		else 
			this.albumUri = null;
	}

	/* (非 Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return getPass();
	}

}
