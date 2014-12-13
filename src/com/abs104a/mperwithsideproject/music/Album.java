package com.abs104a.mperwithsideproject.music;

import java.io.Serializable;

import android.net.Uri;

/**
 * アルバムを示すクラス
 * @author Kouki
 *
 */
public class Album  implements Serializable,Comparable<Album>{

	/**
	 * 
	 */
	private static final long serialVersionUID = -4147646815466104141L;
	//アルバムの名前
	private String albumName;
	//アルバムのId
	private long albumId;
	//アルバム画像のUri
	private Uri jacketUri;
	//アーティスト
	private String artist;
	//アルバムの曲
	private Music[] musics;
	
	public Album(String albumName,String artist,long albumId,Uri jacketUri){
		this.albumId = albumId;
		this.albumName = albumName;
		this.jacketUri = jacketUri;
		this.artist = artist;
	}
	
	
	/**
	 * @return albumName
	 */
	public final String getAlbum() {
		return albumName;
	}
	/**
	 * @param albumName セットする albumName
	 */
	public final void setAlbum(String albumName) {
		this.albumName = albumName;
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
	 * @return jacketUri
	 */
	public final Uri getJacketUri() {
		return jacketUri;
	}
	/**
	 * @param jacketUri セットする jacketUri
	 */
	public final void setJacketUri(Uri jacketUri) {
		this.jacketUri = jacketUri;
	}
	/**
	 * @return musics
	 */
	public final Music[] getMusics() {
		return musics;
	}
	/**
	 * @param musics セットする musics
	 */
	public final void setMusics(Music[] musics) {
		this.musics = musics;
	}
	
	/**
	 * @return artist
	 */
	public String getArtist() {
		return artist;
	}

	/**
	 * @param artist セットする artist
	 */
	public void setArtist(String artist) {
		this.artist = artist;
	}
	
	@Override
	public int compareTo(Album another) {
		if(another == null)return 1;
		Album item = (Album)another;
		int result = albumName.compareTo(item.albumName);
		if (result != 0) {
	        return result;
	    }
	    return (int) (albumId - item.albumId);
	}
}
