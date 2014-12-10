package com.abs104a.mperwithsideproject.music;

import java.io.Serializable;

/**
 * アルバムを示すクラス
 * @author Kouki
 *
 */
public final class Album  implements Serializable,Comparable<Album>{

	/**
	 * 
	 */
	private static final long serialVersionUID = -4147646815466104141L;
	//アルバムの名前
	private String albumName;
	//アルバムのId
	private int albumId;
	//アルバム画像のUri
	private String jacketUri;
	//アルバムの曲
	private Music[] musics;
	
	
	/**
	 * @return albumName
	 */
	public final String getAlbumName() {
		return albumName;
	}
	/**
	 * @param albumName セットする albumName
	 */
	public final void setAlbumName(String albumName) {
		this.albumName = albumName;
	}
	/**
	 * @return albumId
	 */
	public final int getAlbumId() {
		return albumId;
	}
	/**
	 * @param albumId セットする albumId
	 */
	public final void setAlbumId(int albumId) {
		this.albumId = albumId;
	}
	/**
	 * @return jacketUri
	 */
	public final String getJacketUri() {
		return jacketUri;
	}
	/**
	 * @param jacketUri セットする jacketUri
	 */
	public final void setJacketUri(String jacketUri) {
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
	@Override
	public int compareTo(Album another) {
		if(another == null)return 1;
		Album item = (Album)another;
		int result = albumName.compareTo(item.albumName);
		if (result != 0) {
	        return result;
	    }
	    return albumId - item.albumId;
	}
	
	
}
