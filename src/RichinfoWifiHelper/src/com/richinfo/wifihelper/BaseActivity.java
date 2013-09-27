package com.richinfo.wifihelper;

import com.umeng.analytics.MobclickAgent;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

public class BaseActivity extends FragmentActivity {

	@Override
	protected void onCreate(Bundle arg0) {

		super.onCreate(arg0);
	}

	@Override
	protected void onDestroy() {

		super.onDestroy();
	}

	@Override
	protected void onResume() {

		super.onResume();
		MobclickAgent.onResume(this);
	}

	@Override
	protected void onPause() {

		super.onPause();
		MobclickAgent.onPause(this);
	}

}
