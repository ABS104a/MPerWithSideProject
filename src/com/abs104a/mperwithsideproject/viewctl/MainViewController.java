package com.abs104a.mperwithsideproject.viewctl;

import android.app.Service;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;

import com.abs104a.mperwithsideproject.R;
import com.abs104a.mperwithsideproject.music.MusicPlayerWithQueue;
import com.abs104a.mperwithsideproject.viewctl.listener.MainHandleActionOnTouchImpl;

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
		// Viewからインフレータを作成する
		LayoutInflater layoutInflater = LayoutInflater.from(mService);
		// レイアウトファイルから重ね合わせするViewを作成する
		View mView = layoutInflater.inflate(R.layout.main_service_view, null);
		initButtonOfView(mService,mView);
		initActionOfView(mService,mView);
		return mView;
	}
	
	/**
	 * 生成したメインView
	 * @param mService
	 * @param mView
	 * @param mpwpl 
	 */
	private final static void initButtonOfView(Service mService,View mView){
		//Viewのボタンに動作をつける
		ImageButton handleButton = (ImageButton)mView.findViewById(R.id.imageButton_handle);
		handleButton.setOnTouchListener(new MainHandleActionOnTouchImpl(mService));
	}
	
	/**
	 * メインViewの動作を設定する．
	 * @param mService
	 * @param mView
	 * @param mpwpl 
	 */
	private final static void initActionOfView(Service mService,View mView){
		//MainViewのアクションを設定する
	}
	
}
