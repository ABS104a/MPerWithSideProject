package com.abs104a.mperwithsideproject.viewctl.listener;

import com.abs104a.mperwithsideproject.MainService;
import com.abs104a.mperwithsideproject.settings.SettingsActivity;
import com.abs104a.mperwithsideproject.viewctl.MusicViewCtl;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;

public class SettigsOnClickImpl implements OnClickListener {

	@Override
	public void onClick(View v) {
		Context con = MainService.getService();
		Intent intent = new Intent(con,SettingsActivity.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK); 
		con.startActivity(intent);
		MusicViewCtl.removePlayerView();
	}

}
