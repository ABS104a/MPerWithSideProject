package com.abs104a.mperwithsideproject.music;

import com.abs104a.mperwithsideproject.MainService;
import com.abs104a.mperwithsideproject.Notifications;
import com.abs104a.mperwithsideproject.utl.MusicUtils;
import com.abs104a.mperwithsideproject.viewctl.MainViewCtl;
import com.abs104a.mperwithsideproject.viewctl.MusicViewCtl;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.view.KeyEvent;

/**
 * ロック画面の通知に対する動作がされた時に受けるBroadCastReceiver
 * @author Kouki-Mobile
 *
 */
public class MusicPlayerReceiver extends BroadcastReceiver {
	
	private static boolean buttonFlag = false;
	
	private final static int DELAY_TIME = 300;

	@Override
	public void onReceive(Context context, Intent intent) {
		if(intent.getAction().equals(Notifications.MAIN)){
			if(MainService.getService() != null && MainViewCtl.getRootView() != null)
				MusicViewCtl.createPlayerView(
						MainService.getService(),
						MainViewCtl.getRootView());
			android.util.Log.v("MusicPlayerReceiver", intent.toString());
		}else if(intent.getAction().equals(Notifications.PLAY)){
			MusicViewCtl.playOrPauseWithView();
			android.util.Log.v("MusicPlayerReceiver", intent.toString());
		}else if(intent.getAction().equals(Notifications.PREVIOUS)){
			MusicViewCtl.playBackWithView();
			android.util.Log.v("MusicPlayerReceiver", intent.toString());
		}else if(intent.getAction().equals(Notifications.NEXT)){
			MusicViewCtl.playNextWithView();
			android.util.Log.v("MusicPlayerReceiver", intent.toString());
		}else if(intent.getAction().equals("android.intent.action.MEDIA_BUTTON")){
			if(buttonFlag)return;;
			buttonFlag = true;
		
			//MedioButtonの動作
			KeyEvent event = (KeyEvent)intent.getParcelableExtra(Intent.EXTRA_KEY_EVENT);
			MusicPlayerWithQueue mpwpl = MusicUtils.getMusicController(context);

			if(mpwpl != null){
				try{
					switch (event.getKeyCode()) {
					case KeyEvent.KEYCODE_HEADSETHOOK:
					case KeyEvent.KEYCODE_MEDIA_PLAY_PAUSE:
						//Pause
						mpwpl.playStartAndPause();
						break;
					case KeyEvent.KEYCODE_MEDIA_PLAY:
						mpwpl.playStartAndPause();
						//Play
						break;
					case KeyEvent.KEYCODE_MEDIA_PAUSE:
						//Pause
						mpwpl.playStartAndPause();
						break;
					case KeyEvent.KEYCODE_MEDIA_STOP:
						//Stop
						mpwpl.playStop();
						break;
					case KeyEvent.KEYCODE_MEDIA_NEXT:
						//Next
						mpwpl.playNext();
						break;
					case KeyEvent.KEYCODE_MEDIA_PREVIOUS:
						//Previous
						mpwpl.playBack();
						break;
					}
					Notifications.putNotification();
				}catch(Exception e){
					e.printStackTrace();
				}
			}
			new Handler().postDelayed(new Runnable(){

				@Override
				public void run() {
					buttonFlag  = false;
				}
				
			}, DELAY_TIME);
		}
	}

}
