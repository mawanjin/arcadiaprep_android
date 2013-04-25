
package com.arcadiaprep.app.login.dialog;

import com.arcadiaprep.app.arca.event.EventDispatcher;
import com.arcadiaprep.app.arca.event.ApplicationEvent;
import com.arcadiaprep.app.arca.R;
import com.arcadiaprep.app.login.common.ConnectivityUtil;
import com.arcadiaprep.app.login.common.Constants;
import com.arcadiaprep.app.login.common.FileUtil;
import com.arcadiaprep.app.login.common.ResizeImageTask;
import com.arcadiaprep.app.login.common.UserService;
import com.arcadiaprep.app.login.communication.Callback;
import com.arcadiaprep.app.login.log.Logger;
import com.arcadiaprep.app.login.util.StringUtil;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DialogFragment;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore.MediaColumns;
import android.util.Log;
import android.view.View;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;

public abstract class CustomDialogFragment extends DialogFragment {
	private static final String TAG = Logger.getClassTag(CustomDialogFragment.class);
	private static final boolean debug = Logger.isDebugMode(CustomDialogFragment.class);

	protected static final int DIALOG_NO_CONNECTIVITY = 2;
	protected static final int DIALOG_LOGIN_FAIL = 3;
	protected static final int DIALOG_REGISTRATION_FAIL = 4;

	public static final int REQUEST_SELECT_FROM_GALLERY = 0;
	public static final int REQUEST_TAKE_PHOTO = 1;

	protected enum Dialog {
		CONNECTIVITY_ERROR, LOGIN_ERROR, REGISTRATION_ERROR;
	}

	int mNum;
	private ProgressDialog progress;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mNum = getArguments().getInt("num");
		setStyle(DialogFragment.STYLE_NO_TITLE, android.R.style.Theme_Holo_Light_Dialog);
	}

	protected void showErrorDialog(Dialog dialog, String message) {
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		builder.setCancelable(true);

		switch (dialog) {
			case CONNECTIVITY_ERROR:
				builder.setTitle(R.string.dialog_alert_title_connectivity_problem);
				builder.setMessage(R.string.dialog_alert_message_connectivity_problem);
				break;

			case LOGIN_ERROR:
				builder.setTitle(R.string.dialog_alert_title_login_problem);
				if (!StringUtil.isEmpty(message))
					builder.setMessage(message);
				else
					builder.setMessage(getString(R.string.dialog_alert_message_login_failed));
				break;

			case REGISTRATION_ERROR:
				builder.setTitle(R.string.dialog_alert_title_register_problem);
				if (!StringUtil.isEmpty(message))
					builder.setMessage(message);
				else
					builder.setMessage(getString(R.string.dialog_alert_message_register_failed));
				break;
		}

		builder.setIcon(android.R.drawable.ic_dialog_alert);
		builder.setPositiveButton(R.string.dialog_ok, null);
		builder.create().show();
	}

	protected void showProgress() {
		try {
			if (null != progress)
				progress.dismiss();
			progress = ProgressDialog.show(getActivity(), getString(R.string.dialog_loading),
					getString(R.string.dialog_wait), false, true, cancelListener);
		} catch (Exception e) {
			Log.w(TAG, Logger.desc() + "Can't show progress! Exception: " + e);
		}
	}

	protected void dismissProgress() {
		try {
			progress.dismiss();
		} catch (Exception e) {
			Log.w(TAG, Logger.desc() + "Can't dismiss progress! Exception: " + e);
		}
	}

	private OnCancelListener cancelListener = new OnCancelListener() {

		@Override
		public void onCancel(DialogInterface dialog) {
			dismiss();
		}

	};

	/* listener */

	protected final View.OnClickListener buttonDoListener = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			if (debug)
				Log.d(TAG, Logger.descName());

			EventDispatcher dispatcher = EventDispatcher.getInstance();

			if (!ConnectivityUtil.isConnected(getActivity())) {
				if (debug)
					Log.w(TAG, Logger.desc() + "no internet -> do nothing!");

				dispatcher.fireEvent(new ApplicationEvent(new Object(),
						ApplicationEvent.TYPE_NO_CONNECTIVITY, null));
				showErrorDialog(CustomDialogFragment.Dialog.CONNECTIVITY_ERROR, null);

			} else {
				showProgress();
				callWebService();
			}
		}
	};

	protected abstract void callWebService();

	/* other */

	/* moved from LoginBasicActivity */

	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (debug)
			Log.d(TAG, Logger.descName());

		switch (requestCode) {
			case REQUEST_SELECT_FROM_GALLERY:
				if (resultCode == Activity.RESULT_OK) {
					parseResultIntentData(data);
				} else {
					if (debug)
						Log.d(TAG, Logger.desc() + "resultCode(" + resultCode
								+ ") is NOT ok! Do nothing.");
				}
				break;
			case REQUEST_TAKE_PHOTO:
				if (resultCode == Activity.RESULT_OK) {
					handlePhoto(FileUtil.getPhotoPathName());
				} else {
					if (debug)
						Log.d(TAG, Logger.desc() + "resultCode(" + resultCode
								+ ") is NOT ok! Do nothing.");
				}
				break;
		}
	};

	private static final String PROVIDER_WRONG = "content://com.android.gallery3d.provider";
	private static final String PROVIDER_RIGHT = "content://com.android.sec.gallery3d.provider";

	//	private static final String PROVIDER_RIGHT = "content://com.google.android.gallery3d.provider";
	//	private static final String PROVIDER_RIGHT = "content://com.google.android.sec.gallery3d.provider";

	private void parseResultIntentData(Intent data) {
		if (null == data) {
			Log.w(TAG, Logger.desc() + "Can't get image from null intent! Do nothing.");
			return;
		}

		Uri selectedImage = data.getData();
		if (debug)
			Log.d(TAG, Logger.desc() + "Gallery data: " + selectedImage + "\nPROVIDER_WRONG: "
					+ PROVIDER_WRONG + "\nPROVIDER_RIGHT: " + PROVIDER_RIGHT);

		final String[] filePathColumn = {
				MediaColumns.DATA, MediaColumns.DISPLAY_NAME
		};

		if (
		//(Build.VERSION.SDK_INT == 11 || Build.VERSION.SDK_INT == 12)
		//&&
		selectedImage.toString().startsWith(PROVIDER_WRONG)) {
			selectedImage = Uri.parse(selectedImage.toString().replace(PROVIDER_WRONG,
					PROVIDER_RIGHT));
		}
		Cursor cursor = getActivity().getContentResolver().query(selectedImage, filePathColumn,
				null, null, null);
		if (cursor != null) {

			if (selectedImage.toString().startsWith(PROVIDER_RIGHT)) {

				// if it is a Picasa image on newer devices with OS 3.0 and up
				new DownloadImageTask(selectedImage).execute();

			} else {

				// it is a regular local image file
				cursor.moveToFirst();
				int columnIndex = cursor.getColumnIndex(MediaColumns.DATA);
				String filePath = cursor.getString(columnIndex);
				handlePhoto(filePath);
			}
			cursor.close();

		} else if (selectedImage != null && selectedImage.toString().length() > 0) {

			// If it is a picasa image on devices running OS prior to 3.0
			new DownloadImageTask(selectedImage).execute();
		}
	}

	private class DownloadImageTask extends AsyncTask<Void, Void, Void> {
		private Uri url;

		public DownloadImageTask(Uri url) {
			this.url = url;
		}

		@Override
		protected Void doInBackground(Void... params) {
			File f = new File(FileUtil.getPhotoDirectory(), Constants.PHOTO_NAME);

			InputStream is = null;
			OutputStream os = null;
			try {
				if (url.toString().startsWith(PROVIDER_RIGHT)) {
					is = getActivity().getContentResolver().openInputStream(url);
				} else {
					is = new URL(url.toString()).openStream();
				}
				os = new FileOutputStream(f);

				// copy from input to output stream
				byte[] buffer = new byte[1024];
				int len;
				while ((len = is.read(buffer)) != -1) {
					os.write(buffer, 0, len);
				}

			} catch (Exception ex) {
				Log.w(TAG,
						Logger.desc() + "Can't get image from content provider! Exception: "
								+ ex.getMessage());
				return null;

			} finally {
				// close streams safely
				if (null != is)
					try {
						is.close();
					} catch (IOException e) {
						Log.w(TAG,
								Logger.desc() + "Can't close input source! Exception: "
										+ e.getMessage());
					}
				if (null != os)
					try {
						os.close();
					} catch (IOException e) {
						Log.w(TAG,
								Logger.desc() + "Can't close output source! Exception: "
										+ e.getMessage());
					}
			}
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
			handlePhoto(FileUtil.getPhotoPathName());
		}
	};

	private void handlePhoto(String fileSrc) {
		final String fileDst = FileUtil.getPhotoPathName();
		new ResizeImageTask(fileSrc, fileDst, new Callback<Boolean>() {
			@Override
			public void onFinish(Boolean result) {
				setPhoto(fileDst);
				UserService.persistPhotoSrc(getActivity(), fileDst);
			}
		}).execute();
	}

	/**
	 * Implement in RegistrationDialogFragment
	 * @param fileSrc
	 */
	public void setPhoto(String fileSrc) {
	}
}
