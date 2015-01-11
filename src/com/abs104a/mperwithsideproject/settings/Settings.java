package com.abs104a.mperwithsideproject.settings;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.abs104a.mperwithsideproject.R;
/**
 * 設定を取得するクラス
 * @author Kouki
 *
 */
public class Settings {

	/**
	 * ヘッドセットが接続された時に再生を開始するかどうかの設定状態を取得する．
	 * @param con	アプリケーションのコンテキスト
	 * @return	true：再生を行う　false：再生しない．
	 */
	public static final boolean getHeadsetOnStartPlay(Context con){
		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(con);
		return preferences.getBoolean(con.getString(R.string.connected_headset_key), true);
	}
	
	/**
	 * ヘッドセットを取り外した時に再生を終了するかどうかの設定状態を取得する．
	 * @param con	アプリケーションのコンテキスト
	 * @return	true：再生を停止する　false：再生を停止しない．
	 */
	public static final boolean getHeadsetUnStopPlay(Context con){
		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(con);
		return preferences.getBoolean(con.getString(R.string.unconnected_headset_key), true);
	}
	
	/**
	 * ハンドルの幅設定を取得
	 * @param con
	 * @return
	 */
	public static final int getHandleWidth(Context con){
		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(con);
		return Integer.parseInt(preferences.getString(con.getString(R.string.handle_width_key), con.getString(R.string.handle_width_defvalue)));
	}
}
