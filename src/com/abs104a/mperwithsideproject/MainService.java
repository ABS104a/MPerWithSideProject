package com.abs104a.mperwithsideproject;

import com.abs104a.mperwithsideproject.music.MusicPlayerWithQueue;
import com.abs104a.mperwithsideproject.music.PlayList;
import com.abs104a.mperwithsideproject.utl.ImageCache;
import com.abs104a.mperwithsideproject.utl.MusicUtils;
import com.abs104a.mperwithsideproject.viewctl.MainViewCtl;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;

import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;
import android.widget.Toast;

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
	
	
	//定数////////////////////////////
	
	//アプリケーションのTAG
	private final static String TAG = "MainService";
	
	//変数////////////////////////////
	
	//自分のサービス（Context取得用)
	private static Service mService = null;
	
	// ブロードキャストリスナー  
	private MyBroadCastReceiver broadcastReceiver;
	
	private boolean finishFlag = false;
	
	//バインドしているかどうか．
	private boolean isBind = false;

	private IPlayerService mIPlayerService = null;
	
	//プロセス間通信用のバインダー
	private IMainService.Stub mIMainServiceIf = new IMainService.Stub() {
		
		@Override
		public boolean stopService() throws RemoteException {
			return false;
		}
	};
	
	@Override
	public IBinder onBind(Intent intent) {
		// バインドされた時
		return mIMainServiceIf;
	}
	
	/* (非 Javadoc)
	 * @see android.app.Service#onUnbind(android.content.Intent)
	 */
	@Override
	public boolean onUnbind(Intent intent) {
		if(finishFlag)
			stopSelf();
		else if(isBind == false)
			mService.bindService(new Intent(mService,PlayerService.class), mMainServiceConnection , Context.BIND_AUTO_CREATE);	
		return super.onUnbind(intent);
	}

	public final void stopService(){
		finishFlag = true;
		if(mIPlayerService != null){
			try {
				mIPlayerService.stopService();
			} catch (RemoteException e) {
				e.printStackTrace();
			}
		}
		//バインドの解除
		unbindService(mMainServiceConnection);
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
		//Notifications.putNotification();
		
		//Intentfilterの登録
		broadcastReceiver = new MyBroadCastReceiver(mService);
		mService.registerReceiver(broadcastReceiver, new IntentFilter(Intent.ACTION_SCREEN_ON));  
		mService.registerReceiver(broadcastReceiver, new IntentFilter(Intent.ACTION_SCREEN_OFF)); 
		mService.registerReceiver(broadcastReceiver, new IntentFilter(Intent.ACTION_HEADSET_PLUG)); 
		mService.registerReceiver(broadcastReceiver, new IntentFilter(MyBroadCastReceiver.VOLUME_CHANGE)); 
			
		//開始ログ
		Log.v("MainService","Service is Start!");

	}


	/* (非 Javadoc)
	 * @see android.app.Service#onStartCommand(android.content.Intent, int, int)
	 */
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		//PlayerServiceをバインドする．
		if(!isBind)
			mService.bindService(new Intent(mService,PlayerService.class), mMainServiceConnection , Context.BIND_AUTO_CREATE);
	
		return super.onStartCommand(intent, flags, startId);
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

		MainViewCtl.removeRootView(true);
		PlayList.writePlayList(mService);
		PlayList.clearPlayList();
		//キャッシュのClear
		ImageCache.clearCache();
		System.gc();
		
		
		//BroadcastReceiverの消去
		mService.unregisterReceiver(broadcastReceiver); 
		
		//通知の消去
		Notifications.removeNotification(mService);
		
		
		//Serviseの消去
		mService = null;
		
		//終了Log
		Log.v("MainService","Service is Finished!");
		
		if(!finishFlag && isBind == false)
			mService.bindService(new Intent(mService,PlayerService.class), mMainServiceConnection , Context.BIND_AUTO_CREATE);
		
		finishFlag = false;
		
		super.onDestroy();
	}
	
	
	//バインダ-関係////////////////////////////////
	
	//ServiceConnection
	private MainServiceConnection mMainServiceConnection = new MainServiceConnection();
	
	/**
	 * サービスをバインドした時に呼ばれるインスタンス．
	 * @author Kouki
	 *
	 */
	public class MainServiceConnection implements ServiceConnection{

		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {
			isBind = true;
			mIPlayerService = IPlayerService.Stub.asInterface(service);
			try {
				mIPlayerService.bindService();
			} catch (RemoteException e) {
				e.printStackTrace();
			}
			android.util.Log.v(TAG, "onServiceConnected MainService");
			Toast.makeText(mService, "Service Start", Toast.LENGTH_SHORT).show();
		}

		@Override
		public void onServiceDisconnected(ComponentName name) {
			isBind = false;
			mIPlayerService = null;
			android.util.Log.v(TAG, "onServiceDisconnected MainService");
			if(!finishFlag)
				mService.bindService(new Intent(mService,PlayerService.class), mMainServiceConnection , Context.BIND_AUTO_CREATE);		
		}
		
	}
	
}
