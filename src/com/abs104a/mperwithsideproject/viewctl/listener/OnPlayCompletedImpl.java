package com.abs104a.mperwithsideproject.viewctl.listener;

import android.view.View;

import com.abs104a.mperwithsideproject.music.MusicPlayer.OnPlayCompletedListener;
import com.abs104a.mperwithsideproject.utl.MusicUtils;

/**
 * 再生が完了した時に呼ばれるリスナImpl
 * @author Kouki-Mobile
 *
 */
public final class OnPlayCompletedImpl implements OnPlayCompletedListener{

	//rootView
	private final View rootView;

	/**
	 * インスタンスの作成
	 * @param mpwpl　ミュージックコントロールクラスのインスタンス．
	 */
	public OnPlayCompletedImpl(View rootView) {
		this.rootView = rootView;
	}

	/**
	 * 再生が完了した時
	 */
	@Override
	public void onPlayCompleted() {
		//再生が終了したとき 次の曲をセットする．
		try {
			//次の曲を再生
			MusicUtils.playNextWithView(rootView);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
