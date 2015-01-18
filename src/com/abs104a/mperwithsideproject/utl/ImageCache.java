package com.abs104a.mperwithsideproject.utl;

import java.util.Map.Entry;
import java.util.Set;

import android.graphics.Bitmap;

/**
 * ジャケット画像のキャッシュをするクラス
 * @author Kouki
 *
 */
public class ImageCache {

	//キャッシュするイメージ数
	private static final int IMAGESIZE = 15;
	//LRUキャッシュ
	private static ConcurrentLRUCache<String, Bitmap> cache = new ConcurrentLRUCache<String,Bitmap>(IMAGESIZE);
	
	/**
	 * キャッシュがあるか確認する
	 * @param key album
	 * @return	キャッシュがあるかどうか
	 */
	public static final boolean isCache(String key){
		return cache.containsKey(key);
	}
	
	/**
	 * キャッシュから画像を取得する
	 * @param key
	 * @return
	 */
	public static final Bitmap getImage(String key){
		return cache.get(key);
	}
	
	/**
	 * キャッシュに画像を登録する．
	 * @param key
	 * @param bitmap
	 */
	public static final void setImage(String key ,Bitmap bitmap){
		cache.put(key, bitmap);
	}
	
	/**
	 * キャッシュをクリアする
	 */
	public static final void clearCache(){
		Set<Entry<String, Bitmap>> data = cache.entrySet();
		for(Entry<String, Bitmap> d : data){
			if(!d.getValue().isRecycled())
				d.getValue().recycle();
			d.setValue(null);
		}
		cache.clear();
	}
}
