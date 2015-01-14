package com.abs104a.mperwithsideproject.music;

import com.abs104a.mperwithsideproject.utl.MusicUtils;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.view.KeyEvent;

/**
 * ロック画面の通知に対する動作がされた時に受けるBroadCastReceiver
 * @author Kouki-Mobile
 *
 */
public class MediaButtonReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		
		if(intent.getAction().equals("android.intent.action.MEDIA_BUTTON")){
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
				}catch(Exception e){
					e.printStackTrace();
				}
			}
		}
	}

}
