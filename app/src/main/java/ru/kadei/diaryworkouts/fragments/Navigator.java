package ru.kadei.diaryworkouts.fragments;

import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.util.Pair;
import android.util.SparseArray;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;

import com.github.clans.fab.FloatingActionButton;

import ru.kadei.diaryworkouts.R;
import ru.kadei.diaryworkouts.activities.MainActivity;

import static android.support.v7.app.ActionBar.DISPLAY_SHOW_CUSTOM;
import static android.view.animation.AnimationUtils.loadAnimation;

/**
 * Created by kadei on 20.09.15.
 */
public class Navigator {

    private final MainActivity activity;

    private final int idContainer;
    private final DrawerLayout drawer;
    private final SparseArray<Pair<Class<? extends CustomFragment>, Bundle>> fragments;

    private final FloatingActionButton floatingActionButton;

    private int currentItemID = -1;
    private final NavigationView.OnNavigationItemSelectedListener listenerUnbound;

    Navigator(MainActivity activity,
              @LayoutRes int IdLayout,
              @IdRes int idContainer,
              @IdRes int idToolbar,
              @IdRes int idDrawer,
              @IdRes int idNavigationList,
              @IdRes int idFloatingActionButton,
              SparseArray<Class<? extends CustomFragment>> fragments,
              NavigationView.OnNavigationItemSelectedListener listenerUnbound) {

        this.activity = activity;
        activity.setContentView(IdLayout);

        configToolbar(idToolbar);
        this.idContainer = idContainer;
        this.drawer = configDrawer(idDrawer, idNavigationList);
        this.fragments = createBundlesFor(fragments);
        this.floatingActionButton = createAndConfigFAB(idFloatingActionButton);
        this.listenerUnbound = listenerUnbound;
    }

    private void configToolbar(int idToolbar) {
        final Toolbar toolbar = (Toolbar) activity.findViewById(idToolbar);
        toolbar.setSubtitleTextAppearance(activity, R.style.toolbar_title);
        activity.setSupportActionBar(toolbar);
        ActionBar ab = activity.getSupportActionBar();
        if (ab != null) {
            ab.setDisplayOptions(DISPLAY_SHOW_CUSTOM);
            ab.setCustomView(R.layout.toolbar_custom_view);
        }
    }

    private DrawerLayout configDrawer(int idDrawer, int idNavigationList) {
        final DrawerLayout drawer = (DrawerLayout) activity.findViewById(idDrawer);
        ActionBarDrawerToggle toggle = createToggleFor(drawer);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = (NavigationView) activity.findViewById(idNavigationList);
        navigationView.setNavigationItemSelectedListener(navigationItemSelectedListener);
        return drawer;
    }

    private SparseArray<Pair<Class<? extends CustomFragment>, Bundle>> createBundlesFor(
            SparseArray<Class<? extends CustomFragment>> fragments) {

        final int size = fragments.size();
        final SparseArray<Pair<Class<? extends CustomFragment>, Bundle>> frg = new SparseArray<>(size);

        for (int i = 0; i < size; ++i) {
            int key = fragments.keyAt(i);
            Class<? extends CustomFragment> c = fragments.get(key);
            Bundle b = new Bundle(4);
            frg.put(key, new Pair<Class<? extends CustomFragment>, Bundle>(c, b));
        }
        return frg;
    }

    private FloatingActionButton createAndConfigFAB(int idFloatingActionButton) {
        final FloatingActionButton fab = (FloatingActionButton) activity.findViewById(idFloatingActionButton);
        fab.hide(false);

        final Animation show = loadAnimation(activity, R.anim.fab_scale_up);
        show.setAnimationListener(fabAnimationNotifier.getShowAnimationListener());

        final Animation hide = loadAnimation(activity, R.anim.fab_scale_down);
        hide.setAnimationListener(fabAnimationNotifier.getHideAnimationListener());

        fab.setShowAnimation(show);
        fab.setHideAnimation(hide);
        return fab;
    }

    private final FABAnimationNotifier fabAnimationNotifier = new FABAnimationNotifier();

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

    @SuppressWarnings("FieldCanBeLocal")
    private final NavigationView.OnNavigationItemSelectedListener navigationItemSelectedListener = new NavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(MenuItem menuItem) {
            final int id = menuItem.getItemId();

            if (id == currentItemID) {
                getActiveFragment().update();
                closeDrawer();
                return true;
            }

            currentItemID = id;
            saveStateActiveFragment();

            if (id == R.id.exit) {
                activity.finish();
            } else {
                if (isBound(id))
                    openFragmentBy(id);
                else
                    delegateHandle(menuItem);

                closeDrawer();
            }
            return true;
        }
    };

    private void closeDrawer() {
        drawer.closeDrawer(GravityCompat.START);
    }

    private boolean isBound(int id) {
        return fragments.indexOfKey(id) >= 0;
    }

    private void saveStateActiveFragment() {
        final CustomFragment frg = getActiveFragment();
        if (frg != null) {
            final int key = keyByClass(frg.getClass());
            frg.save(fragments.get(key).second);
        }
    }

    public CustomFragment getActiveFragment() {
        return (CustomFragment) activity.getFragmentManager().findFragmentById(idContainer);
    }

    private int keyByClass(Class<? extends CustomFragment> classFragment) {
        final SparseArray<Pair<Class<? extends CustomFragment>, Bundle>> f = fragments;
        for (int i = 0, end = f.size(); i < end; ++i) {
            if (f.valueAt(i).first == classFragment) {
                return f.keyAt(i);
            }
        }
        throw new RuntimeException("Unexpected class = " + classFragment);
    }

    private void openFragmentBy(int id) {
        final CustomFragment prevFragment = getActiveFragment();
        if (prevFragment == null)
            replaceFragment(id);
        else {
            prevFragment.prepareForClose(new Inspector(id) {
                @Override
                public void iReady() {
                    replaceFragment(getNextFragment());
                }
            });
        }
    }

    private void delegateHandle(MenuItem menuItem) {
        if (listenerUnbound != null)
            listenerUnbound.onNavigationItemSelected(menuItem);
    }

    private void replaceFragment(int id) {
        final Pair<Class<? extends CustomFragment>, Bundle> pair = fragments.get(id);
        final CustomFragment frg = getInstanceFor(pair.first);
        frg.setFloatingActionButton(floatingActionButton);
        frg.restore(pair.second);
        fabAnimationNotifier.setListener(frg);
        activity.getFragmentManager().beginTransaction().replace(idContainer, frg).commit();
    }

    @SuppressWarnings({"ThrowableInstanceNeverThrown", "TryWithIdenticalCatches"})
    private CustomFragment getInstanceFor(Class<? extends CustomFragment> classFragment) {
        try {
            return classFragment.newInstance();
        } catch (InstantiationException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public void openFragment(Class<? extends CustomFragment> classFragment) {
        final int key = keyByClass(classFragment);
        navigationItemSelectedListener.onNavigationItemSelected(new StubMenuItem(key));
    }
}
