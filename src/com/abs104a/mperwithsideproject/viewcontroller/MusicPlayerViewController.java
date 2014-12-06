package com.abs104a.mperwithsideproject.viewcontroller;


import java.io.IOException;

import com.abs104a.mperwithsideproject.R;
import com.abs104a.mperwithsideproject.music.MusicPlayer.OnPlayCompletedListener;
import com.abs104a.mperwithsideproject.music.MusicPlayerWithPlayLists;
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
public class MusicPlayerViewController {

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
		
		//再生ボタンの設定
		Button playButton = (Button)mView.findViewById(R.id.button_play);
		//TODO　再生ボタンの動作を登録する．
		//ex.. playButton.setOnClickListener(hoge));
		
		//次へのボタンの設定
		Button afterButton = (Button)mView.findViewById(R.id.button_next_seek);
		//TODO 次へボタンの動作を登録する．
		
		Button beforeButton = (Button)mView.findViewById(R.id.button_back_seek);
		//TODO 前へボタンの動作を登録する．
		
		Button repeatButton = (Button)mView.findViewById(R.id.button_repeat);
		//TODO リピートボタンの動作を登録する．
		
		Button shuffleButton = (Button)mView.findViewById(R.id.button_shuffle);
		//TODO シャッフルボタンの動作を登録する．
		
		Button showListButton = (Button)mView.findViewById(R.id.button_action_show_list);
		//TODO リスト表示ボタンの設定を登録する
		
		Button showSettigsButton = (Button)mView.findViewById(R.id.button_action_show_settings);
		//TODO 設定表示ボタンの設定を登録する．
		
	}
	
	public static void initAction(Service mService,View mView){
		//プレーヤーコントロールクラスのインスタンスを取得
		final MusicPlayerWithPlayLists mpwpl = new MusicPlayerWithPlayLists();
		//再生が終了した時に呼ばれるリスナを実装する．
		OnPlayCompletedListener mOnPlayCompletedListener = new OnPlayCompletedListener(){

			@Override
			public void onPlayCompleted() {
				//再生が終了したとき 次の曲をセットする．
				try {
					//次の曲を再生
					mpwpl.playNext();
				} catch (IllegalArgumentException e) {
					// TODO 自動生成された catch ブロック
					e.printStackTrace();
				} catch (SecurityException e) {
					// TODO 自動生成された catch ブロック
					e.printStackTrace();
				} catch (IllegalStateException e) {
					// TODO 自動生成された catch ブロック
					e.printStackTrace();
				} catch (IOException e) {
					// TODO 自動生成された catch ブロック
					e.printStackTrace();
				}
			}
			
		};
		//再生が完了したときのリスナをセット．
		mpwpl.setOnPlayCompletedListener(mOnPlayCompletedListener);
		//TODO プレイリストを設定
		
	}
	
}
