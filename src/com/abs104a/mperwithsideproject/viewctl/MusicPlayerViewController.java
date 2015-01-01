package com.abs104a.mperwithsideproject.viewctl;


import com.abs104a.mperwithsideproject.MainService;
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
	public static void removePlayerView(final View rootView){
		//Viewの消去を行う
		if(MusicUtils.getPlayerView() != null && rootView != null){
			final ImageButton handle = (ImageButton) rootView.findViewById(R.id.imageButton_handle);
			handle.setVisibility(View.INVISIBLE);
			Animation closeAnimation = 
					AnimationUtils.loadAnimation(rootView.getContext(), R.anim.left_to_right_out);
			closeAnimation.setAnimationListener(new AnimationListener(){

				@Override
				public void onAnimationEnd(Animation animation) {
					((LinearLayout)rootView).removeView(MusicUtils.getPlayerView());
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
									.loadAnimation(rootView.getContext(), android.R.anim.fade_in));
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
	public static void createPlayerView(final Service mService,View rootView){
		if(MusicUtils.getPlayerView() == null){
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
		MusicUtils.setPlayerView(layoutInflater.inflate(R.layout.player_view, null));
		MusicUtils.getPlayerView().setId(PLAYER_VIEW_ID);
		((LinearLayout)rootView).addView(MusicUtils.getPlayerView());
		//Action Settings 
		init(mService, MusicUtils.getPlayerView(),rootView);
		return MusicUtils.getPlayerView();
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
				intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK); 
				mService.startActivity(intent);
				removePlayerView(MainService.getRootView());
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
	
}
