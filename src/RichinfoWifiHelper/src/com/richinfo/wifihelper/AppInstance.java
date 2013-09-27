package com.richinfo.wifihelper;

import android.app.Application;

public class AppInstance extends Application{

	private static AppInstance mInstance;
	
	@Override
	public void onCreate() {
		mInstance = this;
		super.onCreate();
	}
	
	public static AppInstance getInstance(){
		return mInstance;
	}
	
	@Override
	public void onTerminate() {
	
		super.onTerminate();
	}



	
	
}
