package com.abs104a.mperwithsideproject.viewctl.listener;

import com.abs104a.mperwithsideproject.Column;
import com.abs104a.mperwithsideproject.music.Music;
import com.abs104a.mperwithsideproject.utl.DialogUtils;
import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;

public final class DeleteOnClickImpl implements OnClickListener, OnLongClickListener  {

	//rootView(MusicPlayerView)
	//music of item
	private final Music item;
	//music controller class
	//Itemのcolumn
	private final int column;

	/**
	 * インスタンスの生成
	 * @param mContext	アプリケーションのコンテキスト
	 * @param expandposition 
	 * @param item 
	 * @param rootView 
	 */
	public DeleteOnClickImpl(Context mContext,int column, Music item) {
		this.item = item;
		this.column = column;
	}

	/**
	 * ボタンがクリックされたとき
	 * プレイリストへの消去を行う.
	 */
	@Override
	public void onClick(View v) {
		if(column == Column.QUEUE){
			//Queueへの消去を行う
			new DialogUtils().deleteQueueDialog(v.getContext(), item);
		}
	}

	/**
	 * 長押しされたとき
	 */
	@Override
	public boolean onLongClick(View v) {
		// TODO プレイリストへの追加を行う
		return true;
	}
	
}
