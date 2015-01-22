package com.abs104a.mperwithsideproject;

import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;

public class PlayerService extends Service {
	
	private final static String TAG = "PlayerService";
    private final Service mService = this;
    private PlayerServiceConnection mPlayerServiceConnection = new PlayerServiceConnection();
    private Intent mMainServiceIntent = null;
    
    private boolean finishFlag = false;
    //バインドしているかどうか．
    private boolean isBind;
 
    @SuppressWarnings("unused")
	private IMainService mIMainService = null;
    private IPlayerService.Stub mIPlayerService = new IPlayerService.Stub() {
		
		@Override
		public boolean stopService() throws RemoteException {
			finishFlag = true;
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
		// バインドされた時
		if(!isBind){
			mService.startService(mMainServiceIntent);
			mService.bindService(
					mMainServiceIntent,
					mPlayerServiceConnection , 
					Context.BIND_AUTO_CREATE);
		}
		return mIPlayerService;
	}
	
	/* (非 Javadoc)
	 * @see android.app.Service#onUnbind(android.content.Intent)
	 */
	@Override
	public boolean onUnbind(Intent intent) {
		// アンバインドされた時
		if(!finishFlag && !isBind){
			//MainServiceをバインドする．
			mService.bindService(mMainServiceIntent, mPlayerServiceConnection , Context.BIND_AUTO_CREATE);
		}else if(finishFlag && isBind){
			mService.unbindService(mPlayerServiceConnection);
		}
		
		return super.onUnbind(intent);
		
	}

	public class PlayerServiceConnection implements ServiceConnection{

		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {
			mIMainService = IMainService.Stub.asInterface(service);
			isBind = true;
			android.util.Log.v(TAG, "onServiceConnected PlayerService" );
			//Toast.makeText(mService, "Player", Toast.LENGTH_SHORT).show();
		}

		@Override
		public void onServiceDisconnected(ComponentName name) {
			android.util.Log.v(TAG, "onServiceDisconnected PlayerService");
			isBind = false;
			mIMainService = null;
			if(finishFlag)
				mService.stopSelf();
		}
		
	}

	/* (非 Javadoc)
	 * @see android.app.Service#onCreate()
	 */
	@Override
	public void onCreate() {
		isBind = false;
		mMainServiceIntent = new Intent(mService,MainService.class);
		super.onCreate();
	}

	/* (非 Javadoc)
	 * @see android.app.Service#onDestroy()
	 */
	@Override
	public void onDestroy() {
		if(!finishFlag){
			//MainServiceをバインドする．
			if(!isBind)mService.bindService(mMainServiceIntent, mPlayerServiceConnection , Context.BIND_AUTO_CREATE);
		}
		isBind = false;
		mMainServiceIntent = null;
		super.onDestroy();
	}
	
}
