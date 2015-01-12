package com.abs104a.mperwithsideproject.utl;

import com.abs104a.mperwithsideproject.music.Music;
import com.abs104a.mperwithsideproject.music.PlayList;

import android.net.Uri;
import android.widget.ImageView;
import android.widget.TextView;

public class GetJacketImageItem {
	
	private final Uri uri;
	private final String album;
	private final String confilmString;
	private final ImageView jacketImage;
	private final TextView titleText;
	
	public GetJacketImageItem( TextView titleText,
			ImageView jacketImage, Music item) {
		this.uri = item.getAlbumUri();
		this.confilmString = item.getTitle();
		this.jacketImage = jacketImage;
		this.titleText = titleText;
		this.album = item.getAlbum();
	}
    
    public GetJacketImageItem(TextView albumText,
			ImageView jacketImage, PlayList group) {
		this.uri = group.getJacketUri();
		this.confilmString = group.getAlbum();
		this.jacketImage = jacketImage;
		this.titleText = albumText;
		this.album = group.getAlbum();
	}

	/**
	 * @return uri
	 */
	public final Uri getUri() {
		return uri;
	}

	/**
	 * @return album
	 */
	public final String getAlbum() {
		return album;
	}

	/**
	 * @return confilmString
	 */
	public final String getConfilmString() {
		return confilmString;
	}

	/**
	 * @return jacketImage
	 */
	public final ImageView getJacketImage() {
		return jacketImage;
	}

	/**
	 * @return titleText
	 */
	public final TextView getTitleText() {
		return titleText;
	}
}
