package com.abs104a.mperwithsideproject.utl;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

import com.abs104a.mperwithsideproject.R;
import com.abs104a.mperwithsideproject.music.Music;
import com.abs104a.mperwithsideproject.music.MusicPlayerWithQueue;
import com.abs104a.mperwithsideproject.music.MusicQueue;
import com.abs104a.mperwithsideproject.music.PlayList;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

/**
 * ファイル管理を行うUtilクラス
 * データ永続化などを行う
 * 
 * ・Serialize
 * ・SQLite
 * 
 * @author Kouki-Mobile
 *
 */
public final class FileUtils {

	/**
	 * プレイリストを読み込むメソッド
	 * @param mContext　アプリケーションのコンテキスト
	 * @return　読み込んだプレイリスト
	 */
	public final static ArrayList<PlayList> readSerializablePlayList(Context mContext){
		final ArrayList<PlayList> resultList = new ArrayList<PlayList>();
		final PlayList[] lists = (PlayList[]) readDataObject(
				mContext, 
				mContext.getString(R.string.playlist_serializable)
				);
		
		if(lists != null){
			for(PlayList list : lists)
				resultList.add(list);
		}
		
		return resultList;
	}
	
	/**
	 * プレイリストを書き込むメソッド
	 * @param mContext	アプリケーションのコンテキスト
	 * @param data	書き込むプレイリスト
	 * @return	書き込みが成功したか？
	 */
	public final static boolean writeSerializablePlayList(Context mContext,ArrayList<PlayList> data){
		return writeDataObject(
				mContext, 
				mContext.getString(R.string.playlist_serializable), 
				data.toArray(new PlayList[data.size()])
				);
	}
	
	/**
	 * Queueを読み込むメソッド
	 * @param mContext　アプリケーションのコンテキスト
	 * @return　読み込んだプレイリスト
	 */
	public final static MusicQueue readSerializableQueue(Context mContext){
		MusicQueue lists = (MusicQueue) readDataObject(
				mContext, 
				mContext.getString(R.string.queue_serializable)
				);
		
		//読み込みができなかった場合
		if(lists == null){
			lists = new MusicQueue(
					new ArrayList<Music>(), 
					0, 
					false, 
					MusicPlayerWithQueue.NOT_LOOP);
		}
		return lists;
	}
	
	/**
	 * Queueを書き込むメソッド
	 * @param mContext	アプリケーションのコンテキスト
	 * @param data	書き込むプレイリスト
	 * @return	書き込みが成功したか？
	 */
	public final static boolean writeSerializableQueue(Context mContext,MusicQueue data){
		return writeDataObject(
				mContext, 
				mContext.getString(R.string.queue_serializable), 
				data
				);
	}
	
	/**
	 * Serializeされたオブジェクトを保存するクラス
	 * @param fileName	保存ファイル名
	 * @param obj	保存オブジェクト
	 * @return	成功かどうか
	 */
	public final static boolean writeDataObject(Context context,String fileName,Object obj){
		try {
		    FileOutputStream fos = context.openFileOutput(fileName, Activity.MODE_PRIVATE);
		    ObjectOutputStream oos = new ObjectOutputStream(fos);

		    oos.writeObject(obj);

		    oos.close();
		    return true;
		}catch (Exception e) {
			e.printStackTrace();
		    Log.d("FileDataObjectSave", e.getMessage()+e.toString());
		    return false;
		}
	}
	
	/**
	 * Serializeされたオブジェクトを読み出すクラス
	 * writeDataObjectで書き込んだデータを読み込む
	 * @param fileName	読み出しファイル名
	 * @return	読み込んだオブジェクト，読み込めない時はNull
	 */
	public final static Object readDataObject(Context context,String fileName){
		try {
		    FileInputStream fis = context.openFileInput(fileName);
		    ObjectInputStream ois = new ObjectInputStream(fis);
			Object data = ois.readObject();
		    ois.close();
		    return data;
		} catch(FileNotFoundException e){
			Log.d("FileDataObjectSave","FileNotFound");
		} catch (Exception e) {
			e.printStackTrace();
			Log.d("FileDataObjectRead",  e.getMessage()+e.toString());
		}
		return null;
	}
}
