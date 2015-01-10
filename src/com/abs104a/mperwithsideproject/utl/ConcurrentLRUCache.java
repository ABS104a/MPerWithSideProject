package com.abs104a.mperwithsideproject.utl;

import java.util.LinkedHashMap;
import java.util.Map;

import android.graphics.Bitmap;

/**
 * LRUキャッシュ
 * @author Kouki
 *
 * @param <A>
 * @param <B>
 */
public class ConcurrentLRUCache<A, B> extends LinkedHashMap<A, B> {
    /**
	 * 
	 */
	private static final long serialVersionUID = -8773466021097489813L;
	private final int maxEntries;

	/**
	 * インスタンスの生成
	 * @param maxEntries	最大保存数．
	 */
    public ConcurrentLRUCache(final int maxEntries) {
        super(maxEntries + 1, 1.0f, true);
        this.maxEntries = maxEntries;
    }

    /**
     * Returns <tt>true</tt> if this <code>LruCache</code> has more entries than the maximum specified when it was
     * created.
     *
     * <p>
     * This method <em>does not</em> modify the underlying <code>Map</code>; it relies on the implementation of
     * <code>LinkedHashMap</code> to do that, but that behavior is documented in the JavaDoc for
     * <code>LinkedHashMap</code>.
     * </p>
     *
     * @param eldest
     *            the <code>Entry</code> in question; this implementation doesn't care what it is, since the
     *            implementation is only dependent on the size of the cache
     * @return <tt>true</tt> if the oldest
     * @see java.util.LinkedHashMap#removeEldestEntry(Map.Entry)
     */
    @Override
    protected boolean removeEldestEntry(final Map.Entry<A, B> eldest) {
        return super.size() > maxEntries;
    }

	@Override
	public B remove(Object key) {
		try{
			//消去時の動作，意図的に破棄する必要のあるリソースはここで破棄する．
			B removeObject = super.remove(key);	
			if(removeObject instanceof Bitmap){
				((Bitmap) removeObject).recycle();
				removeObject = null;
			}
			return removeObject;
		}catch(Exception e){
			e.printStackTrace();
			android.util.Log.e("LRUCache","NotDeleted!");
			return null;
		}
		
	}
    
}
