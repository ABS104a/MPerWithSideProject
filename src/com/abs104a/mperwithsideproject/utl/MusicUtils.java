package com.abs104a.mperwithsideproject.utl;

import java.util.ArrayList;
import java.util.Collections;

import com.abs104a.mperwithsideproject.R;
import com.abs104a.mperwithsideproject.music.Album;
import com.abs104a.mperwithsideproject.music.Music;

import android.app.Service;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class MusicUtils {

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
			String pass = cur.toString();

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
						pass);

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
	
	/**
	 * MusicPlayerView中の再生曲情報を表示するViewへのデータセットを行う
	 * @param mService
	 * @param mView
	 * @param music
	 */
	public static void setPartOfPlayerView(Service mService,View mView,Music music){
		
		//タイトルView
		TextView title = (TextView)mView.findViewById(R.id.textView_now_music_name);
		title.setText(music.getTitle());
		//アーティスト
		TextView artist = (TextView)mView.findViewById(R.id.textView_now_artist_name);
		artist.setText(music.getArtist());
		//アルバム名
		TextView album = (TextView)mView.findViewById(R.id.textView_now_album);
		album.setText(music.getAlbum());
		//曲時間
		TextView maxTime = (TextView)mView.findViewById(R.id.textView_now_max_time);
		maxTime.setText(music.getDuration()+"");
		//現在の再生時間
		TextView currentTime = (TextView)mView.findViewById(R.id.TextView_now_current_time);
		currentTime.setText("0:00");
		//アルバムジャケット
		ImageView jacket = (ImageView)mView.findViewById(R.id.imageView_now_jacket);
		jacket.setImageURI(music.getAlbumUri());
	}
	
}
