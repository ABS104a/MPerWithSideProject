package com.abs104a.mperwithsideproject.viewctl;

import com.abs104a.mperwithsideproject.music.MusicPlayerWithQueue;
import com.abs104a.mperwithsideproject.utl.DisplayUtils;

import android.os.Handler;
import android.os.Message;
import android.widget.SeekBar;
import android.widget.TextView;

public class MusicSeekBarHandler extends Handler {

	private final TextView durtation;
	private final SeekBar seekbar;
	private final MusicPlayerWithQueue mpwpl;

	public MusicSeekBarHandler(TextView durtation, SeekBar seekbar,
			MusicPlayerWithQueue mpwpl) {
		this.durtation = durtation;
		this.seekbar = seekbar;
		this.mpwpl = mpwpl;
		android.util.Log.v("handlar","getInstance");
	}

	/* (非 Javadoc)
	 * @see android.os.Handler#handleMessage(android.os.Message)
	 */
	@Override
	public void handleMessage(Message msg) {
		try{
			//シークバーの状態を変更させる（曲の進捗にあわせる）
			durtation.setText(DisplayUtils.long2TimeString(mpwpl.getCurrentTime()));
			seekbar.setProgress(mpwpl.getCurrentTime());
			sleep(100); 
		}catch(NullPointerException e){
			android.util.Log.e("SeekbarHandler", "View is null");
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
