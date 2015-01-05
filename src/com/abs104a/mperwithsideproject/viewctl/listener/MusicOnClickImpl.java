package com.abs104a.mperwithsideproject.viewctl.listener;

import com.abs104a.mperwithsideproject.music.Music;
import com.abs104a.mperwithsideproject.music.MusicPlayerWithQueue;
import com.abs104a.mperwithsideproject.utl.MusicUtils;
import com.abs104a.mperwithsideproject.viewctl.MusicViewCtl;

import android.view.View;
import android.view.View.OnClickListener;

/**
 * ListView上のMusicItemが選択されたときの動作
 * @author Kouki
 *
 */
public final class MusicOnClickImpl implements OnClickListener {

	//音楽リスト
	private final Music music;

	/**
	 * インスタンスの生成
	 * @param context	アプリケーションのコンテキスト
	 * @param rootView	ルートのView
	 * @param item		対象となるMusicインスタンス
	 * @param mpwpl		ミュージックコントロールインスタンス
	 * @param isAddQueue	タップされたときにキューに登録するかどうか
	 */
	public MusicOnClickImpl( Music item) {
		this.music = item;
	}

	@Override
	public void onClick(View view) {
		MusicPlayerWithQueue mpwpl = MusicUtils.getMusicController(view.getContext());
		view.setSelected(true);
		if(music != null){
			try {
				int index = 0;
				index = mpwpl.getQueue().indexOf(music);
				//曲がQueueにないときだけ追加する．
				if(index == -1){
					index = 0;
					mpwpl.addMusic(music, index);
				}
				MusicViewCtl.playOrPauseWithView(index);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

}
