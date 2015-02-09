package com.abs104a.mperwithsideproject.viewctl;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import com.abs104a.mperwithsideproject.Column;
import com.abs104a.mperwithsideproject.MainService;
import com.abs104a.mperwithsideproject.Notifications;
import com.abs104a.mperwithsideproject.R;
import com.abs104a.mperwithsideproject.adapter.MusicViewPagerAdapter;
import com.abs104a.mperwithsideproject.music.Music;
import com.abs104a.mperwithsideproject.music.MusicPlayerWithQueue;
import com.abs104a.mperwithsideproject.music.PlayList;
import com.abs104a.mperwithsideproject.settings.Settings;
import com.abs104a.mperwithsideproject.utl.DisplayUtils;
import com.abs104a.mperwithsideproject.utl.GetImageTask;
import com.abs104a.mperwithsideproject.utl.ImageCache;
import com.abs104a.mperwithsideproject.utl.MusicUtils;
import com.abs104a.mperwithsideproject.viewctl.listener.BackButtonOnClickImpl;
import com.abs104a.mperwithsideproject.viewctl.listener.ExitButtonOnClickListenerImpl;
import com.abs104a.mperwithsideproject.viewctl.listener.ListButtonOnClickImpl;
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
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.PixelFormat;
import android.graphics.Bitmap.CompressFormat;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.provider.MediaStore.Images;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
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
	
	private static final long DELAYTIME = 100;

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
	
	//UIスレッドのHandler
	private static MusicSeekBarHandler mHandler = null;
	
	private static View playerView = null;
	
	public static View getPlayerView(){
		return playerView;
	}
	
	public static void setPlayerView(View view){
		playerView = view;
	}
	
	//クラスの識別用タグ
	public static final String TAG = "MusicViewCtl";
	
	/**
	 * PlayerViewを消去する．
	 * @param PlayerView
	 */
	public static void removePlayerView(){
		removePlayerView(false);
	}
	
	/**
	 * PlayerViewを消去する．
	 * @param PlayerView
	 */
	public static void removeAndStopView(){
		removePlayerView(true);
	}
	
	/**
	 * PlayerViewを消去する．
	 * @param PlayerView
	 */
	private static void removePlayerView(final boolean isStop){
		final View rootView = MainViewCtl.getRootView();
		//Viewの消去を行う
		if(getPlayerView() != null && rootView != null){
			
			//Visualizerの無効化
			ViewPagerForEqualizerViewCtl.setIsVisualizer(false);
			
			//Viewのクリーンアップ
			try{
				ViewPager mViewPager = (ViewPager)getPlayerView().findViewById(R.id.player_list_part);
				((MusicViewPagerAdapter)mViewPager.getAdapter()).cleanUp();
			}catch(NullPointerException e){
				
			}
			
			final Button handle = (Button) rootView.findViewById(R.id.imageButton_handle);
			LayoutParams params = handle.getLayoutParams();
			params.width = Settings.getHandleWidth(getContext());
			params.height = Settings.getHandleHeight(getContext());
			handle.setLayoutParams(params);
			
			/*
			final View emptyView = rootView.findViewById(R.id.view_handle_view);
			LayoutParams viewpParams = emptyView.getLayoutParams();
			viewpParams.width = 0;
			emptyView.setLayoutParams(viewpParams);
			*/
			
			//プレイリストの書き込みを行う
			if(mHandler != null){
				mHandler.stopHandler();
				mHandler = null;
			}
			
			PlayList.writePlayList(getPlayerView().getContext());
			PlayList.clearPlayList();
			DisplayUtils.printHeapSize();	

			
			final Animation closeAnimation = 
					AnimationUtils.loadAnimation(rootView.getContext(), R.anim.left_to_right_out);
			closeAnimation.setAnimationListener(new AnimationListener(){

				@Override
				public void onAnimationStart(Animation animation) {}

				@Override
				public void onAnimationEnd(Animation animation) {
					try{
						ViewPagerForEqualizerViewCtl.removeMusicVisualizer();
						//WindowManagerの取得
						WindowManager mWindowManager = (WindowManager)getPlayerView().getContext().getSystemService(Context.WINDOW_SERVICE);
						mWindowManager.removeView((View)getPlayerView().getParent());
						setPlayerView(null);
						if(isStop){
							((MainService)MainService.getService()).stop();
						}
					}catch(NullPointerException e){
						e.printStackTrace();
					}
				}

				@Override
				public void onAnimationRepeat(Animation animation) {}
				
			});
			
			getPlayerView().startAnimation(closeAnimation);
			
		}
	}
	
	/**
	 * PlayerView
	 *　を生成する（non-OpenAnimation）
	 * @param PlayerView
	 */
	public static void createPlayerView(final Service mService,View rootView){
		if(getPlayerView() == null){
			final View mView = createView(mService);
			
			/*
			final View emptyView = rootView.findViewById(R.id.view_handle_view);
			LayoutParams viewpParams = emptyView.getLayoutParams();
			viewpParams.width = (int) (emptyView.getContext().getResources().getDimensionPixelSize(R.dimen.player_view_width) + 
					1.5f * emptyView.getContext().getResources().getDimensionPixelSize(R.dimen.player_view_padding));
			emptyView.setLayoutParams(viewpParams);
			*/
			
			final Button handle = (Button) rootView.findViewById(R.id.imageButton_handle);
			//handle.setVisibility(View.INVISIBLE);
			LayoutParams params = handle.getLayoutParams();
			params.width = params.width * 2 + (int) (handle.getContext().getResources().getDimensionPixelSize(R.dimen.player_view_width) + 
					1.5f * handle.getContext().getResources().getDimensionPixelSize(R.dimen.player_view_padding));
			handle.setLayoutParams(params);
			
			final Animation showAnimation = 
					AnimationUtils.loadAnimation(mService, R.anim.right_to_left_in);
			
			showAnimation.setAnimationListener(new AnimationListener(){

				@Override
				public void onAnimationStart(Animation animation) {}

				@Override
				public void onAnimationEnd(Animation animation) {
					mView.setVisibility(View.VISIBLE);
				}

				@Override
				public void onAnimationRepeat(Animation animation) {}
				
			});
			
			//Animationの設定
			mView.startAnimation(showAnimation);

		}	
	}
	
	/**
	 * MainViewを生成して画面に表示を行うメソッド
	 * @param mService	アプリケーションのContext
	 * @return 
	 */
	private final static View createView(Service mService){
		ImageCache.clearCache();
		if(mService == null)return null;
		//WindowManagerの取得
		WindowManager mWindowManager = (WindowManager) mService.getSystemService(Context.WINDOW_SERVICE);

		// 重ね合わせするViewの設定を行う
		WindowManager.LayoutParams params = new WindowManager.LayoutParams(
				WindowManager.LayoutParams.WRAP_CONTENT,
				WindowManager.LayoutParams.WRAP_CONTENT,
				WindowManager.LayoutParams.TYPE_PHONE,
				WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | 
				WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED ,// | 
				PixelFormat.TRANSLUCENT);

		params.gravity = Gravity.CENTER_VERTICAL | Gravity.RIGHT;
		params.x = 0;
		params.y = 0;
		
		// Viewからインフレータを作成する
		LayoutInflater layoutInflater = LayoutInflater.from(mService);
		// レイアウトファイルから重ね合わせするViewを作成する
		setPlayerView(layoutInflater.inflate(R.layout.player_view, (ViewGroup)getPlayerView(),false));
		getPlayerView().setId(PLAYER_VIEW_ID);
		//Action Settings 
		init(mService, getPlayerView(),getPlayerView());
		DisplayUtils.printHeapSize();
		
		//プレイヤーのViewはハンドル部をタップした時に生成する．
		//ハンドル部が引き出される動作と同時に大きさを変更させ，
		//完全にハンドルが収納されたらViewを破棄する．
		
		FrameLayout parentLayout = new FrameLayout(mService);
		parentLayout.addView(getPlayerView());
		
		//WindowManagerにViewとLayoutParamsを登録し，表示する
		try{
			mWindowManager.addView(parentLayout, params);
			//こっちは↓更新用
			//mWindowManager.updateViewLayout(mMainView, params);
		}catch(NullPointerException mNullPointerException){
			mNullPointerException.printStackTrace();
			//自分のサービスを終了させる．
			mService.stopSelf();
		}
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
		showListButton.setOnClickListener(new ListButtonOnClickImpl());
		
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
	private static void initAction(Service mService,View mView, final MusicPlayerWithQueue _mpwpl){
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
		
		if(pageCount == Column.EQUALIZER){
			new Handler().post(new Runnable(){

				@Override
				public void run() {
					//プレイリストを設定
					if(_mpwpl.getNowPlayingMusic() != null)
						reflectOfView(true);
				}
				
			});
		}
		
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
				playButton.setImageResource(R.drawable.pause);
				playButton.setPadding(0, 0, 0, 0);
			}else{
				//Viewを再生ボタンに
				playButton.setImageResource(R.drawable.play);
				playButton.setPadding(
						playButton.getContext().getResources().getDimensionPixelSize(R.dimen.play_button_left_padding),
						0,
						0,
						0);
			}
		}
		ImageButton loopButton = (ImageButton)playerView.findViewById(R.id.button_repeat);
		if(loopButton != null){
			switch(mpwpl.getLoopState()){
			default:
			case MusicPlayerWithQueue.NOT_LOOP:
				loopButton.setImageResource(R.drawable.not_repeat);
				loopButton.setBackgroundResource(R.drawable.button_circle_white_xml);
				break;
			case MusicPlayerWithQueue.ALL_LOOP:
				loopButton.setImageResource(R.drawable.all_repeat);
				loopButton.setBackgroundResource(R.drawable.button_circle_xml);
				break;
			case MusicPlayerWithQueue.ONE_LOOP:
				loopButton.setImageResource(R.drawable.one_repeat);
				loopButton.setBackgroundResource(R.drawable.button_circle_xml);
			}
		}
		ImageButton shuffleButton = (ImageButton)playerView.findViewById(R.id.button_shuffle);
		if(mpwpl.isShuffle()){
			shuffleButton.setBackgroundResource(R.drawable.button_circle_xml);
			//シャッフルがONの時
		}else{
			//シャッフルがOFFの時
			shuffleButton.setBackgroundResource(R.drawable.button_circle_white_xml);
		}
		
		setPartOfPlayerView(
				getContext(),
				playerView, 
				mpwpl.getNowPlayingMusic(),
				mpwpl);
		//ViewPager の設定
		ViewPager mViewPager = (ViewPager)playerView.findViewById(R.id.player_list_part);
		if(mViewPager != null && mViewPager.getCurrentItem() == Column.EQUALIZER){
			ViewPagerForEqualizerViewCtl.createMusicVisualizer(mViewPager.getContext());
			ViewPagerForEqualizerViewCtl.setIsVisualizer(true);
		}else{
			ViewPagerForEqualizerViewCtl.setIsVisualizer(false);
		}
		
		if(mpwpl.getStatus() == MusicPlayerWithQueue.PLAYING){
			ViewPagerForEqualizerViewCtl.setIsVisualizer(true);
		}else{
			ViewPagerForEqualizerViewCtl.setIsVisualizer(false);
			new Handler().postDelayed(new Runnable(){

				@Override
				public void run() {
					ViewPagerForEqualizerViewCtl.removeMusicVisualizer();
				}
				
			},DELAYTIME);
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
		if(mView == null || music == null) return;
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
		new GetImageTask(
				context,
				context.getResources().getDimensionPixelSize(R.dimen.player_now_jacket_size),
				new GetImageTask.OnGetImageListener() {
					
					@Override
					public void onGetImage(Bitmap image) {
						if(image == null){
							jacket.setImageResource(R.drawable.no_image);
						}else{
							jacket.setImageBitmap(image);
						}
					}
				}).execute(music.getAlbumUri());
		
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
	        Uri imageuri = addImageAsApplication(con,cr,bm);
	        tweetIntent.putExtra(Intent.EXTRA_TEXT, message);
	        tweetIntent.putExtra(Intent.EXTRA_STREAM,imageuri);
	        tweetIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK); 
	        tweetIntent.setType("image/jpeg");
	        con.startActivity(tweetIntent);
	        removePlayerView();
	    } catch (final ActivityNotFoundException e) {
	        Toast.makeText(con,"You don't seem to have twitter installed on this device", Toast.LENGTH_SHORT).show();
	    }
	}
	 
	//twitter投稿時の画像保存
	private static final String APPLICATION_NAME = "MperWithSideProjects";
	private static final Uri IMAGE_URI = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
	private static final String PATH = Environment.getExternalStorageDirectory().toString() + "/" + APPLICATION_NAME;
	private static final String FILE_NAME = "TwitImage.jpg";
	public static Uri addImageAsApplication(Context con,ContentResolver cr, Bitmap bitmap) {
	    long dateTaken = System.currentTimeMillis();
	    ;//String name = createName(dateTaken) + ".jpg";
	    return addImageAsApplication(con,cr, FILE_NAME, dateTaken, PATH, FILE_NAME, bitmap, null);
	}
	 
	//画像ファイル名を生成
	/*
	private static String createName(long dateTaken) {
	    return DateFormat.format("yyyy-MM-dd_kk.mm.ss", dateTaken).toString();
	}*/
	 
	//画像をアルバムに保存しURI取得
	public static Uri addImageAsApplication(
			Context con,
	        ContentResolver cr,
	        String name,
	        long dateTaken,
	        String directory,
	        String filename,
	        Bitmap source,
	        byte[] jpegData
	){
		SharedPreferences sp = con.getSharedPreferences(name, Context.MODE_PRIVATE);
		String oldUri = sp.getString(FILE_NAME, null);
	    OutputStream outputStream = null;
	    String filePath = directory + "/" + filename;
	    try {
	        File dir = new File(directory);
	        if (!dir.exists()) {
	            dir.mkdirs();
	            Log.d(TAG, dir.toString() + " create");
	        }
	        File file = new File(directory, filename);
	        if(file.exists() && oldUri != null){
	        	//ファイルがあるとき
	        	try{
	        		cr.delete(Uri.parse(oldUri), null, null);
	        	}catch(Exception e){
	        		file.delete();
	        	}
	        }

	        if (file.createNewFile()) {
	            outputStream = new FileOutputStream(file);
	            if (source != null) {
	                source.compress(CompressFormat.JPEG, 75, outputStream);
	                Log.d(TAG, dir.toString() + " data is save");
	            } else {
	            	Log.d(TAG, dir.toString() + " data is null");
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
	    Uri uri = cr.insert(IMAGE_URI, values);
	    if(uri != null)
	    	sp.edit().putString(FILE_NAME, uri.toString()).commit();
	    return uri;
	}
	
}
