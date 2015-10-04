package ru.kadei.diaryworkouts.util;

import java.util.ArrayList;

import ru.kadei.diaryworkouts.managers.WorkoutManagerClient;
import ru.kadei.diaryworkouts.models.workouts.Description;
import ru.kadei.diaryworkouts.models.workouts.DescriptionExercise;
import ru.kadei.diaryworkouts.models.workouts.DescriptionProgram;
import ru.kadei.diaryworkouts.models.workouts.DescriptionWorkout;
import ru.kadei.diaryworkouts.models.workouts.Workout;

/**
 * Created by kadei on 03.10.15.
 */
public class StubWorkoutManagerClient implements WorkoutManagerClient {
    @Override
    public void allProgramsLoaded(ArrayList<DescriptionProgram> programs) {

    }

    @Override
    public void allWorkoutsLoaded(ArrayList<DescriptionWorkout> workouts) {

    }

    @Override
    public void allExercisesLoaded(ArrayList<DescriptionExercise> exercises) {

    }

    @Override
    public void allHistoryLoaded(ArrayList<Workout> history) {

    }

    @Override
    public void allHistoryLoadedFor(Workout target, ArrayList<Workout> history) {

    }

    @Override
    public void lastWorkoutLoaded(Workout workout) {

    }

    @Override
    public void descriptionSaved(Description description) {

    }

    @Override
    public void workoutSaved(Workout workout) {

    }

    @Override
    public void fail(Throwable throwable) {

    }
}
