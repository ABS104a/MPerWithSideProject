package com.abs104a.mperwithsideproject;

import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Binder;
import android.os.IBinder;

public class PlayerService extends Service {
	
	private final static String TAG = "PlayerService";
    private final IBinder mBinder =  new LocalBinder();
    private final Service mService = this;
    private MainService mMainService = null;
    private PlayerServiceConnection mPlayerServiceConnection = new PlayerServiceConnection();
 
    public class LocalBinder extends Binder {
    	PlayerService getService() {
            return (PlayerService) mService;
        }
    }

	@Override
	public IBinder onBind(Intent intent) {
		// バインドされた時
		return mBinder;
	}

	public class PlayerServiceConnection implements ServiceConnection{

		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {
			MainService.LocalBinder binder = (MainService.LocalBinder)service;
			mMainService = binder.getService();
			android.util.Log.v(TAG, "onServiceConnected " + mMainService.getPackageName());
		}

		@Override
		public void onServiceDisconnected(ComponentName name) {
			android.util.Log.v(TAG, "onServiceDisconnected PlayerService");
			mMainService = null;
		}
		
	}

	/* (非 Javadoc)
	 * @see android.app.Service#onCreate()
	 */
	@Override
	public void onCreate() {
		super.onCreate();
		//MainServiceをバインドする．
		mService.bindService(new Intent(mService,MainService.class), mPlayerServiceConnection , Context.BIND_AUTO_CREATE);
	}

	/* (非 Javadoc)
	 * @see android.app.Service#onDestroy()
	 */
	@Override
	public void onDestroy() {
		mService.unbindService(mPlayerServiceConnection);
		super.onDestroy();
	}
	
	
	
	
}