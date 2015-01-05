package com.abs104a.mperwithsideproject.viewctl.listener;

import java.util.ArrayList;

import com.abs104a.mperwithsideproject.music.Music;
import com.abs104a.mperwithsideproject.music.MusicPlayerWithQueue;
import com.abs104a.mperwithsideproject.music.PlayList;
import com.abs104a.mperwithsideproject.utl.MusicUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.widget.Toast;

/**
 * PlayListの再生ボタンを
 * タップ　or　長押し
 * した時に呼ばれるImplリスナ
 * @author Kouki-Mobile
 *
 */
public class AddOfPlayListOnLCImpl implements OnClickListener,
		OnLongClickListener {
	
	private final PlayList mPlayList;

	public AddOfPlayListOnLCImpl(PlayList playList){
		this.mPlayList = playList;
	}

	@Override
	public boolean onLongClick(View view) {
		//TODO
		return true;
	}

	@Override
	public void onClick(View view) {
		// タップした時，Queueに追加する．
		MusicPlayerWithQueue mpwpl = MusicUtils.getMusicController(view.getContext());
		try {
			ArrayList<Music> list = mPlayList.getMusicList();
			mpwpl.addPlayList(list);	
			Toast.makeText(view.getContext(), mPlayList.getAlbum() + " → Queue", Toast.LENGTH_SHORT).show();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
