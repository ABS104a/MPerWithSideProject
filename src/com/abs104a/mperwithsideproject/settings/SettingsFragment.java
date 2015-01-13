package com.abs104a.mperwithsideproject.settings;

import com.abs104a.mperwithsideproject.R;

import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * 設定画面を表示するFragment
 * @author Kouki
 *
 */
public class SettingsFragment extends PreferenceFragment {

	/* (非 Javadoc)
	 * @see android.preference.PreferenceFragment#onCreate(android.os.Bundle)
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.preferences);
		
		Preference pf = findPreference(getText(R.string.version_title_key));
		pf.setSummary("Build : " + Settings.getVersionCode(getActivity()) + " , Version : " + Settings.getVersionName(getActivity()) + ", By ABS104a");
		
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
