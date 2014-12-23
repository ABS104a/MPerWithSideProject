package com.abs104a.mperwithsideproject.adapter;

import java.util.ArrayList;
import com.abs104a.mperwithsideproject.R;
import com.abs104a.mperwithsideproject.music.Music;
import com.abs104a.mperwithsideproject.music.MusicPlayerWithQueue;
import com.abs104a.mperwithsideproject.utl.DisplayUtils;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;

/**
 * アルバムのリスト管理をするAdapter
 * @author Kouki
 *
 */
public final class MusicListAdapter extends ArrayAdapter<Music> {

	//ミュージックプレイヤーコントロールインスタンス
	private final MusicPlayerWithQueue mpwpl;
	//RootView
	private final View rootView;
	//プレイリスト追加ボタンを消去ボタンにするかどうか
	private boolean isDelete = false;
	
	private ListAdapter adapter = this;

	public MusicListAdapter(
			Context context,
			View rootView, ArrayList<Music> items,
			MusicPlayerWithQueue mpwpl) 
	{
		super(context, R.layout.album_item_row, items);
		this.mpwpl = mpwpl;
		this.rootView = rootView;
	}
	
	/**
	 * Viewのカラムにプレイリスト用のボタンを追加するかどうかの設定
	 * true = 消去
	 * false = Queueに追加
	 * default = false;
	 * @param isShow 
	 */
	public void setButtonForDelete(boolean isdelete) {
		this.isDelete = isdelete;
	}
	
	/**
	 * Viewのカラムにプレイリスト用のボタンを追加するかどうかの設定を取得
	 * @return
	 */
	public boolean isButtonForDelete(){
		return isDelete;
	}

	/**
	 * Viewの生成
	 */
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final Music item = getItem(position);
		return DisplayUtils
				.getChildView(convertView, item, getContext(), isDelete, rootView, adapter, mpwpl);
	}
	


}
