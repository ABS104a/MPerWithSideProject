package com.abs104a.mperwithsideproject;

import org.fourthline.cling.android.AndroidUpnpService;
import org.fourthline.cling.android.AndroidUpnpServiceImpl;
import org.fourthline.cling.android.FixedAndroidLogHandler;
import org.fourthline.cling.model.meta.Device;

import com.abs104a.mperwithsideproject.adapter.MusicViewPagerForActivityAdapter;
import com.abs104a.mperwithsideproject.upnp.BrowseRegistryListener;
import com.abs104a.mperwithsideproject.viewctl.listener.ViewPagerOnPagerChangeForActivityImpl;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;

/**
 * オーバーレイViewのViewPagerをActivityで表示するActivity
 * DLNAの動作もこのActivityで行う．
 * @author ABS104a
 *
 */
public class MusicListActivity extends Activity {


	private final Activity mActivity = this;

	/**
	 * The {@link ViewPager} that will host the section contents.
	 */

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.part_of_player_list);

		View root = (View)getWindow().getDecorView().findViewById(android.R.id.content);

		//ViewPager の設定
		ViewPager mViewPager = (ViewPager)root.findViewById(R.id.music_pager);
		LayoutParams params = mViewPager.getLayoutParams();
		params.height = ViewGroup.LayoutParams.MATCH_PARENT;
		mViewPager.setLayoutParams(params);
		
		//Adapterの設定
		mViewPager.setAdapter(new MusicViewPagerForActivityAdapter(mActivity));
		//ページの設定
		ViewPagerOnPagerChangeForActivityImpl pageChangeListener = new ViewPagerOnPagerChangeForActivityImpl(mViewPager);
		mViewPager.setOnPageChangeListener(pageChangeListener);

		// Fix the logging integration between java.util.logging and Android internal logging
		
       	org.seamless.util.logging.LoggingUtil.resetRootHandler(
           	new FixedAndroidLogHandler()
       	);

		// This will start the UPnP service if it wasn't already started
		getApplicationContext().bindService(
				new Intent(this, AndroidUpnpServiceImpl.class),
				serviceConnection,
				Context.BIND_AUTO_CREATE
				);

	}
	
	private BrowseRegistryListener registryListener = new BrowseRegistryListener();

    private AndroidUpnpService upnpService;

    private ServiceConnection serviceConnection = new ServiceConnection() {

        public void onServiceConnected(ComponentName className, IBinder service) {
            upnpService = (AndroidUpnpService) service;

            // Get ready for future device advertisements
            upnpService.getRegistry().addListener(registryListener);

            // Now add all devices to the list we already know about
            for (Device device : upnpService.getRegistry().getDevices()) {
                registryListener.deviceAdded(device);
            }

            // Search asynchronously for all devices, they will respond soon
            upnpService.getControlPoint().search();
        }

        public void onServiceDisconnected(ComponentName className) {
            upnpService = null;
        }
    };
    
    

	/* (非 Javadoc)
	 * @see android.app.Activity#onDestroy()
	 */
	@Override
	protected void onDestroy() {
		
		if (upnpService != null) {
            upnpService.getRegistry().removeListener(registryListener);
        }
        // This will stop the UPnP service if nobody else is bound to it
        getApplicationContext().unbindService(serviceConnection);
		super.onDestroy();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.music_list, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

}
