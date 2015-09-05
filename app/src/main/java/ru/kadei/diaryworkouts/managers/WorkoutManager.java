package ru.kadei.diaryworkouts.managers;

import android.content.Context;

import ru.kadei.diaryworkouts.builder_models.BufferDescriptions;
import ru.kadei.diaryworkouts.builder_models.DescriptionBuilder;
import ru.kadei.diaryworkouts.builder_models.DescriptionExerciseBuilder;
import ru.kadei.diaryworkouts.builder_models.DescriptionProgramBuilder;
import ru.kadei.diaryworkouts.builder_models.DescriptionWorkoutBuilder;
import ru.kadei.diaryworkouts.builder_models.WorkoutBuilder;
import ru.kadei.diaryworkouts.database.Database;
import ru.kadei.diaryworkouts.models.workouts.Workout;

/**
 * Created by kadei on 02.09.15.
 */
public class WorkoutManager {

    private final Database database;

    private BufferDescriptions bufferDescriptions;
    private DescriptionBuilder descriptionExerciseBuilder;
    private DescriptionBuilder descriptionWorkoutBuilder;
    private DescriptionBuilder descriptionProgramBuilder;
    private WorkoutBuilder workoutBuilder;

    public WorkoutManager(Context context, Database database) {
        this.database = database;

        bufferDescriptions = new BufferDescriptions();
        descriptionExerciseBuilder = new DescriptionExerciseBuilder(bufferDescriptions);
        descriptionWorkoutBuilder = new DescriptionWorkoutBuilder(bufferDescriptions, descriptionExerciseBuilder);
        descriptionProgramBuilder = new DescriptionProgramBuilder(bufferDescriptions, descriptionWorkoutBuilder);

        workoutBuilder = new WorkoutBuilder(descriptionProgramBuilder);
    }

    public void loadAllDescriptionPrograms(WorkoutManagerClient client) {
        database.load("SELECT * FROM descriptionProgram", descriptionProgramBuilder,
                new BridgeLoadAllProgram(client));
    }

    public void loadAllDescriptionWorkout(WorkoutManagerClient client) {
        database.load("SELECT * FROM descriptionWorkout", descriptionWorkoutBuilder,
                new BridgeLoadAllWorkouts(client));
    }

    public void loadAllDescriptionExercise(WorkoutManagerClient client) {
        database.load("SELECT * FROM descriptionExercise", descriptionExerciseBuilder,
                new BridgeLoadAllExercises(client));
    }

    public void loadAllHistory(WorkoutManagerClient client) {
        database.load("SELECT * FROM historyWorkout ORDER BY startDate DECK", workoutBuilder,
                new BridgeLoadAllHistory(client));
    }

    public void loadLastWorkout(WorkoutManagerClient client) {
        database.load("SELECT * FROM historyWorkout ORDER BY startDate LIMIT 1", workoutBuilder,
                new BridgeLoadLastWorkout(client));
    }

    public void loadHistoryFor(Workout workout, WorkoutManagerClient client) {
        long idPrg = workout.getWorkout().id;
        String query = "SELECT * FROM historyWorkout WHERE idProgram = " + idPrg +
                " AND posWorkout = " + workout.posCurrentWorkout;
        database.load(query, workoutBuilder, new BridgeLoadHistoryFor(client, workout));
    }
}
