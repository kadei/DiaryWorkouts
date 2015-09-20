package ru.kadei.diaryworkouts.view;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.TextView;

import ru.kadei.diaryworkouts.R;
import ru.kadei.diaryworkouts.fragments.CustomFragment;

public class SlidingTabLayout extends HorizontalScrollView {

    public static final int TITLE_OFFSET_DIPS =     24;
    public static final int TAB_VIEW_PADDING_DIPS = 16;
    public static final int TAB_VIEW_TEXT_SIZE_SP = 12;

    private int titleOffset;

    private int tabViewLayoutId = R.layout.item__tab;
    private int tabViewTextViewId = android.R.id.button1;

    private ViewPager viewPager;
    private ViewPager.OnPageChangeListener viewPagerListener;

    private SlidingTabStrip tabStrip;

    CustomFragment mPageChangedListener = null;

    public SlidingTabLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SlidingTabLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        setHorizontalScrollBarEnabled(false);
        setFillViewport(true);

        titleOffset = (int) (TITLE_OFFSET_DIPS * getResources().getDisplayMetrics().density);

        tabStrip = new SlidingTabStrip(context);
        addView(tabStrip, LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
    }

    public void setOnPageChangeListener(ViewPager.OnPageChangeListener listener) {
        viewPagerListener = listener;
    }

    public void setCustomTabView(int layoutResId, int textViewId) {
        tabViewLayoutId = layoutResId;
        tabViewTextViewId = textViewId;
    }

    public void setViewPager(ViewPager viewPager) {
        tabStrip.removeAllViews();

        this.viewPager = viewPager;
        if(viewPager != null) {
            viewPager.setOnPageChangeListener(new InternalViewPagerListener());
            populateTabStrip();
        }
    }

    private void populateTabStrip() {
        final PagerAdapter adapter = viewPager.getAdapter();
        final OnClickListener tabListener = new TabClickListener();

        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(
                Context.LAYOUT_INFLATER_SERVICE);

        for(int i = 0, end = adapter.getCount(); i < end; ++i) {
            View tabView = inflater.inflate(tabViewLayoutId, tabStrip, false);
            TextView tabTitleView = (TextView) tabView.findViewById(tabViewTextViewId);

            tabTitleView.setText(adapter.getPageTitle(i));
            tabView.setOnClickListener(tabListener);

            tabStrip.addView(tabView);
        }
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        if(viewPager != null)
            scrollToTab(viewPager.getCurrentItem(), 0);
    }

    private void scrollToTab(int tabIndex, int positionOffset) {
        final int childCount  = tabStrip.getChildCount();

        if(childCount == 0 || tabIndex < 0 || tabIndex >= childCount)
            return;

        View selectedChild = tabStrip.getChildAt(tabIndex);
        if(selectedChild != null) {
            int targetScrollX = selectedChild.getLeft() + positionOffset;

            if(tabIndex > 0 || positionOffset > 0)
                targetScrollX -= titleOffset;

            scrollTo(targetScrollX, 0);
        }
    }

    public void setOnPageChangedListener(CustomFragment fragment) {
        mPageChangedListener = fragment;
    }

    private class InternalViewPagerListener implements ViewPager.OnPageChangeListener {

        private int scrollState;

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            int childCount = tabStrip.getChildCount();

            if(childCount == 0 || position < 0 || position >= childCount)
                return;

            tabStrip.onViewPagerPageChanged(position, positionOffset);

            View selectedTitle = tabStrip.getChildAt(position);
            int extraOffset = selectedTitle != null
                    ? (int) (positionOffset * selectedTitle.getWidth())
                    : 0;

            scrollToTab(position, extraOffset);

            if(viewPagerListener != null)
                viewPagerListener.onPageScrolled(position, positionOffset, positionOffsetPixels);
        }

        @Override
        public void onPageSelected(int position) {
            if(scrollState == ViewPager.SCROLL_STATE_IDLE) {
                tabStrip.onViewPagerPageChanged(position, 0f);
                scrollToTab(position, 0);
            }

            if(viewPagerListener != null)
                viewPagerListener.onPageSelected(position);
        }

        @Override
        public void onPageScrollStateChanged(int state) {
            scrollState = state;
            if(viewPagerListener != null)
                viewPagerListener.onPageScrollStateChanged(state);
        }
    }

    private class TabClickListener implements OnClickListener {
        @Override
        public void onClick(View v) {

            for(int i = 0, end = tabStrip.getChildCount(); i < end; ++i) {
                if(v == tabStrip.getChildAt(i)) {
                    viewPager.setCurrentItem(i);
                    return;
                }
            }
        }
    }
}


