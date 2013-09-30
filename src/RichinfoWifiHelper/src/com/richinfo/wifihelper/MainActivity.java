package com.richinfo.wifihelper;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.TextView;
import com.richinfo.wifihelper.WifiStateReceiver.WifiStateChangeListener;
import com.richinfo.wifihelper.Data.Account;
import com.richinfo.wifihelper.Data.DataManager;
import com.richinfo.wifihelper.Data.AuthConnectManager;
import com.richinfo.wifihelper.Data.NotifyManager;
import com.richinfo.wifihelper.Data.WifiStatusManager;
import com.richinfo.wifihelper.common.net.AsyncHttpResponseHandler;
import com.richinfo.wifihelper.ui.SwitchButton;

public class MainActivity extends BaseActivity implements OnClickListener,
		WifiStateChangeListener, OnCheckedChangeListener {

	private AuthConnectManager mAuthConnectManager;
	private DataManager mAccountManager;
	private WifiStatusManager mWifiStatusManager;

	private WifiStatus mWifiStatus;

	private ImageView mAboutImg;
	private TextView mWifiInfoText;
	private SwitchButton mSwitchButton;
	private TextView mWifiStatusText;
	private Button mWifiStatusBtn;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mAuthConnectManager = AuthConnectManager.getInstance();
		mAccountManager = DataManager.getInstance();
		mWifiStatusManager = WifiStatusManager.getInstance();
		setContentView(R.layout.activity_main);
		initView();
		WifiStateReceiver.listeners.add(this);
		checkWifiState();
	}

	private void initView() {
		mAboutImg = (ImageView) findViewById(R.id.about_img);
		mAboutImg.setOnClickListener(this);
		mWifiInfoText = (TextView) findViewById(R.id.wifi_info_text);
		mSwitchButton = (SwitchButton) findViewById(R.id.wifi_switchbutton);
		mSwitchButton.setOnCheckedChangeListener(this);
		mWifiStatusText = (TextView) findViewById(R.id.wifi_status_text);
		mWifiStatusBtn = (Button) findViewById(R.id.wifi_status_btn);
		mWifiStatusBtn.setOnClickListener(this);
	}

	private void checkWifiState() {
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

		changeWifiStatus(status);
	}

	public void changeWifiStatus(WifiStatus status) {
		mWifiStatus = status;
		switch (status) {
		case DISABLE:
			mSwitchButton.setChecked(false);
			mWifiInfoText.setText(R.string.wifi_close);
			mWifiStatusText.setText(R.string.unauthed);
			mWifiStatusBtn.setText(R.string.auth);
			break;
		case ENABLE:
			mSwitchButton.setChecked(true);
			mWifiInfoText.setText(R.string.wifi_unconnect);
			mWifiStatusText.setText(R.string.unauthed);
			mWifiStatusBtn.setText(R.string.auth);
			break;
		case CONNECTED:
			mSwitchButton.setChecked(true);
			mWifiInfoText.setText(getString(R.string.wifi_connect) + " "
					+ mWifiStatusManager.getBSSID());
			mWifiStatusText.setText(R.string.unauthed);
			mWifiStatusBtn.setText(R.string.auth);
			break;
		case AUTHED:
			mSwitchButton.setChecked(true);
			mWifiInfoText.setText(getString(R.string.wifi_connect) + " "
					+ mWifiStatusManager.getBSSID());
			mWifiStatusText.setText(R.string.authed);
			mWifiStatusBtn.setText(R.string.change_account);
			break;

		default:
			break;
		}

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		return true;
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		WifiStateReceiver.listeners.remove(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.about_img:
			Intent intent = new Intent(this, AboutActivity.class);
			startActivity(intent);
			break;
		case R.id.wifi_status_btn:
			if (mWifiStatus == WifiStatus.CONNECTED
					|| mWifiStatus == WifiStatus.AUTHED)
				showAuthDialog();
			break;
		default:
			break;
		}

	}

	private void showAuthDialog() {
		AuthDialogFragment dialogFragment = new AuthDialogFragment();
		dialogFragment.show(getSupportFragmentManager(), "AuthDialogFragment");
	}

	private void setBasicAuth() {
		Account account = mAccountManager.getAccount();
		if (account != null) {
			mAuthConnectManager.setBasicAuth(account.getUsername(),
					account.getPassword());
		}
	}

	private void checkNetConnect() {
		setBasicAuth();
		mAuthConnectManager.checkNetConnect(new AsyncHttpResponseHandler() {

			@Override
			public void onSuccess(String content) {
				super.onSuccess(content);
			}

			@Override
			public void onSuccess(int statusCode, String content) {
				super.onSuccess(statusCode, content);
				changeWifiStatus(WifiStatus.AUTHED);
				NotifyManager.getInstance().showNotification(WifiStatus.AUTHED);
			}

			@Override
			public void onFailure(Throwable error, String content) {
				super.onFailure(error, content);
			}

		});
	}

	@Override
	public void stateChange(WifiStatus status) {
		changeWifiStatus(status);
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
