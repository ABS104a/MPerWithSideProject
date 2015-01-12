package com.abs104a.mperwithsideproject.viewctl.listener;

import com.abs104a.mperwithsideproject.R;
import com.abs104a.mperwithsideproject.adapter.MusicListAdapter;
import com.abs104a.mperwithsideproject.adapter.PlayListForExpandableListAdapter;
import com.abs104a.mperwithsideproject.music.ExpandPosition;
import com.abs104a.mperwithsideproject.music.Music;

import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.widget.TextView;

/**
 * ListView用のItemがタップされた時にExpandメニューを表示するためのImplリスナ
 * @author Kouki
 *
 */
public final class ExpandActionOnClickImpl implements OnClickListener, OnLongClickListener {

	//ListViewのAdapter
	private final Object adapter;
	//対象となるMusicインスタンス
	private final Music item;
	private final ExpandPosition expandPosition;

	/**
	 * インスタンス生成
	 * @param adapter
	 * @param item
	 * @param expandPosition 
	 */
	public ExpandActionOnClickImpl(Object adapter,Music item, ExpandPosition expandPosition) {
		this.adapter = adapter;
		this.item = item;
		this.expandPosition = expandPosition;
	}

	/**
	 * クリックされた時
	 */
	@Override
	public void onClick(View view) {
		//Viewのタイトルで真のItemかどうか判断する．
		TextView textView = (TextView)((View)view.getParent()).findViewById(R.id.textView_album_title);
		if(textView == null || !textView.getText().toString().equals(item.getTitle()))
			return;
		if(expandPosition.equals(item.getId())){
			expandPosition.setExpandPosition(-1);
		}else{
			expandPosition.setExpandPosition(item.getId());
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

	/**
	 * 長押しされたとき
	 */
	@Override
	public boolean onLongClick(View v) {
		// TODO 自動生成されたメソッド・スタブ
		return false;
	}

}
