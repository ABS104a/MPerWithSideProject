package com.abs104a.mperwithsideproject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

public class MainActivity extends Activity{

	public final static String TAG = "MainActivity";
	
	//自分自身のActivity
	private final Activity mActivity = this;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		//MainServiceを起動する
		super.onCreate(savedInstanceState);
		//MainServiceを起動する
		Intent mainIntent = new Intent(mActivity,MainService.class);
		mActivity.startService(mainIntent);
		
		Log.v(TAG,"ActivityStart!");
	}

	@Override
	protected void onStart() {
		//Activityを終了させる
		super.onStart();
		finish();
	}
}
