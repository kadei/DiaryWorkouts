package ru.kadei.diaryworkouts.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;


public class CustomTextView extends TextView {

    public CustomTextView(Context context) {
        this(context, null);
    }

    public CustomTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setTypeface(CustomView.obtainTypeface(context, attrs));
    }
}
