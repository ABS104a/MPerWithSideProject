package com.abs104a.mperwithsideproject;

import com.abs104a.mperwithsideproject.music.MusicPlayerWithQueue;
import com.abs104a.mperwithsideproject.utl.ImageCache;
import com.abs104a.mperwithsideproject.utl.MusicUtils;
import com.abs104a.mperwithsideproject.viewctl.MainViewCtl;
import android.app.Service;
import android.content.ComponentName;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.util.Log;

/**
 * メイン画面を表示するService
 * 
 * WindowManagerからにViewを登録して
 * 画面上に重畳表示する．
 * 
 * 機能として
 * ・プレイリストリスト表示		（タブ
 * ・簡易アルバムリスト表示　	（タブ
 * ・音楽の詳細表示
 * ・再生・一時停止などの操作
 * ・音量操作
 * ・シーク
 * ・ランダム&リピート
 * ・リスト表示画面の遷移ボタン
 * ・設定画面への遷移ボタン
 * 
 * 基本的な操作はこちらでカバーさせる．
 * 
 * @author Kouki
 *
 */
public class MainService extends Service{
	
	//自分のサービス（Context取得用)
	private static Service mService = null;
	
	// ブロードキャストリスナー  
	private MyBroadCastReceiver broadcastReceiver;

	@Override
	public IBinder onBind(Intent intent) {
		// Activityからバインドされた時
		return null;
	}
	
	/**
	 * アプリケーションのコンテキストを取得する．
	 * @return
	 */
	public static Service getService(){
		return mService;
	}

	/**
	 * Serviceが開始された時
	 */
	@Override
	public void onCreate() {
		super.onCreate();
		
		mService = this;
		
		//テーマの適応
		mService.setTheme(R.style.AppTheme);
		
		MainViewCtl.createAndShowMainView(mService);
		
		//通知の生成
		Notifications.setService(mService);
		Notifications.putNotification();
		
		//Intentfilterの登録
		broadcastReceiver = new MyBroadCastReceiver(mService);
		mService.registerReceiver(broadcastReceiver, new IntentFilter(Intent.ACTION_SCREEN_ON));  
		mService.registerReceiver(broadcastReceiver, new IntentFilter(Intent.ACTION_SCREEN_OFF)); 
		mService.registerReceiver(broadcastReceiver, new IntentFilter(Intent.ACTION_HEADSET_PLUG)); 
		mService.registerReceiver(broadcastReceiver, new IntentFilter(MyBroadCastReceiver.VOLUME_CHANGE)); 
		
		//開始ログ
		Log.v("MainService","Service is Start!");

	}

	/**
	 * Serviceが終了した時
	 */
	@Override
	public void onDestroy() {
		MusicPlayerWithQueue mpwpl = MusicUtils.getMusicController(mService);
		if(mpwpl != null){
			//Queueの保存
			mpwpl.writeQueue();
		}
		
		MainViewCtl.removeRootView();
		
		//BroadcastReceiverの消去
		mService.unregisterReceiver(broadcastReceiver); 
		
		//通知の消去
		Notifications.removeNotification(mService);
		
		//キャッシュのClear
		try{
			ImageCache.clearCache();
		}catch(Exception e){
			
		}
		
		mService = null;
		
		//終了Log
		Log.v("MainService","Service is Finished!");
		super.onDestroy();
	}
	
	public class MainServiceConnection implements ServiceConnection{

		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {
			// TODO 自動生成されたメソッド・スタブ
			
		}

		@Override
		public void onServiceDisconnected(ComponentName name) {
			// TODO 自動生成されたメソッド・スタブ
			
		}
		
	}
	

	
}
