package com.abs104a.mperwithsideproject.utl;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;

import com.abs104a.mperwithsideproject.R;
import com.abs104a.mperwithsideproject.adapter.MusicViewPagerAdapter;
import com.abs104a.mperwithsideproject.music.Album;
import com.abs104a.mperwithsideproject.music.Music;
import com.abs104a.mperwithsideproject.music.MusicPlayerWithQueue;
import com.abs104a.mperwithsideproject.viewctl.MusicSeekBarHandler;
import com.abs104a.mperwithsideproject.viewctl.listener.MusicSeekBarOnChangeImpl;

import android.app.Service;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.audiofx.Visualizer;
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
	
	private static Visualizer mVisualizer = null;
	
	//UIスレッドのHandler
	private static MusicSeekBarHandler mHandler = null;
	
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
	 * @param rootView
	 * @param mpwpl
	 */
	public static final void playOrPauseWithView(View rootView){
		//再生動作を行う
		try {
			MusicPlayerWithQueue mpwpl = getMusicController(rootView.getContext());
			mpwpl.playStartAndPause();
			reflectOfView(rootView);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 再生と停止を行う．またViewへの反映も同時に行う
	 * @param rootView
	 * @param mpwpl
	 */
	public static final void playWithView(View rootView){
		//再生動作を行う
		try {
			MusicPlayerWithQueue mpwpl = getMusicController(rootView.getContext());
			if(mpwpl.getStatus() != MusicPlayerWithQueue.PLAYING){
				mpwpl.playStartAndPause();
				reflectOfView(rootView);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 再生と停止を行う．またViewへの反映も同時に行う
	 * @param rootView
	 * @param mpwpl
	 */
	public static final void pauseWithView(View rootView){
		//再生動作を行う
		try {
			MusicPlayerWithQueue mpwpl = getMusicController(rootView.getContext());
			if(mpwpl.getStatus() == MusicPlayerWithQueue.PLAYING){
				mpwpl.playStartAndPause();
				reflectOfView(rootView);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 再生の停止を行う．
	 * @param rootView
	 * @param mpwpl
	 */
	public static final void stopWithView(View rootView){
		try{
			MusicPlayerWithQueue mpwpl = getMusicController(rootView.getContext());
			mpwpl.playStop();
			reflectOfView(rootView);
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	/**
	 * 次の曲へ移動する．
	 * @param rootView
	 */
	public static final void playNextWithView(View rootView){
		try{
			MusicPlayerWithQueue mpwpl = getMusicController(rootView.getContext());
			mpwpl.playNext();
			reflectOfView(rootView);
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	/**
	 * 前の曲に戻る．
	 * @param rootView
	 */
	public static final void playBackWithView(View rootView){
		try{
			MusicPlayerWithQueue mpwpl = getMusicController(rootView.getContext());
			mpwpl.playBack();
			reflectOfView(rootView);
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	/**
	 * Repeat状態の変化
	 * @param rootView
	 */
	public static final void changeRepeatState(View rootView){
		MusicPlayerWithQueue mpwpl = getMusicController(rootView.getContext());
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
		ImageButton repeatButton = (ImageButton)rootView.findViewById(R.id.button_repeat);
		
		final String message;
		switch(loopState){
		default:
		case MusicPlayerWithQueue.NOT_LOOP:
			message = rootView.getContext().getString(R.string.play_loop_state_notloop);
			break;
		case MusicPlayerWithQueue.ALL_LOOP:
			message = rootView.getContext().getString(R.string.play_loop_state_allloop);
			break;
		case MusicPlayerWithQueue.ONE_LOOP:
			message = rootView.getContext().getString(R.string.play_loop_state_oneloop);
		}
		Toast.makeText(rootView.getContext(), message, Toast.LENGTH_SHORT).show();
	}
	
	/**
	 * シャッフルの状態を変更する
	 * Viewの変更を伴う
	 * @param rootView
	 */
	public static final void changeShuffleState(View rootView){
		MusicPlayerWithQueue mpwpl = getMusicController(rootView.getContext());
		boolean isShuffle = mpwpl.setShuffle(!mpwpl.isShuffle());
		if(isShuffle){
			Toast.makeText(rootView.getContext(), R.string.shuffle_on, Toast.LENGTH_SHORT).show();
			//シャッフルがONの時
			//Viewへの反映
		}else{
			//シャッフルがOFFの時
			Toast.makeText(rootView.getContext(), R.string.shuffle_off, Toast.LENGTH_SHORT).show();
		}
		//TODO　動作を登録する．
		ImageButton shuffleButton = (ImageButton)rootView.findViewById(R.id.button_shuffle);
	}
	
	/**
	 * PlayerViewへ変更を反映させる．
	 * @param rootView
	 * @param mpwpl
	 */
	public static final void reflectOfView(View rootView){
		if(rootView == null)return; //ViewがNullの時は反映しない．
		MusicPlayerWithQueue mpwpl = getMusicController(rootView.getContext());
		//再生ボタンの設定
		ImageButton playButton = (ImageButton)rootView.findViewById(R.id.button_play);
		if(playButton != null){
			if(mpwpl.getStatus() == MusicPlayerWithQueue.PLAYING){
				//Viewを一時停止ボタンに
				playButton.setBackgroundResource(android.R.drawable.ic_media_pause);
			}else{
				//Viewを再生ボタンに
				playButton.setBackgroundResource(android.R.drawable.ic_media_play);
			}
		}
		setPartOfPlayerView(
				rootView.getContext(),
				rootView, 
				mpwpl.getNowPlayingMusic(),
				mpwpl);
		//ViewPager の設定
		ViewPager mViewPager = (ViewPager)rootView.findViewById(R.id.player_list_part);
		//Viewへの反映
		if(mViewPager != null)
			((MusicViewPagerAdapter)mViewPager.getAdapter()).notifitionDataSetChagedForQueueView();
	}
	
	/**
	 * MusicPlayerView中の再生曲情報を表示するViewへのデータセットを行う
	 * @param context
	 * @param mView
	 * @param music
	 */
	private static final void setPartOfPlayerView(final Context context,final View mView,final Music music,final MusicPlayerWithQueue mpwpl){
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
		album.setEllipsize(TextUtils.TruncateAt.MARQUEE);
		album.setSingleLine(true);
		album.setMarqueeRepeatLimit(5);
		album.setSelected(true);
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
	
	/**
	 * Visualizerを作成する．
	 * @param context
	 * @return Visualizer
	 */
	public static final Visualizer createMusicVisualizer(final Context context){
		final MusicPlayerWithQueue mpwpl = getMusicController(context);
		//再生している曲がない場合はViewを作成しない
		if(mpwpl.getMediaPlayerSessionId() == -1)return null;
		final Visualizer mVisualizer = new Visualizer(mpwpl.getMediaPlayerSessionId());
		mVisualizer.setEnabled(false);
		
		mVisualizer.setCaptureSize(Visualizer.getCaptureSizeRange()[1]);
		
		//キャプチャしたデータを定期的に取得するリスナーを設定
        mVisualizer.setDataCaptureListener(new Visualizer.OnDataCaptureListener() {
            //Wave形式のキャプチャーデータ
            public void onWaveFormDataCapture(Visualizer visualizer, byte[] bytes,
                    int samplingRate) {
                //mVisualizerView.updateVisualizer(bytes);
            	android.util.Log.v("Visualizer","setVisualizer");
            }
 
            //高速フーリエ変換のキャプチャーデータ
            public void onFftDataCapture(Visualizer visualizer, byte[] bytes,
                    int samplingRate) {
            }
        },
        Visualizer.getMaxCaptureRate() / 2, //キャプチャーデータの取得レート（ミリヘルツ）
        true,//これがTrueだとonWaveFormDataCaptureにとんでくる
        false);//これがTrueだとonFftDataCaptureにとんでくる
        mVisualizer.setEnabled(true);
        MusicUtils.mVisualizer = mVisualizer;
		return mVisualizer;
	}
	
	/**
	 * Visualizerを解放する
	 */
	public static void removeMusicVisualizer(){
		if(mVisualizer != null){
			mVisualizer.release();
			mVisualizer = null;
		}
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
