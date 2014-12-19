package com.abs104a.mperwithsideproject.viewctl;

import android.app.Service;
import android.view.LayoutInflater;
import android.view.View;

import com.abs104a.mperwithsideproject.R;
import com.abs104a.mperwithsideproject.music.MusicPlayerWithQueue;

/**
 * EqualizerViewの生成とコントロールを行うクラス
 * @author Kouki-Mobile
 *
 */
public class ViewPagerForEqualizerViewCtl {

	/**
	 * Viewの生成
	 * @param mService	親サービスのコンテキスト
	 * @param mpwpl		音楽playerのコントロール
	 * @return			生成したView
	 */
	public static View createView(
			Service mService,
			MusicPlayerWithQueue mpwpl) 
	{
		// TODO 動作の登録
		//TODO Visualizerの設定
		LayoutInflater layoutInflater = LayoutInflater.from(mService);
		View mView = layoutInflater.inflate(R.layout.equalizer, null);
		return mView;
	}

}
