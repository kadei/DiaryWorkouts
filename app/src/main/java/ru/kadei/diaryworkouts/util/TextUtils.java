package ru.kadei.diaryworkouts.util;

import android.content.res.ColorStateList;
import android.support.annotation.NonNull;
import android.widget.TextView;

/**
 * Created by kadei on 23.06.2015.
 */
public class TextUtils {

    public static String cutExcess(String str, String ending, int limit) {
        if(str.length() <= limit) return str;

        if(ending == null) return str.substring(0, limit - 1);
        else return str.substring(0, limit - ending.length() - 1) + ending;
    }

    public static void setTextColor(TextView tv, ColorStateList color) {
        if(color != tv.getTextColors()) tv.setTextColor(color);
    }

    public static void setText(@NonNull TextView tv, @NonNull String text) {
        CharSequence chs = tv.getText();
        if(chs == text) return;

        if(chs.length() == text.length()) {
            for(int i = 0, end = chs.length(); i < end; ++i) {
                if(chs.charAt(i) != text.charAt(i)) {
                    tv.setText(text);
                    return;
                }
            }
        }
        else tv.setText(text);
    }
}
