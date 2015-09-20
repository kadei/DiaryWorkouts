package ru.kadei.diaryworkouts.fragments;

import android.content.res.Configuration;
import android.graphics.drawable.Drawable;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.util.Pair;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

import ru.kadei.diaryworkouts.R;
import ru.kadei.diaryworkouts.activities.MainActivity;
import ru.kadei.diaryworkouts.adapters.NavigationAdapter;
import ru.kadei.diaryworkouts.managers.ResourceManager;

import static ru.kadei.diaryworkouts.fragments.FragmentSwitcher.CREATE_EXERCISE_FRAGMENT;
import static ru.kadei.diaryworkouts.fragments.FragmentSwitcher.CREATE_PROGRAM_FRAGMENT;
import static ru.kadei.diaryworkouts.fragments.FragmentSwitcher.CREATE_WORKOUT_FRAGMENT;
import static ru.kadei.diaryworkouts.fragments.FragmentSwitcher.MAIN_FRAGMENT;
import static ru.kadei.diaryworkouts.fragments.FragmentSwitcher.MEASUREMENTS_FRAGMENT;
import static ru.kadei.diaryworkouts.fragments.FragmentSwitcher.SELECT_PROGRAM_FRAGMENT;
import static ru.kadei.diaryworkouts.fragments.FragmentSwitcher.STATISTIC_FRAGMENT;

/**
 * Created by kadei on 20.09.15.
 */
public class Navigator {

    private final MainActivity activity;
    private final FragmentSwitcher fragmentSwitcher;

    private final DrawerLayout drawer;
    private final ActionBarDrawerToggle toggle;
    private final ListView navigationList;

    public Navigator(final MainActivity activity) {
        this.activity = activity;
        fragmentSwitcher = new FragmentSwitcher(activity, R.id.container_fragment);

        drawer = (DrawerLayout) activity.findViewById(R.id.drawer);
        toggle = createToggleFor(drawer);
        drawer.setDrawerListener(toggle);

        ArrayList<Pair<Drawable, String>> items = loadItems(activity.getResourceManager());
        NavigationAdapter navigationAdapter = new NavigationAdapter(activity, items);

        navigationList = (ListView) activity.findViewById(R.id.navigation_list);
        navigationList.setAdapter(navigationAdapter);
        navigationList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(position == parent.getCount() - 1)
                    activity.finish();
                else {
                    final int idFragment = ID_FRAGMENTS[position];
                    fragmentSwitcher.openFragment(idFragment);
                    drawer.closeDrawer(navigationList);
                }
            }
        });
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

    private ArrayList<Pair<Drawable, String>> loadItems(ResourceManager resourceManager) {
        final String[] names = resourceManager.getStringArray(R.array.navigation_names);
        final Drawable[] icons = new Drawable[names.length];
        icons[0] = resourceManager.getDrawable(R.drawable.ic_done_all_black_18dp);
        icons[1] = resourceManager.getDrawable(R.drawable.ic_history_black_18dp);
        icons[2] = resourceManager.getDrawable(R.drawable.ic_trending_up_black_18dp);
        icons[3] = resourceManager.getDrawable(R.drawable.ic_list_black_18dp);
        icons[4] = resourceManager.getDrawable(R.drawable.ic_playlist_add_black_18dp);
        icons[5] = icons[4];
        icons[6] = icons[4];
        icons[7] = resourceManager.getDrawable(R.drawable.ic_close_black_18dp);

        ArrayList<Pair<Drawable, String>> items = new ArrayList<>(names.length);
        for (int i = 0, end = names.length; i < end; ++i)
            items.add(new Pair<>(icons[i], names[i]));
        return items;
    }

    private static final int[] ID_FRAGMENTS = {
            MAIN_FRAGMENT,
            MEASUREMENTS_FRAGMENT,
            STATISTIC_FRAGMENT,
            SELECT_PROGRAM_FRAGMENT,
            CREATE_PROGRAM_FRAGMENT,
            CREATE_WORKOUT_FRAGMENT,
            CREATE_EXERCISE_FRAGMENT
    };

    public void openMainFragment() {
        fragmentSwitcher.openFragment(MAIN_FRAGMENT);
    }

    public CustomFragment getActiveFragment() {
        return fragmentSwitcher.getActiveFragment();
    }

    public void syncState() {
        toggle.syncState();
    }

    public void configurationChange(Configuration configuration) {
        toggle.onConfigurationChanged(configuration);
    }
}
