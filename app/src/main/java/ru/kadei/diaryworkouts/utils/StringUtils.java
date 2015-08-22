package ru.kadei.diaryworkouts.utils;

/**
 * Created by kadei on 22.08.15.
 */
public class StringUtils {

    public static String extractNameClass(Class c) {
        return extractNameClass(c.getName());
    }

    public static String extractNameClass(String fullName) {
        int dotIndex = fullName.indexOf('.', 0);
        int start = 0;
        while (dotIndex != -1) {
            start = dotIndex + 1;
            dotIndex = fullName.indexOf('.', start);
        }
        return fullName.substring(start);
    }
}
