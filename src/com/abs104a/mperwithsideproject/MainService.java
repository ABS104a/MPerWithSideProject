package com.abs104a.mperwithsideproject;

import com.abs104a.mperwithsideproject.music.MusicPlayerWithQueue;
import com.abs104a.mperwithsideproject.viewctl.MainViewController;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.IBinder;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;

/**
 * メイン画面を表示するService
 * 
 * WindowManagerからにViewを登録して
 * 画面上に重畳表示する．
 * 
 * 機能として
 * ・プレイリストリスト表示		（タブ
 * ・簡易アルバムリスト表示　	（タブ
 * ・音楽の詳細表示
 * ・再生・一時停止などの操作
 * ・音量操作
 * ・シーク
 * ・ランダム&リピート
 * ・リスト表示画面の遷移ボタン
 * ・設定画面への遷移ボタン
 * 
 * 基本的な操作はこちらでカバーさせる．
 * 
 * @author Kouki
 *
 */
public class MainService extends Service{
	
	//自分のサービス（Context取得用)
	private final Service mService = this;
	//メインビュー生成用WindowManager
	private WindowManager mWindowManager = null;
	//メインビュー保持用
	private ViewGroup mMainView = null;
	//音楽リソースのコントロールクラスのインスタンス
	private final static MusicPlayerWithQueue _mpwpl = new MusicPlayerWithQueue();

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Activityからバインドされた時
		return null;
	}

	/**
	 * Serviceが開始された時
	 */
	@Override
	public void onCreate() {
		super.onCreate();

		//MainViewの生成
		LayoutInflater inflater = LayoutInflater.from( mService );
		inflater.inflate(R.layout.player_view, null);
		

		mWindowManager  = (WindowManager) getSystemService(Context.WINDOW_SERVICE);

		// 重ね合わせするViewの設定を行う
		LayoutParams params = new WindowManager.LayoutParams(
				WindowManager.LayoutParams.WRAP_CONTENT,
				WindowManager.LayoutParams.WRAP_CONTENT,
				WindowManager.LayoutParams.TYPE_TOAST,
				WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | 
				WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL,// | 
				//WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL ,//| WindowManager.LayoutParams.FLAG_TOUCHABLE_WHEN_WAKING,
				//WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
				PixelFormat.TRANSLUCENT);

		params.gravity = Gravity.CENTER_VERTICAL | Gravity.RIGHT;
		params.x = 0;
		params.y = 0;

		//重畳表示するViewを取得する．
		mMainView = (LinearLayout)MainViewController.createView(_mpwpl,mService);
		
		//プレイヤーのViewはハンドル部をタップした時に生成する．
		//ハンドル部が引き出される動作と同時に大きさを変更させ，
		//完全にハンドルが収納されたらViewを破棄する．
		
		//WindowManagerにViewとLayoutParamsを登録し，表示する
		try{
			mWindowManager.addView(mMainView, params);
			//こっちは↓更新用
			//mWindowManager.updateViewLayout(mMainView, params);
		}catch(NullPointerException mNullPointerException){
			mNullPointerException.printStackTrace();
			//自分のサービスを終了させる．
			this.stopSelf();
		}
		
		//表示するためのアニメーションを作成
		Animation showAnimation = 
				AnimationUtils.loadAnimation(mService, android.R.anim.fade_in);
		//Animationの設定
		mMainView.startAnimation(showAnimation);
		
		//開始ログ
		Log.v("MainService","Service is Start!");

	}

	/**
	 * Serviceが終了した時
	 */
	@Override
	public void onDestroy() {
		try{
			//MainViewを消去する．
			mWindowManager.removeView(mMainView);
		}catch(NullPointerException mNullPointerException){
			mNullPointerException.printStackTrace();
		}
		//終了
		Log.v("MainService","Service is Finished!");
		super.onDestroy();
	}
}
