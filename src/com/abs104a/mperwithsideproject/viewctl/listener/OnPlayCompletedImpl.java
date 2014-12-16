package com.abs104a.mperwithsideproject.viewctl.listener;

import android.view.View;

import com.abs104a.mperwithsideproject.music.MusicPlayer.OnPlayCompletedListener;
import com.abs104a.mperwithsideproject.music.MusicPlayerWithQueue;
import com.abs104a.mperwithsideproject.viewctl.MusicPlayerViewController;

/**
 * 再生が完了した時に呼ばれるリスナImpl
 * @author Kouki-Mobile
 *
 */
public final class OnPlayCompletedImpl implements OnPlayCompletedListener{

	//ミュージックコントロールクラスのインスタンス
	private final MusicPlayerWithQueue _mpwpl;
	private final View rootView;

	/**
	 * インスタンスの作成
	 * @param mpwpl　ミュージックコントロールクラスのインスタンス．
	 */
	public OnPlayCompletedImpl(MusicPlayerWithQueue mpwpl,View rootView) {
		this._mpwpl = mpwpl;
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
			_mpwpl.playNext();
			MusicPlayerViewController.setPartOfPlayerView(
					rootView.getContext(), 
					rootView,
					_mpwpl.getNowPlayingMusic(),
					_mpwpl);
			
		} catch (Exception e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		}
	}
}
