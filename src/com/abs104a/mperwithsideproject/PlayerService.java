package com.abs104a.mperwithsideproject;

import android.app.Service;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;

public class PlayerService extends Service {

	@Override
	public IBinder onBind(Intent intent) {
		// TODO 自動生成されたメソッド・スタブ
		return null;
	}

	public class PlayerServiceConnection implements ServiceConnection{

		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {
			// TODO 自動生成されたメソッド・スタブ
			
		}

		@Override
		public void onServiceDisconnected(ComponentName name) {
			// TODO 自動生成されたメソッド・スタブ
			
		}
		
	}
	
}
