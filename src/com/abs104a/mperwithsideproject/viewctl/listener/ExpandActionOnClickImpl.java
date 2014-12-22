package com.abs104a.mperwithsideproject.viewctl.listener;

import com.abs104a.mperwithsideproject.R;

import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.FrameLayout;

/**
 * ListView用のItemがタップされた時にExpandメニューを表示するためのImplリスナ
 * @author Kouki
 *
 */
public final class ExpandActionOnClickImpl implements OnClickListener {

	private final View convertView;

	public ExpandActionOnClickImpl(View convertView){
		this.convertView = convertView;
	}
	
	/**
	 * クリックされた時
	 */
	@Override
	public void onClick(View v) {
		LayoutInflater layoutInflater = LayoutInflater.from(v.getContext());
		
		//データを挿入するViewを取得
		final FrameLayout frameLayout = (FrameLayout) convertView.findViewById(R.id.framelayout_album);
		final ViewGroup expandView = (ViewGroup)layoutInflater.inflate(R.layout.expand_album_item, null);
		
		//Viewを追加する．
		frameLayout.addView(expandView);
		
		//TODO ExpandViewの動作を登録する．
	}

}
