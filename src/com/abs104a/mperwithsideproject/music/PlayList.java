package com.abs104a.mperwithsideproject.music;

import java.util.ArrayList;

import android.net.Uri;

public final class PlayList extends Album {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7865838533199336968L;

	public PlayList(String albumName, String artist, long albumId, Uri jacketUri) {
		super(albumName, artist, albumId, jacketUri);
	}
	
	public static final ArrayList<PlayList> readPlayLists(){
		//TODO プレイリストを読み取るリスト部分の実装
		return null;
	}
	
	public static final boolean writePlayLists(){
		//PlayListを書き込む部分の実装
		return false;
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
