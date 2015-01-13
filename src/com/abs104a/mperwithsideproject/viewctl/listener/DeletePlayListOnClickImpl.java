package com.abs104a.mperwithsideproject.viewctl.listener;

import java.util.ArrayList;

import com.abs104a.mperwithsideproject.music.PlayList;
import com.abs104a.mperwithsideproject.utl.DialogUtils;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;

/**
 * プレイリストの消去ボタンが押された時に呼ばれるImplリスナ
 * @author Kouki
 *
 */
public final class DeletePlayListOnClickImpl implements OnClickListener  {

	//アプリケーションのコンテキスト
	private Context mContext;
	//GroupPosition
	private int groupPosition;
	//ChildPosition
	private int childPosition;
	//Music PlayList
	private ArrayList<PlayList> playLists;

	/**
	 * インスタンスの生成
	 * @param mContext	アプリケーションのコンテキスト
	 * @param childPosition 
	 * @param groupPosition 
	 * @param rootView 
	 */
	public DeletePlayListOnClickImpl(Context mContext,ArrayList<PlayList> playLists, int groupPosition, int childPosition) {
		this.mContext = mContext;
		this.groupPosition = groupPosition;
		this.childPosition = childPosition;
		this.playLists = playLists;
	}

	/**
	 * ボタンがクリックされたとき
	 * プレイリストの消去を行う．
	 */
	@Override
	public void onClick(View v) {
		//TODO 確認画面
		//PlayList消去を行う
		new DialogUtils().deletePlayListItemDialog(mContext, childPosition, playLists.get(groupPosition));
	}
	
}
