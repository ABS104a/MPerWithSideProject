package com.abs104a.mperwithsideproject.upnp;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

import org.apache.http.conn.util.InetAddressUtils;
import org.fourthline.cling.UpnpService;
import org.fourthline.cling.UpnpServiceImpl;
import org.fourthline.cling.support.igd.PortMappingListener;
import org.fourthline.cling.support.model.PortMapping;

import android.util.Log;

public class PortMappingUtil {

	
	private static UpnpService mService = null;
	
	public static UpnpService getUpnpService(){
		if(mService == null){
			PortMapping desiredMapping =
					new PortMapping(
							8123,
							getLocalIpv4Address(),
							PortMapping.Protocol.TCP,
							"My Android Device Port Mapping"
							);

			UpnpService upnpService =
					new UpnpServiceImpl(
							new PortMappingListener(desiredMapping)
							);
			return upnpService;
		}else{
			return mService;
		}
	}
	
	public static String getLocalIpv4Address(){
		try{
			for (Enumeration<NetworkInterface> networkInterfaceEnum =
					NetworkInterface.getNetworkInterfaces();
					networkInterfaceEnum.hasMoreElements();){
				NetworkInterface networkInterface =
						networkInterfaceEnum.nextElement();

				Enumeration<InetAddress> ipAddressEnum = networkInterface.getInetAddresses();
				while (ipAddressEnum.hasMoreElements()){
					InetAddress inetAddress = (InetAddress) ipAddressEnum.nextElement();
					//---check that it is not a loopback address and it is ipv4---
					if(!inetAddress.isLoopbackAddress() &&
							InetAddressUtils.isIPv4Address(inetAddress.getHostAddress())){
						return inetAddress.getHostAddress();
					}
				}
			}
		}catch (SocketException ex){
			Log.e("getLocalIpv4Address", ex.toString());
		}
		return null;
	}
}
