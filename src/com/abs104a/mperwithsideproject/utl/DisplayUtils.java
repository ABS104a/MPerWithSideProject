package com.abs104a.mperwithsideproject.utl;

import java.text.SimpleDateFormat;
import java.util.Date;
import com.abs104a.mperwithsideproject.R;
import com.abs104a.mperwithsideproject.music.Music;
import com.abs104a.mperwithsideproject.music.MusicPlayerWithQueue;
import com.abs104a.mperwithsideproject.viewctl.MusicSeekBarHandler;
import com.abs104a.mperwithsideproject.viewctl.listener.MusicSeekBarOnChangeImpl;

import android.content.Context;
import android.graphics.Point;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

/**
 * ディスプレイに関するユーティリティクラス
 * @author Kouki
 *
 */
public class DisplayUtils {
	
	private static MusicSeekBarHandler mHandler = null;

	/**
	 * 画面の幅を取得するクラス
	 * @param mContext アプリケーションのコンテキスト
	 * @return 画面幅
	 */
	public static float getDisplayWidth(Context mContext){
		  //画面サイズ取得の準備
        WindowManager wm = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
        Display disp = wm.getDefaultDisplay();
        final float width;
        // AndroidのAPIレベルによって画面サイズ取得方法が異なるので条件分岐
        if (Integer.valueOf(android.os.Build.VERSION.SDK_INT) < 13) {
        	//APILevel 12
            width = disp.getWidth();
        } else {
        	//APILevel 13以上
            Point size = new Point();
            disp.getSize(size);
            width = size.x;
        }
        return width;
	}
	
	/**
	 * Longでの時間を，分:秒に変換する
	 * @param time
	 * @return 変換されたString
	 */
	public static String long2TimeString(long time){
		SimpleDateFormat DFYS = new SimpleDateFormat("mm:ss");
		Date date = new Date(time);
		return DFYS.format(date);
	}
	
	/**
	 * MusicPlayerView中の再生曲情報を表示するViewへのデータセットを行う
	 * @param context
	 * @param mView
	 * @param music
	 */
	public static void setPartOfPlayerView(final Context context,final View mView,final Music music,final MusicPlayerWithQueue mpwpl){
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
		jacket.setImageURI(music.getAlbumUri());
		
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
		
		
		mHandler = new MusicSeekBarHandler(currentTime,seekbar,mpwpl);
		mHandler.sleep(0);
		
	}
}
