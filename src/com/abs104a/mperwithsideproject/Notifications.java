package com.abs104a.mperwithsideproject;

import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.AudioManager;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.media.RemoteControlClient;
import android.os.PowerManager;

import com.abs104a.mperwithsideproject.music.Music;
import com.abs104a.mperwithsideproject.music.MusicPlayerWithQueue;
import com.abs104a.mperwithsideproject.utl.ImageCache;
import com.abs104a.mperwithsideproject.utl.MusicUtils;

/**
 * 通知バーへの通知機能を提供するクラス
 * @author Kouki-Mobile
 *
 */
public final class Notifications {

	/**
	 * Lockスクリーンに通知を表示させるメソッド
	 * @param mPlayer
	 * @param mContext
	 */
	public final static void setLockScreenNotifition(final MediaPlayer mPlayer,final Context mContext){
		MusicPlayerWithQueue mpwpl = MusicUtils.getMusicController(mContext);
		Music item = mpwpl.getNowPlayingMusic();
		if(mpwpl.getNowPlayingMusic() == null)
			return;
		
		AudioManager mAudioManager = (AudioManager) mContext.getSystemService(Context.AUDIO_SERVICE);

		// setWakeModeを以下のように呼び出す
		mPlayer.setWakeMode(mContext.getApplicationContext(),
		    PowerManager.PARTIAL_WAKE_LOCK);
		 
		// Intent.ACTION_MEDIA_BUTTONのブロードキャストを受け取る
		// BroadcastReceiverでComponentNameを生成
		ComponentName mMediaButtonReceiverComponent =
		    new ComponentName(mContext, MusicPlayerReceiver.class); // ※
		// AudioManagerにComponentNameを登録
		mAudioManager.registerMediaButtonEventReceiver(mMediaButtonReceiverComponent);
		 
		// Intent.ACTION_MEDIA_BUTTONをアクションに持つIntentを生成
		Intent intent = new Intent(Intent.ACTION_MEDIA_BUTTON);
		// ComponentNameをIntentに設定
		intent.setComponent(mMediaButtonReceiverComponent);
		// RemoteControlClientを生成
		RemoteControlClient mRemoteControlClient = new RemoteControlClient(
		    PendingIntent.getBroadcast(mContext, 0 , intent, 0));
		// AudioManagerにRemoteControlClientを登録
		mAudioManager.registerRemoteControlClient(mRemoteControlClient);
		 
		// リモコンの状態を設定
		mRemoteControlClient.setPlaybackState(RemoteControlClient.PLAYSTATE_PLAYING);
		// リモコン上で扱える操作を設定
		mRemoteControlClient.setTransportControlFlags(
		    RemoteControlClient.FLAG_KEY_MEDIA_PLAY
		    | RemoteControlClient.FLAG_KEY_MEDIA_PAUSE
		    | RemoteControlClient.FLAG_KEY_MEDIA_NEXT
		    | RemoteControlClient.FLAG_KEY_MEDIA_STOP);
		
		Bitmap mArbumArt;
		if(ImageCache.isCache(item.getAlbumUri().toString()))
			mArbumArt = ImageCache.getImage(item.getAlbumUri().toString());
		else
			mArbumArt = null;
		
		// リモコン上の曲情報を更新
		mRemoteControlClient.editMetadata(true)
		    .putString(MediaMetadataRetriever.METADATA_KEY_ARTIST, item.getArtist())
		    .putString(MediaMetadataRetriever.METADATA_KEY_ALBUM, item.getAlbum())
		    .putString(MediaMetadataRetriever.METADATA_KEY_TITLE, item.getTitle())
		    .putLong(MediaMetadataRetriever.METADATA_KEY_DURATION, item.getDuration())
		    .putBitmap(RemoteControlClient.MetadataEditor.BITMAP_KEY_ARTWORK, mArbumArt).apply();
	}
}
