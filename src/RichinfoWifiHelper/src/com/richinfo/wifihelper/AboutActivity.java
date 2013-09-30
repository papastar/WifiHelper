package com.richinfo.wifihelper;

import com.richinfo.wifihelper.Data.DownLoadManager;
import com.umeng.update.UmengUpdateAgent;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

public class AboutActivity extends BaseActivity implements OnClickListener {

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		UmengUpdateAgent.update(this);
		setContentView(R.layout.activity_about);
		findViewById(R.id.close_img).setOnClickListener(this);
		findViewById(R.id.thinkdrive_app_img).setOnClickListener(this);
		findViewById(R.id.thinkmail_app_img).setOnClickListener(this);

	}

	@Override
	public void onClick(View v) {
		DownLoadManager manager = new DownLoadManager(this);
		switch (v.getId()) {
		case R.id.close_img:
			finish();
			break;
		case R.id.thinkdrive_app_img:
			manager.downLoadThinkDrive();
			break;
		case R.id.thinkmail_app_img:
			manager.downLoadThinkMail();
			break;
		default:
			break;
		}

	}

}
