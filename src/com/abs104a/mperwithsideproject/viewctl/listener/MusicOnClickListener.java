package com.abs104a.mperwithsideproject.viewctl.listener;

import java.util.ArrayList;

import com.abs104a.mperwithsideproject.music.Music;
import com.abs104a.mperwithsideproject.music.MusicPlayerWithPlayLists;
import com.abs104a.mperwithsideproject.utl.MusicUtils;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;

/**
 * ListView上のMusicItemが選択されたときの動作
 * @author Kouki
 *
 */
public final class MusicOnClickListener implements OnClickListener {

	//ミュージックコントロールクラス
	private final MusicPlayerWithPlayLists mpwpl;
	//サービスのコンテキスト
	private final Context context;
	//音楽リスト
	private final Music music;
	private final View rootView;

	public MusicOnClickListener(Context context, View rootView, Music item,
			MusicPlayerWithPlayLists mpwpl) {
		this.context = context;
		this.music = item;
		this.mpwpl = mpwpl;
		this.rootView = rootView;
	}

	@Override
	public void onClick(View view) {
		view.setSelected(true);
		if(music != null){
			//TODO
			try {
				ArrayList<Music> musicList = new ArrayList<Music>(1);
				musicList.add(music);
				mpwpl.setPlayList(musicList);
				mpwpl.playStartAndPause();
				MusicUtils.setPartOfPlayerView(context, rootView, music);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

}
