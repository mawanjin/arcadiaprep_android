
package com.arcadiaprep.app.login.common;


import android.os.Environment;

import java.io.File;

public class FileUtil {

	public static final String getPhotoPathName() {

		StringBuilder sb = new StringBuilder(getPhotoDirectory());
		sb.append(Constants.PHOTO_NAME);
		return sb.toString();
	}

	public static final String getPhotoDirectory() {

		// build directory name
		StringBuilder sb = new StringBuilder();
		sb.append(Environment.getExternalStorageDirectory());
		sb.append(Constants.PHOTO_DIRECTORY);

		// create directory if not exist
		File file = new File(sb.toString());
		if (!file.exists())
			file.mkdirs();

		return sb.toString();
	}
}
