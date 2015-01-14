package com.abs104a.mperwithsideproject.viewctl;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Context;
import android.graphics.PixelFormat;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.LinearLayout;

import com.abs104a.mperwithsideproject.R;
import com.abs104a.mperwithsideproject.music.MusicPlayerWithQueue;
import com.abs104a.mperwithsideproject.settings.Settings;
import com.abs104a.mperwithsideproject.utl.ImageCache;
import com.abs104a.mperwithsideproject.utl.MusicUtils;
import com.abs104a.mperwithsideproject.viewctl.listener.MainHandleOnTouchImpl;

/**
 * メインビューの生成と設定を行うクラス
 * @author Kouki
 *
 */
public final class MainViewCtl {

	//メインビュー保持用
	private static ViewGroup rootView = null;
	
	/**
	 * RootViewを取得する．
	 * RootViewが内場合はNullを返す．
	 * @return	RootViewまたはNull
	 */
	public static View getRootView(){
		return rootView;
	}
	
	private static void setRootView(ViewGroup mRootView){
		rootView = mRootView;
	}
	
	/**
	 * MainViewを生成して画面に表示を行うメソッド
	 * @param mService	アプリケーションのContext
	 */
	public final static void createAndShowMainView(Service mService){
		ImageCache.clearCache();
		if(mService == null)return;
		//WindowManagerの取得
		WindowManager mWindowManager = (WindowManager) mService.getSystemService(Context.WINDOW_SERVICE);

		// 重ね合わせするViewの設定を行う
		LayoutParams params = new WindowManager.LayoutParams(
				WindowManager.LayoutParams.WRAP_CONTENT,
				WindowManager.LayoutParams.WRAP_CONTENT,
				WindowManager.LayoutParams.TYPE_TOAST,
				WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | 
				WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL |
				WindowManager.LayoutParams.FLAG_SPLIT_TOUCH |
				WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED ,// | 
				PixelFormat.TRANSLUCENT);

		params.gravity = Gravity.CENTER_VERTICAL | Gravity.RIGHT;
		params.x = 0;
		params.y = 0;
		

		//重畳表示するViewを取得する．
		LinearLayout rootView = (LinearLayout)MainViewCtl.createView(mService);
		
		//プレイヤーのViewはハンドル部をタップした時に生成する．
		//ハンドル部が引き出される動作と同時に大きさを変更させ，
		//完全にハンドルが収納されたらViewを破棄する．
		
		//WindowManagerにViewとLayoutParamsを登録し，表示する
		try{
			mWindowManager.addView(rootView, params);
			//こっちは↓更新用
			//mWindowManager.updateViewLayout(mMainView, params);
		}catch(NullPointerException mNullPointerException){
			mNullPointerException.printStackTrace();
			//自分のサービスを終了させる．
			mService.stopSelf();
		}
	}
	
	/**
	 * RootViewを消去する．
	 */
	public static final void removeRootView(){
		try{
			//RootViewの取得
			View rootView = getRootView();
			WindowManager mWindowManager = 
					(WindowManager) rootView.getContext()
					.getSystemService(Context.WINDOW_SERVICE);
			
			//Viewの中身を消去
			MusicViewCtl.removePlayerView();
			//WindowManagerからMainViewを消去する．
			mWindowManager.removeView(rootView);
			MusicViewCtl.setPlayerView(null);
			setRootView(null);
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	/**
	 * MainのViewを生成するメソッド
	 * サービスのコンテキストを受けとりViewを生成する．
	 * @param mService
	 * @return　生成したViewGroup
	 */
	@SuppressLint("InflateParams") private final static View createView(Service mService){
		// Viewからインフレータを作成する
		LayoutInflater layoutInflater = LayoutInflater.from(mService);
		// レイアウトファイルから重ね合わせするViewを作成する
		rootView = (ViewGroup) layoutInflater.inflate(R.layout.main_service_view, null);
		//ミュージックcontrollerインスタンス
		MusicPlayerWithQueue mpwpl = MusicUtils.getMusicController(mService);
		//ボタンの動作設定
		initButtonOfView(mService,rootView,mpwpl);
		//その他Actionの設定
		initActionOfView(mService,rootView,mpwpl);
		return rootView;
	}
	
	/**
	 * 生成したメインView
	 * @param mService
	 * @param mView
	 * @param mpwpl 
	 * @param mpwpl 
	 */
	private final static void initButtonOfView(Service mService,View mView, MusicPlayerWithQueue mpwpl){
		//Viewのボタンに動作をつける
		Button handleButton = (Button)mView.findViewById(R.id.imageButton_handle);
		handleButton.getLayoutParams().width = Settings.getHandleWidth(mService);
		handleButton.setClickable(true);
		handleButton.setOnTouchListener(new MainHandleOnTouchImpl(mService));
	}
	
	/**
	 * メインViewの動作を設定する．
	 * @param mService
	 * @param mView
	 * @param mpwpl 
	 * @param mpwpl 
	 */
	private final static void initActionOfView(Service mService,View mView, MusicPlayerWithQueue mpwpl){
		//MainViewのアクションを設定する
		//自動的に引っ張り出るようにする．
		//MusicViewCtl.createPlayerView(mService, mView);
		final Button handle = (Button) rootView.findViewById(R.id.imageButton_handle);
		handle.setVisibility(View.VISIBLE);
		//Animation設定
		handle.startAnimation(
				AnimationUtils
				.loadAnimation(rootView.getContext(), android.R.anim.fade_in));
	}
	
}
