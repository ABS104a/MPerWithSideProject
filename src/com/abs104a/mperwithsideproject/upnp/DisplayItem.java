package com.abs104a.mperwithsideproject.upnp;

public class DisplayItem {

	private String title;
	private String subTitle;
	private String icon;
	private Object content;
	private Class<?> objType;
	
	public DisplayItem(String title,String subTitle,String icon,Object content,Class<?> objType){
		this.title = title;
		this.subTitle = subTitle;
		this.icon = icon;
		this.content = content;
		this.objType = objType;
	}
	
	/**
	 * @return title
	 */
	public final String getTitle() {
		return title;
	}
	/**
	 * @param title セットする title
	 */
	public final void setTitle(String title) {
		this.title = title;
	}
	/**
	 * @return subTitle
	 */
	public final String getSubTitle() {
		return subTitle;
	}
	/**
	 * @param subTitle セットする subTitle
	 */
	public final void setSubTitle(String subTitle) {
		this.subTitle = subTitle;
	}
	/**
	 * @return content
	 */
	public final Object getContent() {
		return content;
	}
	/**
	 * @param content セットする content
	 */
	public final void setContent(Object content,Class<?> objType) {
		this.content = content;
		this.objType = objType;
	}
	/**
	 * @return icon
	 */
	public String getIcon() {
		return icon;
	}
	/**
	 * @param icon セットする icon
	 */
	public void setIcon(String icon) {
		this.icon = icon;
	}
	/**
	 * @return objType
	 */
	public final Class<?> getObjType() {
		return objType;
	}

	/* (非 Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object o) {
		if(o instanceof DisplayItem){
			return this.title.equals(((DisplayItem)o).title) && this.subTitle.equals(((DisplayItem)o).subTitle);
		}else{
			return false;
		}
	}
	
	
	
}
