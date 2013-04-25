
package com.arcadiaprep.app.login.common;

import com.arcadiaprep.app.login.log.Logger;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ProviderInfo;
import android.util.Log;

import java.util.List;

public class PackagesUtil {
	private static final String TAG = Logger.getClassTag(PackagesUtil.class);

	public static void checkPackages(Context ctx) {
		try {
			List<PackageInfo> packs = ctx.getPackageManager().getInstalledPackages(
					PackageManager.GET_PROVIDERS);
			for (PackageInfo pack : packs) {
				ProviderInfo[] providers = pack.providers;
				if (providers != null) {
					for (ProviderInfo provider : providers) {
						Log.d(TAG, Logger.desc() + "provider: " + provider.authority);
					}
				}
			}
		} catch (Exception e) {
			Log.w(TAG, Logger.desc() + "Can't list installed packages! Exception: " + e, e);
		}
	}
}
