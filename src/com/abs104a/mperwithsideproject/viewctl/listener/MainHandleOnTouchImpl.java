package com.abs104a.mperwithsideproject.viewctl.listener;

import com.abs104a.mperwithsideproject.R;
import com.abs104a.mperwithsideproject.viewctl.MusicViewCtl;

import android.app.Service;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;

/**
 * メインビューのハンドル（取っ手）がタップされた時に呼ばれるリスナ
 * @author Kouki
 *
 */
public final class MainHandleOnTouchImpl implements OnTouchListener {

	private static final long DELAYTIME = 3000;
	//サービスのコンテキスト
	private final Service mService;
	private boolean useFlag = false;

	/**
	 * インスタンスの生成
	 * @param mService　サービスのコンテキスト
	 */
	public MainHandleOnTouchImpl(Service mService) {
		this.mService = mService;
	}

	/**
	 * ハンドルViewがタップされた時
	 */
	@Override
	public boolean onTouch(View v, MotionEvent event) {	
		final View rootView = (View) v.getParent();
		//ViewSize
		final int musicPlayerWidth = 
				mService
				.getResources()
				.getDimensionPixelSize(R.dimen.player_view_width)
				+
				2 * mService.getResources().getDimensionPixelSize(R.dimen.player_view_padding);

		//タップ動作によって動作を設定する．
		switch(event.getAction()){
		case MotionEvent.ACTION_UP:
		case MotionEvent.ACTION_OUTSIDE:
			useFlag = false;
			break;
		case MotionEvent.ACTION_DOWN:	//画面を押した時
		case MotionEvent.ACTION_MOVE:	//画面上を動いている時
			if(useFlag)return true;
			if(rootView.getWidth() < musicPlayerWidth / 2){
				MusicViewCtl.createPlayerView(mService, rootView);
			}else{
				MusicViewCtl.removePlayerView();
			}
			useFlag = true;
			new Handler().postDelayed(new Runnable(){

				@Override
				public void run() {
					useFlag = false;
				}
				
			}, DELAYTIME);

		}
		
		return true;
	}

}
