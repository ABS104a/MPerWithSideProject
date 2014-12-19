package com.abs104a.mperwithsideproject.viewctl.listener;

import com.abs104a.mperwithsideproject.R;
import com.abs104a.mperwithsideproject.utl.DisplayUtils;
import com.abs104a.mperwithsideproject.viewctl.MusicPlayerViewController;

import android.app.Service;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;

/**
 * メインビューのハンドル（取っ手）がタップされた時に呼ばれるリスナ
 * @author Kouki
 *
 */
public final class MainHandleOnTouchImpl implements OnTouchListener {

	
	
	//サービスのコンテキスト
	private final Service mService;
	//画面幅
	private final int screenWidth;
	private float downPos = 0;

	/**
	 * インスタンスの生成
	 * @param mService　サービスのコンテキスト
	 */
	public MainHandleOnTouchImpl(Service mService) {
		this.mService = mService;
		screenWidth = (int) DisplayUtils.getDisplayWidth(mService);
	}

	/**
	 * ハンドルViewがタップされた時
	 */
	@Override
	public boolean onTouch(View v, MotionEvent event) {
		//タップ座標
		final int rawX = (int) event.getRawX();
		final View rootView = (View) v.getParent();
		//ViewSize
		final int musicPlayerWidth = 
				mService
				.getResources()
				.getDimensionPixelSize(R.dimen.player_view_width) 
				+ 
				2 * mService.getResources()
				.getDimensionPixelSize(R.dimen.activity_vertical_margin);
		
		View mPlayerView = null;
		//タップ動作によって動作を設定する．
		switch(event.getAction()){
		case MotionEvent.ACTION_UP:
			if(rootView.getWidth() < musicPlayerWidth / 3){
				if(downPos < musicPlayerWidth / 3)
					MusicPlayerViewController.animateOpen(mService, rootView);
				else
					MusicPlayerViewController.animateClose(mService, rootView);
			}else if(rootView.getWidth() > (musicPlayerWidth / 3) * 2){
				if(downPos > (musicPlayerWidth / 3) * 2)
					MusicPlayerViewController.animateClose(mService, rootView);
				else
					MusicPlayerViewController.animateOpen(mService, rootView);
			}
			break;
		case MotionEvent.ACTION_DOWN:	//画面を押した時
			downPos  = screenWidth - event.getRawX();
		case MotionEvent.ACTION_MOVE:	//画面上を動いている時
			if(((LinearLayout)rootView).getChildCount() == 2){
				//MusicPlayerViewの作成
				MusicPlayerViewController.createPlayerView(mService, rootView);
			}else{
				mPlayerView = 
						((LinearLayout)rootView)
						.findViewById(MusicPlayerViewController.PLAYER_VIEW_ID);
			}
		}
		
		//Playerが取得出来た時
		if(mPlayerView != null){
			//Viewが端っこまで移動した時
			if(rawX >= screenWidth - v.getWidth())
			{
				//Viewの消去を行う
				MusicPlayerViewController.removePlayerView(rootView);
			}else{
				//Layout設定
				final LayoutParams params = (LayoutParams) mPlayerView.getLayoutParams();
				params.width = Math.min(musicPlayerWidth, screenWidth - rawX);
				//Layoutの変更
				mPlayerView.setLayoutParams(params);
			}
		}
		
		return true;
	}

}