package com.abs104a.mperwithsideproject;

import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Handler;
import android.os.IBinder;
import android.os.RemoteException;

public class PlayerService extends Service {
	
	private final static String TAG = "PlayerService";

	private static final long DELAY = 1000;
	
    private final Service mService = this;
    private PlayerServiceConnection mPlayerServiceConnection = new PlayerServiceConnection();
    private Intent mMainServiceIntent = null;
    
    private boolean finishFlag = false;
    //バインドしているかどうか．
    private boolean isBind = false;
 
    @SuppressWarnings("unused")
	private IMainService mIMainService = null;
    private IPlayerService.Stub mIPlayerService = new IPlayerService.Stub() {
		
		@Override
		public boolean stopService() throws RemoteException {
			finishFlag = true;
			if(isBind)mService.unbindService(mPlayerServiceConnection);
			return finishFlag;
		}

		@Override
		public boolean bindService() throws RemoteException {
			//MainServiceをバインドする．
			return isBind;
			
		}
	};
	
	@Override
	public IBinder onBind(Intent intent) {
		return mIPlayerService;
	}

	public class PlayerServiceConnection implements ServiceConnection{

		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {
			mIMainService = IMainService.Stub.asInterface(service);
			isBind = true;
			android.util.Log.v(TAG, "onServiceConnected PlayerService" );
		}

		@Override
		public void onServiceDisconnected(ComponentName name) {
			android.util.Log.v(TAG, "onServiceDisconnected PlayerService");
			isBind = false;
			mIMainService = null;
			if(finishFlag)
				mService.stopSelf();
			else if(!isBind){
				new Handler().postDelayed(new Runnable(){

					@Override
					public void run() {
						if(!isBind)
							mService.bindService(new Intent(mService,MainService.class), 
									mPlayerServiceConnection ,
									Context.BIND_AUTO_CREATE);
					}
					
				}, DELAY);
						
			}
		}
		
	}

	/* (非 Javadoc)
	 * @see android.app.Service#onCreate()
	 */
	@Override
	public void onCreate() {
		//MainServiceをバインドする．
		isBind = false;
		mMainServiceIntent = new Intent(mService,MainService.class);
		if(!isBind)
			mService.bindService(
					mMainServiceIntent, 
					mPlayerServiceConnection ,
					Context.BIND_AUTO_CREATE);
		super.onCreate();
	}

	/* (非 Javadoc)
	 * @see android.app.Service#onDestroy()
	 */
	@Override
	public void onDestroy() {
		isBind = false;
		mMainServiceIntent = null;
		super.onDestroy();
	}
	
}
