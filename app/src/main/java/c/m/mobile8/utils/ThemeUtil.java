package c.m.mobile8.utils;

import android.content.Context;
import android.graphics.Color;

import c.m.mobile8.R;


/**
 * Created by JunLee on 2016-05-28.
 */
public class ThemeUtil {
    public static enum MIDAS_THEME {
        THEME_INDIGO, THEME_RED, THEME_PINK, THEME_PURPLE, THEME_DEEP_PURPLE, THEME_BLUE, THEME_LIGHT_BLUE, THEME_CYAN, THEME_TEAL, THEME_GREEN, THEME_LIGHT_GREEN, THEME_DEEP_ORANGE, THEME_BROWN, THEME_BLUE_GREY, THEME_BLACK
    }

    public static MIDAS_THEME getTheme(Context context) {
        //TODO : using shared preference
        MIDAS_THEME theme = SharedPreferenceManager.getPreferencesTheme(context);
        return theme;
    }

    public static void setTheme(Context context, MIDAS_THEME theme) {
        SharedPreferenceManager.setPreferenceTheme(context, theme);
    }

    public static int getMainColor(Context context, MIDAS_THEME theme) {
        int mainColor = Color.parseColor("#FF000000");
        switch (theme) {
            case THEME_INDIGO:
                mainColor = context.getResources().getColor(R.color.theme_color_indigo_main);
                break;
            case THEME_BLACK:
                mainColor = context.getResources().getColor(R.color.theme_color_black_main);
                break;
            case THEME_BLUE:
                mainColor = context.getResources().getColor(R.color.theme_color_blue_main);
                break;
            case THEME_BLUE_GREY:
                mainColor = context.getResources().getColor(R.color.theme_color_blue_grey_main);
                break;
            case THEME_BROWN:
                mainColor = context.getResources().getColor(R.color.theme_color_brown_main);
                break;
            case THEME_CYAN:
                mainColor = context.getResources().getColor(R.color.theme_color_cyan_main);
                break;
            case THEME_DEEP_ORANGE:
                mainColor = context.getResources().getColor(R.color.theme_color_deep_orange_main);
                break;
            case THEME_DEEP_PURPLE:
                mainColor = context.getResources().getColor(R.color.theme_color_deep_purple_main);
                break;
            case THEME_GREEN:
                mainColor = context.getResources().getColor(R.color.theme_color_green_main);
                break;
            case THEME_LIGHT_BLUE:
                mainColor = context.getResources().getColor(R.color.theme_color_light_blue_main);
                break;
            case THEME_LIGHT_GREEN:
                mainColor = context.getResources().getColor(R.color.theme_color_light_green_main);
                break;
            case THEME_PINK:
                mainColor = context.getResources().getColor(R.color.theme_color_pink_main);
                break;
            case THEME_PURPLE:
                mainColor = context.getResources().getColor(R.color.theme_color_purple_main);
                break;
            case THEME_RED:
                mainColor = context.getResources().getColor(R.color.theme_color_red_main);
                break;
            case THEME_TEAL:
                mainColor = context.getResources().getColor(R.color.theme_color_teal_main);
                break;
        }

        return mainColor;
    }

    public static int getSystemColor(Context context, MIDAS_THEME theme) {
        int systemColor = Color.parseColor("#FF000000");
        switch (theme) {
            case THEME_INDIGO:
                systemColor = context.getResources().getColor(R.color.theme_color_indigo_system);
                break;
            case THEME_BLACK:
                systemColor = context.getResources().getColor(R.color.theme_color_black_system);
                break;
            case THEME_BLUE:
                systemColor = context.getResources().getColor(R.color.theme_color_blue_system);
                break;
            case THEME_BLUE_GREY:
                systemColor = context.getResources().getColor(R.color.theme_color_blue_grey_system);
                break;
            case THEME_BROWN:
                systemColor = context.getResources().getColor(R.color.theme_color_brown_system);
                break;
            case THEME_CYAN:
                systemColor = context.getResources().getColor(R.color.theme_color_cyan_system);
                break;
            case THEME_DEEP_ORANGE:
                systemColor = context.getResources().getColor(R.color.theme_color_deep_orange_system);
                break;
            case THEME_DEEP_PURPLE:
                systemColor = context.getResources().getColor(R.color.theme_color_deep_purple_system);
                break;
            case THEME_GREEN:
                systemColor = context.getResources().getColor(R.color.theme_color_green_system);
                break;
            case THEME_LIGHT_BLUE:
                systemColor = context.getResources().getColor(R.color.theme_color_light_blue_system);
                break;
            case THEME_LIGHT_GREEN:
                systemColor = context.getResources().getColor(R.color.theme_color_light_green_system);
                break;
            case THEME_PINK:
                systemColor = context.getResources().getColor(R.color.theme_color_pink_system);
                break;
            case THEME_PURPLE:
                systemColor = context.getResources().getColor(R.color.theme_color_purple_system);
                break;
            case THEME_RED:
                systemColor = context.getResources().getColor(R.color.theme_color_red_system);
                break;
            case THEME_TEAL:
                systemColor = context.getResources().getColor(R.color.theme_color_teal_system);
                break;
        }
        return systemColor;
    }
}
