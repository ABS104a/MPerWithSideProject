package com.abs104a.mperwithsideproject.utl;

import java.util.ArrayList;
import java.util.Collections;

import com.abs104a.mperwithsideproject.music.Music;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.provider.MediaStore;

public class MusicUtils {

	/**
	 * 外部ストレージから音楽データをスキャンする
	 * @param context
	 * @return
	 */
	public static ArrayList<Music> getMusicList(final Context context){
		ArrayList<Music> items = new ArrayList<Music>();

		// ContentResolver を取得
		ContentResolver cr = context.getContentResolver();

		// 外部ストレージから音楽を検索
		Cursor cur = cr.query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
				null,
				MediaStore.Audio.Media.IS_MUSIC + " = 1",
				null,
				null);

		if (cur != null) {
			do{
				// 曲情報のカラムを取得
				int artistColumn = cur.getColumnIndex(MediaStore.Audio.Media.ARTIST);
				int titleColumn = cur.getColumnIndex(MediaStore.Audio.Media.TITLE);
				int albumColumn = cur.getColumnIndex(MediaStore.Audio.Media.ALBUM);
				int albumIdColumn = cur.getColumnIndex(MediaStore.Audio.Media.ALBUM_ID);
				int durationColumn = cur.getColumnIndex(MediaStore.Audio.Media.DURATION);
				int idColumn = cur.getColumnIndex(MediaStore.Audio.Media._ID);
				int idTruck = cur.getColumnIndex(MediaStore.Audio.Media.TRACK);
				String pass = cur.getString(0);

				// リストに追加
				do {
					items.add(new Music(cur.getLong(idColumn),
							cur.getString(artistColumn),
							cur.getString(titleColumn),
							cur.getString(albumColumn),
							cur.getInt(albumIdColumn),
							cur.getInt(idTruck),
							cur.getLong(durationColumn),
							pass));
				} while (cur.moveToNext());
			}while(cur.moveToNext());
			// カーソルを閉じる
			cur.close();


			//結果をアルバム順でソート
			Collections.sort(items);
			
			//TODO アルバムごとにAlbumクラスに仕分ける

			//TODO アルバムごとにジャケット画像のUriを取得


		}
		return items;
	}
	
}
