package com.abs104a.mperwithsideproject;

import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;
import android.widget.Toast;

public class PlayerService extends Service {
	
	private final static String TAG = "PlayerService";
    private final Service mService = this;
    private PlayerServiceConnection mPlayerServiceConnection = new PlayerServiceConnection();
    
    private boolean finishFlag = false;
 
    @SuppressWarnings("unused")
	private IMainService mIMainService = null;
    private IPlayerService.Stub mIPlayerService = new IPlayerService.Stub() {
		
		@Override
		public boolean stopService() throws RemoteException {
			finishFlag = true;			
			mService.unbindService(mPlayerServiceConnection);
			mService.stopSelf();
			return finishFlag;
		}

		@Override
		public boolean bindService() throws RemoteException {
			//MainServiceをバインドする．
			return mService.bindService(
					new Intent(mService,MainService.class),
					mPlayerServiceConnection , 
					Context.BIND_IMPORTANT);
		}
	};

	@Override
	public IBinder onBind(Intent intent) {
		// バインドされた時
		return mIPlayerService;
	}

	public class PlayerServiceConnection implements ServiceConnection{

		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {
			mIMainService = IMainService.Stub.asInterface(service);
			android.util.Log.v(TAG, "onServiceConnected PlayerService" );
			Toast.makeText(mService, "Player", Toast.LENGTH_SHORT).show();
		}

		@Override
		public void onServiceDisconnected(ComponentName name) {
			android.util.Log.v(TAG, "onServiceDisconnected PlayerService");
			mIMainService = null;
		}
		
	}

	/* (非 Javadoc)
	 * @see android.app.Service#onCreate()
	 */
	@Override
	public void onCreate() {
		super.onCreate();
	}

	/* (非 Javadoc)
	 * @see android.app.Service#onDestroy()
	 */
	@Override
	public void onDestroy() {
		if(!finishFlag)
			//MainServiceをバインドする．
			mService.bindService(new Intent(mService,MainService.class), mPlayerServiceConnection , Context.BIND_AUTO_CREATE);
		
		super.onDestroy();
	}
	
	
	
	
}
