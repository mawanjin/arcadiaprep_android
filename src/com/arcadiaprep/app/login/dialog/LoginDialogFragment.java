
package com.arcadiaprep.app.login.dialog;

import com.arcadiaprep.app.arca.event.EventDispatcher;
import com.arcadiaprep.app.arca.event.ApplicationEvent;
import com.arcadiaprep.app.arca.R;
import com.arcadiaprep.app.login.common.UserService;
import com.arcadiaprep.app.login.communication.Callback;
import com.arcadiaprep.app.login.communication.DataService;
import com.arcadiaprep.app.login.communication.Result;
import com.arcadiaprep.app.login.communication.model.DataLogin;
import com.arcadiaprep.app.login.communication.model.LoginResponse;
import com.arcadiaprep.app.login.log.Logger;
import com.arcadiaprep.app.login.model.User;
import com.arcadiaprep.app.login.widget.LoginView;

import android.app.Activity;
import android.app.DialogFragment;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class LoginDialogFragment extends CustomDialogFragment {
	private static final String TAG = Logger.getClassTag(LoginDialogFragment.class);
	private static final boolean debug = Logger.isDebugMode(LoginDialogFragment.class);

	private LoginView view;

	/* fragment related methods */

	/**
	 * Create a new instance of MyDialogFragment, providing "num"
	 * as an argument.
	 */
	static LoginDialogFragment newInstance(int num) {
		LoginDialogFragment f = new LoginDialogFragment();

		// Supply num input as an argument.
		Bundle args = new Bundle();
		args.putInt("num", num);
		f.setArguments(args);

		return f;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View v = inflater.inflate(R.layout.dialog_login, container, false);
		view = (LoginView) v.findViewById(R.id.idLoginView);
		view.setOnLoginListener(buttonDoListener);
		view.setOnRegisterListener(buttonRegisterListener);
		return v;
	}

	@Override
	public void onStart() {
		super.onStart();
		if (null != UserService.getUser(getActivity()))
			dismiss();
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
		DialogFragment newFragment = LoginDialogFragment.newInstance(mStackLevel);
		newFragment.show(ft, "dialog");
	}

	/* moved from LoginDialog */

	protected void callWebService() {

		// persist username/password
		showProgress();
		DataService.getInstance(getActivity()).doLogin(callback, view.getData());
	}

	private View.OnClickListener buttonRegisterListener = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			if (debug)
				Log.d(TAG, Logger.descName());

			RegistrationDialogFragment.showDialog(getActivity());
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
			LoginResponse response = null;

			try {
				response = LoginResponse.parse(result.getResult());
				if (response.isLoggedIn()) {

					// success

					// save to singleton and SharedPreferences
					DataLogin data = view.getData();
					User user = new User(data.getUserName(), data.getUserPassword(),
							response.getProfile());
					UserService.setUser(getActivity(), user);

					dispatcher.fireEvent(new ApplicationEvent(response,
							ApplicationEvent.TYPE_LOGIN_SUCCESS, null));
					dismiss();

				} else {

					// fail
					dispatcher.fireEvent(new ApplicationEvent(new Object(),
							ApplicationEvent.TYPE_LOGIN_FAIL, response.getErrorMessage()));
					showErrorDialog(CustomDialogFragment.Dialog.LOGIN_ERROR,
							response.getErrorMessage());
				}

			} catch (IllegalArgumentException e) {
				Log.w(TAG, Logger.desc() + "Can't parse login response! Exception: " + e);

				// unknown problem
				dispatcher.fireEvent(new ApplicationEvent(new Object(),
						ApplicationEvent.TYPE_LOGIN_FAIL, null));
				showErrorDialog(CustomDialogFragment.Dialog.LOGIN_ERROR, null);
			}
		}
	};
}
