package ru.kadei.diaryworkouts.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.util.AttributeSet;

import ru.kadei.diaryworkouts.R;
import ru.kadei.diaryworkouts.activities.MainActivity;

import static android.graphics.Typeface.createFromAsset;
import static android.text.TextUtils.isEmpty;

public class CustomView {

    public static final String DEFAULT_FONT = "roboto_regular.ttf";

    public static Typeface obtainTypeface(Context c, AttributeSet attr) {

        TypedArray a = c.getTheme().obtainStyledAttributes(attr,
                R.styleable.CustomView, 0, 0);

        try {
            String fontName = a.getString(R.styleable.CustomView_font);

            if(isEmpty(fontName))
                fontName = DEFAULT_FONT;

            if(c instanceof MainActivity)
                return ((MainActivity)c).getResourceManager().getFont(fontName);
            else
                return createFromAsset(c.getAssets(), fontName);

        } finally {
            a.recycle();
        }
    }
}
