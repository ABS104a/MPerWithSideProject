package com.abs104a.mperwithsideproject.settings;


import android.app.Activity;
import android.os.Bundle;

public class SettingsActivity extends Activity{

	/* (非 Javadoc)
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		SettingsFragment fragment = new SettingsFragment();
		setContentView(fragment.getView());
	}

}
