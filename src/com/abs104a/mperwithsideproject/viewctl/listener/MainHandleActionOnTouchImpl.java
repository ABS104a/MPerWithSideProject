package com.abs104a.mperwithsideproject.viewctl.listener;

import com.abs104a.mperwithsideproject.R;
import com.abs104a.mperwithsideproject.utl.DisplayUtils;
import com.abs104a.mperwithsideproject.viewctl.MusicPlayerViewController;

import android.app.Service;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;

/**
 * メインビューのハンドル（取っ手）がタップされた時に呼ばれるリスナ
 * @author Kouki
 *
 */
public final class MainHandleActionOnTouchImpl implements OnTouchListener {

	//PlayerViewのID
	public final static int PLAYER_VIEW_ID = 12;
	
	//サービスのコンテキスト
	private final Service mService;
	//画面幅
	private int screenWidth;

	/**
	 * インスタンスの生成
	 * @param mService　サービスのコンテキスト
	 */
	public MainHandleActionOnTouchImpl(Service mService) {
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
		View mPlayerView = null;
		//タップ動作によって動作を設定する．
		switch(event.getAction()){
		case MotionEvent.ACTION_DOWN:	//画面を押した時
		case MotionEvent.ACTION_MOVE:	//画面上を動いている時
			if(((LinearLayout)v.getParent()).getChildCount() == 2){
				//MusicPlayerViewの作成
				mPlayerView = MusicPlayerViewController
						.createView(mService);
				mPlayerView.setId(PLAYER_VIEW_ID);
				((LinearLayout)v.getParent()).addView(mPlayerView);

				/*
				//表示するためのアニメーションを作成
				Animation showAnimation = 
						AnimationUtils
						.loadAnimation(mService, android.R.anim.fade_in);
				//Animationの設定
				mPlayerView.startAnimation(showAnimation);*/
			}else{
				mPlayerView = 
						((LinearLayout)v.getParent())
						.findViewById(PLAYER_VIEW_ID);
			}
		}
		
		//Playerが取得出来た時
		if(mPlayerView != null){
			//Viewが端っこまで移動した時
			if(rawX >= screenWidth - v.getWidth())
			{
				//Viewの消去を行う
				((LinearLayout)v.getParent()).removeView(mPlayerView);
			}else{
				final int musicPlayerWidth = 
						mService
						.getResources()
						.getDimensionPixelSize(R.dimen.player_view_width) 
						+ 
						2 * mService.getResources()
						.getDimensionPixelSize(R.dimen.activity_vertical_margin);
				
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
