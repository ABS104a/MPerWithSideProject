package com.abs104a.mperwithsideproject.viewctl.listener;

import java.util.ArrayList;

import com.abs104a.mperwithsideproject.music.PlayList;
import com.abs104a.mperwithsideproject.utl.DialogUtils;

import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
/**
 * Editボタンが
 * タップ　or　長押し
 * された時に呼ばれるImplリスナ
 * @author Kouki-Mobile
 *
 */
public final class EditOfPlayListOnLCImpl implements OnClickListener,
		OnLongClickListener {
	
	public final static String TAG = "EditOfPlayListOnLCImpl";
	
	private final ArrayList<PlayList> playList;
	private final int index;

	public EditOfPlayListOnLCImpl(int index ,ArrayList<PlayList> playList){
		this.index = index;
		this.playList = playList;
	}

	@Override
	public boolean onLongClick(View v) {
		// TODO Editボタンが長押しされた時
		return true;
	}

	@Override
	public void onClick(View v) {
		DialogUtils.createDialogIfEditPlayList(v.getContext(), index, playList);
		android.util.Log.v(TAG, "position : " + index);

	}

}
