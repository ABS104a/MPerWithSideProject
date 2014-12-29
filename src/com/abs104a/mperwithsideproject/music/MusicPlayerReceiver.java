package com.abs104a.mperwithsideproject.music;

import com.abs104a.mperwithsideproject.Notifications;

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
		// TODO 自動生成されたメソッド・スタブ
		if(intent.getAction().equals(Intent.ACTION_MEDIA_BUTTON)){
			android.util.Log.v("MusicPlayerReceiver", intent.toString());
		}else if(intent.getAction().equals(Notifications.PLAY)){
			android.util.Log.v("MusicPlayerReceiver", intent.toString());
		}else if(intent.getAction().equals(Notifications.PREVIOUS)){
			android.util.Log.v("MusicPlayerReceiver", intent.toString());
		}else if(intent.getAction().equals(Notifications.NEXT)){
			android.util.Log.v("MusicPlayerReceiver", intent.toString());
		}
		android.util.Log.v("MusicPlayerReceiver", intent.toString());
	}

}
