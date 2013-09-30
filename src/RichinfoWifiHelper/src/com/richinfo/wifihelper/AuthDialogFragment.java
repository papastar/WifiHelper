package com.richinfo.wifihelper;

import com.richinfo.wifihelper.Data.Account;
import com.richinfo.wifihelper.Data.DataManager;
import com.richinfo.wifihelper.Data.AuthConnectManager;
import com.richinfo.wifihelper.Data.NotifyManager;
import com.richinfo.wifihelper.common.net.AsyncHttpResponseHandler;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class AuthDialogFragment extends DialogFragment implements
		OnClickListener {

	private EditText mNameEdit;
	private EditText mPasswordEdit;

	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		View view = LayoutInflater.from(getActivity()).inflate(
				R.layout.dialog_auth_layout, null);
		mNameEdit = (EditText) view.findViewById(R.id.name_edit);
		mPasswordEdit = (EditText) view.findViewById(R.id.password_edit);
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		Dialog dialog = builder.setCancelable(true)
				.setPositiveButton(R.string.auth, this).setTitle(R.string.auth)
				.setNegativeButton(R.string.cancel, this).setView(view)
				.create();
		return dialog;
	}

	@Override
	public void onClick(DialogInterface dialog, int which) {
		switch (which) {
		case Dialog.BUTTON_POSITIVE:
			if (TextUtils.isEmpty(mNameEdit.getText().toString())){
//				Toast.makeText(getActivity(), R.string.input_name,
//						Toast.LENGTH_SHORT).show();
				}
			else {
				auth();
			
			}
			break;
		case Dialog.BUTTON_NEGATIVE:

			break;

		default:
			break;
		}
	}

	private void auth() {
		final MainActivity activity = (MainActivity) getActivity();
		final AuthConnectManager authConnectManager = AuthConnectManager
				.getInstance();
		authConnectManager.setBasicAuth(mNameEdit.getText().toString(),
				mPasswordEdit.getText().toString());
		authConnectManager.checkAuthConnect(new AsyncHttpResponseHandler() {

			@Override
			public void onSuccess(String content) {
				super.onSuccess(content);
				authConnectManager
						.checkNetConnect(new AsyncHttpResponseHandler() {

							@Override
							public void onSuccess(String content) {
								super.onSuccess(content);
								NotifyManager.getInstance().showNotification(
										WifiStatus.AUTHED);
								activity.changeWifiStatus(WifiStatus.AUTHED);
								Account account = new Account();
								account.setUsername(mNameEdit.getText()
										.toString());
								account.setPassword(mPasswordEdit.getText()
										.toString());
								DataManager.getInstance()
										.putAccount(account);
							}

							@Override
							public void onFailure(Throwable error,
									String content) {

								super.onFailure(error, content);
							}

						});
			}

			@Override
			public void onFailure(Throwable error, String content) {

				super.onFailure(error, content);
				Toast.makeText(getActivity(), " »œ÷§ ß∞‹", Toast.LENGTH_SHORT)
						.show();
			}

		});
	}
}
