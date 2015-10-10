package ru.kadei.diaryworkouts.util;

import android.util.Log;

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

    public ProxyWorkoutManagerClient(CustomFragment fragment, WorkoutManagerClient client) {
        this.fragment = fragment;
        this.client = client;
    }

    @Override
    public void allProgramsLoaded(ArrayList<DescriptionProgram> programs) {
        try {
            if (fragment.isAlive())
                client.allProgramsLoaded(programs);
        } catch (Exception e) { /*ignore*/ }
    }

    @Override
    public void allWorkoutsLoaded(ArrayList<DescriptionWorkout> workouts) {
        try {
            if (fragment.isAlive())
                client.allWorkoutsLoaded(workouts);
        } catch (Exception e) { /*ignore*/ }
    }

    @Override
    public void allExercisesLoaded(ArrayList<DescriptionExercise> exercises) {
        try {
            if (fragment.isAlive())
                client.allExercisesLoaded(exercises);
        } catch (Exception e) { /*ignore*/ }
    }

    @Override
    public void allHistoryLoaded(ArrayList<Workout> history) {
        try {
            if (fragment.isAlive())
                client.allHistoryLoaded(history);
        } catch (Exception e) { /*ignore*/ }
    }

    @Override
    public void allHistoryLoadedFor(Workout target, ArrayList<Workout> history) {
        try {
            if (fragment.isAlive())
                client.allHistoryLoadedFor(target, history);
        } catch (Exception e) { /*ignore*/ }
    }

    @Override
    public void lastWorkoutLoaded(Workout workout) {
        try {
            Log.d("TEST", "PROXY lastWorkoutLoaded");
            if (fragment.isAlive())
                client.lastWorkoutLoaded(workout);
        } catch (Exception e) { /*ignore*/ }
    }

    @Override
    public void statisticPeriodsLoaded(StatisticPeriodOfProgram statistic) {
        try {
            if (fragment.isAlive())
                client.statisticPeriodsLoaded(statistic);
        } catch (Exception e) { /*ignore*/ }
    }

    @Override
    public void descriptionSaved(Description description) {
        try {
            if (fragment.isAlive())
                client.descriptionSaved(description);
        } catch (Exception e) { /*ignore*/ }
    }

    @Override
    public void workoutSaved(Workout workout) {
        try {
            if (fragment.isAlive())
                client.workoutSaved(workout);
        } catch (Exception e) { /*ignore*/ }
    }

    @Override
    public void fail(Throwable throwable) {
        try {
            if (fragment.isAlive())
                client.fail(throwable);
        } catch (Exception e) { /*ignore*/ }
    }
}
