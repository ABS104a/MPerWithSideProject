package com.abs104a.mperwithsideproject.viewctl.listener;

import com.abs104a.mperwithsideproject.MainService;
import com.abs104a.mperwithsideproject.settings.SettingsActivity;
import com.abs104a.mperwithsideproject.viewctl.MusicViewCtl;

import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;

public class SettigsOnClickImpl implements OnClickListener {

	@Override
	public void onClick(View v) {
		Intent intent = new Intent(v.getContext(),SettingsActivity.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK); 
		v.getContext().startActivity(intent);
		MusicViewCtl.removePlayerView(MainService.getRootView());
	}

}
