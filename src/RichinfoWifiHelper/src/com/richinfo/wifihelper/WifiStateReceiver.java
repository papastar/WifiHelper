package com.richinfo.wifihelper;

import java.util.ArrayList;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.os.Parcelable;
import com.richinfo.wifihelper.Data.DataManager;
import com.richinfo.wifihelper.Data.AuthConnectManager;
import com.richinfo.wifihelper.Data.NotifyManager;
import com.richinfo.wifihelper.common.net.AsyncHttpResponseHandler;

public class WifiStateReceiver extends BroadcastReceiver {

	public static ArrayList<WifiStateChangeListener> listeners = new ArrayList<WifiStateReceiver.WifiStateChangeListener>();

	public void notifyState(WifiStatus status) {
		for (WifiStateChangeListener item : listeners) {
			item.stateChange(status);
		}
	}

	private void autoAuth() {
		DataManager accountManager = DataManager.getInstance();
		final AuthConnectManager connectManager = AuthConnectManager
				.getInstance();
		if (accountManager.checkAutoAuth()) {
			connectManager.setBasicAuth();
			connectManager.checkAuthConnect(new AsyncHttpResponseHandler() {

				@Override
				public void onSuccess(String content) {

					super.onSuccess(content);
				}

				@Override
				public void onSuccess(int statusCode, String content) {
					checkNetConnect();
				}

				@Override
				public void onFailure(Throwable error, String content) {

					super.onFailure(error, content);
				}

			});
		}
	}
	
	private void checkNetConnect() {
		AuthConnectManager.getInstance().checkNetConnect(new AsyncHttpResponseHandler() {

			@Override
			public void onSuccess(String content) {
				super.onSuccess(content);
			}

			@Override
			public void onSuccess(int statusCode, String content) {
				super.onSuccess(statusCode, content);
			    notifyState(WifiStatus.AUTHED);
				NotifyManager.getInstance().showNotification(
						WifiStatus.AUTHED);
			}

			@Override
			public void onFailure(Throwable error, String content) {
				super.onFailure(error, content);
			}

		});
	}
	
	

	@Override
	public void onReceive(Context context, Intent intent) {

		if (WifiManager.WIFI_STATE_CHANGED_ACTION.equals(intent.getAction())) {
			int wifiState = intent.getIntExtra(WifiManager.EXTRA_WIFI_STATE, 0);
			switch (wifiState) {
			case WifiManager.WIFI_STATE_ENABLED:
				notifyState(WifiStatus.ENABLE);
				NotifyManager.getInstance().showNotification(WifiStatus.ENABLE);
				break;
			case WifiManager.WIFI_STATE_ENABLING:

				break;
			case WifiManager.WIFI_STATE_DISABLED:
				notifyState(WifiStatus.DISABLE);
				NotifyManager.getInstance().cancelNotify();
				break;
			case WifiManager.WIFI_STATE_DISABLING:
				NotifyManager.getInstance()
						.showNotification(WifiStatus.DISABLE);
				break;
			}
		}

		// WIFI的连接状态处理
		if (WifiManager.NETWORK_STATE_CHANGED_ACTION.equals(intent.getAction())) {
			Parcelable parcelable = intent
					.getParcelableExtra(WifiManager.EXTRA_NETWORK_INFO);
			if (parcelable instanceof NetworkInfo) {
				NetworkInfo networkInfo = (NetworkInfo) parcelable;
				switch (networkInfo.getState()) {
				case CONNECTED:
					autoAuth();
					notifyState(WifiStatus.CONNECTED);
					checkNetConnect();
					NotifyManager.getInstance().showNotification(
							WifiStatus.CONNECTED);
					break;
				case CONNECTING:

					break;
				case DISCONNECTING:

					break;
				case DISCONNECTED:
					NotifyManager.getInstance().showNotification(
							WifiStatus.DISCONNECTED);
					break;
				case SUSPENDED:

					break;
				case UNKNOWN:

					break;
				default:
					break;
				}
			}
		}
	}

	public interface WifiStateChangeListener {
		public void stateChange(WifiStatus status);
	}

}
