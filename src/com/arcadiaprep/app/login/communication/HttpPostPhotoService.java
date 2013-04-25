
package com.arcadiaprep.app.login.communication;

import com.arcadiaprep.app.login.common.EntityPart;
import com.arcadiaprep.app.login.communication.model.EntityHelper;

import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import android.content.Context;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.List;

public class HttpPostPhotoService extends HttpPostService {

	private String filePathName;

	public HttpPostPhotoService(Context ctx, Callback<Result> callback, List<EntityPart> parts,
			String filePathName) {
		super(ctx, callback, parts);
		this.filePathName = filePathName;
	}

	@Override
	protected MultipartEntity getMultipartEntity() throws UnsupportedEncodingException {
		MultipartEntity multipartEntity = super.getMultipartEntity();

		multipartEntity.addPart(EntityHelper.PICTURE, new FileBody(new File(filePathName),
				"image/jpeg"));

		return multipartEntity;
	}
}
