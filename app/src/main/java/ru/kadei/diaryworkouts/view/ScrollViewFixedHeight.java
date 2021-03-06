package ru.kadei.diaryworkouts.view;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.widget.ScrollView;
import android.widget.Scroller;
import android.widget.TableLayout;

import static android.view.View.MeasureSpec.AT_MOST;
import static android.view.View.MeasureSpec.makeMeasureSpec;

/**
 * Created by kadei on 02.06.2015.
 */
public class ScrollViewFixedHeight extends ScrollView {

    int mMaxHeight = -1;

    public ScrollViewFixedHeight(Context context) {
        super(context);
    }

    public ScrollViewFixedHeight(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    /** @param factor Must be from 0 to 1 (max height display) */
    public void setMaxHeight(Activity activity, float factor) {
        DisplayMetrics metrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(metrics);
        float h = metrics.heightPixels;
        h *= factor;
        mMaxHeight = (int) h;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if(mMaxHeight != -1) heightMeasureSpec = makeMeasureSpec(mMaxHeight, AT_MOST);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }
}
