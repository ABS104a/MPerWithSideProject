package com.abs104a.mperwithsideproject.upnp;

import org.fourthline.cling.model.meta.Device;
import org.fourthline.cling.model.meta.LocalDevice;
import org.fourthline.cling.model.meta.RemoteDevice;
import org.fourthline.cling.registry.DefaultRegistryListener;
import org.fourthline.cling.registry.Registry;

import android.util.Log;

import com.abs104a.mperwithsideproject.viewctl.ViewPagerForDLNACtl;

public class BrowseRegistryListener extends DefaultRegistryListener {

	private final static String TAG = "BROWSE_REGISTRY_LISTENER";
	

	public BrowseRegistryListener() {
		Log.v(TAG,"BrowseRegistryListener CONSTRUCTOR" );
		ViewPagerForDLNACtl.clearList();
	}

	/* Discovery performance optimization for very slow Android devices! */
    @Override
    public void remoteDeviceDiscoveryStarted(Registry registry, RemoteDevice device) {
        deviceAdded(device);
        Log.v(TAG,"remoteDeviceDiscoveryStated");
        Log.v(TAG,device.getDisplayString());
    }

    @Override
    public void remoteDeviceDiscoveryFailed(Registry registry, final RemoteDevice device, final Exception ex) {
        deviceRemoved(device);
        Log.v(TAG,"remoteDeviceDiscoveryFailed");
        Log.v(TAG,device.getDisplayString());
    }
    /* End of optimization, you can remove the whole block if your Android handset is fast (>= 600 Mhz) */

    @Override
    public void remoteDeviceAdded(Registry registry, RemoteDevice device) {
        deviceAdded(device);
        Log.v(TAG,"remoteDeviceAdded");
        Log.v(TAG,device.getDisplayString());
    }

    @Override
    public void remoteDeviceRemoved(Registry registry, RemoteDevice device) {
        deviceRemoved(device);
        Log.v(TAG,"remoteDeviceRemoved");
        Log.v(TAG,device.getDisplayString());
    }

    @Override
    public void localDeviceAdded(Registry registry, LocalDevice device) {
        deviceAdded(device);
        Log.v(TAG,"localDeviceAdded");
        Log.v(TAG,device.getDisplayString());
    }

    @Override
    public void localDeviceRemoved(Registry registry, LocalDevice device) {
        deviceRemoved(device);
        Log.v(TAG,"localDeviceRemoved");
        Log.v(TAG,device.getDisplayString());
    }

    @SuppressWarnings("rawtypes")
	public void deviceAdded(final Device device) {
    	//デバイスを追加する時（List等）
    	if (device.getType().getType().equals("MediaServer")) {  
    		Log.v(TAG,"deviceAdded");
        	Log.v(TAG,device.getDisplayString());
        	
        	
        	ViewPagerForDLNACtl.addList(
        			new DisplayItem(
        			device.getDisplayString(), 
        			device.getType().getType(), 
        			device.getIcons()[0].getUri().toString(), 
        			device, Device.class));
    	}
    	
    }

    @SuppressWarnings("rawtypes")
	public void deviceRemoved(final Device device) {
    	//デバイスを消去する時（List等）
    	Log.v(TAG,"deviceRemoved");
    	Log.v(TAG,device.getDisplayString());
    	ViewPagerForDLNACtl.removeList(
    			new DisplayItem(
    			device.getDisplayString(), 
    			device.getType().getType(), 
    			device.getIcons()[0].getUri().toString(), 
    			device, Device.class));
    }
}