package com.abs104a.mperwithsideproject.music;

import java.io.Serializable;

/**
 * EqualizerのItem
 * @author Kouki-Mobile
 *
 */
public class EqualizerItem implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 4973814894685270450L;
	
	//バンド幅
	private short band = 0;
	//設定レベル
	private short level = 0;
	
	public EqualizerItem(short band,short level){
		this.band = band;
		this.level = level;
	}
	
	/**
	 * @return band
	 */
	public short getBand() {
		return band;
	}
	/**
	 * @param band セットする band
	 */
	public void setBand(short band) {
		this.band = band;
	}
	/**
	 * @return level
	 */
	public short getLevel() {
		return level;
	}
	/**
	 * @param level セットする level
	 */
	public void setLevel(short level) {
		this.level = level;
	}

	
}
