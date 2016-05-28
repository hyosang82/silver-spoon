package c.m.mobile8.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.preference.PreferenceManager;

public class AppInfo {
    public Context context = null;
    private static AppInfo instance = null;

    private AppInfo(Context context) {
        this.context = context;
    }

    public static AppInfo getInstance(Context context) {
        if (instance == null) {
            instance = new AppInfo(context);
        }

        return instance;
    }

    public String getVersionName() {
        try {
            PackageInfo pi = context.getPackageManager().getPackageInfo(
                    context.getPackageName(), 0);
            return pi.versionName;
        } catch (NameNotFoundException e) {
            return null;
        }
    }

    public int getVersionCode() {
        try {
            PackageInfo pi = context.getPackageManager().getPackageInfo(
                    context.getPackageName(), 0);
            return pi.versionCode;
        } catch (NameNotFoundException e) {
            return -1;
        }
    }
}
