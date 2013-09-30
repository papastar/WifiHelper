package com.richinfo.wifihelper.Data;

import android.app.DownloadManager;
import android.app.DownloadManager.Request;
import android.content.Context;
import android.net.Uri;
import android.os.Environment;

public class DownLoadManager {

	private final static String DOWN_THINKMAIL_URL = "http://mail.richinfo.cn/AppClientUpgrade/app/thinkmail";
	private final static String DOWN_THINKDRIVE_URL = "http://mail.richinfo.cn/AppClientUpgrade/app/thinkdrive";

	private DownloadManager manager;

	public DownLoadManager(Context context) {
		manager = (DownloadManager) context
				.getSystemService(Context.DOWNLOAD_SERVICE);
	}

	public void downLoadThinkMail() {
		Uri uri = Uri.parse(DOWN_THINKMAIL_URL);
		DownloadManager.Request request = new Request(uri);
		try {
			request.setDestinationInExternalPublicDir(
					Environment.DIRECTORY_DOWNLOADS, "thinkmail.apk");
			request.setTitle("ÏÂÔØThinkMail...");
			long id = manager.enqueue(request);
			DataManager.getInstance().setThinkMailDownId(id);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	
	
	public void downLoadThinkDrive() {
		Uri uri = Uri.parse(DOWN_THINKDRIVE_URL);
		DownloadManager.Request request = new Request(uri);
		try {
			request.setDestinationInExternalPublicDir(
					Environment.DIRECTORY_DOWNLOADS, "thinkdrive.apk");
			request.setTitle("ÏÂÔØThinkDrive...");
			long id = manager.enqueue(request);
			DataManager.getInstance().setThinkDriveDownId(id);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
