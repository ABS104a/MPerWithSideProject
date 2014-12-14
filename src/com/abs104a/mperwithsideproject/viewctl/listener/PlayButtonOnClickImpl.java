package com.abs104a.mperwithsideproject.viewctl.listener;

import com.abs104a.mperwithsideproject.music.MusicPlayerWithQueue;

import android.view.View;
import android.view.View.OnClickListener;

/**
 * 再生ボタンを押した時に呼ばれるリスナImpl
 * @author Kouki-Mobile
 *
 */
public final class PlayButtonOnClickImpl implements OnClickListener {

	//プレイヤーコントロールインスタンス
	private final MusicPlayerWithQueue _mpwpl;

	public PlayButtonOnClickImpl(MusicPlayerWithQueue mpwpl) {
		this._mpwpl = mpwpl;
	}

	@Override
	public void onClick(View v) {
		//再生動作を行う
		int state;
		try {
			state = _mpwpl.playStartAndPause();
			if(state == MusicPlayerWithQueue.PLAYING){
				//Viewを一時停止ボタンに
				v.setBackgroundResource(android.R.drawable.ic_media_pause);
			}else{
				//Viewを再生ボタンに
				v.setBackgroundResource(android.R.drawable.ic_media_play);
			}
		} catch (Exception e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		}
	}

}
