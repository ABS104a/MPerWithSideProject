package com.abs104a.mperwithsideproject.settings;

import com.abs104a.mperwithsideproject.R;

import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class SettingsFragment extends PreferenceFragment {

	/* (非 Javadoc)
	 * @see android.preference.PreferenceFragment#onCreate(android.os.Bundle)
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.preferences);
	}
	
	

	/* (非 Javadoc)
	 * @see android.pre
	 * ference.PreferenceFragment#onCreateView(android.view.LayoutInflater, android.view.ViewGroup, android.os.Bundle)
	 */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return super.onCreateView(inflater, container, savedInstanceState);
	}



	/* (非 Javadoc)
	 * @see android.preference.PreferenceFragment#onDestroy()
	 */
	@Override
	public void onDestroy() {
		super.onDestroy();
	}

}
