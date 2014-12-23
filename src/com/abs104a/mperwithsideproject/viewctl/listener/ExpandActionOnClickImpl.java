package com.abs104a.mperwithsideproject.viewctl.listener;

import com.abs104a.mperwithsideproject.R;
import com.abs104a.mperwithsideproject.adapter.MusicListAdapter;
import com.abs104a.mperwithsideproject.adapter.PlayListForExpandableListAdapter;
import com.abs104a.mperwithsideproject.music.Music;

import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.FrameLayout;

/**
 * ListView用のItemがタップされた時にExpandメニューを表示するためのImplリスナ
 * @author Kouki
 *
 */
public final class ExpandActionOnClickImpl implements OnClickListener, OnLongClickListener {

	private final Object adapter;
	private final Music item;

	public ExpandActionOnClickImpl(Object adapter,Music item) {
		this.adapter = adapter;
		this.item = item;
	}

	/**
	 * クリックされた時
	 */
	@Override
	public void onClick(View view) {
		if(item.isExpandView()){
			item.setExpandView(false);
		}else{
			item.setExpandView(true);
		}
		//adapterに変更を通知する．
		if(adapter != null){
			if(adapter instanceof MusicListAdapter){
				((MusicListAdapter)adapter).notifyDataSetChanged();
			}else if(adapter instanceof PlayListForExpandableListAdapter){
				((PlayListForExpandableListAdapter)adapter).notifyDataSetChanged();
			}
		}
	}

	@Override
	public boolean onLongClick(View v) {
		// TODO 自動生成されたメソッド・スタブ
		return false;
	}

}
