
package com.arcadiaprep.app.login.widget;

import com.arcadiaprep.app.arca.R;
import com.arcadiaprep.app.login.common.Constants;
import com.arcadiaprep.app.login.communication.model.DataRegister;
import com.arcadiaprep.app.login.log.Logger;
import com.arcadiaprep.app.login.util.StringUtil;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class RegistrationView extends LinearLayout {
	private static final String TAG = Logger.getClassTag(RegistrationView.class);

	private Button buttonCancel;
	private Button buttonRegister;

	private ImageView image;
	private TextView imageText;

	private EditText editTextFirstName;
	private EditText editTextLastName;
	private EditText editTextEmail;
	private EditText editTextPassword;
	private EditText editTextRepeatPassword;

	public RegistrationView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initUi();
	}

	private void initUi() {
		LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(
				Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.dialog_widget_registration, this);

		image = (ImageView) findViewById(R.id.idPhoto);
		imageText = (TextView) findViewById(R.id.idPhotoText);

		editTextFirstName = (EditText) findViewById(R.id.idEditTextFirstName);
		editTextLastName = (EditText) findViewById(R.id.idEditTextLastName);
		editTextEmail = (EditText) findViewById(R.id.idEditTextEmail);
		editTextPassword = (EditText) findViewById(R.id.idEditTextPassword);
		editTextRepeatPassword = (EditText) findViewById(R.id.idEditTextPasswordAgain);

		buttonCancel = (Button) findViewById(R.id.idButtonCancel);
		buttonRegister = (Button) findViewById(R.id.idButtonRegister);

		// for testing purposes only
		if (Constants.TEST_POPUPLATE_FORMS)
			popuplateTestData();
	}

	public void setOnPictureListener(View.OnClickListener listener) {
		image.setOnClickListener(listener);
	}

	public void setOnCancelListener(View.OnClickListener cancelListener) {
		buttonCancel.setOnClickListener(cancelListener);
	}

	public void setOnRegisterListener(View.OnClickListener registerListener) {
		buttonRegister.setOnClickListener(registerListener);
	}

	public DataRegister getData() {
		return new DataRegister(
				editTextFirstName.getText().toString(),
				editTextLastName.getText().toString(),
				editTextEmail.getText().toString(),
				editTextPassword.getText().toString(),
				editTextRepeatPassword.getText().toString(),
				StringUtil.EMPTY); // TODO what to do with "exam" ? where does it come from?
	}

	/**
	 * For testing purposes only
	 */
	private void popuplateTestData() {
		editTextFirstName.setText(Constants.TEST_FIRST_NAME);
		editTextLastName.setText(Constants.TEST_LAST_NAME);
		editTextEmail.setText(Constants.TEST_EMAIL_NAME);
		editTextPassword.setText(Constants.TEST_PASSWORD);
		editTextRepeatPassword.setText(Constants.TEST_REPEAT_PASSWORD);
	}

	public void setPhoto(String fileSrc) {
		try {
			Bitmap bitmap = BitmapFactory.decodeFile(fileSrc);
			image.setImageBitmap(bitmap);
			imageText.setText(null);
		} catch (Exception e) {
			Log.w(TAG, Logger.desc() + "Can't set photo from file! Exception: " + e);
		}
	}
	
	/**
	 * Use only for popup menu purposes.
	 * @return
	 */
	public ImageView getPhotoView() {
		return image;
	}
}
