
package com.arcadiaprep.app.login.dialog;

import com.arcadiaprep.app.arca.event.EventDispatcher;
import com.arcadiaprep.app.arca.event.ApplicationEvent;
import com.arcadiaprep.app.arca.R;
import com.arcadiaprep.app.login.common.Constants;
import com.arcadiaprep.app.login.common.FileUtil;
import com.arcadiaprep.app.login.common.PackagesUtil;
import com.arcadiaprep.app.login.common.UserService;
import com.arcadiaprep.app.login.communication.Callback;
import com.arcadiaprep.app.login.communication.DataService;
import com.arcadiaprep.app.login.communication.Result;
import com.arcadiaprep.app.login.communication.model.DataLogin;
import com.arcadiaprep.app.login.communication.model.DataRegister;
import com.arcadiaprep.app.login.communication.model.LoginResponse;
import com.arcadiaprep.app.login.communication.model.RegistrationResponse;
import com.arcadiaprep.app.login.log.Logger;
import com.arcadiaprep.app.login.model.User;
import com.arcadiaprep.app.login.util.StringUtil;
import com.arcadiaprep.app.login.widget.RegistrationView;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DialogFragment;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.io.File;

public class RegistrationDialogFragment extends CustomDialogFragment {
	private static final String TAG = Logger.getClassTag(RegistrationDialogFragment.class);
	private static final boolean debug = Logger.isDebugMode(RegistrationDialogFragment.class);

	private static final String BUNDLE_PHOTOSRC = "photoSrc";

	private RegistrationView view;
	private String photoSrc;
	private AlertDialog alert;

	/* fragment related methods */

	/**
	 * Create a new instance of MyDialogFragment, providing "num"
	 * as an argument.
	 */
	static RegistrationDialogFragment newInstance(int num) {
		RegistrationDialogFragment f = new RegistrationDialogFragment();

		// Supply num input as an argument.
		Bundle args = new Bundle();
		args.putInt("num", num);
		f.setArguments(args);

		return f;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View v = inflater.inflate(R.layout.dialog_registration, container, false);
		view = (RegistrationView) v.findViewById(R.id.idRegistrationView);
		view.setOnPictureListener(pictureListener);
		view.setOnCancelListener(buttonCancelListener);
		view.setOnRegisterListener(buttonDoListener);

		if (null != savedInstanceState) {
			photoSrc = savedInstanceState.getString(BUNDLE_PHOTOSRC);
			if (!StringUtil.isEmpty(photoSrc))
				setPhoto(photoSrc);
		}

		return v;
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (debug)
			PackagesUtil.checkPackages(getActivity());
	}

	private static int mStackLevel;

	public static void showDialog(Activity f) {
		mStackLevel++;

		// DialogFragment.show() will take care of adding the fragment
		// in a transaction.  We also want to remove any currently showing
		// dialog, so make our own transaction and take care of that here.
		FragmentTransaction ft = f.getFragmentManager().beginTransaction();
		Fragment prev = f.getFragmentManager().findFragmentByTag("dialog");
		if (prev != null)
			ft.remove(prev);
		ft.addToBackStack(null);

		// Create and show the dialog.
		DialogFragment newFragment = RegistrationDialogFragment.newInstance(mStackLevel);
		newFragment.show(ft, "dialog");
	}

	/* moved from RegistrationDialog */

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		if (!StringUtil.isEmpty(photoSrc))
			outState.putString(BUNDLE_PHOTOSRC, photoSrc);
	}

	/* other */

	private void getPhotoFromGallery() {
		Intent photoPickerIntent = new Intent(Intent.ACTION_PICK,
				android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
		startActivityForResult(
				Intent.createChooser(photoPickerIntent,
						getActivity().getString(R.string.dialog_select_from_gallery)),
				REQUEST_SELECT_FROM_GALLERY);
	}

	private void getPhotoFromCamera() {
		Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		cameraIntent.putExtra(
					MediaStore.EXTRA_OUTPUT,
					Uri.fromFile(new File(FileUtil.getPhotoPathName())));
		startActivityForResult(cameraIntent, REQUEST_TAKE_PHOTO);
	}

	protected void callWebService() {
		showProgress();
		if (Constants.USE_FULL_UPLOAD_SCENARIO_ON_REGISTRATION)
			DataService.getInstance(getActivity()).doRegister(callbackRegisterLoginUpload,
					view.getData());
		else
			DataService.getInstance(getActivity()).doRegister(callback, view.getData());
	}

	public void setPhoto(String fileSrc) {
		photoSrc = fileSrc;
		view.setPhoto(fileSrc);
	}

	private AlertDialog getPhotoDialog() {
		final CharSequence[] items = {
				getString(R.string.dialog_select_from_gallery),
				getString(R.string.dialog_take_photo)
		};
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		builder.setItems(items, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int item) {
				switch (item) {
					case 0:
						getPhotoFromGallery();
						break;
					case 1:
						getPhotoFromCamera();
						break;
				}
			}
		});
		return builder.create();
	}

	/* listeners */

	private View.OnClickListener pictureListener = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			if (null == alert)
				alert = getPhotoDialog();
			alert.show();
		}
	};

	private View.OnClickListener buttonCancelListener = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			if (debug)
				Log.d(TAG, Logger.descName());
			dismiss();
		}

	};

	private Callback<Result> callback = new Callback<Result>() {
		@Override
		public void onFinish(Result result) {
			if (debug)
				Log.d(TAG, Logger.descName());

			if (!isVisible())
				return;

			dismissProgress();
			EventDispatcher dispatcher = EventDispatcher.getInstance();
			RegistrationResponse response = null;

			try {
				response = RegistrationResponse.parse(result.getResult());
				if (response.isRegistered()) {

					// success
					dispatcher.fireEvent(new ApplicationEvent(response,
							ApplicationEvent.TYPE_REGISTER_SUCCESS, null));
					dismiss();

				} else {

					// fail
					dispatcher.fireEvent(new ApplicationEvent(new Object(),
							ApplicationEvent.TYPE_REGISTER_FAIL, response.getErrorMessage()));
					showErrorDialog(CustomDialogFragment.Dialog.REGISTRATION_ERROR,
							response.getErrorMessage());
				}

			} catch (IllegalArgumentException e) {
				Log.w(TAG, Logger.desc() + "Can't parse register response! Exception: " + e);

				// unknown problem
				dispatcher.fireEvent(new ApplicationEvent(new Object(),
						ApplicationEvent.TYPE_REGISTER_FAIL, null));
				showErrorDialog(CustomDialogFragment.Dialog.REGISTRATION_ERROR, null);
			}
		}
	};

	/* Scenario: Select photo + Register + Login + upload photo */

	private Callback<Result> callbackRegisterLoginUpload = new Callback<Result>() {
		@Override
		public void onFinish(Result result) {
			if (debug)
				Log.d(TAG, Logger.descName());

			if (!isVisible())
				return;

			EventDispatcher dispatcher = EventDispatcher.getInstance();
			RegistrationResponse response = null;

			try {
				response = RegistrationResponse.parse(result.getResult());
				if (response.isRegistered()) {

					// success
					dispatcher.fireEvent(new ApplicationEvent(response,
							ApplicationEvent.TYPE_REGISTER_SUCCESS, null));

					DataRegister dataRegister = view.getData();
					DataLogin dataLogin = new DataLogin(dataRegister.getEmail(),
							dataRegister.getPassword());
					DataService.getInstance(getActivity()).doLogin(callbackLoginUpload, dataLogin);

				} else {

					// fail
					dismissProgress();
					dispatcher.fireEvent(new ApplicationEvent(new Object(),
							ApplicationEvent.TYPE_REGISTER_FAIL, response.getErrorMessage()));
					showErrorDialog(CustomDialogFragment.Dialog.REGISTRATION_ERROR,
							response.getErrorMessage());
				}

			} catch (IllegalArgumentException e) {
				Log.w(TAG, Logger.desc() + "Can't parse register response! Exception: " + e);

				// unknown problem
				dismissProgress();
				dispatcher.fireEvent(new ApplicationEvent(new Object(),
						ApplicationEvent.TYPE_REGISTER_FAIL, null));
				showErrorDialog(CustomDialogFragment.Dialog.REGISTRATION_ERROR, null);
			}
		}
	};

	private Callback<Result> callbackLoginUpload = new Callback<Result>() {
		@Override
		public void onFinish(Result result) {
			if (debug)
				Log.d(TAG, Logger.descName());

			if (!isVisible())
				return;

			EventDispatcher dispatcher = EventDispatcher.getInstance();
			LoginResponse response = null;

			try {
				response = LoginResponse.parse(result.getResult());
				if (response.isLoggedIn()) {

					// success

					// save to singleton and SharedPreferences
					DataRegister dataRegister = view.getData();
					DataLogin dataLogin = new DataLogin(dataRegister.getEmail(),
							dataRegister.getPassword());
					User user = new User(dataLogin.getUserName(), dataLogin.getUserPassword(),
							response.getProfile());
					UserService.setUser(getActivity(), user);

					dispatcher.fireEvent(new ApplicationEvent(response,
							ApplicationEvent.TYPE_LOGIN_SUCCESS, null));
					DataService.getInstance(getActivity()).doSendImage(callbackUpload, user);

				} else {

					// fail
					dismissProgress();
					dispatcher.fireEvent(new ApplicationEvent(new Object(),
							ApplicationEvent.TYPE_LOGIN_FAIL, response.getErrorMessage()));
					showErrorDialog(CustomDialogFragment.Dialog.LOGIN_ERROR,
							response.getErrorMessage());
				}

			} catch (IllegalArgumentException e) {
				Log.w(TAG, Logger.desc() + "Can't parse login response! Exception: " + e);

				// unknown problem
				dismissProgress();
				dispatcher.fireEvent(new ApplicationEvent(new Object(),
						ApplicationEvent.TYPE_LOGIN_FAIL, null));
				showErrorDialog(CustomDialogFragment.Dialog.LOGIN_ERROR, null);
			}
		}
	};

	private Callback<Result> callbackUpload = new Callback<Result>() {
		@Override
		public void onFinish(Result result) {
			dismissProgress();
			dismiss();
		}
	};
}
