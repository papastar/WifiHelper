package com.richinfo.wifihelper.ui;

import com.richinfo.wifihelper.R;
import com.richinfo.wifihelper.WifiStatus;
import com.richinfo.wifihelper.Data.WifiStatusManager;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

public class AuthLayout extends FrameLayout {

	private TextView mWifiSsidText;
	private TextView mWifiStatusText;
	private WifiStatus mStatus;

	public AuthLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	private void init() {
		View view = LayoutInflater.from(getContext()).inflate(
				R.layout.auth_layout, null);
		LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.WRAP_CONTENT);
		params.gravity = Gravity.CENTER;
		addView(view, params);
		mWifiSsidText = (TextView) findViewById(R.id.wifi_ssid_text);
		mWifiStatusText = (TextView) findViewById(R.id.wifi_status_text);
	}

	public void changeState(WifiStatus status) {
		mStatus = status;
		switch (status) {
		case DISABLE:
			setEnabled(false);
			mWifiSsidText.setText("null");
			mWifiStatusText.setText(R.string.disable);
			break;
		case ENABLE:
			setEnabled(true);
			mWifiSsidText.setText("null");
			mWifiStatusText.setText(R.string.enable);
			break;
		case CONNECTED:
			setEnabled(true);
			mWifiSsidText.setText(WifiStatusManager.getInstance().getBSSID());
			mWifiStatusText.setText(R.string.connected);
			break;
		case AUTHED:
			mWifiSsidText.setText(WifiStatusManager.getInstance().getBSSID());
			mWifiStatusText.setText(R.string.authed);
			setEnabled(true);
			break;
		default:
			break;
		}
	}

	public WifiStatus getCurrentStatus() {
		return mStatus;
	}

}
