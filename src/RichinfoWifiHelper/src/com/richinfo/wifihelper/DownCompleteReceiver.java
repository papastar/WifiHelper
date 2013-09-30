package com.richinfo.wifihelper;

import com.richinfo.wifihelper.Data.DataManager;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;

public class DownCompleteReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		long reference = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID,
				-1);
		if (reference != -1) {
			long thinkmailId = DataManager.getInstance().getThinkMailDownId();
			long thindriveId = DataManager.getInstance().getThinkDriveDownId();
			if (reference == thindriveId) {
				installApp(thindriveId, context);
			} else if (reference == thinkmailId) {
				installApp(thinkmailId, context);
			}
		}

	}

	public void installApp(long id, Context context) {
		DownloadManager manager = (DownloadManager) context
				.getSystemService(Context.DOWNLOAD_SERVICE);
		DownloadManager.Query query = new DownloadManager.Query();
		query.setFilterById(id);
		Cursor cursor = manager.query(query);
		if (cursor != null)
			try {
				if (cursor.moveToFirst()) {
					int status = cursor.getInt(cursor
							.getColumnIndex(DownloadManager.COLUMN_STATUS));
					if (status == DownloadManager.STATUS_SUCCESSFUL) {
						String uri = cursor
								.getString(cursor
										.getColumnIndex(DownloadManager.COLUMN_LOCAL_URI));
						Intent intent = new Intent(Intent.ACTION_VIEW);
						intent.setDataAndType(Uri.parse(uri),
								"application/vnd.android.package-archive");
						intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
						context.startActivity(intent);
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				cursor.close();
				cursor = null;
			}
	}
}
