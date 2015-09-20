package ru.kadei.diaryworkouts.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.widget.LinearLayout;

import static android.graphics.Color.argb;
import static android.graphics.Color.blue;
import static android.graphics.Color.green;
import static android.graphics.Color.red;
import static android.graphics.Color.rgb;


public class SlidingTabStrip extends LinearLayout {

    public static final int  DEFAULT_BOTTOM_BORDER_THICKNESS_DIPS = 1;
    public static final byte DEFAULT_BOTTOM_BORDER_ALPHA =          0x26;
    public static final int  SELECTED_INDICATOR_THICKNESS_DIPS =    4;
    public static final int  DEFAULT_SELECTED_INDICATOR_COLOR =     0xFF33B5E5;

    public static final int DEFAULT_COLOR_EVEN_ITEMS =  0xffff0000;
    public static final int DEFAULT_COLOR_ODD_ITEMS =   0xff00ff00;

    public static final int   DEFAULT_DIVIDER_THICKNESS_DIPS =  1;
    public static final byte  DEFAULT_DIVIDER_COLOR_ALPHA =     0x20;
    public static final float DEFAULT_DIVIDER_HEIGHT =          0.5f;


    private final int   bottomBorderThickness;
    private final Paint bottomBorderPaint;

    private final int   selectedIndicatorThickness;
    private final Paint selectedIndicatorPaint;

    private final int   defaultBottomBorderColor;

    private final Paint dividerPaint;
    private final float dividerHeight;

    private int     selectedPosition;
    private float   selectionOffset;

    public SlidingTabStrip(Context context) {
        this(context, null);
    }

    public SlidingTabStrip(Context context, AttributeSet attrs) {
        super(context, attrs);

        setWillNotDraw(false);

        final float density = getResources().getDisplayMetrics().density;

        TypedValue outValue = new TypedValue();
        context.getTheme().resolveAttribute(android.R.attr.colorForeground, outValue, true);
        final int themeForeground = outValue.data;

        defaultBottomBorderColor = setColorAlpha(themeForeground, DEFAULT_BOTTOM_BORDER_ALPHA);

        bottomBorderThickness = (int) (DEFAULT_BOTTOM_BORDER_THICKNESS_DIPS * density);
        bottomBorderPaint = new Paint();
        bottomBorderPaint.setColor(defaultBottomBorderColor);

        selectedIndicatorThickness = (int) (SELECTED_INDICATOR_THICKNESS_DIPS * density);
        selectedIndicatorPaint = new Paint();

        dividerHeight = DEFAULT_DIVIDER_HEIGHT;
        dividerPaint = new Paint();
        dividerPaint.setStrokeWidth((int) (DEFAULT_DIVIDER_THICKNESS_DIPS * density));
    }

    void onViewPagerPageChanged(int pos, float posOffset) {
        selectedPosition = pos;
        selectionOffset = posOffset;
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        final int height = getHeight();
        final int childCount = getChildCount();
        final int dividerHeghtPx = (int) (Math.min(Math.max(0f, dividerHeight), 1f) * height);

        if(childCount > 0) {
            View selectedTitle = getChildAt(selectedPosition);
            int left = selectedTitle.getLeft();
            int right = selectedTitle.getRight();
            int color = getColor(selectedPosition);

            if(selectionOffset >0f && selectedPosition < (childCount - 1)) {
                int nextColor = getColor(selectedPosition + 1);
                if(color != nextColor)
                    color = blendColors(color, nextColor, selectionOffset);

                View nextTitle = getChildAt(selectedPosition + 1);
                left = (int) (selectionOffset * nextTitle.getLeft()
                        + (1f - selectionOffset) * left);
                right = (int) (selectionOffset * nextTitle.getRight()
                        + (1f - selectionOffset) * right);
            }

            selectedIndicatorPaint.setColor(color);
            canvas.drawRect(left, height - selectedIndicatorThickness,
                    right, height, selectedIndicatorPaint);
        }

//        canvas.drawRect(0f, height - bottomBorderThickness, getWidth(),
//                height, bottomBorderPaint);

        // Vertical separators
//        int separatorTop = (height - dividerHeghtPx) / 2;
//        for(int i = 0, end = childCount - 1; i < end; ++i) {
//            View child = getChildAt(i);
//            dividerPaint.setColor(getColor(i));
//            canvas.drawLine(child.getRight(), separatorTop,
//                    child.getRight(), dividerHeghtPx, dividerPaint);
//        }
    }

    private static int getColor(int pos) {
        return 0xfffafafa;
//        return pos % 2 == 0 ? DEFAULT_COLOR_EVEN_ITEMS : DEFAULT_COLOR_ODD_ITEMS;
    }

    private static int setColorAlpha(int color, byte alpha) {
        return argb(alpha, red(color), green(color), blue(color));
    }

    /**
     * @return if {@code ratio = 1} will return {@code color1}, {@code ratio = 0}
     * will return {@code color2}
     */
    synchronized public static int blendColors(int color1, int color2, float ratio) {
        final float inverseRation = 1f - ratio;
        float r = (red(color2) * ratio) + (red(color1) * inverseRation);
        float g = (green(color2) * ratio) + (green(color1) * inverseRation);
        float b = (blue(color2) * ratio) + (blue(color1) + inverseRation);
        return rgb((int) r, (int) g, (int) b);
    }
}
