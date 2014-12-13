package com.abs104a.mperwithsideproject.adapter;

import java.util.ArrayList;
import com.abs104a.mperwithsideproject.music.PlayList;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;

/**
 * PlayListを表示するためのExpandListView用Adapter
 * @author Kouki
 *
 */
public class PlayListForExpandableListAdapter extends
	BaseExpandableListAdapter {
	
	//グループのレイアウトID
	private static final int GROUP_LAYOUT = 1; //TODO
	//子要素のレイアウトID
	private static final int CHILD_LAYOUT = 1; //TODO
	
	//アプリケーションコンテキスト
	private Context mContext;
	//プレイリスト
	private ArrayList<PlayList> playLists;
	
	/**
	 * インスタンスの生成
	 * @param mContext　アプリケーションのコンテキスト
	 * @param playLists	プレイリスト
	 */
	public PlayListForExpandableListAdapter(Context mContext,ArrayList<PlayList> playLists){
		this.mContext = mContext;
		this.playLists = playLists;
	}

	@Override
	public Object getChild(int groupPosition, int childPosition) {
		// TODO 自動生成されたメソッド・スタブ
		return null;
	}

	@Override
	public long getChildId(int groupPosition, int childPosition) {
		// TODO 自動生成されたメソッド・スタブ
		return 0;
	}

	@Override
	public View getChildView(int groupPosition, int childPosition,
			boolean isLastChild, View convertView, ViewGroup parent) {
		// TODO 自動生成されたメソッド・スタブ
		return null;
	}

	@Override
	public int getChildrenCount(int groupPosition) {
		// TODO 自動生成されたメソッド・スタブ
		return 0;
	}

	@Override
	public Object getGroup(int groupPosition) {
		// TODO 自動生成されたメソッド・スタブ
		return null;
	}

	@Override
	public int getGroupCount() {
		// TODO 自動生成されたメソッド・スタブ
		return 0;
	}

	@Override
	public long getGroupId(int groupPosition) {
		// TODO 自動生成されたメソッド・スタブ
		return 0;
	}

	@Override
	public View getGroupView(int groupPosition, boolean isExpanded,
			View convertView, ViewGroup parent) {
		// TODO 自動生成されたメソッド・スタブ
		return null;
	}

	@Override
	public boolean hasStableIds() {
		// TODO 自動生成されたメソッド・スタブ
		return false;
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		// TODO 自動生成されたメソッド・スタブ
		return false;
	}

	

	


}
