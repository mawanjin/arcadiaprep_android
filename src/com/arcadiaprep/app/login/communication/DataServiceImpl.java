
package com.arcadiaprep.app.login.communication;

import com.arcadiaprep.app.login.common.UserService;
import com.arcadiaprep.app.login.communication.model.DataLogin;
import com.arcadiaprep.app.login.communication.model.DataRegister;
import com.arcadiaprep.app.login.communication.model.EntityHelper;
import com.arcadiaprep.app.login.model.User;

import android.content.Context;

public class DataServiceImpl extends DataService {

	/**
	 * Do not instantiate directly. It is used only in DataService.
	 * @param ctx Application or activity context
	 */
	protected DataServiceImpl(Context ctx) {
		super(ctx);
	}

	/** <i><b>callback.onFinish(result)</b></i> runs in main UI thread. */
	@Override
	public void doLogin(Callback<Result> callback, DataLogin data) {

		// TODO use SecureHttpGetService instead
		HttpPostService service = new HttpPostService(ctx, callback,
				EntityHelper.getLoginEntityParts(data));
		service.execute("jsonlogin/");
	}

	/** <i><b>callback.onFinish(result)</b></i> runs in main UI thread. */
	@Override
	public void doRegister(Callback<Result> callback, DataRegister data) {

		// TODO use SecureHttpGetService instead
		HttpPostService service = new HttpPostService(ctx, callback,
				EntityHelper.getRegisterEntityParts(data));
		service.execute("jsonregister/");
	}

	/** <i><b>callback.onFinish(result)</b></i> runs in main UI thread. */
	@Override
	public void doSendImage(Callback<Result> callback, User user) {
		HttpPostService service = new HttpPostPhotoService(ctx, callback,
				EntityHelper.getPhotoUploadEntityParts(user), UserService.getPhotoSrc(ctx));
		service.execute("network/add_new_picture/");
	}

}
