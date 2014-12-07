package com.abs104a.mperwithsideproject.viewctl;

import android.app.Service;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

import com.abs104a.mperwithsideproject.R;
import com.abs104a.mperwithsideproject.viewctl.listener.MainHandleActionOnClickListenerImpl;

/**
 * メインビューの生成と設定を行うクラス
 * @author Kouki
 *
 */
public final class MainViewController {

	/**
	 * MainのViewを生成するメソッド
	 * サービスのコンテキストを受けとりViewを生成する．
	 * @param mService
	 * @return　生成したViewGroup
	 */
	public final static View createView(Service mService){
		//TODO Viewの生成
		// Viewからインフレータを作成する
		LayoutInflater layoutInflater = LayoutInflater.from(mService);
		// レイアウトファイルから重ね合わせするViewを作成する
		View mView = layoutInflater.inflate(com.abs104a.mperwithsideproject.R.layout.player_view, null);
		initButtonOfView(mService,mView);
		initActionOfView(mService,mView);
		return mView;
	}
	
	/**
	 * 生成したメインView
	 * @param mService
	 * @param mView
	 */
	public final static void initButtonOfView(Service mService,View mView){
		//TODO Viewのボタンに動作をつける
		Button handleButton = (Button)mView.findViewById(R.id.imageButton_handle);
		handleButton.setOnClickListener(new MainHandleActionOnClickListenerImpl(mService));
	}
	
	/**
	 * メインViewの動作を設定する．
	 * @param mService
	 * @param mView
	 */
	public final static void initActionOfView(Service mService,View mView){
		
	}
	
}
