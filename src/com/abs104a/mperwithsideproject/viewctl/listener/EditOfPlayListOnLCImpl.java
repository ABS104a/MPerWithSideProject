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
	
	//Tag
	public final static String TAG = "EditOfPlayListOnLCImpl";
	//対象となるプレイリスト
	private final ArrayList<PlayList> playList;
	//プレイリストのポジション
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
		//プレイリストを編集するためのダイアログを生成する．
		new DialogUtils().createDialogIfEditPlayList(v.getContext(), index, playList);
		android.util.Log.v(TAG, "position : " + index);

	}

}
