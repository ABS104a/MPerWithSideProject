package com.abs104a.mperwithsideproject.viewctl;

import com.abs104a.mperwithsideproject.music.MusicPlayerWithQueue;
import com.abs104a.mperwithsideproject.utl.DisplayUtils;
import com.abs104a.mperwithsideproject.utl.MusicUtils;

import android.os.Handler;
import android.os.Message;
import android.widget.SeekBar;
import android.widget.TextView;

/**
 * 音楽進捗とシークバーの状態をマッピングさせる
 * @author Kouki
 *
 */
public class MusicSeekBarHandler extends Handler {

	//曲の秒数を表示するTextView
	private final TextView durtation;
	//曲の進捗を示すSeekBar
	private final SeekBar seekbar;

	public MusicSeekBarHandler(TextView durtation, SeekBar seekbar,
			MusicPlayerWithQueue mpwpl) {
		this.durtation = durtation;
		this.seekbar = seekbar;
		android.util.Log.v("handlar","getInstance");
	}

	/* (非 Javadoc)
	 * @see android.os.Handler#handleMessage(android.os.Message)
	 */
	@Override
	public void handleMessage(Message msg) {
		try{
			MusicPlayerWithQueue mpwpl = MusicUtils.getMusicController(durtation.getContext());
			//シークバーの状態を変更させる（曲の進捗にあわせる）
			durtation.setText(DisplayUtils.long2TimeString(mpwpl.getCurrentTime()));
			seekbar.setProgress(mpwpl.getCurrentTime());
			sleep(1000); 
		}catch(NullPointerException e){
			android.util.Log.e("SeekbarHandler", "View is null");
			stopHandler();
		}

	}

	/**
	 * Sleepメソッド
	 * @param delayMills
	 */
    public void sleep(long delayMills) {
        //使用済みメッセージの削除
        removeMessages(0);
        sendMessageDelayed(obtainMessage(0),delayMills); 
    }
    
    /**
     * Stopメソッド
     */
    public void stopHandler(){
    	removeMessages(0);
    }

	/* (非 Javadoc)
	 * @see java.lang.Object#finalize()
	 */
	@Override
	protected void finalize() throws Throwable {
		android.util.Log.v("handlar","finalize");
		super.finalize();
	}
}
