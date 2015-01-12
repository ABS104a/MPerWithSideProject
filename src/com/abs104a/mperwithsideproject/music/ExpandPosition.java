package com.abs104a.mperwithsideproject.music;

import java.io.Serializable;

/**
 * ExpandするMusicのIdを保管するクラス
 * @author Kouki
 *
 */
public final class ExpandPosition implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -8733921217995905246L;
	private long expandPosition = -1;

	/**
	 * @return expandPosition
	 */
	public long getExpandPosition() {
		return expandPosition;
	}

	/**
	 * @param expandPosition セットする expandPosition
	 */
	public void setExpandPosition(long expandPosition) {
		this.expandPosition = expandPosition;
	}

	/* (非 Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object o) {
		return o.equals(expandPosition);
	}
	
	
}
