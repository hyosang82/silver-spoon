package c.m.mobile8.utils;

/**
 * Created by JunLee on 2016-05-28.
 */
public class BSTextUtil {
    public static boolean isEmpty(String str) {
        if (str != null && str.length() != 0 && !str.equals("null")) {
            return false;
        } else {
            return true;
        }
    }
}
