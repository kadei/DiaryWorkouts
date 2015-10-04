package ru.kadei.diaryworkouts.fragments;

import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.support.design.widget.NavigationView;
import android.util.SparseArray;

import ru.kadei.diaryworkouts.activities.MainActivity;

import static ru.kadei.diaryworkouts.util.primitive_collection.ArrayUtil.exists;

/**
 * Created by kadei on 26.09.15.
 */
public class NavigatorBuilder {

    private MainActivity activity = null;
    private final int[] ids = new int[5];

    private static final int UNDEFINED = -1;
    private int floatingButton = UNDEFINED;
    private SparseArray<Class<? extends CustomFragment>> fragments;

    private NavigationView.OnNavigationItemSelectedListener listener;

    private static final int LAYOUT_ID = 0;
    private static final int CONTAINER_ID = 1;
    private static final int TOOLBAR_ID = 2;
    private static final int DRAWER_ID = 3;
    private static final int LIST_ID = 4;

    public NavigatorBuilder() {
        activity = null;

        for (int i = 0; i < ids.length; ++i)
            ids[i] = -1;

        fragments = new SparseArray<>(8);
        listener = null;
    }

    public NavigatorBuilder setActivity(MainActivity activity) {
        this.activity = activity;
        return this;
    }

    public NavigatorBuilder setLayoutId(@LayoutRes int layoutId) {
        ids[LAYOUT_ID] = layoutId;
        return this;
    }

    public NavigatorBuilder setContainerId(@IdRes int containerId) {
        ids[CONTAINER_ID] = containerId;
        return this;
    }

    public NavigatorBuilder setToolbarId(@IdRes int toolbarId) {
        ids[TOOLBAR_ID] = toolbarId;
        return this;
    }

    public NavigatorBuilder setDrawerId(@IdRes int drawerId) {
        ids[DRAWER_ID] = drawerId;
        return this;
    }

    public NavigatorBuilder setListId(@IdRes int listId) {
        ids[LIST_ID] = listId;
        return this;
    }

    public NavigatorBuilder setFloatingButtonId(@IdRes int id) {
        floatingButton = id;
        return this;
    }

    public NavigatorBuilder setListenerUnbound(NavigationView.OnNavigationItemSelectedListener listener) {
        this.listener = listener;
        return this;
    }

    public NavigatorBuilder bind(@IdRes int id, Class<? extends CustomFragment> c) {
        fragments.put(id, c);
        return this;
    }

    public Navigator build() {
        validate();
        return new Navigator(activity,
                ids[LAYOUT_ID],
                ids[CONTAINER_ID],
                ids[TOOLBAR_ID],
                ids[DRAWER_ID],
                ids[LIST_ID],
                floatingButton,
                fragments,
                listener);
    }

    private void validate() {
        if (activity == null)
            throw new RuntimeException("builder contain null");
        if (invalidateId())
            throw new RuntimeException("builder contain invalidate id");
        if (fragments.size() == 0)
            throw new RuntimeException("builder does not contain fragments");
    }

    private boolean invalidateId() {
        return exists(ids, 0, ids.length, -1) >= 0;
    }
}
