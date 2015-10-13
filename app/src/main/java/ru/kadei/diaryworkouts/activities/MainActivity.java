package ru.kadei.diaryworkouts.activities;

import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.SparseArray;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import ru.kadei.diaryworkouts.R;
import ru.kadei.diaryworkouts.database.DBHelper;
import ru.kadei.diaryworkouts.database.Database;
import ru.kadei.diaryworkouts.fragments.CreateExerciseFragment;
import ru.kadei.diaryworkouts.fragments.CreateProgramFragment;
import ru.kadei.diaryworkouts.fragments.CreateWorkoutFragment;
import ru.kadei.diaryworkouts.fragments.CustomFragment;
import ru.kadei.diaryworkouts.fragments.MainFragment;
import ru.kadei.diaryworkouts.fragments.MeasurementsFragment;
import ru.kadei.diaryworkouts.fragments.Navigator;
import ru.kadei.diaryworkouts.fragments.NavigatorBuilder;
import ru.kadei.diaryworkouts.fragments.SelectProgramFragment;
import ru.kadei.diaryworkouts.fragments.StatisticFragment;
import ru.kadei.diaryworkouts.managers.PreferenceManager;
import ru.kadei.diaryworkouts.managers.ResourceManager;
import ru.kadei.diaryworkouts.managers.WorkoutManager;
import ru.kadei.diaryworkouts.threads.BackgroundLogic;

public class MainActivity extends AppCompatActivity {

    public static final int GENERAL_ID_SELECTED_WORKOUT = 1;

    private final SparseArray<Object> generalStorage = new SparseArray<>(8);
    private ResourceManager resourceManager;
    private WorkoutManager workoutManager;
    private PreferenceManager preferenceManager;

    private Navigator navigator;

    public SparseArray<Object> getGeneralStorage() {
        return generalStorage;
    }

    public ResourceManager getResourceManager() {
        return resourceManager;
    }

    public WorkoutManager getWorkoutManager() {
        return workoutManager;
    }

    public PreferenceManager getPreferenceManager() {
        return preferenceManager;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        createManagers();
        createNavigator();
    }

    private void createManagers() {
        resourceManager = new ResourceManager(this);

        final BackgroundLogic backgroundLogic = new BackgroundLogic();
        final Database database = new Database(new DBHelper(this, "data.db", 1), backgroundLogic);
        workoutManager = new WorkoutManager(database);
        preferenceManager = new PreferenceManager(database);
    }

    private void createNavigator() {
        navigator = new NavigatorBuilder()
                .setActivity(this)
                .setLayoutId(R.layout.new_activity_main)
                .setContainerId(R.id.container_fragment)
                .setDrawerId(R.id.drawer_layout)
                .setListId(R.id.navigation_list)
                .setToolbarId(R.id.toolbar)
                .setFloatingButtonId(R.id.primary_fab)
                .bind(R.id.main, MainFragment.class)
                .bind(R.id.measurement, MeasurementsFragment.class)
                .bind(R.id.statistic, StatisticFragment.class)
                .bind(R.id.select_program, SelectProgramFragment.class)
                .bind(R.id.create_program, CreateProgramFragment.class)
                .bind(R.id.create_workout, CreateWorkoutFragment.class)
                .bind(R.id.create_exercise, CreateExerciseFragment.class)
                .setListenerUnbound(listenerUnbound)
                .build();
        navigator.openFragment(MainFragment.class);
    }

    private final NavigationView.OnNavigationItemSelectedListener listenerUnbound = new NavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(MenuItem menuItem) {
            final int id = menuItem.getItemId();
            if (id == R.id.import_data) {
                Log.d("TEST", "IMPORT");
            }
            return true;
        }
    };

    public CustomFragment getActiveFragment() {
        return navigator.getActiveFragment();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d("TEST", "getTypeface method called " + resourceManager.getCounterRequestFont() + " times");
    }
}
