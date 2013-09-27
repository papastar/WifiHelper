package com.richinfo.wifihelper.Data;

import com.richinfo.wifihelper.AppInstance;
import com.richinfo.wifihelper.MainActivity;
import com.richinfo.wifihelper.R;
import com.richinfo.wifihelper.WifiStatus;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

public class NotifyManager {

	private NotificationManager manager;
	private static NotifyManager mNotifyManager;
	private int notifyId = 2;

	private NotifyManager() {
		manager = (NotificationManager) AppInstance.getInstance()
				.getSystemService(Context.NOTIFICATION_SERVICE);
	}

	public static NotifyManager getInstance() {
		if (mNotifyManager == null) {
			mNotifyManager = new NotifyManager();
		}

		return mNotifyManager;
	}

	private void showNotify(String title, String content) {
		Notification notification = new Notification();
		notification.icon = R.drawable.ic_launcher;
		Intent intent = new Intent(AppInstance.getInstance(),
				MainActivity.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

		PendingIntent pendingIntent = PendingIntent.getActivity(
				AppInstance.getInstance(), 0, intent, 0);
		notification.tickerText = "RichinfoWifi";
		notification.when = System.currentTimeMillis();
		notification.flags = Notification.FLAG_ONGOING_EVENT;
		notification.setLatestEventInfo(AppInstance.getInstance(), title,
				content, pendingIntent);
		manager.notify(notifyId, notification);

	}

	public void cancelNotify() {
		manager.cancel(notifyId);
	}

	public void showNotification(WifiStatus status) {
		AccountManager accountManager = AccountManager.getInstance();

		if (accountManager.checkShowNotify()) {
			switch (status) {
			case DISABLE:
				showNotify("Wifi未打开", "");
				break;
			case ENABLE:
				showNotify("Wifi已打开", "");
				break;
			case CONNECTED:
				showNotify("Wifi已连接", WifiStatusManager.getInstance()
						.getBSSID());
				break;
			case AUTHED:
				showNotify("Wifi已验证", WifiStatusManager.getInstance()
						.getBSSID());
				break;
			default:
				break;
			}
		}

	}

}
