
package com.arcadiaprep.app.login.common;

import com.arcadiaprep.app.login.communication.Callback;
import com.arcadiaprep.app.login.log.Logger;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.Matrix;
import android.os.AsyncTask;
import android.util.Log;

import java.io.FileOutputStream;

/**
 * Get image from SD card from source location downsample, decode, resize
 * and save. Largest dimensions are 640x480.
 */
public class ResizeImageTask extends AsyncTask<Void, Void, Boolean> {
	private static final String TAG = Logger.getClassTag(ResizeImageTask.class);
	private static final boolean debug = Logger.isDebugMode(ResizeImageTask.class);

	private String fileSrc;
	private String fileDst;
	private Callback<Boolean> callback;

	public ResizeImageTask(String fileSrc, String fileDst, Callback<Boolean> callback) {
		this.fileSrc = fileSrc;
		this.fileDst = fileDst;
		this.callback = callback;
	}

	@Override
	protected Boolean doInBackground(Void... arg0) {

		// load image
		Bitmap bitmap = null;
		try {

			Options options = new Options();
			options.inSampleSize = 4;
			options.inPurgeable = true;
			options.outMimeType = "jpeg";
			options.inInputShareable = true;
			options.inPreferredConfig = Config.RGB_565;
			
			bitmap = BitmapFactory.decodeFile(fileSrc, options);
			
		} catch (Exception e) {
			if (debug)
				Log.w(TAG, Logger.desc() + "can't decode bitmap! image probably too big. error: "
						+ e.getMessage(), e);
		}

		if (bitmap == null) {
			return false;
		}

		int height = bitmap.getHeight();
		int width = bitmap.getWidth();
		float scaleFactorHeight = ((float) 640) / height;
		float scaleFactorWidth = ((float) 480) / width;

		// create matrix for the manipulation and rescale
		Matrix matrix = new Matrix();
		if (scaleFactorHeight < scaleFactorWidth) {
			matrix.postScale(scaleFactorHeight, scaleFactorHeight);
		} else {
			matrix.postScale(scaleFactorWidth, scaleFactorWidth);
		}

		// recreate the new Bitmap 
		bitmap = Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, true);

		// save processed image
		try {
			FileOutputStream out = new FileOutputStream(fileDst);
			bitmap.compress(Bitmap.CompressFormat.JPEG, 80, out);
		} catch (Exception e) {
			Log.e(TAG, Logger.desc() + "File I/O error");
		}

		if (null != bitmap)
			bitmap.recycle(); // release memory

		return true;
	}

	@Override
	protected void onPostExecute(Boolean result) {
		super.onPostExecute(result);
		callback.onFinish(result);
	}
}
