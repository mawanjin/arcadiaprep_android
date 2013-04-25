
package com.arcadiaprep.app.login.common;

import com.arcadiaprep.app.login.log.Logger;

import android.content.Context;
import android.net.ConnectivityManager;
import android.util.Log;

public class ConnectivityUtil {

    // for testing purposes only
    private static final String TAG = Logger.getClassTag(ConnectivityUtil.class);
    private static final boolean debug = Logger.isDebugMode(ConnectivityUtil.class);

    /**
     * Check if Internet connection is available.
     * 
     * @param ctx application context
     * @return connection state
     */
    public static boolean isConnected(Context ctx) {
        if (debug)
            Log.i(TAG, Logger.descName());
        ConnectivityManager conMgr = (ConnectivityManager) ctx
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        boolean connected = conMgr.getActiveNetworkInfo() != null &&
                            conMgr.getActiveNetworkInfo().isAvailable() &&
                            conMgr.getActiveNetworkInfo().isConnected();
        if (debug)
            Log.d(TAG, Logger.desc() + "connected: " + connected);
        if (connected) {
            return true;
        } else {
            return false;
        }
    }

}
