package com.abs104a.mperwithsideproject.viewctl;


import com.abs104a.mperwithsideproject.R;
import com.abs104a.mperwithsideproject.adapter.MusicViewPagerAdapter;
import com.abs104a.mperwithsideproject.music.MusicPlayerWithQueue;
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
import android.media.AudioManager;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.LinearLayout.LayoutParams;
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
	
	//前回終了時の状態(Viewの幅)
	private static int openViewWidth = 0;
	
	//PlayerView
	private static View playerView = null;
	//ViewPagerのページを記憶する用
	private static int pageCount = 0;
	
	public static int setPageCount(int page){
		return pageCount = page;
	}
	
	public static int getPageCount(){
		return pageCount;
	}
	
	/**
	 * playerViewを取得する．
	 * @return
	 */
	public static View getPlayerView(){
		return playerView;
	}
	
	/**
	 * PlayerViewを消去する．
	 * @param rootView
	 */
	public static void removePlayerView(View rootView){
		//Viewの消去を行う
		if(playerView != null && rootView != null){
			openViewWidth = playerView.getWidth();
			((LinearLayout)rootView).removeView(playerView);
			//キャッシュのClear
			ImageCache.clearCache();
		}else{
			openViewWidth = 0;
		}
		playerView = null;
		
	}
	
	/**
	 * PlayerView
	 *　を生成する（non-OpenAnimation）
	 * @param rootView
	 */
	public static void createPlayerView(Service mService,View rootView){
		if(openViewWidth > 0){
			View mView = createView(mService,rootView);

			mView.getLayoutParams().width = openViewWidth;
			Animation showAnimation = 
					AnimationUtils.loadAnimation(mService, android.R.anim.fade_in);
			//Animationの設定
			mView.startAnimation(showAnimation);
		}
		openViewWidth = 0;
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
		playerView = layoutInflater.inflate(R.layout.player_view, null);
		playerView.setId(PLAYER_VIEW_ID);
		((LinearLayout)rootView).addView(playerView);
		//Action Settings 
		init(mService, playerView,rootView);
		return playerView;
	}
	
	/**
	 * 初期化を行う
	 * @param mService
	 * @param mView
	 * @param rootView 
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
	private static void initButtonOfView(Service mService,View mView, MusicPlayerWithQueue _mpwpl){
		//Viewのボタンに動作をつける
		//再生ボタンの設定
		ImageButton playButton = (ImageButton)mView.findViewById(R.id.button_play);
		//再生ボタンの動作を登録する．
		playButton.setOnClickListener(new PlayButtonOnClickImpl(mView));
		
		//次へのボタンの設定
		ImageButton nextButton = (ImageButton)mView.findViewById(R.id.button_next_seek);
		//次へボタンの動作を登録する．
		nextButton.setOnClickListener(new NextButtonOnClickImpl(mView));
		
		ImageButton backButton = (ImageButton)mView.findViewById(R.id.button_back_seek);
		//前へボタンの動作を登録する．
		backButton.setOnClickListener(new BackButtonOnClickImpl(mView));
		
		ImageButton repeatButton = (ImageButton)mView.findViewById(R.id.button_repeat);
		//リピートボタンの動作を登録する．
		repeatButton.setOnClickListener(new RepeatButtonOnClickImpl(mView));
		
		ImageButton shuffleButton = (ImageButton)mView.findViewById(R.id.button_shuffle);
		//シャッフルボタンの動作を登録する．
		shuffleButton.setOnClickListener(new ShuffleButtonOnClickImpl(mView));
		
		//TODO 設定表示ボタンの設定を登録する．
		//((LinearLayout)mView).addView(ViewPagerForAlbumViewCtl.createView(mService, _mpwpl));
		
		//終了ボタンの設定
		ImageButton exitButton = (ImageButton)mView
				.findViewById(R.id.button_action_exit);
		exitButton.setOnClickListener(new ExitButtonOnClickListenerImpl(mService, MusicUtils.getMusicController(mService)));
		
		ImageButton showListButton = (ImageButton)mView.findViewById(R.id.button_action_show_list);
		//TODO リスト表示ボタンの設定を登録する
		
		ImageButton showSettigsButton = (ImageButton)mView.findViewById(R.id.button_action_show_settings);
		
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
		_mpwpl.setOnPlayCompletedListener(new OnPlayCompletedImpl( mView));
		
		//ViewPager の設定
		ViewPager mViewPager = (ViewPager)mView.findViewById(R.id.player_list_part);
		//Adapterの設定
		mViewPager.setAdapter(new MusicViewPagerAdapter(mService,mView,_mpwpl));
		//ページの設定
		mViewPager.setOnPageChangeListener(new ViewPagerOnPagerChangeImpl(mViewPager));
		mViewPager.setCurrentItem(pageCount);
		
		//TODO プレイリストを設定
		if(_mpwpl.getNowPlayingMusic() != null)
			MusicUtils.reflectOfView(mView,true);
		
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
		if(playerView == null)return;
		//音量の設定
		// AudioManagerを取得する
		final AudioManager am = (AudioManager)playerView.getContext().getSystemService(Context.AUDIO_SERVICE);

		// 現在の音量を取得する
		int musicVolume = am.getStreamVolume(AudioManager.STREAM_MUSIC);

		// ストリームごとの最大音量を取得する
		int musicMaxVolume = am.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
		//音量を調節するシークバー
		final SeekBar volumeBar = (SeekBar)playerView.findViewById(R.id.seekBar_volume);
		volumeBar.setMax(musicMaxVolume);
		volumeBar.setProgress(musicVolume);
		//音量のシークバーが変更された時のリスナ
	}
	
	/**
	 * アニメーション的にViewをオープンするやつ
	 * @param mService
	 * @param rootView
	 */
	public final static void animateOpen(final Service mService,View rootView){
		final Handler mHandler = new Handler();
		final int mWidth;
		if(playerView == null){
			//MusicPlayerViewの作成
			playerView = createView(mService,rootView);
			mWidth = 0;
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

				width += 50;
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
	}
	
	/**
	 * アニメーション的にViewをクローズするやつ
	 * @param mService
	 * @param rootView
	 */
	public final static void animateClose(final Service mService,final View rootView){
		final Handler mHandler = new Handler();
		if(playerView == null){
			//MusicPlayerViewの作成
			playerView = createView(mService,rootView);
		}
		Animation closeAnimation = 
				AnimationUtils
				.loadAnimation(mService, android.R.anim.fade_out);
		//Animationの設定
		playerView.startAnimation(closeAnimation);
		
		final Runnable mRunnable = new Runnable(){
			
			private int width = playerView.getWidth();

			@Override
			public void run() {
				
				//Layout設定
				final LayoutParams params = (LayoutParams) playerView.getLayoutParams();
				//android.util.Log.v("hogebefore", width + " / " + musicPlayerWidth);
				width -= 50;
				if(width > 0){
					//android.util.Log.v("hoge", width + "");
					params.width = Math.max(0, width);
					//Layoutの変更
					playerView.setLayoutParams(params);
					mHandler.post(this);
				}else{
					//Viewの消去を行う
					removePlayerView(rootView);
				}
			}
			
		};
		mHandler.post(mRunnable);
	}
	
}
