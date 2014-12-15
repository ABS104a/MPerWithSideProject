package com.abs104a.mperwithsideproject.viewctl.listener;

import com.abs104a.mperwithsideproject.R;
import com.abs104a.mperwithsideproject.music.MusicPlayerWithQueue;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.Toast;

/**
 * シャッフルボタンが押された時に呼ばれるリスナImpl
 * @author Kouki-Mobile
 *
 */
public final class ShuffleButtonOnClickImpl implements OnClickListener {

	//シャッフルボタンのView
	private final ImageButton _button;
	//ミュージックコントロールクラスのインスタンス
	private final MusicPlayerWithQueue _mpwpl;

	/**
	 * インスタンスの生成
	 * @param shuffleButton　シャッフルボタンのView
	 * @param mpwpl　ミュージッククラスのインスタンス
	 */
	public ShuffleButtonOnClickImpl(ImageButton shuffleButton,
			MusicPlayerWithQueue mpwpl) {
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
			Toast.makeText(v.getContext(), R.string.shuffle_on, Toast.LENGTH_SHORT).show();
			//シャッフルがONの時
			//Viewへの反映
		}else{
			//シャッフルがOFFの時
			Toast.makeText(v.getContext(), R.string.shuffle_off, Toast.LENGTH_SHORT).show();
		}
	}

}
