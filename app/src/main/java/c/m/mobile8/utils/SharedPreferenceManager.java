package c.m.mobile8.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;


public class SharedPreferenceManager {
    private static final String THEME_ID = "theme_id";

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


}
