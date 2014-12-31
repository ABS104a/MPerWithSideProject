package com.abs104a.mperwithsideproject.viewctl;


import com.abs104a.mperwithsideproject.R;
import com.abs104a.mperwithsideproject.adapter.MusicViewPagerAdapter;
import com.abs104a.mperwithsideproject.music.MusicPlayerWithQueue;
import com.abs104a.mperwithsideproject.settings.SettingsActivity;
import com.abs104a.mperwithsideproject.utl.ImageCache;
import com.abs104a.mperwithsideproject.utl.MusicUtils;
import com.abs104a.mperwithsideproject.viewctl.listener.BackButtonOnClickImpl;
import com.abs104a.mperwithsideproject.viewctl.listener.ExitButtonOnClickListenerImpl;
import com.abs104a.mperwithsideproject.viewctl.listener.NextButtonOnClickImpl;
import com.abs104a.mperwithsideproject.viewctl.listener.OnPlayCompletedImpl;
import com.abs104a.mperwithsideproject.viewctl.listener.PlayButtonOnClickImpl;
import com.abs104a.mperwithsideproject.viewctl.listener.RepeatButtonOnClickImpl;
import com.abs104a.mperwithsideproject.viewctl.listener.ShuffleButtonOnClickImpl;
import com.abs104a.mperwithsideproject.viewctl.listener.ViewPagerOnPagerChangeImpl;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;

/**
 * プレイヤーメインViewの生成と設定を行うクラス
 * 
 * 
 * @author Kouki
 *
 */
public final class MusicPlayerViewController {

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
	
	
	/**
	 * PlayerViewを消去する．
	 * @param PlayerView
	 */
	public static void removePlayerView(final View PlayerView){
		//Viewの消去を行う
		if(MusicUtils.getPlayerView() != null && PlayerView != null){
			final ImageButton handle = (ImageButton) PlayerView.findViewById(R.id.imageButton_handle);
			handle.setVisibility(View.INVISIBLE);
			Animation closeAnimation = 
					AnimationUtils.loadAnimation(PlayerView.getContext(), android.R.anim.fade_out);
			closeAnimation.setAnimationListener(new AnimationListener(){

				@Override
				public void onAnimationEnd(Animation animation) {
					((LinearLayout)PlayerView).removeView(MusicUtils.getPlayerView());
					//キャッシュのClear
					ImageCache.clearCache();
					MusicUtils.setPlayerView(null);
					final Handler mHandler = new Handler();
					mHandler.post(new Runnable(){

						@Override
						public void run() {
							handle.setVisibility(View.VISIBLE);
							handle.startAnimation(
									AnimationUtils
									.loadAnimation(PlayerView.getContext(), android.R.anim.fade_in));
						}
						
					});
					
				}

				@Override
				public void onAnimationRepeat(Animation animation) {}

				@Override
				public void onAnimationStart(Animation animation) {}
				
			});
			MusicUtils.getPlayerView().startAnimation(closeAnimation);
			
			//プレイリストの書き込みを行う
			ViewPagerForPlayListViewCtl.writePlayList(MusicUtils.getPlayerView().getContext());
			
		}
	}
	
	/**
	 * PlayerView
	 *　を生成する（non-OpenAnimation）
	 * @param PlayerView
	 */
	public static void createPlayerView(final Service mService,View PlayerView){
		if(MusicUtils.getPlayerView() == null){
			View mView = createView(mService,PlayerView);
			final ImageButton handle = (ImageButton) PlayerView.findViewById(R.id.imageButton_handle);
			handle.setVisibility(View.INVISIBLE);
			Animation showAnimation = 
					AnimationUtils.loadAnimation(mService, android.R.anim.fade_in);
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
	private static View createView(Service mService,View PlayerView){
		// Viewからインフレータを作成する
		LayoutInflater layoutInflater = LayoutInflater.from(mService);
		// レイアウトファイルから重ね合わせするViewを作成する
		MusicUtils.setPlayerView(layoutInflater.inflate(R.layout.player_view, null));
		MusicUtils.getPlayerView().setId(PLAYER_VIEW_ID);
		((LinearLayout)PlayerView).addView(MusicUtils.getPlayerView());
		//Action Settings 
		init(mService, MusicUtils.getPlayerView(),PlayerView);
		return MusicUtils.getPlayerView();
	}
	
	/**
	 * 初期化を行う
	 * @param mService
	 * @param mView
	 * @param PlayerView 
	 * @param mpwpl 
	 */
	private final static void init(Service mService,View mView, View PlayerView){
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
	private static void initButtonOfView(final Service mService,View mView, MusicPlayerWithQueue _mpwpl){
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
		
		//TODO 設定表示ボタンの設定を登録する．
		//((LinearLayout)mView).addView(ViewPagerForAlbumViewCtl.createView(mService, _mpwpl));
		
		//終了ボタンの設定
		ImageButton exitButton = (ImageButton)mView
				.findViewById(R.id.button_action_exit);
		exitButton.setOnClickListener(new ExitButtonOnClickListenerImpl(mService, MusicUtils.getMusicController(mService)));
		
		ImageButton showListButton = (ImageButton)mView.findViewById(R.id.button_action_show_list);
		//TODO リスト表示ボタンの設定を登録する
		
		//設定ボタンを押した時の動作
		ImageButton showSettingsButton = (ImageButton)mView.findViewById(R.id.button_action_show_settings);
		showSettingsButton.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(mService,SettingsActivity.class);
				mService.startActivity(intent);
			}
			
		});
		
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
		mViewPager.setAdapter(new MusicViewPagerAdapter(mService,mView,_mpwpl));
		//ページの設定
		mViewPager.setOnPageChangeListener(new ViewPagerOnPagerChangeImpl(mViewPager));
		mViewPager.setCurrentItem(pageCount);
		
		//TODO プレイリストを設定
		if(_mpwpl.getNowPlayingMusic() != null)
			MusicUtils.reflectOfView(true);
		
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
		if(MusicUtils.getPlayerView() == null)return;
		//音量の設定
		// AudioManagerを取得する
		final AudioManager am = (AudioManager)MusicUtils.getPlayerView().getContext().getSystemService(Context.AUDIO_SERVICE);

		// 現在の音量を取得する
		int musicVolume = am.getStreamVolume(AudioManager.STREAM_MUSIC);

		// ストリームごとの最大音量を取得する
		int musicMaxVolume = am.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
		//音量を調節するシークバー
		final SeekBar volumeBar = (SeekBar)MusicUtils.getPlayerView().findViewById(R.id.seekBar_volume);
		volumeBar.setMax(musicMaxVolume);
		volumeBar.setProgress(musicVolume);
		//音量のシークバーが変更された時のリスナ
	}
	
	
	
	/**
	 * アニメーション的にViewをオープンするやつ
	 * @param mService
	 * @param PlayerView
	 */
	/*
	public final static void animateOpen(final Service mService,View PlayerView){
		final Handler mHandler = new Handler();
		final int mWidth;
		if(playerView == null){
			//MusicPlayerViewの作成
			mWidth = openViewWidth;
			playerView = createView(mService,PlayerView);
		}else{
			mWidth = playerView.getWidth();
		}
		//Layout設定
		final LayoutParams params = (LayoutParams) playerView.getLayoutParams();
		params.width = mWidth;
		//Layoutの変更
		playerView.setLayoutParams(params);
		playerView.setVisibility(View.INVISIBLE);
		
		final Runnable mRunnable = new Runnable(){
			
			private int width = 0;

			@Override
			public void run() {
				final int musicPlayerWidth = 
						mService
						.getResources()
						.getDimensionPixelSize(R.dimen.player_view_width) 
						+ 
						2 * mService.getResources()
						.getDimensionPixelSize(R.dimen.activity_vertical_margin);
				
				//Layout設定
				final LayoutParams params = (LayoutParams) playerView.getLayoutParams();

				width += ANIMATE_OPEN;
				if(musicPlayerWidth > width){
					params.width = Math.min(musicPlayerWidth, width);
					//Layoutの変更
					playerView.setLayoutParams(params);
					mHandler.post(this);
				}else{
					playerView.setVisibility(View.VISIBLE);
					Animation showAnimation = 
							AnimationUtils
							.loadAnimation(mService, android.R.anim.fade_in);
					//Animationの設定
					playerView.startAnimation(showAnimation);
				}
			}
			
		};
		mHandler.post(mRunnable);
	}*/
	
	/**
	 * アニメーション的にViewをクローズするやつ
	 * @param mService
	 * @param PlayerView
	 *//*
	public final static void animateClose(final Service mService,final View PlayerView){
		final Handler mHandler = new Handler();
		if(playerView == null){
			//MusicPlayerViewの作成
			playerView = createView(mService,PlayerView);
		}
		//Animationの設定
		playerView.setVisibility(View.INVISIBLE);
		
		final Runnable mRunnable = new Runnable(){
			
			private int width = playerView.getWidth();

			@Override
			public void run() {
				
				//Layout設定
				final LayoutParams params = (LayoutParams) playerView.getLayoutParams();
				//android.util.Log.v("hogebefore", width + " / " + musicPlayerWidth);
				width -= ANIMATE_OPEN;
				if(width > 0){
					//android.util.Log.v("hoge", width + "");
					params.width = Math.max(0, width);
					//Layoutの変更
					playerView.setLayoutParams(params);
					mHandler.post(this);
				}else{
					//Viewの消去を行う
					removePlayerView(PlayerView);
				}
			}
			
		};
		mHandler.post(mRunnable);
	}*/
	
}
