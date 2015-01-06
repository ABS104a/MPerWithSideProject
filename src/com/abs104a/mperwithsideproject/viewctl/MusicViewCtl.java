package com.abs104a.mperwithsideproject.viewctl;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

import com.abs104a.mperwithsideproject.MainService;
import com.abs104a.mperwithsideproject.Notifications;
import com.abs104a.mperwithsideproject.R;
import com.abs104a.mperwithsideproject.adapter.MusicListAdapter.ViewHolder;
import com.abs104a.mperwithsideproject.adapter.MusicViewPagerAdapter;
import com.abs104a.mperwithsideproject.music.Music;
import com.abs104a.mperwithsideproject.music.MusicPlayerWithQueue;
import com.abs104a.mperwithsideproject.music.PlayList;
import com.abs104a.mperwithsideproject.utl.DisplayUtils;
import com.abs104a.mperwithsideproject.utl.ImageCache;
import com.abs104a.mperwithsideproject.utl.MusicUtils;
import com.abs104a.mperwithsideproject.viewctl.listener.BackButtonOnClickImpl;
import com.abs104a.mperwithsideproject.viewctl.listener.ExitButtonOnClickListenerImpl;
import com.abs104a.mperwithsideproject.viewctl.listener.MusicSeekBarOnChangeImpl;
import com.abs104a.mperwithsideproject.viewctl.listener.NextButtonOnClickImpl;
import com.abs104a.mperwithsideproject.viewctl.listener.OnPlayCompletedImpl;
import com.abs104a.mperwithsideproject.viewctl.listener.PlayButtonOnClickImpl;
import com.abs104a.mperwithsideproject.viewctl.listener.RepeatButtonOnClickImpl;
import com.abs104a.mperwithsideproject.viewctl.listener.SettigsOnClickImpl;
import com.abs104a.mperwithsideproject.viewctl.listener.ShareOnClickImpl;
import com.abs104a.mperwithsideproject.viewctl.listener.ShuffleButtonOnClickImpl;
import com.abs104a.mperwithsideproject.viewctl.listener.ViewPagerOnPagerChangeImpl;

import android.app.Service;
import android.content.ActivityNotFoundException;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.provider.MediaStore.Images;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.SeekBar.OnSeekBarChangeListener;

/**
 * プレイヤーメインViewの生成と設定を行うクラス
 * 
 * 
 * @author Kouki
 *
 */
public final class MusicViewCtl {

	//PlayerViewのID
	public final static int PLAYER_VIEW_ID = 12;
	
	public final static int ANIMATE_OPEN = 35;

	//ViewPagerのページを記憶する用
	private static int pageCount = 0;
	
	public static int setPageCount(int page){
		return pageCount = page;
	}
	
	public static int getPageCount(){
		return pageCount;
	}
	
	public static Context getContext(){
		return MainService.getService();
	}
	
	//クラスの識別用タグ
	public static final String TAG = "MusicViewCtl";
	
	
	/**
	 * PlayerViewを消去する．
	 * @param PlayerView
	 */
	public static void removePlayerView(final View rootView){
		//Viewの消去を行う
		if(getPlayerView() != null && rootView != null){
			final ImageButton handle = (ImageButton) rootView.findViewById(R.id.imageButton_handle);
			handle.setVisibility(View.INVISIBLE);
			Animation closeAnimation = 
					AnimationUtils.loadAnimation(rootView.getContext(), R.anim.left_to_right_out);
			closeAnimation.setAnimationListener(new AnimationListener(){

				@Override
				public void onAnimationEnd(Animation animation) {
					//Visualizerの消去
					//ViewPagerForEqualizerViewCtl.removeMusicVisualizer();
					
					MusicPlayerWithQueue mpwpl = MusicUtils.getMusicController(getContext());
					if(mpwpl.getStatus() != MusicPlayerWithQueue.PLAYING){
						mpwpl.playStop();
						mpwpl.release();
					}

					DisplayUtils.cleanupView(getPlayerView());
					((LinearLayout)rootView).removeView(getPlayerView());
					setPlayerView(null);
					System.gc();
					//キャッシュのClear
					ImageCache.clearCache();
					setPlayerView(null);
					final Handler mHandler = new Handler();
					mHandler.post(new Runnable(){

						@Override
						public void run() {
							handle.setVisibility(View.VISIBLE);
							handle.startAnimation(
									AnimationUtils
									.loadAnimation(rootView.getContext(), android.R.anim.fade_in));
						}
						
					});
					
				}

				@Override
				public void onAnimationRepeat(Animation animation) {}

				@Override
				public void onAnimationStart(Animation animation) {}
				
			});
			getPlayerView().startAnimation(closeAnimation);
			
			//プレイリストの書き込みを行う
			PlayList.writePlayList(getPlayerView().getContext());
			PlayList.clearPlayList();
			DisplayUtils.printHeapSize();
			
		}
	}
	
	/**
	 * PlayerView
	 *　を生成する（non-OpenAnimation）
	 * @param PlayerView
	 */
	public static void createPlayerView(final Service mService,View rootView){
		if(getPlayerView() == null){
			final View mView = createView(mService,rootView);
			final ImageButton handle = (ImageButton) rootView.findViewById(R.id.imageButton_handle);
			handle.setVisibility(View.INVISIBLE);
			
			final Animation showAnimation = 
					AnimationUtils.loadAnimation(mService, R.anim.right_to_left_in);
			showAnimation.setAnimationListener(new AnimationListener(){

				@Override
				public void onAnimationEnd(Animation animation) {
					handle.setVisibility(View.VISIBLE);
					handle.startAnimation(AnimationUtils.loadAnimation(mService, android.R.anim.fade_in));
				}

				@Override
				public void onAnimationRepeat(Animation animation) {}

				@Override
				public void onAnimationStart(Animation animation) {}
				
			});
			//Animationの設定
			mView.startAnimation(showAnimation);
		}	
	}
	
	/**
	 * PlayerのViewを生成するメソッド
	 * サービスのコンテキストを受けとりViewを生成する．
	 * @param mService
	 * @return　生成したViewGroup
	 */
	private static View createView(Service mService,View rootView){
		// Viewからインフレータを作成する
		LayoutInflater layoutInflater = LayoutInflater.from(mService);
		// レイアウトファイルから重ね合わせするViewを作成する
		setPlayerView(layoutInflater.inflate(R.layout.player_view, (ViewGroup)getPlayerView(),false));
		getPlayerView().setId(PLAYER_VIEW_ID);
		((LinearLayout)rootView).addView(getPlayerView());
		//Action Settings 
		init(mService, getPlayerView(),rootView);
		DisplayUtils.printHeapSize();
		return getPlayerView();
	}
	
	/**
	 * 初期化を行う
	 * @param mService
	 * @param mView
	 * @param PlayerView 
	 * @param mpwpl 
	 */
	private final static void init(Service mService,View mView, View rootView){
		MusicPlayerWithQueue mpwpl = MusicUtils.getMusicController(mService);
		initAction(mService,mView,mpwpl);
		initButtonOfView(mService,mView,mpwpl);
	}
	
	/**
	 * 生成したメインView
	 * @param mService
	 * @param mView
	 * @param _mpwpl 
	 */
	private static void initButtonOfView(final Service mService,final View mView, MusicPlayerWithQueue _mpwpl){
		//Viewのボタンに動作をつける
		//再生ボタンの設定
		ImageButton playButton = (ImageButton)mView.findViewById(R.id.button_play);
		//再生ボタンの動作を登録する．
		playButton.setOnClickListener(new PlayButtonOnClickImpl());
		
		//次へのボタンの設定
		ImageButton nextButton = (ImageButton)mView.findViewById(R.id.button_next_seek);
		//次へボタンの動作を登録する．
		nextButton.setOnClickListener(new NextButtonOnClickImpl());
		
		ImageButton backButton = (ImageButton)mView.findViewById(R.id.button_back_seek);
		//前へボタンの動作を登録する．
		backButton.setOnClickListener(new BackButtonOnClickImpl());
		
		ImageButton repeatButton = (ImageButton)mView.findViewById(R.id.button_repeat);
		//リピートボタンの動作を登録する．
		repeatButton.setOnClickListener(new RepeatButtonOnClickImpl());
		
		ImageButton shuffleButton = (ImageButton)mView.findViewById(R.id.button_shuffle);
		//シャッフルボタンの動作を登録する．
		shuffleButton.setOnClickListener(new ShuffleButtonOnClickImpl());
		
		//終了ボタンの設定
		ImageButton exitButton = (ImageButton)mView
				.findViewById(R.id.button_action_exit);
		exitButton.setOnClickListener(new ExitButtonOnClickListenerImpl(mService, MusicUtils.getMusicController(mService)));
		
		//リスト表示ボタン
		ImageButton showListButton = (ImageButton)mView.findViewById(R.id.button_action_show_list);
		showListButton.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				//TODO リスト表示ボタンの設定を登録する
				
			}
			
		});
		
		//ShareButtonの動作
		ImageButton shareButton = (ImageButton)mView.findViewById(R.id.imageButton_share);
		shareButton.setOnClickListener(new ShareOnClickImpl());
		
		//設定ボタンを押した時の動作
		ImageButton showSettingsButton = (ImageButton)mView.findViewById(R.id.button_action_show_settings);
		showSettingsButton.setOnClickListener(new SettigsOnClickImpl());;
		
	}
	
	/**
	 * MainPlayerViewの動作を設定する
	 * @param mService
	 * @param mView
	 * @param _mpwpl
	 */
	private static void initAction(Service mService,View mView, MusicPlayerWithQueue _mpwpl){
		//再生が終了した時に呼ばれるリスナを実装する．
		//再生が完了したときのリスナをセット．
		_mpwpl.setOnPlayCompletedListener(new OnPlayCompletedImpl());
		
		//ViewPager の設定
		ViewPager mViewPager = (ViewPager)mView.findViewById(R.id.player_list_part);
		//Adapterの設定
		mViewPager.setAdapter(new MusicViewPagerAdapter(mService));
		//ページの設定
		ViewPagerOnPagerChangeImpl pageChangeListener = new ViewPagerOnPagerChangeImpl(mViewPager);
		mViewPager.setOnPageChangeListener(pageChangeListener);
		mViewPager.setCurrentItem(pageCount);
		
		
		//プレイリストを設定
		if(_mpwpl.getNowPlayingMusic() != null)
			reflectOfView(true);
		
		//音量の設定
		// AudioManagerを取得する
        final AudioManager am = (AudioManager)mService.getSystemService(Context.AUDIO_SERVICE);
 
        // 現在の音量を取得する
        int musicVolume = am.getStreamVolume(AudioManager.STREAM_MUSIC);
 
        // ストリームごとの最大音量を取得する
        int musicMaxVolume = am.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        //音量を調節するシークバー
        final SeekBar volumeBar = (SeekBar)mView.findViewById(R.id.seekBar_volume);
        volumeBar.setMax(musicMaxVolume);
        volumeBar.setProgress(musicVolume);
        //音量のシークバーが変更された時のリスナ
        volumeBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener(){

			@Override
			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromUser) {
				// シークバーが変更された時
				if(fromUser){//音量を変更する
					am.setStreamVolume(AudioManager.STREAM_MUSIC, progress, 0);
				}
			}
			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {}

			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {}
        	
        });
		
	}
	
	/**
	 * 音量が変更されたときにViewに反映させるメソッド
	 */
	public static void changeVolume(){
		//ViewがNullの場合は変更する必要がないため何もしない．
		if(getPlayerView() == null)return;
		//音量の設定
		// AudioManagerを取得する
		final AudioManager am = (AudioManager)getPlayerView().getContext().getSystemService(Context.AUDIO_SERVICE);

		// 現在の音量を取得する
		int musicVolume = am.getStreamVolume(AudioManager.STREAM_MUSIC);

		// ストリームごとの最大音量を取得する
		int musicMaxVolume = am.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
		//音量を調節するシークバー
		final SeekBar volumeBar = (SeekBar)getPlayerView().findViewById(R.id.seekBar_volume);
		volumeBar.setMax(musicMaxVolume);
		volumeBar.setProgress(musicVolume);
		//音量のシークバーが変更された時のリスナ
	}
	
	
	//UIスレッドのHandler
	private static MusicSeekBarHandler mHandler = null;
	
	private static View playerView = null;
	
	public static View getPlayerView(){
		return playerView;
	}
	
	public static void setPlayerView(View view){
		playerView = view;
	}
	

	
	/**
	 * 再生と停止を行う．またViewへの反映も同時に行う
	 * @param playerView
	 * @param mpwpl
	 */
	public static final void playOrPauseWithView(){
		//再生動作を行う
		try {
			MusicPlayerWithQueue mpwpl = MusicUtils.getMusicController(getContext());
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
			MusicPlayerWithQueue mpwpl = MusicUtils.getMusicController(getContext());
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
			MusicPlayerWithQueue mpwpl = MusicUtils.getMusicController(getContext());
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
			MusicPlayerWithQueue mpwpl = MusicUtils.getMusicController(getContext());
			if(mpwpl.getStatus() == MusicPlayerWithQueue.PLAYING){
				mpwpl.playStartAndPause();
				reflectOfView(true);
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
			MusicPlayerWithQueue mpwpl = MusicUtils.getMusicController(getContext());
			mpwpl.playStop();
			reflectOfView(false);
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
			MusicPlayerWithQueue mpwpl = MusicUtils.getMusicController(getContext());
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
			MusicPlayerWithQueue mpwpl = MusicUtils.getMusicController(getContext());
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
		MusicPlayerWithQueue mpwpl = MusicUtils.getMusicController(getContext());
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
		reflectOfView(false);
		
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
		MusicPlayerWithQueue mpwpl = MusicUtils.getMusicController(getContext());
		boolean isShuffle = mpwpl.setShuffle(!mpwpl.isShuffle());
		if(isShuffle){
			Toast.makeText(getContext(), R.string.shuffle_on, Toast.LENGTH_SHORT).show();
			//シャッフルがONの時
			//Viewへの反映
		}else{
			//シャッフルがOFFの時
			Toast.makeText(getContext(), R.string.shuffle_off, Toast.LENGTH_SHORT).show();
		}
		//　動作を登録する．
		reflectOfView(false);
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
		
		MusicPlayerWithQueue mpwpl = MusicUtils.getMusicController(getContext());
		//再生ボタンの設定
		ImageButton playButton = (ImageButton)playerView.findViewById(R.id.button_play);
		if(playButton != null){
			if(mpwpl.getStatus() == MusicPlayerWithQueue.PLAYING){
				//Viewを一時停止ボタンに
				playButton.setBackgroundResource(R.drawable.button_pause);
			}else{
				//Viewを再生ボタンに
				playButton.setBackgroundResource(R.drawable.button_play);
			}
		}
		ImageButton loopButton = (ImageButton)playerView.findViewById(R.id.button_repeat);
		if(loopButton != null){
			switch(mpwpl.getLoopState()){
			default:
			case MusicPlayerWithQueue.NOT_LOOP:
				loopButton.setBackgroundResource(R.drawable.button_loop_no);
				break;
			case MusicPlayerWithQueue.ALL_LOOP:
				loopButton.setBackgroundResource(R.drawable.button_loop_all);
				break;
			case MusicPlayerWithQueue.ONE_LOOP:
				loopButton.setBackgroundResource(R.drawable.button_loop_once);
			}
		}
		ImageButton shuffleButton = (ImageButton)playerView.findViewById(R.id.button_shuffle);
		if(mpwpl.isShuffle()){
			shuffleButton.setBackgroundResource(R.drawable.button_shuffle_on);
			//シャッフルがONの時
		}else{
			//シャッフルがOFFの時
			shuffleButton.setBackgroundResource(R.drawable.button_shuffle_off);
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
		    	jacket.setImageBitmap(bm);
		    else
		    	jacket.setImageResource(R.drawable.no_image);
		}catch(FileNotFoundException err){
			jacket.setImageResource(R.drawable.no_image);
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
		
		mHandler = new MusicSeekBarHandler(currentTime,seekbar,mpwpl);
		mHandler.sleep(0);
		
	}
	
	
	/**
	 * Twitterに投稿する．
	 * @param con
	 * @param bm
	 * @param message
	 */
	public static void sendShareTwit(Context con,Bitmap bm,String message) {
	    try {
	        Intent tweetIntent = new Intent(Intent.ACTION_SEND);
	        //画像を保存
	        ContentResolver cr = con.getContentResolver();
	        Uri imageuri = addImageAsApplication(cr,bm);
	        tweetIntent.putExtra(Intent.EXTRA_TEXT, message);
	        tweetIntent.putExtra(Intent.EXTRA_STREAM,imageuri);
	        tweetIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK); 
	        tweetIntent.setType("image/jpeg");
	        PackageManager pm = con.getPackageManager();
	        List<ResolveInfo> lract = pm.queryIntentActivities(tweetIntent, PackageManager.MATCH_DEFAULT_ONLY);
	        boolean resolved = false;
	        for (ResolveInfo ri : lract) {
	            if (ri.activityInfo.name.contains("twitter")) {
	                tweetIntent.setClassName(ri.activityInfo.packageName,
	                        ri.activityInfo.name);
	                resolved = true;
	                break;
	            }
	        }
	        con.startActivity(resolved ?
	                tweetIntent :
	                Intent.createChooser(tweetIntent, "Choose one"));
	    } catch (final ActivityNotFoundException e) {
	        Toast.makeText(con,"You don't seem to have twitter installed on this device", Toast.LENGTH_SHORT).show();
	    }
	}
	 
	//twitter投稿時の画像保存
	private static final String APPLICATION_NAME = "PATOM";
	private static final Uri IMAGE_URI = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
	private static final String PATH = Environment.getExternalStorageDirectory().toString() + "/" + APPLICATION_NAME;
	public static Uri addImageAsApplication(ContentResolver cr, Bitmap bitmap) {
	    long dateTaken = System.currentTimeMillis();
	    String name = createName(dateTaken) + ".jpg";
	    return addImageAsApplication(cr, name, dateTaken, PATH, name, bitmap, null);
	}
	 
	//画像ファイル名を生成
	private static String createName(long dateTaken) {
	    return DateFormat.format("yyyy-MM-dd_kk.mm.ss", dateTaken).toString();
	}
	 
	//画像をアルバムに保存しURI取得
	public static Uri addImageAsApplication(
	        ContentResolver cr,
	        String name,
	        long dateTaken,
	        String directory,
	        String filename,
	        Bitmap source,
	        byte[] jpegData
	){
	    OutputStream outputStream = null;
	    String filePath = directory + "/" + filename;
	    try {
	        File dir = new File(directory);
	        if (!dir.exists()) {
	            dir.mkdirs();
	            Log.d(TAG, dir.toString() + " create");
	        }
	        File file = new File(directory, filename);
	        if (file.createNewFile()) {
	            outputStream = new FileOutputStream(file);
	            if (source != null) {
	                source.compress(CompressFormat.JPEG, 75, outputStream);
	            } else {
	                outputStream.write(jpegData);
	            }
	        }
	 
	    } catch (FileNotFoundException ex) {
	            Log.w(TAG, ex);
	            return null;
	    } catch (IOException ex) {
	        Log.w(TAG, ex);
	        return null;
	    } finally {
	        if (outputStream != null) {
	            try {
	                outputStream.close();
	            } catch (Throwable t) {
	            }
	        }
	    }
	    ContentValues values = new ContentValues(7);
	    values.put(Images.Media.TITLE, name);
	    values.put(Images.Media.DISPLAY_NAME, filename);
	    values.put(Images.Media.DATE_TAKEN, dateTaken);
	    values.put(Images.Media.MIME_TYPE, "image/jpeg");
	    values.put(Images.Media.DATA, filePath);
	    return cr.insert(IMAGE_URI, values);
	}
	
}