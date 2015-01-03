package com.abs104a.mperwithsideproject.settings;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.abs104a.mperwithsideproject.R;

public class Settings {

	public static final boolean getHeadsetOnStartPlay(Context con){
		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(con);
		return preferences.getBoolean(con.getString(R.string.connected_headset_key), true);
	}
	
	public static final boolean getHeadsetUnStopPlay(Context con){
		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(con);
		return preferences.getBoolean(con.getString(R.string.unconnected_headset_key), true);
	}
}
