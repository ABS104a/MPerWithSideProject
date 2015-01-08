package com.abs104a.mperwithsideproject;

import com.abs104a.mperwithsideproject.music.MusicPlayerWithQueue;
import com.abs104a.mperwithsideproject.utl.ImageCache;
import com.abs104a.mperwithsideproject.utl.MusicUtils;
import com.abs104a.mperwithsideproject.viewctl.MainViewCtl;
import com.abs104a.mperwithsideproject.viewctl.MusicViewCtl;

import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.graphics.PixelFormat;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.widget.LinearLayout;

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
	
	private final static String TAG = "MainService";
	
	//自分のサービス（Context取得用)
	private static Service mService = null;
	//メインビュー生成用WindowManager
	private WindowManager mWindowManager = null;
	//メインビュー保持用
	private static ViewGroup rootView = null;
	
	public static View getRootView(){
		return rootView;
	}
	
	// ブロードキャストリスナー  
	private MyBroadCastReceiver broadcastReceiver;
	
	private PlayerService mPlayerService = null;

	private final IBinder mBinder = new LocalBinder();
	
	@Override
	public IBinder onBind(Intent intent) {
		// バインドされた時
		return mBinder;
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
		
		//WindowManagerの取得
		mWindowManager  = (WindowManager) getSystemService(Context.WINDOW_SERVICE);

		// 重ね合わせするViewの設定を行う
		LayoutParams params = new WindowManager.LayoutParams(
				WindowManager.LayoutParams.WRAP_CONTENT,
				WindowManager.LayoutParams.WRAP_CONTENT,
				WindowManager.LayoutParams.TYPE_TOAST,
				WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | 
				WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED ,// | 
				PixelFormat.TRANSLUCENT);

		params.gravity = Gravity.CENTER_VERTICAL | Gravity.RIGHT;
		params.x = 0;
		params.y = 0;
		

		//重畳表示するViewを取得する．
		rootView = (LinearLayout)MainViewCtl.createView(mService);
		
		//プレイヤーのViewはハンドル部をタップした時に生成する．
		//ハンドル部が引き出される動作と同時に大きさを変更させ，
		//完全にハンドルが収納されたらViewを破棄する．
		
		//WindowManagerにViewとLayoutParamsを登録し，表示する
		try{
			mWindowManager.addView(rootView, params);
			//こっちは↓更新用
			//mWindowManager.updateViewLayout(mMainView, params);
		}catch(NullPointerException mNullPointerException){
			mNullPointerException.printStackTrace();
			//自分のサービスを終了させる．
			this.stopSelf();
		}
		
		//通知の生成
		Notifications.setService(mService);
		Notifications.putNotification();
		
		//Intentfilterの登録
		broadcastReceiver = new MyBroadCastReceiver(mService, rootView);
		mService.registerReceiver(broadcastReceiver, new IntentFilter(Intent.ACTION_SCREEN_ON));  
		mService.registerReceiver(broadcastReceiver, new IntentFilter(Intent.ACTION_SCREEN_OFF)); 
		mService.registerReceiver(broadcastReceiver, new IntentFilter(Intent.ACTION_HEADSET_PLUG)); 
		mService.registerReceiver(broadcastReceiver, new IntentFilter(MyBroadCastReceiver.VOLUME_CHANGE)); 
		
		//PlayerServiceをバインドする．
		//mService.bindService(new Intent(mService,PlayerService.class), mMainServiceConnection , Context.BIND_AUTO_CREATE);
		
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
		try{
			MusicViewCtl.removePlayerView(rootView);
			//MainViewを消去する．
			mWindowManager.removeView(rootView);
		}catch(Exception mNullPointerException){
			mNullPointerException.printStackTrace();
		}
		
		//BroadcastReceiverの消去
		mService.unregisterReceiver(broadcastReceiver); 
		
		//バインドの解除
		unbindService(mMainServiceConnection);
		
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
	
	public class LocalBinder extends Binder {
    	MainService getService() {
            return (MainService) mService;
        }
    }
	
	private MainServiceConnection mMainServiceConnection = new MainServiceConnection();
	
	public class MainServiceConnection implements ServiceConnection{

		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {
			PlayerService.LocalBinder binder = (PlayerService.LocalBinder)service;
			mPlayerService = binder.getService();
			android.util.Log.v(TAG, "onServiceConnected " + mPlayerService.getPackageName());
		}

		@Override
		public void onServiceDisconnected(ComponentName name) {
			mPlayerService = null;
			android.util.Log.v(TAG, "onServiceDisconnected MainService");
		}
		
	}
	
}
