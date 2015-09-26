package ru.kadei.diaryworkouts.activities;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;

import ru.kadei.diaryworkouts.R;
import ru.kadei.diaryworkouts.database.DBHelper;
import ru.kadei.diaryworkouts.database.Database;
import ru.kadei.diaryworkouts.fragments.CreateExerciseFragment;
import ru.kadei.diaryworkouts.fragments.CreateProgramFragment;
import ru.kadei.diaryworkouts.fragments.CreateWorkoutFragment;
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

    private ResourceManager resourceManager;
    private WorkoutManager workoutManager;
    private PreferenceManager preferenceManager;

    private Navigator navigator;

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
        navigator = new NavigatorBuilder(resourceManager)
                .setActivity(this)
                .setLayoutId(R.layout.activity_main)
                .setContainerId(R.id.container_fragment)
                .setDrawerId(R.id.drawer)
                .setListId(R.id.navigation_list)
                .setToolbarId(R.id.toolbar)
                .addFragment(MainFragment.class, R.drawable.ic_done_all_black_18dp, R.string.main_fragment)
                .addFragment(MeasurementsFragment.class, R.drawable.ic_history_black_18dp, R.string.measurement_fragment)
                .addFragment(StatisticFragment.class, R.drawable.ic_trending_up_black_18dp, R.string.statistic_fragment)
                .addFragment(SelectProgramFragment.class, R.drawable.ic_list_black_18dp, R.string.select_program_fragment)
                .addFragment(CreateProgramFragment.class, R.drawable.ic_playlist_add_black_18dp, R.string.create_program_fragment)
                .addFragment(CreateWorkoutFragment.class, R.drawable.ic_playlist_add_black_18dp, R.string.create_workout_fragment)
                .addFragment(CreateExerciseFragment.class, R.drawable.ic_playlist_add_black_18dp, R.string.create_exercise_fragment)
                .addExit(R.drawable.ic_close_black_18dp, R.string.exit)
                .build();
        navigator.openFragment(0); // MainFragment
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        navigator.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        navigator.configurationChange(newConfig);
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
