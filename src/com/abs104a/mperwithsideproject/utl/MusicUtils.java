package com.abs104a.mperwithsideproject.utl;

import java.util.ArrayList;
import java.util.Collections;

import com.abs104a.mperwithsideproject.music.Album;
import com.abs104a.mperwithsideproject.music.Music;
import com.abs104a.mperwithsideproject.music.MusicPlayerWithQueue;

import android.app.Service;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

public class MusicUtils {

	private static MusicPlayerWithQueue musicController = null;
	
	/**
	 * ミュージックコントロールクラスを返す.
	 * 
	 * @param mContext
	 * @return
	 */
	public final static MusicPlayerWithQueue getMusicController(Context mContext){
		if(musicController == null)
			musicController = new MusicPlayerWithQueue(mContext);
		return musicController;
	}
	
	/**
	 * 外部ストレージから音楽データをスキャンする
	 * @param context
	 * @return
	 */
	public static ArrayList<Music> getMusicList(final Context context){
		
		final ArrayList<Music> items = new ArrayList<Music>();

		// ContentResolver を取得
		final ContentResolver cr = context.getContentResolver();
		
		//アルバムのUriを読み込む
		final Uri albumArtUri = Uri.parse(
		        "content://media/external/audio/albumart");

		// 外部ストレージから音楽を検索
		final Cursor cur = cr.query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
				null,
				MediaStore.Audio.Media.IS_MUSIC + " = 1",
				null,
				null);

		if (cur != null && cur.moveToFirst()) {
			
			// 曲情報のカラムを取得
			int artistColumn = cur.getColumnIndex(MediaStore.Audio.Media.ARTIST);
			int titleColumn = cur.getColumnIndex(MediaStore.Audio.Media.TITLE);
			int albumColumn = cur.getColumnIndex(MediaStore.Audio.Media.ALBUM);
			int albumIdColumn = cur.getColumnIndex(MediaStore.Audio.Media.ALBUM_ID);
			int durationColumn = cur.getColumnIndex(MediaStore.Audio.Media.DURATION);
			int idColumn = cur.getColumnIndex(MediaStore.Audio.Media._ID);
			int idTruck = cur.getColumnIndex(MediaStore.Audio.Media.TRACK);
			int pass = cur.getColumnIndex(MediaStore.Audio.Media.DATA);

			// リストに追加
			do {
				final long albumId = cur.getLong(albumIdColumn);
				Uri album1Uri = ContentUris.withAppendedId(albumArtUri, albumId);
				Music music = new Music(cur.getLong(idColumn),
						cur.getString(artistColumn),
						cur.getString(titleColumn),
						cur.getString(albumColumn),
						albumId,
						cur.getInt(idTruck),
						cur.getLong(durationColumn),
						album1Uri,
						cur.getString(pass));

				items.add(music);

			} while (cur.moveToNext());

			// カーソルを閉じる
			cur.close();

			//結果をアルバム順でソート
			Collections.sort(items);
		}
		return items;
	}

	/**
	 * 音楽ファイルをスキャンして，アルバムクラス配列を取得する．
	 * @param mService
	 * @return AlbumのArrayList
	 */
	public static ArrayList<Album> getMusicAlbumList(Service mService) {
		
		final ArrayList<Album> albums = new ArrayList<Album>();
		
		ArrayList<Music> items = getMusicList(mService);
		
		long _albumId = -1;
		
		ArrayList<Music> tmpMusic = new ArrayList<Music>();
		Album mAlbum = null;
		for(Music item : items){
			if(item.getAlbumId() != _albumId){
				//IDが前のものと違う時
				if(mAlbum != null){
					mAlbum.setMusics(tmpMusic.toArray(new Music[tmpMusic.size()]));
					albums.add(mAlbum);
					tmpMusic.clear();
				}
				mAlbum = new Album(item.getAlbum(),
						item.getArtist(),
						item.getAlbumId(), 
						item.getAlbumUri());
				_albumId = item.getAlbumId();
			}
			tmpMusic.add(item);
		}
		if(tmpMusic.size() > 0){
			mAlbum.setMusics(tmpMusic.toArray(new Music[tmpMusic.size()]));
			albums.add(mAlbum);
			tmpMusic.clear();
		}
		return albums;
	}
	

	
}
