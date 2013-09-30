package com.richinfo.wifihelper.Data;

import com.richinfo.wifihelper.AppInstance;

import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;
import android.text.TextUtils;

public class DataManager {

	private static final String USERNAME_KEY = "username";
	private static final String PASSWORD_KEY = "password";
	private static final String AUTO_AUTH_KEY = "auto_auth";
	private static final String SHOW_NOTIFY_KEY = "show_notify";
	private static final String DOWNLOAD_THINKMAIL ="down_thinkmail";
	private static final String DOWNLOAD_THINKDRIVE ="down_thinkdrive";
	
	private SharedPreferences mPreference;

	private static DataManager mInstance;

	private DataManager() {
		mPreference = PreferenceManager.getDefaultSharedPreferences(AppInstance
				.getInstance());
	}

	public static DataManager getInstance() {
		if (mInstance == null)
			mInstance = new DataManager();
		return mInstance;
	}

	public void putAccount(Account account) {
		Editor editor = mPreference.edit();
		editor.putString(USERNAME_KEY, account.getUsername());
		editor.putString(PASSWORD_KEY, account.getPassword());
		editor.commit();
	}

	public boolean hasAccount() {
		return !TextUtils.isEmpty(mPreference.getString(USERNAME_KEY, null));
	}

	public Account getAccount() {
		Account account = null;
		String username = mPreference.getString(USERNAME_KEY, null);
		if (!TextUtils.isEmpty(username)) {
			account = new Account();
			account.setUsername(username);
			account.setPassword(mPreference.getString(PASSWORD_KEY, null));
		}
		return account;
	}

	public boolean checkAutoAuth() {
		return mPreference.getBoolean(AUTO_AUTH_KEY, true);
	}

	public void setAutoAuth(boolean authAuto) {
		Editor editor = mPreference.edit();
		editor.putBoolean(AUTO_AUTH_KEY, authAuto);
		editor.commit();
	}

	public boolean checkShowNotify() {
		return mPreference.getBoolean(SHOW_NOTIFY_KEY, true);
	}

	public void setShowNotify(boolean showNotify) {
		Editor editor = mPreference.edit();
		editor.putBoolean(SHOW_NOTIFY_KEY, showNotify);
		editor.commit();
	}
	
	public long getThinkMailDownId(){
		return mPreference.getLong(DOWNLOAD_THINKMAIL, -1);
	}
	
	public void setThinkMailDownId(long id){
		Editor editor = mPreference.edit();
		editor.putLong(DOWNLOAD_THINKMAIL, id);
		editor.commit();
	}
	
	public long getThinkDriveDownId(){
		return mPreference.getLong(DOWNLOAD_THINKDRIVE, -1);
	}
	
	public void setThinkDriveDownId(long id){
		Editor editor = mPreference.edit();
		editor.putLong(DOWNLOAD_THINKDRIVE, id);
		editor.commit();
	}
}
