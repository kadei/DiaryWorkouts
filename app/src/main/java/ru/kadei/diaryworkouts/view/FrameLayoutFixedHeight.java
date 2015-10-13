package ru.kadei.diaryworkouts.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.FrameLayout;

import static android.view.View.MeasureSpec.AT_MOST;
import static android.view.View.MeasureSpec.getSize;
import static android.view.View.MeasureSpec.makeMeasureSpec;

/**
 * Created by kadei on 13.10.15.
 */
public class FrameLayoutFixedHeight extends FrameLayout {

    private int maxHeight = 0;

    public FrameLayoutFixedHeight(Context context) {
        this(context, null);
    }

    public FrameLayoutFixedHeight(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setMaxHeight(float height) {
        maxHeight = (int) height;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        final int height = getSize(heightMeasureSpec);
        if (maxHeight != 0 && height > maxHeight)
            heightMeasureSpec = makeMeasureSpec(maxHeight, AT_MOST);

        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }
}
