package com.abs104a.mperwithsideproject.settings;


import com.abs104a.mperwithsideproject.R;

import android.app.Activity;
import android.os.Bundle;

/**
 * 設定画面を表示するActivity
 * @author Kouki
 *
 */
public class SettingsActivity extends Activity{
	
	/* (非 Javadoc)
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.settings);
	}

}
