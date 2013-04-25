
package com.arcadiaprep.app.login.widget;

import com.arcadiaprep.app.arca.R;
import com.arcadiaprep.app.login.common.Constants;
import com.arcadiaprep.app.login.communication.model.DataLogin;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

public class LoginView extends LinearLayout {

	private Button buttonLogin;
	private Button buttonRegister;

	private EditText editTextUserName;
	private EditText editTextPassword;

	public LoginView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initUi();
	}

	private void initUi() {
		LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(
				Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.dialog_widget_login, this);

		editTextUserName = (EditText) findViewById(R.id.idEditTextUserName);
		editTextPassword = (EditText) findViewById(R.id.idEditTextPassword);

		buttonLogin = (Button) findViewById(R.id.idButtonLogin);
		buttonRegister = (Button) findViewById(R.id.idButtonRegister);

		// for testing purposes only
		if (Constants.TEST_POPUPLATE_FORMS)
			popuplateTestData();
	}

	public void setOnLoginListener(View.OnClickListener listener) {
		buttonLogin.setOnClickListener(listener);
	}

	public void setOnRegisterListener(View.OnClickListener listener) {
		buttonRegister.setOnClickListener(listener);
	}

	public DataLogin getData() {
		return new DataLogin(
				editTextUserName.getText().toString(),
				editTextPassword.getText().toString());
	}

	/**
	 * For testing purposes only
	 */
	//TODO remove on production!
	private void popuplateTestData() {
		editTextUserName.setText(Constants.TEST_USERNAME);
		editTextPassword.setText(Constants.TEST_PASSWORD);
	}
}
