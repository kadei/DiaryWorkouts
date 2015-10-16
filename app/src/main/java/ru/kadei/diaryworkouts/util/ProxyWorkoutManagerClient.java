package ru.kadei.diaryworkouts.util;

import android.support.annotation.NonNull;

import java.util.ArrayList;

import ru.kadei.diaryworkouts.fragments.CustomFragment;
import ru.kadei.diaryworkouts.managers.WorkoutManagerClient;
import ru.kadei.diaryworkouts.models.workouts.Description;
import ru.kadei.diaryworkouts.models.workouts.DescriptionExercise;
import ru.kadei.diaryworkouts.models.workouts.DescriptionProgram;
import ru.kadei.diaryworkouts.models.workouts.DescriptionWorkout;
import ru.kadei.diaryworkouts.models.workouts.StatisticPeriodOfProgram;
import ru.kadei.diaryworkouts.models.workouts.Workout;
import ru.kadei.diaryworkouts.util.stubs.StubWorkoutManagerClient;

/**
 * Created by kadei on 09.10.15.
 */
public class ProxyWorkoutManagerClient extends StubWorkoutManagerClient {

    private final CustomFragment fragment;
    private final WorkoutManagerClient client;
    private final ArrayList<Listener> listeners = new ArrayList<>(2);

    public ProxyWorkoutManagerClient(CustomFragment fragment, WorkoutManagerClient client) {
        this.fragment = fragment;
        this.client = client;
    }

    @Override
    public void allProgramsLoaded(ArrayList<DescriptionProgram> programs) {
        try {
            if (fragment.isAlive()) {
                client.allProgramsLoaded(programs);
                notify("allProgramsLoaded", null);
            }
        } catch (Exception e) { /*ignore*/ }
    }

    @Override
    public void allWorkoutsLoaded(ArrayList<DescriptionWorkout> workouts) {
        try {
            if (fragment.isAlive()) {
                client.allWorkoutsLoaded(workouts);
                notify("allWorkoutsLoaded", null);
            }
        } catch (Exception e) { /*ignore*/ }
    }

    @Override
    public void allExercisesLoaded(ArrayList<DescriptionExercise> exercises) {
        try {
            if (fragment.isAlive()) {
                client.allExercisesLoaded(exercises);
                notify("allExercisesLoaded", null);
            }
        } catch (Exception e) { /*ignore*/ }
    }

    @Override
    public void allHistoryLoaded(ArrayList<Workout> history) {
        try {
            if (fragment.isAlive()) {
                client.allHistoryLoaded(history);
                notify("allHistoryLoaded", null);
            }
        } catch (Exception e) { /*ignore*/ }
    }

    @Override
    public void allHistoryLoadedFor(Workout target, ArrayList<Workout> history) {
        try {
            if (fragment.isAlive()) {
                client.allHistoryLoadedFor(target, history);
                notify("allHistoryLoadedFor", null);
            }
        } catch (Exception e) { /*ignore*/ }
    }

    @Override
    public void lastWorkoutLoaded(Workout workout) {
        try {
            if (fragment.isAlive()) {
                client.lastWorkoutLoaded(workout);
                notify("lastWorkoutLoaded", null);
            }
        } catch (Exception e) { /*ignore*/ }
    }

    @Override
    public void statisticPeriodsLoaded(StatisticPeriodOfProgram statistic) {
        try {
            if (fragment.isAlive()) {
                client.statisticPeriodsLoaded(statistic);
                notify("statisticPeriodsLoaded", null);
            }
        } catch (Exception e) { /*ignore*/ }
    }

    @Override
    public void descriptionSaved(Description description) {
        try {
            if (fragment.isAlive()) {
                client.descriptionSaved(description);
                notify("descriptionSaved", null);
            }
        } catch (Exception e) { /*ignore*/ }
    }

    @Override
    public void workoutSaved(Workout workout) {
        try {
            if (fragment.isAlive()) {
                client.workoutSaved(workout);
                notify("workoutSaved", null);
            }
        } catch (Exception e) { /*ignore*/ }
    }

    @Override
    public void fail(Throwable throwable) {
        try {
            if (fragment.isAlive()) {
                client.fail(throwable);
                notify("fail", null);
            }
        } catch (Exception e) { /*ignore*/ }
    }

    public void addListener(@NonNull Listener listener) {
        listeners.add(listener);
    }

    private void notify(String name, Object object) {
        final ArrayList<Listener> l = listeners;
        for (int i = 0, end = l.size(); i < end; ++i)
            l.get(i).executed(name, object);
    }

    public interface Listener {
        void executed(String method, Object returnValue);
    }
}
