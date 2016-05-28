package c.m.mobile8.constants;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Environment;

import java.io.File;

/**
 * Created by JunLee on 5/28/16.
 */
public class MConstants {
    public static boolean isDEBUG = true;
    // DataBase

    public static String getDataBasePath(Context context) {
        PackageManager packageManaeger = context.getPackageManager();
        String packageName = context.getPackageName();
        String appDataPath = "";
        try {
            PackageInfo packageInfo = packageManaeger.getPackageInfo(
                    packageName, 0);
            appDataPath = packageInfo.applicationInfo.dataDir + "/";
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        return appDataPath;
    }

    public static String getTmpPath(Context context) {
        File path = Environment
                .getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);

        String dbPath = path.getParent() + "/MidasChallenge/";
        File result = new File(dbPath);
        result.mkdirs();
        return result.getPath() + "/";

    }


    public static final String MY_DB_NAME = "midas8.db";
}
