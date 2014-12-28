package com.abs104a.mperwithsideproject;

import com.abs104a.mperwithsideproject.music.MusicPlayerReceiver;
import com.abs104a.mperwithsideproject.utl.ImageCache;
import com.abs104a.mperwithsideproject.viewctl.MainViewController;
import com.abs104a.mperwithsideproject.viewctl.MusicPlayerViewController;

import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.PixelFormat;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.view.Gravity;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.widget.LinearLayout;
import android.widget.RemoteViews;

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
	private final Service mService = this;
	//メインビュー生成用WindowManager
	private WindowManager mWindowManager = null;
	//メインビュー保持用
	private ViewGroup rootView = null;
	
	// ブロードキャストリスナー  
	private MyBroadCastReceiver broadcastReceiver;

	@Override
	public IBinder onBind(Intent intent) {
		// Activityからバインドされた時
		return null;
	}

	/**
	 * Serviceが開始された時
	 */
	@Override
	public void onCreate() {
		super.onCreate();
		
		mWindowManager  = (WindowManager) getSystemService(Context.WINDOW_SERVICE);

		// 重ね合わせするViewの設定を行う
		LayoutParams params = new WindowManager.LayoutParams(
				WindowManager.LayoutParams.WRAP_CONTENT,
				WindowManager.LayoutParams.WRAP_CONTENT,
				WindowManager.LayoutParams.TYPE_TOAST,
				WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | 
				//WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL | 
				WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED ,// | 
				//WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL ,//| WindowManager.LayoutParams.FLAG_TOUCHABLE_WHEN_WAKING,
				//WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
				PixelFormat.TRANSLUCENT);

		params.gravity = Gravity.CENTER_VERTICAL | Gravity.RIGHT;
		params.x = 0;
		params.y = 0;

		//重畳表示するViewを取得する．
		rootView = (LinearLayout)MainViewController.createView(mService);
		
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
		putNotification(mService);
		
		//Intentfilterの登録
		broadcastReceiver = new MyBroadCastReceiver(mService, rootView);
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
		try{
			MusicPlayerViewController.removePlayerView(rootView);
			//MainViewを消去する．
			mWindowManager.removeView(rootView);
		}catch(NullPointerException mNullPointerException){
			mNullPointerException.printStackTrace();
		}
		
		//BroadcastReceiverの消去
		mService.unregisterReceiver(broadcastReceiver); 
		
		//通知の消去
		removeNotification();
		
		//キャッシュのClear
		ImageCache.clearCache();
		
		//終了Log
		Log.v("MainService","Service is Finished!");
		super.onDestroy();
	}
	
	/**
	 * 通知バーへの通知を表示する．
	 * @param context
	 */
	private void putNotification(Context context) {
	    
	    NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
	    
	    builder.setContentTitle(context.getString(R.string.app_name));
	    builder.setSmallIcon(android.R.drawable.ic_media_play);
	    
	    //RemoteViewの動作を設定
	    RemoteViews contentView = new RemoteViews(context.getPackageName(), R.layout.notification);
	    
	    //再生ボタンのアクションを設定
	    Intent playIntent = new Intent(context, MusicPlayerReceiver.class);
	    playIntent.setAction("Play");
	    PendingIntent playPi = PendingIntent.getBroadcast(context, 0, playIntent, PendingIntent.FLAG_UPDATE_CURRENT);
	    contentView.setOnClickPendingIntent(R.id.imageButton_notification_play, playPi);
	    
	    //戻るボタンの動作を設定
	    Intent previousIntent = new Intent(context, MusicPlayerReceiver.class);
	    playIntent.setAction("Previous");
	    PendingIntent previousPi = PendingIntent.getBroadcast(context, 0, previousIntent, PendingIntent.FLAG_UPDATE_CURRENT);
	    contentView.setOnClickPendingIntent(R.id.imageButton_notification_previous, previousPi);
	    
	    //次に進むボタンの動作を設定
	    Intent nextIntent = new Intent(this, MusicPlayerReceiver.class);
	    playIntent.setAction("Next");
	    PendingIntent nextPi = PendingIntent.getBroadcast(context, 0, nextIntent, PendingIntent.FLAG_UPDATE_CURRENT);
	    contentView.setOnClickPendingIntent(R.id.imageButton_notification_next, nextPi);
	    
	    builder.setContent(contentView);
	    builder.setWhen(0);
	    builder.setOngoing(true);
	    builder.setAutoCancel(false);
	    
	    // Serviceを継承したクラス内
	    startForeground(R.drawable.ic_launcher, builder.build());
	}
	
	/**
	 * 通知バーへの通知を消去する．
	 */
	private void removeNotification(){
		stopForeground(true);
	}
	
}
