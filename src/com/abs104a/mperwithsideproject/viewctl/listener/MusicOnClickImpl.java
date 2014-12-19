package com.abs104a.mperwithsideproject.viewctl.listener;

import com.abs104a.mperwithsideproject.music.Music;
import com.abs104a.mperwithsideproject.music.MusicPlayerWithQueue;
import com.abs104a.mperwithsideproject.utl.MusicUtils;
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
	//RootView
	private final View rootView;
	//タップした時にキューに追加するかどうか
	private final boolean isAddQueue;

	/**
	 * インスタンスの生成
	 * @param context	アプリケーションのコンテキスト
	 * @param rootView	ルートのView
	 * @param item		対象となるMusicインスタンス
	 * @param mpwpl		ミュージックコントロールインスタンス
	 * @param isAddQueue	タップされたときにキューに登録するかどうか
	 */
	public MusicOnClickImpl(View rootView, Music item,
			boolean isAddQueue) {
		this.music = item;
		this.isAddQueue = isAddQueue;
		this.rootView = rootView;
	}

	@Override
	public void onClick(View view) {
		MusicPlayerWithQueue mpwpl = MusicUtils.getMusicController(view.getContext());
		view.setSelected(true);
		if(music != null){
			try {
				int index = 0;
				if(isAddQueue){
					//キューに追加する時
					mpwpl.addMusic(music, index);
				}else{
					index = mpwpl.getQueue().indexOf(music);
					//曲がQueueにないときだけ追加する．
					if(index == -1){
						mpwpl.addMusic(music, index);
					}
				}
				MusicUtils.playOrPauseWithView(rootView,index);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

}
