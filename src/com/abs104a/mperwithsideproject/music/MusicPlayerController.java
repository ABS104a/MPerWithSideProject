package com.abs104a.mperwithsideproject.music;


import com.abs104a.mperwithsideproject.R;
import com.abs104a.mperwithsideproject.music.listener.ExitActionOnClickListenerImpl;

import android.app.Service;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

/**
 * プレイヤーメインViewの生成と設定を行うクラス
 * 
 * 
 * @author Kouki
 *
 */
public class MusicPlayerController {

	/**
	 * PlayerのViewを生成するメソッド
	 * サービスのコンテキストを受けとりViewを生成する．
	 * @param mService
	 * @return　生成したViewGroup
	 */
	public static View createView(Service mService){
		//TODO Viewの生成
		// Viewからインフレータを作成する
		LayoutInflater layoutInflater = LayoutInflater.from(mService);
		// レイアウトファイルから重ね合わせするViewを作成する
		View mView = layoutInflater.inflate(com.abs104a.mperwithsideproject.R.layout.main_service_view, null);
		//Action Settings 
		initButtonOfView(mService,mView);
		return mView;
	}
	
	/**
	 * 生成したメインView
	 * @param mService
	 * @param mView
	 */
	public static void initButtonOfView(Service mService,View mView){
		//TODO Viewのボタンに動作をつける
		Button exitButton = (Button)mView.findViewById(R.id.button_action_exit);
		exitButton.setOnClickListener(new ExitActionOnClickListenerImpl(mService));
	}
	
}
