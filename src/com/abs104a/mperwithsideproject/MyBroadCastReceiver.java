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

	//MainServiceのコンテキスト
	private final Service mService;
	
	//ボリュームが変更されたときに呼ばれるBroadcast名
	public final static String VOLUME_CHANGE = "android.media.VOLUME_CHANGED_ACTION";

	public MyBroadCastReceiver(Service mService){
		this.mService = mService;
	}
	
	@Override
	public void onReceive(Context context, Intent intent) {
		
		//Broadcastを受信した．
		String action = intent.getAction();  
		if (action != null) {
			//RootViewを取得
			View rootView = MainViewCtl.getRootView();
			
			//画面ONの時の動作
			if (action.equals(Intent.ACTION_SCREEN_ON)) {  
				// 画面ON時  
				Log.d("MainService", "SCREEN_ON");  
				
				if(rootView == null && MusicViewCtl.getPlayerView() == null){
					//MainViewを生成する．
					rootView = MainViewCtl.createAndShowMainView(mService);
				}
			} 
			//画面がOFFの時の動作
			else if (action.equals(Intent.ACTION_SCREEN_OFF)) {  
				// 画面OFF時  
				Log.d("MainService", "SCREEN_OFF");  
				if(rootView != null ){
					MusicViewCtl.removePlayerView();
					MainViewCtl.removeRootView(false);
					if(MusicViewCtl.getPlayerView() != null){
						MusicViewCtl.removePlayerView();
					}
				}
			} 
			
			//ヘッドセットの接続状態が変化した時．
			else if( action.equals(Intent.ACTION_HEADSET_PLUG)){
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
			} 
			//音量が変化したとき
			else if(action.equals(VOLUME_CHANGE)){
				//音量が変わったとき
				//音量の変更に対してViewに反映する．
				MusicViewCtl.changeVolume();
				Log.d("MainService", "VOLUME_CHANGE");
			}
			
		}
	}

}
