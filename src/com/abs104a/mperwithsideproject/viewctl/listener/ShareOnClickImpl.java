package com.abs104a.mperwithsideproject.viewctl.listener;

import com.abs104a.mperwithsideproject.R;
import com.abs104a.mperwithsideproject.music.Music;
import com.abs104a.mperwithsideproject.utl.GetImageTask;
import com.abs104a.mperwithsideproject.utl.MusicUtils;
import com.abs104a.mperwithsideproject.viewctl.MusicViewCtl;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import android.view.View.OnClickListener;

public class ShareOnClickImpl implements OnClickListener {

	@Override
	public void onClick(View v) {
		final Context con = v.getContext();
		final Music music = MusicUtils.getMusicController(con).getNowPlayingMusic();
		if(music != null){
			new GetImageTask(
					con, 
					con.getResources().getDimensionPixelSize(R.dimen.tweet_image_size), 
					new GetImageTask.OnGetImageListener() {
				
				@Override
				public void onGetImage(Bitmap image) {
					final String musicStrings = "NowPlaying... " + music.getTitle() + " / " +  music.getArtist() + " #MusicProjects";
					MusicViewCtl
					.sendShareTwit(
							con, 
							image, 
							musicStrings);
				}
			}).execute(music.getAlbumUri());

		}
	}

}
