package com.abs104a.mperwithsideproject;

import com.abs104a.mperwithsideproject.settings.Settings;
import com.abs104a.mperwithsideproject.viewctl.MainViewCtl;
import com.abs104a.mperwithsideproject.viewctl.MusicViewCtl;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;

/**
 * このアプリケーションのBroadcastレシーバー
 * 
 * ・画面のonoff
 * ・ヘッドセットの接続
 * 
 * を検知する．
 * 
 * @author Kouki
 *
 */
public final class MyBroadCastReceiver extends BroadcastReceiver {

	private final Service mService;
	
	public final static String VOLUME_CHANGE = "android.media.VOLUME_CHANGED_ACTION";
	
	private boolean screenFlag = true;

	public MyBroadCastReceiver(Service mService){
		this.mService = mService;
	}
	
	@Override
	public void onReceive(Context context, Intent intent) {
		String action = intent.getAction();  
		if (action != null) {  
			View rootView = MainViewCtl.getRootView();
			//画面onoff時の動作を設定する
			if (action.equals(Intent.ACTION_SCREEN_ON)) {  
				// 画面ON時  
				Log.d("MainService", "SCREEN_ON");  
				
				if(rootView != null && MusicViewCtl.getPlayerView() == null && screenFlag == false){
					MusicViewCtl.createPlayerView(mService, rootView);
					screenFlag = true;
				}
			} else if (action.equals(Intent.ACTION_SCREEN_OFF)) {  
				// 画面OFF時  
				Log.d("MainService", "SCREEN_OFF");  
				if(rootView != null && MusicViewCtl.getPlayerView() != null){
					MusicViewCtl.removePlayerView(rootView);
					screenFlag = false;
				}
			} else if( action.equals(Intent.ACTION_HEADSET_PLUG)){
				//ヘッドセットの動作を検出した時
				int state = intent.getIntExtra("state", 0);
				
				if(state == 0){
					//ヘッドセットが外されたとき
					if(Settings.getHeadsetUnStopPlay(mService))
						MusicViewCtl.pauseWithView();
					Log.d("MainService","HEDSET_OFF");  
				}else{
					//ヘッドセットがついたとき
					if(Settings.getHeadsetOnStartPlay(mService))
						MusicViewCtl.playWithView();
					Log.d("MainService", "HEDSET_ON");  
				}
			} else if(action.equals(VOLUME_CHANGE)){
				//音量が変わったとき
				//音量の変更に対してViewに反映する．
				MusicViewCtl.changeVolume();
				Log.d("MainService", "VOLUME_CHANGE");
			}
			
		}
	}

}
