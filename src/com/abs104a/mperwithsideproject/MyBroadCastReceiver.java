package com.abs104a.mperwithsideproject;

import com.abs104a.mperwithsideproject.utl.MusicUtils;
import com.abs104a.mperwithsideproject.viewctl.MusicPlayerViewController;

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
	private final View rootView;

	public MyBroadCastReceiver(Service mService,View rootView){
		this.mService = mService;
		this.rootView = rootView;
	}
	
	@Override
	public void onReceive(Context context, Intent intent) {
		String action = intent.getAction();  
		if (action != null) {  
			//画面onoff時の動作を設定する
			if (action.equals(Intent.ACTION_SCREEN_ON)) {  
				// 画面ON時  
				Log.d("MainService", "SCREEN_ON");  
				if(rootView != null)
					MusicPlayerViewController.createPlayerView(mService, rootView);
			} else if (action.equals(Intent.ACTION_SCREEN_OFF)) {  
				// 画面OFF時  
				Log.d("MainService", "SCREEN_OFF");  
				if(rootView != null)
					MusicPlayerViewController.removePlayerView(rootView);
			} else if( action.equals(Intent.ACTION_HEADSET_PLUG)){
				//ヘッドセットの動作を検出した時
				int state = intent.getIntExtra("state", 0);
				if(state == 0){
					MusicUtils.pauseWithView(rootView);
					Log.d("MainService","HEDSET_OFF");  
				}else{
					MusicUtils.playWithView(rootView);
					Log.d("MainService", "HEDSET_ON");  
				}
			}
		}
	}

}
