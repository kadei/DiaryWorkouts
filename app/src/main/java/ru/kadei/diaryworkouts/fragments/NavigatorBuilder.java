package ru.kadei.diaryworkouts.fragments;

import android.graphics.drawable.Drawable;
import android.support.annotation.DrawableRes;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.StringRes;
import android.util.Pair;

import java.util.ArrayList;

import ru.kadei.diaryworkouts.activities.MainActivity;
import ru.kadei.diaryworkouts.managers.ResourceManager;

import static ru.kadei.diaryworkouts.fragments.Navigator.UNDEFINED;
import static ru.kadei.diaryworkouts.util.PrimitiveCollection.ArrayUtil.exists;

/**
 * Created by kadei on 26.09.15.
 */
public class NavigatorBuilder {

    private final ResourceManager resourceManager;

    private MainActivity activity = null;
    private final int[] ids = new int[5];

    private final ArrayList<Pair<Integer, Boolean>> headers;
    private final ArrayList<Pair<Drawable, String>> items;
    private final ArrayList<Class<? extends CustomFragment>> fragments;
    private int exitPos = UNDEFINED;

    private static final int LAYOUT_ID = 0;
    private static final int CONTAINER_ID = 1;
    private static final int TOOLBAR_ID = 2;
    private static final int DRAWER_ID = 3;
    private static final int LIST_ID = 4;

    public NavigatorBuilder(ResourceManager resourceManager) {
        this.resourceManager = resourceManager;

        activity = null;
        for (int i = 0; i < ids.length; ++i)
            ids[i] = -1;

        headers = new ArrayList<>(4);
        items = new ArrayList<>(4);
        fragments = new ArrayList<>(4);
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

    public NavigatorBuilder addHeader(@LayoutRes int layoutId, boolean selectable) {
        headers.add(new Pair<>(layoutId, selectable));
        return this;
    }

    public NavigatorBuilder addFragment(Class<? extends CustomFragment> fragment,
                                        @DrawableRes int drawable, @StringRes int name) {
        final ResourceManager rm = resourceManager;
        items.add(new Pair<>(rm.getDrawable(drawable), rm.getString(name)));
        fragments.add(fragment);
        return this;
    }

    public NavigatorBuilder addExit(@DrawableRes int drawable, @StringRes int name) {
        if (exitPos != -1)
            throw new RuntimeException("Exit already added");

        final ResourceManager rm = resourceManager;
        items.add(new Pair<>(rm.getDrawable(drawable), rm.getString(name)));
        exitPos = items.size() - 1;

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
                fragments,
                headers,
                items,
                exitPos);
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
