package com.abs104a.mperwithsideproject.music;

import com.abs104a.mperwithsideproject.MainService;
import com.abs104a.mperwithsideproject.Notifications;
import com.abs104a.mperwithsideproject.utl.MusicUtils;
import com.abs104a.mperwithsideproject.viewctl.MusicPlayerViewController;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * ロック画面の通知に対する動作がされた時に受けるBroadCastReceiver
 * @author Kouki-Mobile
 *
 */
public class MusicPlayerReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		if(intent.getAction().equals(Notifications.MAIN)){
			if(MainService.getService() != null && MainService.getRootView() != null)
				MusicPlayerViewController.createPlayerView(
						MainService.getService(),
						MainService.getRootView());
			android.util.Log.v("MusicPlayerReceiver", intent.toString());
		}else if(intent.getAction().equals(Notifications.PLAY)){
			MusicUtils.playOrPauseWithView();
			android.util.Log.v("MusicPlayerReceiver", intent.toString());
		}else if(intent.getAction().equals(Notifications.PREVIOUS)){
			MusicUtils.playBackWithView();
			android.util.Log.v("MusicPlayerReceiver", intent.toString());
		}else if(intent.getAction().equals(Notifications.NEXT)){
			MusicUtils.playNextWithView();
			android.util.Log.v("MusicPlayerReceiver", intent.toString());
		}
	}

}
