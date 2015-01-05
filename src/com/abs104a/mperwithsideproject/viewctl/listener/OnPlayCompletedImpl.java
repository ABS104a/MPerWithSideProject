package com.abs104a.mperwithsideproject.viewctl.listener;

import com.abs104a.mperwithsideproject.music.MusicPlayer.OnPlayCompletedListener;
import com.abs104a.mperwithsideproject.viewctl.MusicViewCtl;

/**
 * 再生が完了した時に呼ばれるリスナImpl
 * @author Kouki-Mobile
 *
 */
public final class OnPlayCompletedImpl implements OnPlayCompletedListener{


	/**
	 * 再生が完了した時
	 */
	@Override
	public void onPlayCompleted() {
		//再生が終了したとき 次の曲をセットする．
		try {
			//次の曲を再生
			MusicViewCtl.playNextWithView();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
