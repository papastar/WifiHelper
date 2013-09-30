package com.richinfo.wifihelper;

import com.richinfo.wifihelper.Data.DataManager;
import com.umeng.analytics.MobclickAgent;
import com.umeng.update.UmengUpdateAgent;
import com.umeng.update.UmengUpdateListener;
import com.umeng.update.UpdateResponse;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceActivity;

public class SettingPreferenceActivity extends PreferenceActivity implements
		OnPreferenceChangeListener, OnPreferenceClickListener {

	private DataManager mAccountManager;
	private Preference mVersionPreference;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		UmengUpdateAgent.update(this);
		mAccountManager = DataManager.getInstance();
		addPreferencesFromResource(R.xml.preference);
		// 设置版本号
		Preference versionPreference = findPreference("version_key");
		PackageInfo packageInfo;
		try {
			packageInfo = getPackageManager().getPackageInfo(getPackageName(),
					PackageManager.GET_CONFIGURATIONS);
			versionPreference.setSummary(packageInfo.versionName);
		} catch (PackageManager.NameNotFoundException e) {
			e.printStackTrace();
		}

		CheckBoxPreference autoAuthBoxPreference = (CheckBoxPreference) findPreference("autoauth");
		autoAuthBoxPreference.setChecked(mAccountManager.checkAutoAuth());
		autoAuthBoxPreference.setOnPreferenceChangeListener(this);

		CheckBoxPreference showNotifyBoxPreference = (CheckBoxPreference) findPreference("shownotify");
		showNotifyBoxPreference.setChecked(mAccountManager.checkShowNotify());
		showNotifyBoxPreference.setOnPreferenceChangeListener(this);

		mVersionPreference = findPreference("version");
		mVersionPreference.setOnPreferenceClickListener(this);

		UmengUpdateAgent.setUpdateAutoPopup(false);
		UmengUpdateAgent.setUpdateListener(new UmengUpdateListener() {

			@Override
			public void onUpdateReturned(int arg0, UpdateResponse arg1) {
				 switch (arg0) {
		            case 0: // has update
//		                UmengUpdateAgent.showUpdateDialog(mContext, updateInfo);
		            	mVersionPreference.setSummary("有新版本升级");
		                break;
		            case 1: // has no update
		            	mVersionPreference.setSummary("当前是最新版");
		                break;
		            case 2: // none wifi
		              
		                break;
		            case 3: // time out
		            
		                break;
		            }

			}
		});

	}

	@Override
	public boolean onPreferenceChange(Preference preference, Object newValue) {
		if ("autoauth".equalsIgnoreCase(preference.getKey())) {
			mAccountManager.setAutoAuth((Boolean) newValue);
			return true;
		} else if ("shownotify".equalsIgnoreCase(preference.getKey())) {
			mAccountManager.setShowNotify((Boolean) newValue);
			return true;
		}

		return false;
	}

	@Override
	public boolean onPreferenceClick(Preference preference) {
		UmengUpdateAgent.setUpdateAutoPopup(true);
		UmengUpdateAgent.forceUpdate(this);
		return true;
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
