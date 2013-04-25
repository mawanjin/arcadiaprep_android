
package com.arcadiaprep.app.login.common;

import com.arcadiaprep.app.login.dialog.LoginDialogFragment;
import com.arcadiaprep.app.login.dialog.RegistrationDialogFragment;

import android.app.Activity;
import android.content.Context;

public class Login {

	public static final void login(Activity activity) {
		LoginDialogFragment.showDialog(activity);
	}

	public static final void register(Activity activity) {
		RegistrationDialogFragment.showDialog(activity);
	}

	public static final void logout(Context ctx) {
		UserService.setUser(ctx, null);
	}

}
