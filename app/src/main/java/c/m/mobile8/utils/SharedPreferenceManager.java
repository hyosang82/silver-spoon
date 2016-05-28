package c.m.mobile8.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;


public class SharedPreferenceManager {
    private static final String THEME_ID = "theme_id";
    private static final String ORDER_ID = "order_id";
    public static final int ORDER_BY_CREATED = 0;
    public static final int ORDER_BY_UPDATE = 1;
    public static final int ORDER_BY_THEME = 2;

    public static ThemeUtil.MIDAS_THEME getPreferencesTheme(Context context) {
        SharedPreferences prefs = PreferenceManager
                .getDefaultSharedPreferences(context);
        int theme = prefs.getInt(THEME_ID,
                ThemeUtil.MIDAS_THEME.THEME_BLACK.ordinal());
        return ThemeUtil.MIDAS_THEME.values()[theme];
    }

    public static void setPreferenceTheme(Context context, ThemeUtil.MIDAS_THEME theme) {
        SharedPreferences prefs = PreferenceManager
                .getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt(THEME_ID, theme.ordinal());
        editor.commit();
    }

    public static int getPreferencesOrder(Context context) {
        SharedPreferences prefs = PreferenceManager
                .getDefaultSharedPreferences(context);
        int result = prefs.getInt(ORDER_ID,ORDER_BY_UPDATE);
        return result;
    }

    public static void setPreferenceOrder(Context context, int orderType) {
        SharedPreferences prefs = PreferenceManager
                .getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt(ORDER_ID, orderType);
        editor.commit();
    }


}
