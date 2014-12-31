package com.abs104a.mperwithsideproject;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v4.app.NotificationCompat;
import android.widget.RemoteViews;

import com.abs104a.mperwithsideproject.music.Music;
import com.abs104a.mperwithsideproject.music.MusicPlayerReceiver;
import com.abs104a.mperwithsideproject.music.MusicPlayerWithQueue;
import com.abs104a.mperwithsideproject.utl.GetImageTask;
import com.abs104a.mperwithsideproject.utl.MusicUtils;

/**
 * 通知バーへの通知機能を提供するクラス
 * @author Kouki-Mobile
 *
 */
public final class Notifications {
	//サービスのコンテキスト
	private static Service mService = null;
	
	public static final String PLAY = "com.abs104a.mperwithsideproject.play";
	
	public static final String PREVIOUS = "com.abs104a.mperwithsideproject.previous";
	
	public static final String NEXT = "com.abs104a.mperwithsideproject.next";

	public static final String MAIN = "com.abs104a.mperwithsideproject.main";
	
	public static final int PLAY_REQ = 11;
	public static final int PREVIOUS_REQ = 12;
	public static final int NEXT_REQ = 13;
	public static final int MAIN_REQ = 14;
	
	/**
	 * サービスのコンテキストを登録
	 * @param service
	 */
	public static void setService(Service service){
		mService = service;
	}
	
	/**
	 * セットするRemoteViews
	 */
	private static RemoteViews contentView = null;

	/**
	 * 通知バーへの通知を表示する．
	 * @param mService
	 */
	public static final void putNotification() {
	    
		//サービスが存在しない時は何もしない・
		if(mService == null)return;
		
		//通知の作成
	    NotificationCompat.Builder builder = new NotificationCompat.Builder(mService);
	    
	    builder.setContentTitle(mService.getString(R.string.app_name));
	    builder.setSmallIcon(android.R.drawable.ic_media_play);
	    
	    //RemoteViewの動作を設定
	    contentView = new RemoteViews(mService.getPackageName(), R.layout.notification);
	    Intent mainIntent = new Intent(mService, MusicPlayerReceiver.class);
	    mainIntent.setAction(MAIN);
	    PendingIntent mainPi = PendingIntent.getBroadcast(mService, MAIN_REQ, mainIntent, PendingIntent.FLAG_UPDATE_CURRENT);
	    contentView.setOnClickPendingIntent(R.layout.notification, mainPi);
	    
	    //再生ボタンのアクションを設定
	    Intent playIntent = new Intent(mService, MusicPlayerReceiver.class);
	    playIntent.setAction(PLAY);
	    PendingIntent playPi = PendingIntent.getBroadcast(mService, PLAY_REQ, playIntent, PendingIntent.FLAG_UPDATE_CURRENT);
	    contentView.setOnClickPendingIntent(R.id.imageButton_notification_play, playPi);
	    
	    //戻るボタンの動作を設定
	    Intent previousIntent = new Intent(mService, MusicPlayerReceiver.class);
	    previousIntent.setAction(PREVIOUS);
	    PendingIntent previousPi = PendingIntent.getBroadcast(mService, PREVIOUS_REQ, previousIntent, PendingIntent.FLAG_UPDATE_CURRENT);
	    contentView.setOnClickPendingIntent(R.id.imageButton_notification_previous, previousPi);
	    
	    //次に進むボタンの動作を設定
	    Intent nextIntent = new Intent(mService, MusicPlayerReceiver.class);
	    nextIntent.setAction(NEXT);
	    android.util.Log.v(NEXT, nextIntent.getAction());
	    PendingIntent nextPi = PendingIntent.getBroadcast(mService, NEXT_REQ, nextIntent, PendingIntent.FLAG_UPDATE_CURRENT);
	    contentView.setOnClickPendingIntent(R.id.imageButton_notification_next, nextPi);
	    
	    builder.setContent(contentView);
	    builder.setWhen(0);
	    builder.setOngoing(true);
	    builder.setAutoCancel(false);
	    
	    Notification notification = builder.build();
	    setDataOfRemoteViews(notification);
	    
	    // Serviceを継承したクラス内
	    mService.startForeground(R.drawable.ic_launcher,notification );
	}
	
	/**
	 * 通知バーへデータをセットする
	 * @param notification
	 */
	private static final void setDataOfRemoteViews(final Notification notification){
		//コントローラークラスの取得
		MusicPlayerWithQueue mpwpl = MusicUtils.getMusicController(mService);
		//RemoteViewsがnull　の時はなにもしない．
		if(notification == null || mpwpl.getNowPlayingMusic() == null)return;
		final RemoteViews contentView = notification.contentView;
		if(contentView == null)return;
		
		int currentState = mpwpl.getStatus();
		final Music currentMusic = mpwpl.getNowPlayingMusic();
		
		new GetImageTask(mService,new GetImageTask.OnGetImageListener() {
			
			@Override
			public void onGetImage(Bitmap image) {
				contentView.setImageViewBitmap(
						R.id.imageView_notification_jacket,
						image);
				// Serviceを継承したクラス内
			    mService.startForeground(R.drawable.ic_launcher,notification );
			}
		}).execute(currentMusic.getAlbumUri());
			
		
		contentView.setTextViewText(R.id.textView_notification_title, currentMusic.getTitle());
		contentView.setTextViewText(
				R.id.textView_notification_albumandartist,
				currentMusic.getArtist() + " / " + currentMusic.getAlbum());	
		if(currentState == MusicPlayerWithQueue.PLAYING){
			//一時停止ボタンにする．
			contentView.setImageViewResource(R.id.imageButton_notification_play, android.R.drawable.ic_media_pause);
		}else{
			//再生ボタンにする．
			contentView.setImageViewResource(R.id.imageButton_notification_play, android.R.drawable.ic_media_play);
		}
	}
	
	/**
	 * 通知バーへの通知を消去する．
	 */
	public  static final void removeNotification(Service mService){
		mService.stopForeground(true);
		contentView = null;
		mService = null;
	}

}
