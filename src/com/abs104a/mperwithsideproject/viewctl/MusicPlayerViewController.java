package com.abs104a.mperwithsideproject.viewctl;


import java.io.FileNotFoundException;
import java.io.InputStream;

import com.abs104a.mperwithsideproject.R;
import com.abs104a.mperwithsideproject.adapter.MusicViewPagerAdapter;
import com.abs104a.mperwithsideproject.music.Music;
import com.abs104a.mperwithsideproject.music.MusicPlayerWithQueue;
import com.abs104a.mperwithsideproject.utl.DisplayUtils;
import com.abs104a.mperwithsideproject.utl.MusicUtils;
import com.abs104a.mperwithsideproject.viewctl.listener.BackButtonOnClickImpl;
import com.abs104a.mperwithsideproject.viewctl.listener.ExitButtonOnClickListenerImpl;
import com.abs104a.mperwithsideproject.viewctl.listener.MusicSeekBarOnChangeImpl;
import com.abs104a.mperwithsideproject.viewctl.listener.NextButtonOnClickImpl;
import com.abs104a.mperwithsideproject.viewctl.listener.OnPlayCompletedImpl;
import com.abs104a.mperwithsideproject.viewctl.listener.PlayButtonOnClickImpl;
import com.abs104a.mperwithsideproject.viewctl.listener.RepeatButtonOnClickImpl;
import com.abs104a.mperwithsideproject.viewctl.listener.ShuffleButtonOnClickImpl;

import android.app.Service;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
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
	//AnimateOpen時のDelayTime
	public final static int DELAY_TIME = 20;
	
	//UIスレッドのHandler
	private static MusicSeekBarHandler mHandler = null;
	
	/**
	 * PlayerのViewを生成するメソッド
	 * サービスのコンテキストを受けとりViewを生成する．
	 * @param mService
	 * @return　生成したViewGroup
	 */
	public static View createView(Service mService){
		// Viewからインフレータを作成する
		LayoutInflater layoutInflater = LayoutInflater.from(mService);
		// レイアウトファイルから重ね合わせするViewを作成する
		View mView = layoutInflater.inflate(com.abs104a.mperwithsideproject.R.layout.player_view, null);
		//Action Settings 
		init(mService, mView);
		return mView;
	}
	
	/**
	 * 初期化を行う
	 * @param mService
	 * @param mView
	 * @param mpwpl 
	 */
	private final static void init(Service mService,View mView){
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
		//終了ボタンの設定
		ImageButton exitButton = (ImageButton)mView.findViewById(R.id.button_action_exit);
		exitButton.setOnClickListener(new ExitButtonOnClickListenerImpl(mService, _mpwpl));
		
		//再生ボタンの設定
		ImageButton playButton = (ImageButton)mView.findViewById(R.id.button_play);
		//再生ボタンの動作を登録する．
		playButton.setOnClickListener(new PlayButtonOnClickImpl(_mpwpl));
		
		//次へのボタンの設定
		ImageButton nextButton = (ImageButton)mView.findViewById(R.id.button_next_seek);
		//次へボタンの動作を登録する．
		nextButton.setOnClickListener(new NextButtonOnClickImpl(mService,_mpwpl,mView));
		
		ImageButton backButton = (ImageButton)mView.findViewById(R.id.button_back_seek);
		//前へボタンの動作を登録する．
		backButton.setOnClickListener(new BackButtonOnClickImpl(mService,_mpwpl,mView));
		
		ImageButton repeatButton = (ImageButton)mView.findViewById(R.id.button_repeat);
		//リピートボタンの動作を登録する．
		repeatButton.setOnClickListener(new RepeatButtonOnClickImpl(repeatButton,_mpwpl));
		
		ImageButton shuffleButton = (ImageButton)mView.findViewById(R.id.button_shuffle);
		//シャッフルボタンの動作を登録する．
		shuffleButton.setOnClickListener(new ShuffleButtonOnClickImpl(shuffleButton,_mpwpl));
		
		ImageButton showListButton = (ImageButton)mView.findViewById(R.id.button_action_show_list);
		//TODO リスト表示ボタンの設定を登録する
		
		ImageButton showSettigsButton = (ImageButton)mView.findViewById(R.id.button_action_show_settings);
		//TODO 設定表示ボタンの設定を登録する．
		//((LinearLayout)mView).addView(ViewPagerForAlbumViewCtl.createView(mService, _mpwpl));
		
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
		_mpwpl.setOnPlayCompletedListener(new OnPlayCompletedImpl(_mpwpl, mView));
		
		//ViewPager の設定
		ViewPager mViewPager = (ViewPager)mView.findViewById(R.id.player_list_part);
		mViewPager.setAdapter(new MusicViewPagerAdapter(mService,mView,_mpwpl));
		mViewPager.setOnPageChangeListener(new ViewPagerOnPagerChangeImpl(mViewPager));
		
		//TODO プレイリストを設定
		if(_mpwpl.getNowPlayingMusic() != null)
			setPartOfPlayerView(mService, mView, _mpwpl.getNowPlayingMusic(),_mpwpl);
		
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
	 * アニメーション的にViewをオープンするやつ
	 * @param mService
	 * @param rootView
	 */
	public final static void animateOpen(final Service mService,View rootView){
		final View mPlayerView;
		final Handler mHandler = new Handler();
		if(((LinearLayout)rootView).getChildCount() == 2){
			//MusicPlayerViewの作成
			mPlayerView = createView(mService);
			mPlayerView.setId(PLAYER_VIEW_ID);
			((LinearLayout)rootView).addView(mPlayerView);
		}else{
			mPlayerView = 
					((LinearLayout)rootView)
					.findViewById(MusicPlayerViewController.PLAYER_VIEW_ID);
		}
		//Layout設定
		final LayoutParams params = (LayoutParams) mPlayerView.getLayoutParams();
		params.width = 0;
		//Layoutの変更
		mPlayerView.setLayoutParams(params);
		mPlayerView.setVisibility(View.INVISIBLE);
		
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
				final LayoutParams params = (LayoutParams) mPlayerView.getLayoutParams();
				//android.util.Log.v("hogebefore", width + " / " + musicPlayerWidth);
				width += 50;
				if(musicPlayerWidth > width){
					//android.util.Log.v("hoge", width + "");
					params.width = Math.min(musicPlayerWidth, width);
					//Layoutの変更
					mPlayerView.setLayoutParams(params);
					mHandler.postDelayed(this, DELAY_TIME);
				}else{
					mPlayerView.setVisibility(View.VISIBLE);
					Animation showAnimation = 
							AnimationUtils
							.loadAnimation(mService, android.R.anim.fade_in);
					//Animationの設定
					mPlayerView.startAnimation(showAnimation);
				}
			}
			
		};
		mHandler.postDelayed(mRunnable, DELAY_TIME);
	}
	
	/**
	 * アニメーション的にViewをクローズするやつ
	 * @param mService
	 * @param rootView
	 */
	public final static void animateClose(final Service mService,final View rootView){
		final View mPlayerView;
		final Handler mHandler = new Handler();
		if(((LinearLayout)rootView).getChildCount() == 2){
			//MusicPlayerViewの作成
			mPlayerView = createView(mService);
			mPlayerView.setId(PLAYER_VIEW_ID);
			((LinearLayout)rootView).addView(mPlayerView);
		}else{
			mPlayerView = 
					((LinearLayout)rootView)
					.findViewById(MusicPlayerViewController.PLAYER_VIEW_ID);
		}
		
		Animation closeAnimation = 
				AnimationUtils
				.loadAnimation(mService, android.R.anim.fade_out);
		//Animationの設定
		mPlayerView.startAnimation(closeAnimation);
		
		final Runnable mRunnable = new Runnable(){
			
			private int width = mPlayerView.getWidth();

			@Override
			public void run() {
				
				//Layout設定
				final LayoutParams params = (LayoutParams) mPlayerView.getLayoutParams();
				//android.util.Log.v("hogebefore", width + " / " + musicPlayerWidth);
				width -= 50;
				if(width > 0){
					//android.util.Log.v("hoge", width + "");
					params.width = Math.max(0, width);
					//Layoutの変更
					mPlayerView.setLayoutParams(params);
					mHandler.postDelayed(this, DELAY_TIME);
				}else{
					//Viewの消去を行う
					((LinearLayout)rootView).removeView(mPlayerView);
				}
			}
			
		};
		mHandler.postDelayed(mRunnable, DELAY_TIME);
	}
	
	
	/**
	 * MusicPlayerView中の再生曲情報を表示するViewへのデータセットを行う
	 * @param context
	 * @param mView
	 * @param music
	 */
	public static final void setPartOfPlayerView(final Context context,final View mView,final Music music,final MusicPlayerWithQueue mpwpl){
		//一定時間おきの動作設定
		if(mHandler != null)
			mHandler.stopHandler();
		mHandler = null;
		
		//タイトルView
		final TextView title = (TextView)mView.findViewById(R.id.textView_now_music_name);
		title.setText(music.getTitle());
		//アーティスト
		final TextView artist = (TextView)mView.findViewById(R.id.textView_now_artist_name);
		artist.setText(music.getArtist());
		//アルバム名
		final TextView album = (TextView)mView.findViewById(R.id.textView_now_album);
		album.setText(music.getAlbum());
		//曲時間
		final TextView maxTime = (TextView)mView.findViewById(R.id.textView_now_max_time);
		maxTime.setText(DisplayUtils.long2TimeString(music.getDuration()));
		//現在の再生時間
		final TextView currentTime = (TextView)mView.findViewById(R.id.TextView_now_current_time);
		currentTime.setText("0:00");
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
		
		//曲のシークバー
		final SeekBar seekbar = (SeekBar)mView.findViewById(R.id.seekBar_now_music_seek);
		seekbar.setMax((int)(music.getDuration()));
		seekbar.setProgress(0);
		
		seekbar.setOnSeekBarChangeListener(new MusicSeekBarOnChangeImpl(currentTime,mpwpl));
		
		final ImageButton playButton = (ImageButton)mView.findViewById(R.id.button_play);
		
		if(mpwpl.getStatus() == MusicPlayerWithQueue.PLAYING){
			//Viewを一時停止ボタンに
			playButton.setBackgroundResource(android.R.drawable.ic_media_pause);
		}else{
			playButton.setBackgroundResource(android.R.drawable.ic_media_play);
		}
		
		//ListViewの変更も行う
		ViewPager viewPager = (ViewPager)mView.findViewById(R.id.player_list_part);
		if(viewPager != null)
			((MusicViewPagerAdapter)viewPager.getAdapter()).notifitionDataSetChagedForQueueView();
		
		
		mHandler = new MusicSeekBarHandler(currentTime,seekbar,mpwpl);
		mHandler.sleep(0);
		
	}
	
}
