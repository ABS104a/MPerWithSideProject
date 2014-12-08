package com.abs104a.mperwithsideproject.viewctl.listener;

import com.abs104a.mperwithsideproject.music.MusicPlayerWithPlayLists;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;

/**
 * シャッフルボタンが押された時に呼ばれるリスナImpl
 * @author Kouki-Mobile
 *
 */
public final class ShuffleButtonOnClickImpl implements OnClickListener {

	//シャッフルボタンのView
	private final ImageButton _button;
	//ミュージックコントロールクラスのインスタンス
	private final MusicPlayerWithPlayLists _mpwpl;

	/**
	 * インスタンスの生成
	 * @param shuffleButton　シャッフルボタンのView
	 * @param mpwpl　ミュージッククラスのインスタンス
	 */
	public ShuffleButtonOnClickImpl(ImageButton shuffleButton,
			MusicPlayerWithPlayLists mpwpl) {
		_button = shuffleButton;
		_mpwpl = mpwpl;
	}

	/**
	 * クリックされた時
	 */
	@Override
	public void onClick(View v) {
		boolean isShuffle = _mpwpl.setShuffle(!_mpwpl.isShuffle());
		if(isShuffle){
			//シャッフルがONの時
			//Viewへの反映
		}else{
			//シャッフルがOFFの時
		}
	}

}
