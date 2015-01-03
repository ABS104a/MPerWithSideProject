package com.abs104a.mperwithsideproject.utl;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;

import com.abs104a.mperwithsideproject.MainService;
import com.abs104a.mperwithsideproject.Notifications;
import com.abs104a.mperwithsideproject.R;
import com.abs104a.mperwithsideproject.adapter.MusicViewPagerAdapter;
import com.abs104a.mperwithsideproject.music.Album;
import com.abs104a.mperwithsideproject.music.Music;
import com.abs104a.mperwithsideproject.music.MusicPlayerWithQueue;
import com.abs104a.mperwithsideproject.viewctl.MusicSeekBarHandler;
import com.abs104a.mperwithsideproject.viewctl.ViewPagerForEqualizerViewCtl;
import com.abs104a.mperwithsideproject.viewctl.listener.MusicSeekBarOnChangeImpl;

import android.app.Service;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

/**
 * このアプリケーションにおける
 * ViewとPlayerをつなぐ仲介クラス．
 * 
 * 音楽の再生，次の曲，前の曲
 * Repeat，シャッフルについて，
 * Viewの変化と再生処理を同時に行う．
 * 
 * また，ミュージックcontrollerのインスタンスを保持し，
 * 端末から曲データを取得してListとして返すメソッドを持つ．
 * 
 * @author Kouki
 *
 */
public class MusicUtils {

	private static MusicPlayerWithQueue musicController = null;
	
	//クラスの識別用タグ
	public static final String TAG = "MUSIC_UTILS";
	
	//UIスレッドのHandler
	private static MusicSeekBarHandler mHandler = null;
	
	private static View playerView = null;
	
	public static View getPlayerView(){
		return playerView;
	}
	
	public static void setPlayerView(View view){
		playerView = view;
	}
	
	public static Context getContext(){
		return MainService.getService();
	}
	
	
	/**
	 * ミュージックコントロールクラスを返す.
	 * 
	 * @param mContext
	 * @return
	 */
	public final static MusicPlayerWithQueue getMusicController(Context mContext){
		if(musicController == null)
			musicController = new MusicPlayerWithQueue(mContext);
		return musicController;
	}
	
	
	/**
	 * 再生と停止を行う．またViewへの反映も同時に行う
	 * @param playerView
	 * @param mpwpl
	 */
	public static final void playOrPauseWithView(){
		//再生動作を行う
		try {
			MusicPlayerWithQueue mpwpl = getMusicController(getContext());
			mpwpl.playStartAndPause();
			reflectOfView(false);
			android.util.Log.v(TAG, "PlayAndPauseWithView");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 再生と停止を行う．またViewへの反映も同時に行う
	 * @param playerView
	 * @param mpwpl
	 */
	public static final void playOrPauseWithView(int index){
		//再生動作を行う
		try {
			MusicPlayerWithQueue mpwpl = getMusicController(getContext());
			mpwpl.seekQueue(index);
			mpwpl.playStartAndPause();
			reflectOfView(true);
			android.util.Log.v(TAG, "PlayAndPauseWithView");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 再生と停止を行う．またViewへの反映も同時に行う
	 * @param playerView
	 * @param mpwpl
	 */
	public static final void playWithView(){
		//再生動作を行う
		try {
			MusicPlayerWithQueue mpwpl = getMusicController(getContext());
			if(mpwpl.getStatus() != MusicPlayerWithQueue.PLAYING){
				mpwpl.playStartAndPause();
				reflectOfView(false);
				android.util.Log.v(TAG, "PlayWithView");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 再生と停止を行う．またViewへの反映も同時に行う
	 * @param playerView
	 * @param mpwpl
	 */
	public static final void pauseWithView(){
		//再生動作を行う
		try {
			MusicPlayerWithQueue mpwpl = getMusicController(getContext());
			if(mpwpl.getStatus() == MusicPlayerWithQueue.PLAYING){
				mpwpl.playStartAndPause();
				reflectOfView(false);
				android.util.Log.v(TAG, "PauseWithView");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 再生の停止を行う．
	 * @param playerView
	 * @param mpwpl
	 */
	public static final void stopWithView(){
		try{
			MusicPlayerWithQueue mpwpl = getMusicController(getContext());
			mpwpl.playStop();
			reflectOfView(true);
			android.util.Log.v(TAG, "StopWithView");
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	/**
	 * 次の曲へ移動する．
	 * @param playerView
	 */
	public static final void playNextWithView(){
		try{
			MusicPlayerWithQueue mpwpl = getMusicController(getContext());
			mpwpl.playNext();
			reflectOfView(true);
			android.util.Log.v(TAG, "PlayNextWithView");
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	/**
	 * 前の曲に戻る．
	 * @param playerView
	 */
	public static final void playBackWithView(){
		try{
			MusicPlayerWithQueue mpwpl = getMusicController(getContext());
			mpwpl.playBack();
			reflectOfView(true);
			android.util.Log.v(TAG, "PlayBackWithView");
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	/**
	 * Repeat状態の変化
	 * @param playerView
	 */
	public static final void changeRepeatState(){
		MusicPlayerWithQueue mpwpl = getMusicController(getContext());
		//現在の状態を取得する
		int loopState = mpwpl.getLoopState();
		//LOOPしてない時
		if(loopState == MusicPlayerWithQueue.NOT_LOOP){
			loopState = MusicPlayerWithQueue.ALL_LOOP;
		}
		//全曲LOOPの時
		else if(loopState == MusicPlayerWithQueue.ALL_LOOP){
			loopState = MusicPlayerWithQueue.ONE_LOOP;
		}
		//1曲ループの時
		else if(loopState == MusicPlayerWithQueue.ONE_LOOP){
			loopState = MusicPlayerWithQueue.NOT_LOOP;
		}
		//LOOP状態の取得
		loopState = mpwpl.setLoopState(loopState);
		
		//リピートボタンの動作設定
		ImageButton repeatButton = (ImageButton)playerView.findViewById(R.id.button_repeat);
		
		final String message;
		switch(loopState){
		default:
		case MusicPlayerWithQueue.NOT_LOOP:
			message = getContext().getString(R.string.play_loop_state_notloop);
			break;
		case MusicPlayerWithQueue.ALL_LOOP:
			message = getContext().getString(R.string.play_loop_state_allloop);
			break;
		case MusicPlayerWithQueue.ONE_LOOP:
			message = getContext().getString(R.string.play_loop_state_oneloop);
		}
		Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
	}
	
	/**
	 * シャッフルの状態を変更する
	 * Viewの変更を伴う
	 * @param playerView
	 */
	public static final void changeShuffleState(){
		MusicPlayerWithQueue mpwpl = getMusicController(getContext());
		boolean isShuffle = mpwpl.setShuffle(!mpwpl.isShuffle());
		if(isShuffle){
			Toast.makeText(getContext(), R.string.shuffle_on, Toast.LENGTH_SHORT).show();
			//シャッフルがONの時
			//Viewへの反映
		}else{
			//シャッフルがOFFの時
			Toast.makeText(getContext(), R.string.shuffle_off, Toast.LENGTH_SHORT).show();
		}
		//TODO　動作を登録する．
		ImageButton shuffleButton = (ImageButton)playerView.findViewById(R.id.button_shuffle);
	}
	
	/**
	 * PlayerViewへ変更を反映させる．
	 * @param playerView
	 * @param mpwpl
	 */
	public static final void reflectOfView(boolean isNotifitionViewPager){
		
		try{
			//Notificationに通知を設定
			Notifications.putNotification();
		}catch(Exception e){
			e.printStackTrace();
		}
		
		if(playerView == null)return; //ViewがNullの時は反映しない．
		
		MusicPlayerWithQueue mpwpl = getMusicController(getContext());
		//再生ボタンの設定
		ImageButton playButton = (ImageButton)playerView.findViewById(R.id.button_play);
		if(playButton != null){
			if(mpwpl.getStatus() == MusicPlayerWithQueue.PLAYING){
				//Viewを一時停止ボタンに
				playButton.setBackgroundResource(R.drawable.pause_not_pushed);
			}else{
				//Viewを再生ボタンに
				playButton.setBackgroundResource(R.drawable.play_not_pushed);
			}
		}
		setPartOfPlayerView(
				getContext(),
				playerView, 
				mpwpl.getNowPlayingMusic(),
				mpwpl);
		//ViewPager の設定
		ViewPager mViewPager = (ViewPager)playerView.findViewById(R.id.player_list_part);
		if(mViewPager != null && mViewPager.getCurrentItem() == MusicViewPagerAdapter.EQUALIZER){
			ViewPagerForEqualizerViewCtl.createMusicVisualizer(mViewPager.getContext());
		}
		
		//ViewPagerに通知する必要があるかどうか選択する場合
		if(isNotifitionViewPager){
			//Viewへの反映
			if(mViewPager != null){
				//ViewPagerのQueueに再生曲変更の通知
				((MusicViewPagerAdapter)mViewPager.getAdapter()).notifitionDataSetChagedForQueueView();
			}
		}
	}
	
	/**
	 * MusicPlayerView中の再生曲情報を表示するViewへのデータセットを行う
	 * @param context
	 * @param mView
	 * @param music
	 */
	private static final void setPartOfPlayerView(final Context context,final View mView,final Music music,final MusicPlayerWithQueue mpwpl){
		
		//ViewがNullの場合は何もしない．
		if(mView == null) return;
		//一定時間おきの動作設定
		if(mHandler != null)
			mHandler.stopHandler();
		mHandler = null;
		
		//タイトルView
		final TextView title = (TextView)mView.findViewById(R.id.textView_now_music_name);
		title.setText(music.getTitle());
		title.setEllipsize(TextUtils.TruncateAt.MARQUEE);
		title.setSingleLine(true);
		title.setMarqueeRepeatLimit(5);
		title.setSelected(true);
		//アーティスト
		final TextView artist = (TextView)mView.findViewById(R.id.textView_now_artist_name);
		artist.setText(music.getArtist());
		//アルバム名
		final TextView album = (TextView)mView.findViewById(R.id.textView_now_album);
		album.setText(music.getAlbum());

		//曲時間
		final TextView maxTime = (TextView)mView.findViewById(R.id.textView_now_max_time);
		maxTime.setText(DisplayUtils.long2TimeString(music.getDuration()));

		//アルバムジャケット
		final ImageView jacket = (ImageView)mView.findViewById(R.id.imageView_now_jacket);
		//ジャケットの取得
		Uri albumArtUri = Uri.parse(
		        "content://media/external/audio/albumart");
		Uri album1Uri = ContentUris.withAppendedId(albumArtUri, music.getAlbumId());
		try{
		    ContentResolver cr = context.getContentResolver();
		    InputStream is = cr.openInputStream(album1Uri);
		    Bitmap bm = BitmapFactory.decodeStream(is);
		    if(bm != null)
		    	jacket.setImageBitmap(DisplayUtils.RadiusImage(bm));
		    else
		    	jacket.setImageResource(android.R.drawable.ic_menu_search);
		}catch(FileNotFoundException err){
			jacket.setImageResource(android.R.drawable.ic_menu_search);
		}
		
		//現在の再生時間
		final TextView currentTime = (TextView)mView.findViewById(R.id.TextView_now_current_time);
		//currentTime.setText("0:00");
		
		//曲のシークバー
		final SeekBar seekbar = (SeekBar)mView.findViewById(R.id.seekBar_now_music_seek);
		if(seekbar.getMax() != music.getDuration())
			seekbar.setMax((int)(music.getDuration()));
		//seekbar.setProgress(0);
		
		seekbar.setOnSeekBarChangeListener(new MusicSeekBarOnChangeImpl(currentTime,mpwpl));
		
		final ImageButton playButton = (ImageButton)mView.findViewById(R.id.button_play);
		
		if(mpwpl.getStatus() == MusicPlayerWithQueue.PLAYING){
			//Viewを一時停止ボタンに
			playButton.setBackgroundResource(android.R.drawable.ic_media_pause);
		}else{
			playButton.setBackgroundResource(android.R.drawable.ic_media_play);
		}
		
		mHandler = new MusicSeekBarHandler(currentTime,seekbar,mpwpl);
		mHandler.sleep(0);
		
	}
	
	
	/**
	 * 外部ストレージから音楽データをスキャンする
	 * @param context
	 * @return
	 */
	public static ArrayList<Music> getMusicList(final Context context){
		
		final ArrayList<Music> items = new ArrayList<Music>();

		// ContentResolver を取得
		final ContentResolver cr = context.getContentResolver();
		
		//アルバムのUriを読み込む
		final Uri albumArtUri = Uri.parse(
		        "content://media/external/audio/albumart");

		// 外部ストレージから音楽を検索
		final Cursor cur = cr.query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
				null,
				MediaStore.Audio.Media.IS_MUSIC + " = 1",
				null,
				null);

		if (cur != null && cur.moveToFirst()) {
			
			// 曲情報のカラムを取得
			int artistColumn = cur.getColumnIndex(MediaStore.Audio.Media.ARTIST);
			int titleColumn = cur.getColumnIndex(MediaStore.Audio.Media.TITLE);
			int albumColumn = cur.getColumnIndex(MediaStore.Audio.Media.ALBUM);
			int albumIdColumn = cur.getColumnIndex(MediaStore.Audio.Media.ALBUM_ID);
			int durationColumn = cur.getColumnIndex(MediaStore.Audio.Media.DURATION);
			int idColumn = cur.getColumnIndex(MediaStore.Audio.Media._ID);
			int idTruck = cur.getColumnIndex(MediaStore.Audio.Media.TRACK);
			int pass = cur.getColumnIndex(MediaStore.Audio.Media.DATA);

			// リストに追加
			do {
				final long albumId = cur.getLong(albumIdColumn);
				Uri album1Uri = ContentUris.withAppendedId(albumArtUri, albumId);
				Music music = new Music(cur.getLong(idColumn),
						cur.getString(artistColumn),
						cur.getString(titleColumn),
						cur.getString(albumColumn),
						albumId,
						cur.getInt(idTruck),
						cur.getLong(durationColumn),
						album1Uri,
						cur.getString(pass));

				items.add(music);

			} while (cur.moveToNext());

			// カーソルを閉じる
			cur.close();

			//結果をアルバム順でソート
			Collections.sort(items);
		}
		return items;
	}

	/**
	 * 音楽ファイルをスキャンして，アルバムクラス配列を取得する．
	 * @param mService
	 * @return AlbumのArrayList
	 */
	public static ArrayList<Album> getMusicAlbumList(Service mService) {
		
		final ArrayList<Album> albums = new ArrayList<Album>();
		
		ArrayList<Music> items = getMusicList(mService);
		
		long _albumId = -1;
		
		ArrayList<Music> tmpMusic = new ArrayList<Music>();
		Album mAlbum = null;
		for(Music item : items){
			if(item.getAlbumId() != _albumId){
				//IDが前のものと違う時
				if(mAlbum != null){
					mAlbum.setMusics(tmpMusic.toArray(new Music[tmpMusic.size()]));
					albums.add(mAlbum);
					tmpMusic.clear();
				}
				mAlbum = new Album(item.getAlbum(),
						item.getArtist(),
						item.getAlbumId(), 
						item.getAlbumUri());
				_albumId = item.getAlbumId();
			}
			tmpMusic.add(item);
		}
		if(tmpMusic.size() > 0){
			mAlbum.setMusics(tmpMusic.toArray(new Music[tmpMusic.size()]));
			albums.add(mAlbum);
			tmpMusic.clear();
		}
		return albums;
	}
	

	
}
