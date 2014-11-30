package com.abs104a.mperwithsideproject.music;

import java.util.ArrayList;
import java.util.Collections;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.provider.MediaStore;

public class MusicUtils {

	/**
	 * �O���X�g���[�W���特�y�f�[�^���X�L��������
	 * @param context
	 * @return
	 */
	public static ArrayList<Item> getMusicList(final Context context){
	    ArrayList<Item> items = new ArrayList<Item>();
	    
		// ContentResolver ���擾
		ContentResolver cr = context.getContentResolver();
		 
		// �O���X�g���[�W���特�y������
		Cursor cur = cr.query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
		    null,
		    MediaStore.Audio.Media.IS_MUSIC + " = 1",
		    null,
		    null);
		 
		if (cur != null) {
		    do{
		    	// �ȏ��̃J�������擾
		    	int artistColumn = cur.getColumnIndex(MediaStore.Audio.Media.ARTIST);
		    	int titleColumn = cur.getColumnIndex(MediaStore.Audio.Media.TITLE);
		    	int albumColumn = cur.getColumnIndex(MediaStore.Audio.Media.ALBUM);
		    	int durationColumn = cur.getColumnIndex(MediaStore.Audio.Media.DURATION);
		    	int idColumn = cur.getColumnIndex(MediaStore.Audio.Media._ID);
		    	int idTruck = cur.getColumnIndex(MediaStore.Audio.Media.TRACK);
		    	 
		    	// ���X�g�ɒǉ�
		    	do {
					items.add(new Item(cur.getLong(idColumn),
		    	            cur.getString(artistColumn),
		    	            cur.getString(titleColumn),
		    	            cur.getString(albumColumn),
		    	            cur.getInt(idTruck),
		    	            cur.getLong(durationColumn)));
		    	} while (cur.moveToNext());
		    }while(cur.moveToNext());
		    // �J�[�\�������
		    cur.close();
		    //���ʂ��A���o�����Ń\�[�g
		    Collections.sort(items);
		}
		return items;
	}
	
}
