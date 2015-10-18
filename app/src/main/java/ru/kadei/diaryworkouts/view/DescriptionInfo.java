package ru.kadei.diaryworkouts.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.FrameLayout;
import android.widget.TextView;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

/**
 * Created by kadei on 18.10.15.
 */
public class DescriptionInfo extends FrameLayout {

    private TextView tvDescription;

    private final float density;

    public DescriptionInfo(Context context) {
        this(context, null);
    }

    public DescriptionInfo(Context context, AttributeSet attrs) {
        super(context, attrs);

        density = context.getResources().getDisplayMetrics().density;
    }

    private TextView createViewForDescription() {
        final FrameLayout.LayoutParams lp = new LayoutParams(MATCH_PARENT, WRAP_CONTENT);

        final TextView tv = new TextView(getContext());
        tv.setLayoutParams(lp);
        return tv;
    }
}
