package com.abs104a.mperwithsideproject.music;

import java.util.ArrayList;
import java.util.Collections;

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
	public static ArrayList<Item> getMusicList(final Context context){
	    ArrayList<Item> items = new ArrayList<Item>();
	    
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
		    	int durationColumn = cur.getColumnIndex(MediaStore.Audio.Media.DURATION);
		    	int idColumn = cur.getColumnIndex(MediaStore.Audio.Media._ID);
		    	int idTruck = cur.getColumnIndex(MediaStore.Audio.Media.TRACK);
		    	 
		    	// リストに追加
		    	do {
					items.add(new Item(cur.getLong(idColumn),
		    	            cur.getString(artistColumn),
		    	            cur.getString(titleColumn),
		    	            cur.getString(albumColumn),
		    	            cur.getInt(idTruck),
		    	            cur.getLong(durationColumn)));
		    	} while (cur.moveToNext());
		    }while(cur.moveToNext());
		    // カーソルを閉じる
		    cur.close();
		    //結果をアルバム順でソート
		    Collections.sort(items);
		}
		return items;
	}
	
}
