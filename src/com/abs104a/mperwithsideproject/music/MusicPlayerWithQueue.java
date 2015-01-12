package com.abs104a.mperwithsideproject.music;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Random;

import com.abs104a.mperwithsideproject.R;
import com.abs104a.mperwithsideproject.utl.FileUtils;
import com.abs104a.mperwithsideproject.utl.MusicUtils;

import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.AudioManager;
import android.media.AudioManager.OnAudioFocusChangeListener;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.media.RemoteControlClient;
import android.media.audiofx.Equalizer;
import android.os.PowerManager;

/**
 * 音楽プレーヤーにくわえてプレイリスト再生を実現する
 * ArrayList型のプレイリストを読み込むことで
 * 
 * ・リピート再生　1曲orリスト内
 * ・シャッフル再生　リスト内
 * ・前の曲への移動
 * ・後の曲への移動
 * 
 * を提供する．
 * 
 * @author Kouki
 *
 */
public final class MusicPlayerWithQueue extends MusicPlayer {

	/**===================================
	 * 定数
	 ===================================*/
	
	//ループが有効ではない
	public final static int NOT_LOOP = 0;
	//1曲ループ
	public final static int ONE_LOOP = 1;
	//全曲ループ
	public final static int ALL_LOOP = 2;
	
	public final static String TAG = "MusicPlayerWithQueue";
	
	/**===================================
	 * 内部変数
	 ===================================*/
	
	private MusicQueue mQueue = null;
	//アプリケーションのコンテキスト
	private Context mContext;
	//equalizerのインスタンス
	private Equalizer mEqualizer;
	//ロック画面通知用のインスタンス
	private RemoteControlClient mRemoteControlClient = null;
	
	
	/**===================================
	 * メソッド
	 ===================================*/
	
	public MusicPlayerWithQueue(Context mContext){
		//Queueを読み込む
		MusicQueue mQueue = FileUtils.readSerializableQueue(mContext);
		this.mQueue = mQueue;
		this.mContext = mContext;
	}
	
	@Override
	protected void finalize() throws Throwable {
		//Queueの内容を書き出す．
		FileUtils.writeSerializableQueue(mContext, mQueue);
		super.finalize();
	}
	
	/**
	 * QueueItemの中身をファイルに書き込む（永続化）
	 */
	public void writeQueue(){
		FileUtils.writeSerializableQueue(mContext, mQueue);
		android.util.Log.d("MusicPlayerWithQueue","WriteQueue!");
	}
	

	//===================================
	// ・ シャッフル機能について
	// ==================================
	
	/**
	 * シャッフル機能有効フラグの状態を取得する
	 * @return　シャッフル機能が有効かどうか
	 */
	public final boolean isShuffle() {
		return mQueue.isShuffle();
	}

	/**
	 * シャッフル機能を設定する．
	 * @param isShuffle　シャッフルが有効かどうか
	 */
	public final boolean setShuffle(boolean isShuffle) {
		mQueue.setShuffle(isShuffle);
		return mQueue.isShuffle();
	}
	
	//===================================
	// ・ シャッフル機能について
	// ==================================
	
	/**
	 * ループの状態を取得する
	 * @return ループ状態
	 */
	public final int getLoopState() {
		return mQueue.getRepeatState();
	}

	/**
	 * ループの状態を設定する
	 * 設定外の値は設定されない
	 * @param loopState ループ状態
	 */
	public final int setLoopState(int loopState) {
		if(loopState == NOT_LOOP || 
				loopState == ALL_LOOP || loopState == ONE_LOOP){
			mQueue.setRepeatState(loopState);
		}
		return mQueue.getRepeatState();
			
	}
	
	
	//===================================
	// ・再生コントロール機能
	// ==================================
	
	/* (非 Javadoc)
	 * @see com.abs104a.mperwithsideproject.music.MusicPlayer#playStartAndPause()
	 */
	@Override
	public int playStartAndPause()
			throws IndexOutOfBoundsException, 
			IllegalArgumentException, 
			SecurityException, 
			IllegalStateException, 
			IOException 
	{
		if(getStatus() != PLAYING && getStatus() != PAUSEING){
			setSource(mQueue.getCursorMusic().getPass());
		}
		initEqualizer(mMediaPlayer);
		initLockScreenNotifition(mMediaPlayer);
		if(mRemoteControlClient != null){
			if(getStatus() == PLAYING)
				mRemoteControlClient.setPlaybackState(RemoteControlClient.PLAYSTATE_PAUSED);
			else if (getStatus() == PAUSEING || getStatus() == STOPPING)
				mRemoteControlClient.setPlaybackState(RemoteControlClient.PLAYSTATE_PLAYING);
		}
		return super.playStartAndPause();
	}	
	
	/**
	 * プレイリストをセットする
	 * @param playList　セットするプレイリスト
	 * @throws IOException 
	 * @throws IllegalStateException 
	 * @throws SecurityException 
	 * @throws IllegalArgumentException 
	 */
	public final void setPlayList(ArrayList<Music> playList) 
			throws IllegalArgumentException, 
			SecurityException, 
			IllegalStateException, 
			IOException
			{
		ArrayList<Music> mPlayList = mQueue.getQueueMusics();
		mPlayList.clear();
		mPlayList.addAll(playList);
		setSource(playList.get(0).getPass());
		writeQueue();
	}
	
	/**
	 * プレイリストをセットする (indexの曲目から再生する)
	 * @param playList　セットするプレイリスト
	 * @throws IOException 
	 * @throws IllegalStateException 
	 * @throws SecurityException 
	 * @throws IllegalArgumentException 
	 * @throws IndexOutOfBoundsException
	 */
	public final void setPlayList(ArrayList<Music> playList,int index) throws IndexOutOfBoundsException ,IllegalArgumentException, SecurityException, IllegalStateException, IOException{
		ArrayList<Music> mPlayList = mQueue.getQueueMusics();
		mPlayList.clear();
		mPlayList.addAll(playList);
		mQueue.setCursor(index);
		setSource(playList.get(index).getPass());
		writeQueue();
	}
	
	/**
	 * 既存のQueueにPlayListを追加します．
	 * @param playList　追加するプレイリスト
	 * @throws IllegalArgumentException
	 * @throws SecurityException
	 * @throws IllegalStateException
	 * @throws IOException
	 */
	public final void addPlayList(ArrayList<Music> playList) 
			throws IllegalArgumentException, 
			SecurityException, 
			IllegalStateException, 
			IOException
			{
		ArrayList<Music> mPlayList = mQueue.getQueueMusics();
		mPlayList.addAll(playList);
		writeQueue();
	}
	
	/**
	 * 曲をセットする（新たなプレイリストを作成して曲をセットする．）
	 * @param music
	 * @throws IllegalArgumentException
	 * @throws SecurityException
	 * @throws IllegalStateException
	 * @throws IOException
	 */
	public final void setMusic(final Music music) throws IllegalArgumentException, SecurityException, IllegalStateException, IOException{
		ArrayList<Music> mPlayList = mQueue.getQueueMusics();
		mPlayList.clear();
		mPlayList.add(music);
		mQueue.setCursor(0);
		setSource(music.getPass());
		writeQueue();
	}
	
	/**
	 * 曲を現在のプレイリストに追加する．
	 * 
	 * プレイリストがない場合は新規に作成をして追加します．
	 * @param music　追加する音楽要素
	 */
	public final void addMusic(final Music music){
		mQueue.getQueueMusics().add(music);
		writeQueue();
	}
	
	
	
	/**
	 * 曲を現在のプレイリストの任意の場所に挿入します．
	 * 
	 * プレイリストがない場合は作成します．
	 * 
	 * @param music　挿入する音楽
	 * @param index	挿入箇所
	 * @throws IndexOutOfBoundsException　プレイリスト外のIndex指定があった場合
	 */
	public final void addMusic(final Music music ,final int index) throws IndexOutOfBoundsException{
		mQueue.getQueueMusics().add(index, music);
		writeQueue();
	}
	
	/**
	 * 再生Queueのなかから指定した音楽を消去します．
	 * @param music
	 */
	public final void removeMusic(final Music music){
		int index = mQueue.getQueueMusics().indexOf(music);
		if(index != -1){
			if(index <= mQueue.getCursor())
				mQueue.setCursor(Math.max(0, mQueue.getCursor() - 1));
			mQueue.getQueueMusics().remove(music);
			writeQueue();
		}
	}
	
	/**
	 * 再生Queueのなかから指定した音楽を消去します．
	 * @param music
	 */
	public final void removeMusic(final int index){
		if(index >= 0 && index < mQueue.getQueueMusics().size()){
			if(index <= mQueue.getCursor())
				mQueue.setCursor(Math.max(0, mQueue.getCursor() - 1));
			mQueue.getQueueMusics().remove(index);
			writeQueue();
		}
	}
	
	/**
	 * 現在再生しているMusic
	 * @return
	 */
	public final Music getNowPlayingMusic(){
		if(mQueue.getQueueMusics() != null && mQueue.getQueueMusics().size() > 0)
			return mQueue.getCursorMusic();
		else
			return null;
	}
	
	/**
	 * 再生Queueを取得します．
	 * @return　再生Queue
	 */
	public final ArrayList<Music> getQueue(){
		return mQueue.getQueueMusics();
	}
	
	/**
	 * Queueの任意のインデックスにシークする．
	 * @param index
	 * @return
	 * @throws IndexOutOfBoundsException
	 * @throws IllegalArgumentException
	 * @throws SecurityException
	 * @throws IllegalStateException
	 * @throws IOException
	 */
	public final int seekQueue(final int index) 
			throws IndexOutOfBoundsException, 
			IllegalArgumentException,
			SecurityException, 
			IllegalStateException, 
			IOException
	{
		mQueue.setCursor(index);
		return setSource(mQueue.getCursorMusic().getPass());
	}
	
	/**
	 * Queue内のMusicインスタンスの位置を取得する
	 * @param music
	 * @return
	 */
	public final int indexOfQueue(Music music){
		return mQueue.getQueueMusics().indexOf(music);
	}
	
	/**
	 * 次の曲へ移動する
	 * @return 次の曲のカーソル
	 * @throws IOException 
	 * @throws IllegalStateException 
	 * @throws SecurityException 
	 * @throws IllegalArgumentException 
	 */
	public final int playNext() 
			throws IllegalArgumentException, 
			SecurityException, 
			IllegalStateException, 
			IOException
			{
		//List形式を実装する
		//再生を終了するかどうか設定するフラグ
		boolean flag = false;
		
		//シャッフル機能が有効の時
		if(isShuffle()){
			//ランダムにカーソル値を設定する．
			Random random = new Random();
			int oldCursor = mQueue.getCursor();
			mQueue.setCursor(random.nextInt(mQueue.getQueueMusics().size()));
			//前の曲と同じ値になった場合はその次の曲or前の曲にする．
			if(oldCursor == mQueue.getCursor())
				mQueue.setCursor(mQueue.getCursor() == (mQueue.getQueueMusics().size()-1) ? Math.max(0, mQueue.getCursor()-1) : mQueue.getCursor()+1);
		}
		//シャッフル機能が無効の時
		else{
			//ループの状態
			switch(mQueue.getRepeatState()){
			case NOT_LOOP: 	//ループ無しの時
				//終了フラグを立てる．
				flag = true;
			case ALL_LOOP: 	//全曲ループの時
				mQueue.setCursor(mQueue.getCursor()+1);	//カーソルを進める
				if(mQueue.getQueueMusics().size() == mQueue.getCursor()){
					//プレイリストの最後まで来たとき
					//カーソルを0に戻す
					mQueue.setCursor(0);
					setSource(mQueue.getCursorMusic().getPass());
				}else{
					flag = false;
				}
			case ONE_LOOP:	//1曲ループの時
			}
		}
		//次の曲情報を取得
		String nextMusic = mQueue.getCursorMusic().getPass();
		//データをセットする
		setSource(nextMusic);
		//全曲ループでないときは再生を終了する．
		if(flag)return mQueue.getCursor();
		//再生を開始
		playStartAndPause();
		return mQueue.getCursor();
		
	}
	
	/**
	 * 前の曲へ移動する．
	 * 
	 * 再生秒数が1s未満であれば前の曲
	 * 再生秒数が1s以上であれば曲の先頭へシークする
	 * @return 再生を行うカーソル
	 * @throws IOException 
	 * @throws IllegalStateException 
	 * @throws SecurityException 
	 * @throws IllegalArgumentException 
	 */
	public final int playBack() 
			throws IllegalArgumentException, 
			SecurityException, 
			IllegalStateException, 
			IOException
			{
		//秒数が1秒以上なら前に戻らない
		if(getCurrentTime() > 1000){
			mQueue.setCursor(mQueue.getCursor()+1);
		}
		android.util.Log.v("playback", getCurrentTime() + "");
		
		//次の曲情報を取得
		mQueue.setCursor(mQueue.getCursor()-1);
		if(mQueue.getCursor() < 0){
			mQueue.setCursor(0);
		}
		
		String nextMusic = mQueue.getCursorMusic().getPass();
		//データをセットする
		setSource(nextMusic);
		//再生を開始
		playStartAndPause();
		return mQueue.getCursor();	
	}
	
	//イコライザの設定を行う
	/**
	 * Equalizerの初期化
	 * @param mediaPlayer
	 */
	public void initEqualizer(MediaPlayer mediaPlayer){
		
		mEqualizer = new Equalizer(0,mediaPlayer.getAudioSessionId());
		mEqualizer.setEnabled(mQueue.isEqualizer());
		if(mQueue.getEqualizerItem() != null){
			for(int i = 0;i < mQueue.getEqualizerItem().length;i++){
				EqualizerItem item = mQueue.getEqualizerItem()[i];
				android.util.Log.v("MusicPlayer", "EQ : " + item.getBand() + " / " + item.getLevel());
				mEqualizer.setBandLevel((short)i, item.getLevel());
			}
		}	
	}
	
	/**
	 * Equalizerを取得する．
	 * @return
	 */
	public Equalizer getEqualizerInstance(){
		if(mEqualizer != null){
			return mEqualizer;
		}else{
			return new Equalizer(0, R.raw.sample);
		}
	}
	
	/**
	 * Equalizerを有効にするか設定する
	 * @param isEqualizer
	 * @return
	 */
	public boolean setEqualizer(boolean isEqualizer){
		Equalizer eq = getEqualizerInstance();
		eq.setEnabled(isEqualizer);
		writeQueue();
		return this.mQueue.setEqualizer(isEqualizer);
	}
	
	/**
	 * Equalizerが有効かどうか取得する．
	 * @return
	 */
	public boolean getEqualizer(){
		return mQueue.isEqualizer();
	}
	
	public short getEqualizerCursor(){
		return mQueue.getEqualizerCursor();
	}
	
	public void setEqualizerCursor(short cursor){
		mQueue.setEqualizerCursor(cursor);
		writeQueue();
	}
	
	/**
	 * @return mEqualizerItem
	 */
	public EqualizerItem[] getEqualizerItem() {
		return mQueue.getEqualizerItem();
	}

	/**
	 * @param mEqualizerItem セットする mEqualizerItem
	 */
	public void setEqualizerItem(EqualizerItem[] mEqualizerItem) {
		mQueue.setEqualizerItem(mEqualizerItem);
		writeQueue();
	}

	/**
	 * Queueの中でItemを一つ前に持ってくる
	 * Indexが1つ減る．
	 * @param item
	 */
	public void upIndexOfQueue(Music item) {
		ArrayList<Music> musics = mQueue.getQueueMusics();
		int index = musics.indexOf(item);
		int newIndex = Math.max(0, index - 1);
		
		if(index == -1) return;
		
		musics.remove(index);
		musics.add(newIndex,item);
		
		if(index == mQueue.getCursor())
			mQueue.setCursor(newIndex);
		else if(newIndex == mQueue.getCursor())
			mQueue.setCursor(index);
		
	}

	/**
	 * Queueの中でItemを一つ後に持ってくる
	 * Indexが1つ増える．
	 * @param item
	 */
	public void downIndexOfQueue(Music item) {
		ArrayList<Music> musics = mQueue.getQueueMusics();
		int index = musics.indexOf(item);
		int newIndex = Math.min(musics.size()-1, index + 1);
		
		if(index == -1) return;
		
		musics.remove(index);
		musics.add(newIndex,item);
		
		if(index == mQueue.getCursor())
			mQueue.setCursor(newIndex);
		else if(newIndex == mQueue.getCursor())
			mQueue.setCursor(index);
	} 
	
	/**
	 * 現在のCursorを取得する
	 * @param index
	 * @return
	 */
	public final int setCursor(int index){
		if(mQueue != null){
			mQueue.setCursor(index);
			return index;
		}else
			return 0;
	}
	
	public final int getCursor(){
		if(mQueue != null)
			return mQueue.getCursor();
		else 
			return 0;
	}
	
	/**
	 * Lockスクリーンに通知を表示させるメソッド
	 * @param mMediaPlayer 
	 * @param mPlayer
	 * @param mContext
	 */
	public final void initLockScreenNotifition(MediaPlayer mMediaPlayer){
		MusicPlayerWithQueue mpwpl = MusicUtils.getMusicController(mContext);
		final Music item = mpwpl.getNowPlayingMusic();
		if(mpwpl.getNowPlayingMusic() == null)
			return;
		
		AudioManager mAudioManager = (AudioManager) mContext.getSystemService(Context.AUDIO_SERVICE);

		// setWakeModeを以下のように呼び出す
		mMediaPlayer.setWakeMode(mContext,PowerManager.PARTIAL_WAKE_LOCK);
		 
		// Intent.ACTION_MEDIA_BUTTONのブロードキャストを受け取る
		// BroadcastReceiverでComponentNameを生成
		ComponentName mMediaButtonReceiverComponent =
		    new ComponentName(mContext.getPackageName(), MusicPlayerReceiver.class.getName()); // ※
		// AudioManagerにComponentNameを登録
		mAudioManager.registerMediaButtonEventReceiver(mMediaButtonReceiverComponent);
		 
		// Intent.ACTION_MEDIA_BUTTONをアクションに持つIntentを生成
		Intent intent = new Intent(Intent.ACTION_MEDIA_BUTTON);
		// ComponentNameをIntentに設定
		intent.setComponent(mMediaButtonReceiverComponent);
		// RemoteControlClientを生成
		mRemoteControlClient = new RemoteControlClient(
		    PendingIntent.getBroadcast(mContext.getApplicationContext(), 0 , intent, 0));
		// AudioManagerにRemoteControlClientを登録
		mAudioManager.registerRemoteControlClient(mRemoteControlClient);
		mAudioManager.requestAudioFocus(new OnAudioFocusChangeListener(){

			@Override
			public void onAudioFocusChange(int focusChange){
				android.util.Log.d(TAG,"Focus changed: " + focusChange);
			}
		},
		AudioManager.STREAM_MUSIC,
		AudioManager.AUDIOFOCUS_GAIN);
		 
		// リモコンの状態を設定
		mRemoteControlClient.setPlaybackState(RemoteControlClient.PLAYSTATE_PLAYING);
		// リモコン上で扱える操作を設定
		mRemoteControlClient.setTransportControlFlags(
		    RemoteControlClient.FLAG_KEY_MEDIA_PLAY
		    | RemoteControlClient.FLAG_KEY_MEDIA_PAUSE
		    | RemoteControlClient.FLAG_KEY_MEDIA_NEXT);
		
		Bitmap image = null;
		try{
			ContentResolver cr = mContext.getContentResolver();
			InputStream is = cr.openInputStream(item.getAlbumUri());
			image =  BitmapFactory.decodeStream(is);
		}catch(FileNotFoundException err){

		}
		
		// リモコン上の曲情報を更新
		mRemoteControlClient.editMetadata(true)
		    .putString(MediaMetadataRetriever.METADATA_KEY_ARTIST, item.getArtist())
		    .putString(MediaMetadataRetriever.METADATA_KEY_ALBUM, item.getAlbum())
		    .putString(MediaMetadataRetriever.METADATA_KEY_TITLE, item.getTitle() + " / " + item.getAlbum() + " / " + item.getArtist())
		    .putLong(MediaMetadataRetriever.METADATA_KEY_DURATION, item.getDuration())
		    .putBitmap(RemoteControlClient.MetadataEditor.BITMAP_KEY_ARTWORK, image).apply();
		
		
		android.util.Log.v(TAG, "initLockScreenNotifition");
	}

}