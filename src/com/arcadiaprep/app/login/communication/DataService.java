
package com.arcadiaprep.app.login.communication;

import com.arcadiaprep.app.login.communication.model.DataLogin;
import com.arcadiaprep.app.login.communication.model.DataRegister;
import com.arcadiaprep.app.login.log.Logger;
import com.arcadiaprep.app.login.model.User;

import android.content.Context;
import android.util.Log;

public abstract class DataService {

    // for testing purposes only
    private static final String TAG = Logger.getClassTag(DataService.class);
    private static final boolean debug = Logger.isDebugMode(DataService.class);

    protected Context ctx;

    DataService(Context ctx) {
        super();
        this.ctx = ctx;

        if (debug)
            Log.i(TAG, Logger.desc() + "DataService constructed");
    }

    public static DataService getInstance(Context ctx) {
        return new DataServiceImpl(ctx);
    }

    public abstract void doLogin(final Callback<Result> callback, DataLogin data);
    public abstract void doRegister(final Callback<Result> callback, DataRegister data);
    public abstract void doSendImage(final Callback<Result> callback, User user);

}
