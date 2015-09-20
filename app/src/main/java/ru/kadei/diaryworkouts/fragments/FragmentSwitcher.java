package ru.kadei.diaryworkouts.fragments;

import android.app.Activity;
import android.app.FragmentManager;
import android.util.SparseArray;

/**
 * Created by kadei on 20.09.15.
 */
public class FragmentSwitcher {

    public static final int NO_FRAGMENT = -1;
    public static final int MAIN_FRAGMENT = 1;
    public static final int MEASUREMENTS_FRAGMENT = 2;
    public static final int STATISTIC_FRAGMENT = 3;
    public static final int SELECT_PROGRAM_FRAGMENT = 4;
    public static final int CREATE_PROGRAM_FRAGMENT = 5;
    public static final int CREATE_WORKOUT_FRAGMENT = 6;
    public static final int CREATE_EXERCISE_FRAGMENT = 7;

    private final FragmentManager fragmentManager;

    private final int idContainer;
    private final SparseArray<Class<? extends CustomFragment>> fragments;

    public FragmentSwitcher(Activity activity, int idContainer) {
        fragmentManager = activity.getFragmentManager();

        this.idContainer = idContainer;
        fragments = new SparseArray<>();
        initFragments();
    }

    private void initFragments() {
        fragments.put(MAIN_FRAGMENT, MainFragment.class);
        fragments.put(MEASUREMENTS_FRAGMENT, MeasurementsFragment.class);
        fragments.put(STATISTIC_FRAGMENT, StatisticFragment.class);
        fragments.put(SELECT_PROGRAM_FRAGMENT, SelectProgramFragment.class);
        fragments.put(CREATE_PROGRAM_FRAGMENT, CreateProgramFragment.class);
        fragments.put(CREATE_WORKOUT_FRAGMENT, CreateWorkoutFragment.class);
        fragments.put(CREATE_EXERCISE_FRAGMENT, CreateExerciseFragment.class);
    }

    public void openFragment(final int idFragment) {
        final CustomFragment prevFragment = (CustomFragment) fragmentManager.findFragmentById(idContainer);
        if (prevFragment == null)
            replaceFragment(idFragment);
        else
            prevFragment.prepareForClose(new Notifier(idFragment) {
                @Override
                public void iReadyToClose() {
                    replaceFragment(idFragment);
                }
            });
    }

    private void replaceFragment(int idFragment) {
        CustomFragment newFragment = getFragmentById(idFragment);
        fragmentManager.beginTransaction().replace(idContainer, newFragment).commit();
    }

    @SuppressWarnings("TryWithIdenticalCatches")
    private CustomFragment getFragmentById(int id) {
        try {
            return fragments.get(id).newInstance();
        } catch (InstantiationException e) {
            throw new RuntimeException(e.getMessage());
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e.getMessage());
        }
    }
}
