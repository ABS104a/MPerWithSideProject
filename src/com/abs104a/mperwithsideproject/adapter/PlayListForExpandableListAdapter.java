package com.abs104a.mperwithsideproject.adapter;

import java.util.ArrayList;
import com.abs104a.mperwithsideproject.music.PlayList;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;

public class PlayListForExpandableListAdapter extends
	BaseExpandableListAdapter {
	
	private static final int LAYOUT = 1; //TODO
	private Context mContext;
	private ArrayList<PlayList> playLists;
	
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
