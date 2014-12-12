package com.abs104a.mperwithsideproject.viewctl.listener;

import java.util.ArrayList;

import com.abs104a.mperwithsideproject.music.Music;
import com.abs104a.mperwithsideproject.music.MusicPlayerWithPlayLists;
import com.abs104a.mperwithsideproject.utl.MusicUtils;

import android.app.Service;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

/**
 * ListView上のMusicItemが選択されたときの動作
 * @author Kouki
 *
 */
public final class MusicItemOnClickListener implements OnItemClickListener {

	//ミュージックコントロールクラス
	private final MusicPlayerWithPlayLists mpwpl;
	//サービスのコンテキスト
	private final Service mService;
	//音楽リスト
	private final ArrayList<Music> items;
	private final View rootView;

	public MusicItemOnClickListener(Service mService, View rootView, ArrayList<Music> items,
			MusicPlayerWithPlayLists mpwpl) {
		this.mService = mService;
		this.items = items;
		this.mpwpl = mpwpl;
		this.rootView = rootView;
	}

	/**
	 * 
	 * 選択された時
	 */
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		view.setSelected(true);
		Music music = items.get(position);
		if(music != null){
			//TODO
			try {
				mpwpl.setSource(music.getPass());
				mpwpl.playStartAndPause();
				MusicUtils.setPartOfPlayerView(mService, rootView, music);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

}
