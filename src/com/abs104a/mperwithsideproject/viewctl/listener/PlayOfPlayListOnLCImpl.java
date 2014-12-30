package com.abs104a.mperwithsideproject.viewctl.listener;

import java.util.ArrayList;

import com.abs104a.mperwithsideproject.music.Music;
import com.abs104a.mperwithsideproject.music.MusicPlayerWithQueue;
import com.abs104a.mperwithsideproject.music.PlayList;
import com.abs104a.mperwithsideproject.utl.MusicUtils;
import com.abs104a.mperwithsideproject.viewctl.MusicPlayerViewController;

import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;

/**
 * PlayListの再生ボタンを
 * タップ　or　長押し
 * した時に呼ばれるImplリスナ
 * @author Kouki-Mobile
 *
 */
public class PlayOfPlayListOnLCImpl implements OnClickListener,
		OnLongClickListener {
	
	private final PlayList mPlayList;

	public PlayOfPlayListOnLCImpl(PlayList playList){
		this.mPlayList = playList;
	}

	@Override
	public boolean onLongClick(View view) {
		//　長押しした時　PlayListをセットする
		MusicPlayerWithQueue mpwpl = MusicUtils.getMusicController(view.getContext());
		try {
			ArrayList<Music> list = mPlayList.getMusicList();
			mpwpl.setPlayList(list);
			mpwpl.setCursor(0);
			if(list.size() > 0){
				MusicUtils.playOrPauseWithView(MusicPlayerViewController.getPlayerView());
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return true;
	}

	@Override
	public void onClick(View view) {
		// タップした時，Queueに追加する．
		MusicPlayerWithQueue mpwpl = MusicUtils.getMusicController(view.getContext());
		try {
			ArrayList<Music> list = mPlayList.getMusicList();
			mpwpl.addPlayList(list);	
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
