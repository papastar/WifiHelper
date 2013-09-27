package com.richinfo.wifihelper;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.Toast;
import com.richinfo.wifihelper.WifiStateReceiver.WifiStateChangeListener;
import com.richinfo.wifihelper.Data.Account;
import com.richinfo.wifihelper.Data.AccountManager;
import com.richinfo.wifihelper.Data.AuthConnectManager;
import com.richinfo.wifihelper.Data.NotifyManager;
import com.richinfo.wifihelper.Data.WifiStatusManager;
import com.richinfo.wifihelper.common.net.AsyncHttpResponseHandler;
import com.richinfo.wifihelper.ui.AuthLayout;
import com.richinfo.wifihelper.ui.SwitchButton;

public class MainActivity extends BaseActivity implements OnClickListener,
		WifiStateChangeListener, OnCheckedChangeListener {

	private AuthConnectManager mAuthConnectManager;

	private AccountManager mAccountManager;
	private WifiStatusManager mWifiStatusManager;
	private AuthLayout mAuthLayout;
	private View mLoginView;
	private EditText mUserEdit;
	private EditText mPsEdit;
	private SwitchButton mSwitchButton;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mAuthConnectManager = AuthConnectManager.getInstance();
		mAccountManager = AccountManager.getInstance();
		mWifiStatusManager = WifiStatusManager.getInstance();
		setContentView(R.layout.activity_main);
		mAuthLayout = (AuthLayout) findViewById(R.id.auth_layout);
		mAuthLayout.changeState(getWifiState());
		mSwitchButton = (SwitchButton) findViewById(R.id.wifi_checkbox);
		mSwitchButton.setOnCheckedChangeListener(this);
		mSwitchButton.setChecked(mWifiStatusManager.isWifiEnable());
		mLoginView = findViewById(R.id.login_layout);
		mUserEdit = (EditText) findViewById(R.id.username);
		mPsEdit = (EditText) findViewById(R.id.userpwd);
		findViewById(R.id.submit).setOnClickListener(this);

		WifiStateReceiver.listeners.add(this);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.action_settings:
			Intent intent = new Intent(this, SettingPreferenceActivity.class);
			startActivity(intent);
			return true;

		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		WifiStateReceiver.listeners.remove(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.auth_layout:
			authClick();
			break;
		case R.id.submit:
			checkAuth();
			break;
		default:
			break;
		}

	}

	private void authClick() {
		if (mAuthLayout.getCurrentStatus() == WifiStatus.CONNECTED)
			if (!mAccountManager.hasAccount()) {

			} else {
				Account account = mAccountManager.getAccount();
				if (account != null) {
					mAuthConnectManager.setBasicAuth(account.getUsername(),
							account.getPassword());
				}
				checkAuth();
			}
	}

	private void setBasicAuth() {
		Account account = mAccountManager.getAccount();
		if (account != null) {
			mAuthConnectManager.setBasicAuth(account.getUsername(),
					account.getPassword());
		} else {
			if (mLoginView != null) {
				mAuthConnectManager.setBasicAuth(
						mUserEdit.getText().toString(), mPsEdit.getText()
								.toString());
			}
		}
	}

	private void checkAuth() {
		if (mAuthLayout.getCurrentStatus() == WifiStatus.CONNECTED) {
			setBasicAuth();
			mAuthConnectManager
					.checkAuthConnect(new AsyncHttpResponseHandler() {

						@Override
						public void onSuccess(String content) {
							super.onSuccess(content);
						}

						@Override
						public void onSuccess(int statusCode, String content) {
							super.onSuccess(statusCode, content);
							checkNetConnect();
						}

						@Override
						public void onFailure(Throwable error, String content) {
							super.onFailure(error, content);
							Toast.makeText(MainActivity.this, "—È÷§ ß∞‹",
									Toast.LENGTH_SHORT).show();
						}

					});
		}
	}

	private WifiStatus getWifiState() {
		WifiStatus status = WifiStatus.AUTHED;
		WifiStatusManager statusManager = WifiStatusManager.getInstance();
		if (statusManager.isWifiEnable()) {
			if (statusManager.isWifiConnect()) {
				status = WifiStatus.CONNECTED;
				NotifyManager.getInstance().showNotification(status);
				checkNetConnect();
			} else {
				status = WifiStatus.ENABLE;
				NotifyManager.getInstance().showNotification(status);
			}
		} else {
			status = WifiStatus.DISABLE;
		}

		return status;
	}

	private void checkNetConnect() {
		mAuthConnectManager.checkNetConnect(new AsyncHttpResponseHandler() {

			@Override
			public void onSuccess(String content) {
				super.onSuccess(content);
			}

			@Override
			public void onSuccess(int statusCode, String content) {
				super.onSuccess(statusCode, content);
				mAuthLayout.changeState(WifiStatus.AUTHED);
				NotifyManager.getInstance().showNotification(WifiStatus.AUTHED);
				mLoginView.setVisibility(View.GONE);
				String name = mUserEdit.getText().toString();
				String password = mPsEdit.getText().toString();
				if (!TextUtils.isEmpty(name)) {
					Account account = new Account();
					account.setUsername(name);
					account.setPassword(password);
					mAccountManager.putAccount(account);

				}
			}

			@Override
			public void onFailure(Throwable error, String content) {
				super.onFailure(error, content);
				mLoginView.setVisibility(View.VISIBLE);
			}

		});
	}

	@Override
	public void stateChange(WifiStatus status) {
		mAuthLayout.changeState(status);
		if (status == WifiStatus.CONNECTED) {
			mLoginView.setVisibility(View.VISIBLE);
		} else if (status == WifiStatus.AUTHED) {
			mLoginView.setVisibility(View.GONE);
		}
	}

	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		if (isChecked) {
			mWifiStatusManager.openWifi();
		} else {
			mWifiStatusManager.closeWifi();
		}

	}

}
