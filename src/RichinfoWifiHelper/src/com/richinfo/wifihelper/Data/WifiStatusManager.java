package com.richinfo.wifihelper.Data;

import com.richinfo.wifihelper.AppInstance;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;

public class WifiStatusManager {

	private static WifiStatusManager mStatusManager;

	private WifiManager mWifiManager;
	private ConnectivityManager mConnectivityManager;

	public static WifiStatusManager getInstance() {
		if (mStatusManager == null)
			mStatusManager = new WifiStatusManager();
		return mStatusManager;
	}

	private WifiStatusManager() {
		mConnectivityManager = (ConnectivityManager) AppInstance.getInstance()
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		mWifiManager = (WifiManager) AppInstance.getInstance()
				.getSystemService(Context.WIFI_SERVICE);
	}

	public void openWifi() {
		if (!this.mWifiManager.isWifiEnabled())
			this.mWifiManager.setWifiEnabled(true);
	}

	public void closeWifi() {
		this.mWifiManager.setWifiEnabled(false);
	}

	public boolean isWifiConnect() {
		NetworkInfo networkInfo = mConnectivityManager
				.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
		return networkInfo.isConnected();
	}

	public boolean isWifiEnable() {
		return mWifiManager.isWifiEnabled();
	}

	public WifiInfo getWifiInfo() {
		return this.mWifiManager.getConnectionInfo();
	}

	public String getBSSID() {
		WifiInfo wifiInfo = getWifiInfo();
		if (wifiInfo != null)
			return wifiInfo.getSSID();
		return null;
	}

}
