package com.abs104a.mperwithsideproject;

import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.widget.RemoteViews;

import com.abs104a.mperwithsideproject.music.Music;
import com.abs104a.mperwithsideproject.music.MusicPlayerReceiver;
import com.abs104a.mperwithsideproject.music.MusicPlayerWithQueue;
import com.abs104a.mperwithsideproject.utl.ImageCache;
import com.abs104a.mperwithsideproject.utl.MusicUtils;

/**
 * 通知バーへの通知機能を提供するクラス
 * @author Kouki-Mobile
 *
 */
public final class Notifications {
	
	private static RemoteViews contentView = null;

	/**
	 * 通知バーへの通知を表示する．
	 * @param mService
	 */
	public static final void putNotification(Service mService) {
	    
	    NotificationCompat.Builder builder = new NotificationCompat.Builder(mService);
	    
	    builder.setContentTitle(mService.getString(R.string.app_name));
	    builder.setSmallIcon(android.R.drawable.ic_media_play);
	    
	    //RemoteViewの動作を設定
	    contentView = new RemoteViews(mService.getPackageName(), R.layout.notification);
	    
	    //再生ボタンのアクションを設定
	    Intent playIntent = new Intent(mService, MusicPlayerReceiver.class);
	    playIntent.setAction("Play");
	    PendingIntent playPi = PendingIntent.getBroadcast(mService, 0, playIntent, PendingIntent.FLAG_UPDATE_CURRENT);
	    contentView.setOnClickPendingIntent(R.id.imageButton_notification_play, playPi);
	    
	    //戻るボタンの動作を設定
	    Intent previousIntent = new Intent(mService, MusicPlayerReceiver.class);
	    playIntent.setAction("Previous");
	    PendingIntent previousPi = PendingIntent.getBroadcast(mService, 0, previousIntent, PendingIntent.FLAG_UPDATE_CURRENT);
	    contentView.setOnClickPendingIntent(R.id.imageButton_notification_previous, previousPi);
	    
	    //次に進むボタンの動作を設定
	    Intent nextIntent = new Intent(mService, MusicPlayerReceiver.class);
	    playIntent.setAction("Next");
	    PendingIntent nextPi = PendingIntent.getBroadcast(mService, 0, nextIntent, PendingIntent.FLAG_UPDATE_CURRENT);
	    contentView.setOnClickPendingIntent(R.id.imageButton_notification_next, nextPi);
	    
	    builder.setContent(contentView);
	    builder.setWhen(0);
	    builder.setOngoing(true);
	    builder.setAutoCancel(false);
	    
	    // Serviceを継承したクラス内
	    mService.startForeground(R.drawable.ic_launcher, builder.build());
	    
	    setDataOfRemoteViews(mService);
	}
	
	public static final void setDataOfRemoteViews(Service mService){
		//コントローラークラスの取得
		MusicPlayerWithQueue mpwpl = MusicUtils.getMusicController(mService);
		//RemoteViewsがnull　の時はなにもしない．
		if(contentView == null || mpwpl.getNowPlayingMusic() == null)return;
		
		int currentState = mpwpl.getStatus();
		Music currentMusic = mpwpl.getNowPlayingMusic();
		
		if(ImageCache.isCache(currentMusic.getAlbumUri().toString())){
			contentView.setBitmap(
					R.id.imageView_notification_jacket,
					null,
					ImageCache.getImage(currentMusic.getAlbumUri().toString()));
		}
		contentView.setTextViewText(R.id.textView_notification_title, currentMusic.getTitle());
		contentView.setTextViewText(
				R.id.textView_notification_albumandartist,
				currentMusic.getArtist() + " / " + currentMusic.getAlbum());	
		if(currentState == MusicPlayerWithQueue.PLAYING){
			//一時停止ボタンにする．
			
		}else{
			//再生ボタンにする．
		}
	}
	
	/**
	 * 通知バーへの通知を消去する．
	 */
	public  static final void removeNotification(Service mService){
		mService.stopForeground(true);
		contentView = null;
	}

}
