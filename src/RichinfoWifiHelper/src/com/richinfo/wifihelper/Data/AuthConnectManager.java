package com.richinfo.wifihelper.Data;

import com.richinfo.wifihelper.common.net.AsyncHttpClient;
import com.richinfo.wifihelper.common.net.AsyncHttpResponseHandler;

public class AuthConnectManager {

	private static final String AUTH_URL = "http://192.168.6.254:80/login/";
	private static final String NET_URL = "http://www.baidu.com";

	private static AuthConnectManager mInstance;

	private AsyncHttpClient mAsyncHttpClient;

	public static AuthConnectManager getInstance() {
		if (mInstance == null)
			mInstance = new AuthConnectManager();
		return mInstance;
	}

	private AuthConnectManager() {
		mAsyncHttpClient = new AsyncHttpClient();
	}

	public void setBasicAuth(String name, String password) {
		mAsyncHttpClient.setBasicAuth(name, password);
	}

	public void checkAuthConnect(AsyncHttpResponseHandler handler) {
		mAsyncHttpClient.post(AUTH_URL, handler);
	}

	public void checkNetConnect(AsyncHttpResponseHandler handler) {
		mAsyncHttpClient.post(NET_URL, handler);
	}

	public void setBasicAuth() {
		Account account = DataManager.getInstance().getAccount();
		if (account != null) {
			setBasicAuth(account.getUsername(), account.getPassword());
		}
	}
}
