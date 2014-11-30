package com.abs104a.mperwithsideproject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

public class MainActivity extends Activity{

	public final static String TAG = "MainActivity";
	
	//�������g��Activity
	private final Activity mActivity = this;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO MainService���N������
		super.onCreate(savedInstanceState);
		
		//MainService���N������
		Intent mainIntent = new Intent(mActivity,MainService.class);
		mActivity.startService(mainIntent);
		
		Log.v(TAG,"ActivityStart!");
	}

	@Override
	protected void onStart() {
		// TODO Activity���I��������
		super.onStart();
		finish();
	}
}
