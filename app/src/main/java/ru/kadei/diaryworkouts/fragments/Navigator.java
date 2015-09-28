package ru.kadei.diaryworkouts.fragments;

import android.content.res.Configuration;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.Pair;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.HeaderViewListAdapter;
import android.widget.ListView;

import java.util.ArrayList;

import ru.kadei.diaryworkouts.R;
import ru.kadei.diaryworkouts.activities.MainActivity;
import ru.kadei.diaryworkouts.adapters.NavigationAdapter;

/**
 * Created by kadei on 20.09.15.
 */
public class Navigator {

    private final MainActivity activity;

    private final int containerId;

    static final int UNDEFINED = -1;
    private final int exitPos;
    private final ArrayList<Class<? extends CustomFragment>> fragments;
    private final SparseArray<Bundle> states;

    private final DrawerLayout drawer;
    private final ActionBarDrawerToggle toggle;
    private final ListView navigationList;
    private final int countItems;
    private final int offsetHeader;

    Navigator(MainActivity activity,
              @LayoutRes int layoutId,
              @IdRes int containerId,
              @IdRes int toolbarId,
              @IdRes int drawerId,
              @IdRes int listId,
              ArrayList<Class<? extends CustomFragment>> fragments,
              ArrayList<Pair<Integer, Boolean>> headers,
              ArrayList<Pair<Drawable, String>> items,
              int exitPos) {

        this.activity = activity;
        this.containerId = containerId;
        this.exitPos = exitPos;
        this.fragments = fragments;

        activity.setContentView(layoutId);

        createToolBar(toolbarId);

        drawer = (DrawerLayout) activity.findViewById(drawerId);
        toggle = createToggleFor(drawer);
        navigationList = (ListView) activity.findViewById(listId);

        countItems = items.size();
        offsetHeader = (headers.size() + countItems) - countItems;
        configDrawer(headers, items);

        final SparseArray<Bundle> s = new SparseArray<>(fragments.size());
        for (int i = 0, end = fragments.size(); i < end; ++i)
            s.put(i + 1, new Bundle(4));

        states = s;
    }

    private void createToolBar(@IdRes int toolbarId) {
        Toolbar toolbar = (Toolbar) activity.findViewById(toolbarId);
        toolbar.setTitleTextAppearance(activity, R.style.toolbar_title);
        toolbar.setNavigationOnClickListener(toolbarListener);
        activity.setSupportActionBar(toolbar);
        android.support.v7.app.ActionBar ab = activity.getSupportActionBar();
        if (ab != null) {
            ab.setDisplayShowCustomEnabled(true);
            ab.setDisplayHomeAsUpEnabled(true);
        }
    }

    @SuppressWarnings("FieldCanBeLocal")
    private final View.OnClickListener toolbarListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Log.d("TEST", "toolbar click");
            if (drawer.isDrawerOpen(navigationList))
                drawer.closeDrawer(navigationList);
            else
                drawer.openDrawer(navigationList);
        }
    };

    private ActionBarDrawerToggle createToggleFor(DrawerLayout drawer) {
        return new ActionBarDrawerToggle(activity, drawer, R.string.drawer_open, R.string.drawer_close) {
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                activity.invalidateOptionsMenu();
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                activity.invalidateOptionsMenu();
            }
        };
    }

    private void configDrawer(ArrayList<Pair<Integer, Boolean>> headers,
                              ArrayList<Pair<Drawable, String>> items) {
        addHeaders(headers);
        NavigationAdapter navigationAdapter = new NavigationAdapter(activity, items);
        navigationList.setAdapter(navigationAdapter);
        navigationList.setOnItemClickListener(navigationListener);
        drawer.setDrawerListener(toggle);
    }

    private void addHeaders(ArrayList<Pair<Integer, Boolean>> headers) {
        final ListView lv = navigationList;
        final LayoutInflater inflater = activity.getLayoutInflater();
        for (Pair<Integer, Boolean> pair : headers) {
            View h = inflater.inflate(pair.first, lv, false);
            lv.addHeaderView(h, null, pair.second);
        }
    }

    @SuppressWarnings("FieldCanBeLocal")
    private final AdapterView.OnItemClickListener navigationListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            position -= offsetHeader;
            notifyAdapter(parent.getAdapter(), position);
            saveStateActiveFragment();
            solveActionFor(position);
        }
    };

    private void notifyAdapter(Adapter adapter, int posFragment) {
        if (adapter instanceof HeaderViewListAdapter)
            adapter = ((HeaderViewListAdapter) adapter).getWrappedAdapter();

        ((NavigationAdapter) adapter).setSelectedPos(posFragment);
    }

    private void saveStateActiveFragment() {
        CustomFragment frg = getActiveFragment();
        if (frg != null) {
            final int pos = positionByClass(frg.getClass());
            frg.save(states.get(pos + 1)); // id should be not equals 0
        }
    }

    private void solveActionFor(int position) {
        if (position == exitPos)
            activity.finish();
        else {
            int posFrg = adjustPositionRelativeExitPosition(position);
            _openFragment(posFrg);
            drawer.closeDrawer(navigationList);
        }
    }

    private int adjustPositionRelativeExitPosition(int position) {
        return position > exitPos && exitPos != UNDEFINED ? position - 1 : position;
    }

    public CustomFragment getActiveFragment() {
        return (CustomFragment) activity.getFragmentManager().findFragmentById(containerId);
    }

    private int positionByClass(Class<? extends CustomFragment> classOfFragment) {
        final ArrayList<Class<? extends CustomFragment>> list = fragments;
        for (int i = 0, end = list.size(); i < end; ++i) {
            if (classOfFragment == list.get(i))
                return i;
        }
        return -1;
    }

    // this method invoke from ItemClickListener
    private void _openFragment(final int posFragment) {
        final CustomFragment prevFragment = getActiveFragment();
        if (prevFragment == null) {
            replaceFragment(posFragment);
        } else
            prevFragment.prepareForClose(new Preparer(fragments.get(posFragment)) {
                @Override
                public void iReady() {
                    replaceFragment(posFragment);
                }
            });
    }

    // this method invoke manual
    public void openFragment(int posFragment) {
        posFragment += offsetHeader;
        navigationListener.onItemClick(navigationList, null, posFragment, posFragment);
    }

    private void replaceFragment(int posFragment) {
        final CustomFragment frg = createFragment(posFragment);
        frg.restore(states.get(posFragment + 1)); // id should be not equals 0
        activity.getFragmentManager().beginTransaction().replace(containerId, frg).commit();
    }

    @SuppressWarnings("TryWithIdenticalCatches")
    private CustomFragment createFragment(int pos) {
        try {
            return fragments.get(pos).newInstance();
        } catch (InstantiationException e) {
            throw new RuntimeException(e.getMessage());
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public void syncState() {
        toggle.syncState();
    }

    public void configurationChange(Configuration configuration) {
        toggle.onConfigurationChanged(configuration);
    }
}
