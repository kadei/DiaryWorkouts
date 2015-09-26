package ru.kadei.diaryworkouts.fragments;

import android.app.FragmentManager;
import android.content.res.Configuration;
import android.graphics.drawable.Drawable;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.widget.AdapterView;
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

    private final int exitPos;
    private final ArrayList<Class<? extends CustomFragment>> fragments;

    private CustomFragment activeFragment;

    private final DrawerLayout drawer;
    private final ActionBarDrawerToggle toggle;
    private final ListView navigationList;

    Navigator(MainActivity activity,
              @LayoutRes int layoutId,
              @IdRes int containerId,
              @IdRes int toolbarId,
              @IdRes int drawerId,
              @IdRes int listId,
              ArrayList<Class<? extends CustomFragment>> fragments,
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
        configDrawer(items);
    }

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

    private void configDrawer(ArrayList<Pair<Drawable, String>> items) {
        NavigationAdapter navigationAdapter = new NavigationAdapter(activity, items);
        navigationList.setAdapter(navigationAdapter);
        navigationList.setOnItemClickListener(navigationListener);
        drawer.setDrawerListener(toggle);
    }

    @SuppressWarnings("FieldCanBeLocal")
    private final AdapterView.OnItemClickListener navigationListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            if (position == exitPos)
                activity.finish();
            else {
                int posFrg = position > exitPos ? position - 1 : position;
                openFragment(posFrg);
                drawer.closeDrawer(navigationList);
            }
        }
    };

    public void openFragment(final int posFragment) {
        final FragmentManager fm = activity.getFragmentManager();
        final CustomFragment prevFragment = (CustomFragment) fm.findFragmentById(containerId);
        if (prevFragment == null)
            replaceFragment(posFragment);
        else
            prevFragment.prepareForClose(new Notifier(fragments.get(posFragment)) {
                @Override
                public void iReadyToClose() {
                    replaceFragment(posFragment);
                }
            });
    }

    private void replaceFragment(int posFragment) {
        final FragmentManager fm = activity.getFragmentManager();
        activeFragment = createFragment(posFragment);
        fm.beginTransaction().replace(containerId, activeFragment).commit();
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

    private void createToolBar(@IdRes int toolbarId) {
        Toolbar toolbar = (Toolbar) activity.findViewById(toolbarId);
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

    public CustomFragment getActiveFragment() {
        return activeFragment;
    }

    public void syncState() {
        toggle.syncState();
    }

    public void configurationChange(Configuration configuration) {
        toggle.onConfigurationChanged(configuration);
    }
}
