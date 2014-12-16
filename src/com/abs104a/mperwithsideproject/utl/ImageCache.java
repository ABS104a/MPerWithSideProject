package com.abs104a.mperwithsideproject.utl;

import android.graphics.Bitmap;

/**
 * ジャケット画像のキャッシュをするクラス
 * @author Kouki
 *
 */
public class ImageCache {

	//キャッシュするイメージ数
	private static final int IMAGESIZE = 20;
	//LRUキャッシュ
	private static ConcurrentLRUCache<String, Bitmap> cache = new ConcurrentLRUCache<String,Bitmap>(IMAGESIZE);
	
	/**
	 * キャッシュがあるか確認する
	 * @param key
	 * @return	キャッシュがあるかどうか
	 */
	public static final boolean isCache(String key){
		return cache.containsKey(cache);
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
}
