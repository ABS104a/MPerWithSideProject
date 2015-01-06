package com.abs104a.mperwithsideproject.music;

import java.util.ArrayList;

import com.abs104a.mperwithsideproject.utl.FileUtils;

import android.content.Context;
import android.net.Uri;

public final class PlayList extends Album {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7865838533199336968L;

	public PlayList(String albumName, String artist, long albumId, Uri jacketUri) {
		super(albumName, artist, albumId, jacketUri);
	}
	
	//プレイリスト
	private static ArrayList<PlayList> pList = null;

	public static ArrayList<PlayList> getPlayList(Context mContext){
		if(pList == null){
			pList = FileUtils.readSerializablePlayList(mContext);	
		}
		return pList;
	}

	public static void writePlayList(Context con){
		if(pList != null)
			FileUtils.writeSerializablePlayList(con, pList);
	}
	
	/**
	 * 配列要素を入れ替える
	 * @param index1　1つ目の要素
	 * @param index2　2つ目の要素
	 * @return
	 */
	public boolean swapMusic(int index1,int index2){
		try{
			Music[] musics = getMusics();
			if((index1 >= 0 && index1 < musics.length) &&
					(index2 >= 0 && index2 < musics.length)){
				Music tmp = musics[index2];
				musics[index2] = musics[index1];
				musics[index1] = tmp;
			}
			return true;
		}catch(NullPointerException e){
			return false;
		}
	}
	
	/**
	 * MusicListをArrayListで取得する．
	 * @return
	 */
	public ArrayList<Music> getMusicList(){
		try{
			Music[] musics = getMusics();
			ArrayList<Music> result = new ArrayList<Music>();
			for(Music music : musics)
				result.add(music);
			return result;
		}catch(NullPointerException e){
			return null;
		}
	}
	
	/**
	 * Musicリストを消去する．
	 * @param index
	 * @return
	 */
	public Music removeMusic(int index){
		try{
			Music[] musics = getMusics();
			ArrayList<Music> result = new ArrayList<Music>();
			for(Music music : musics)
				result.add(music);
			Music m = result.remove(index);
			setMusics(result.toArray(new Music[result.size()]));
			return m;
		}catch(NullPointerException e){
			return null;
		}
	}
}
