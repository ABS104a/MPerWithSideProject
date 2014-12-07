package com.abs104a.mperwithsideproject.viewctl.listener;

import java.io.IOException;

import com.abs104a.mperwithsideproject.music.MusicPlayer.OnPlayCompletedListener;
import com.abs104a.mperwithsideproject.music.MusicPlayerWithPlayLists;

/**
 * 再生が完了した時に呼ばれるリスナImpl
 * @author Kouki-Mobile
 *
 */
public final class OnPlayCompletedImpl implements OnPlayCompletedListener{

	//ミュージックコントロールクラスのインスタンス
	private final MusicPlayerWithPlayLists _mpwpl;

	/**
	 * インスタンスの作成
	 * @param mpwpl　ミュージックコントロールクラスのインスタンス．
	 */
	public OnPlayCompletedImpl(MusicPlayerWithPlayLists mpwpl) {
		this._mpwpl = mpwpl;
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
		} catch (IllegalArgumentException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		} catch (SecurityException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		} catch (IllegalStateException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		} catch (IOException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		}
	}
}
