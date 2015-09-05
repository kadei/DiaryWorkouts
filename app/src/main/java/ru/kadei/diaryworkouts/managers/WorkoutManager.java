package ru.kadei.diaryworkouts.managers;

import android.content.Context;

import java.util.ArrayList;

import ru.kadei.diaryworkouts.builder_models.BufferDescriptions;
import ru.kadei.diaryworkouts.builder_models.DefaultBuilder;
import ru.kadei.diaryworkouts.builder_models.DescriptionBuilder;
import ru.kadei.diaryworkouts.builder_models.DescriptionExerciseBuilder;
import ru.kadei.diaryworkouts.builder_models.DescriptionProgramBuilder;
import ru.kadei.diaryworkouts.builder_models.DescriptionWorkoutBuilder;
import ru.kadei.diaryworkouts.builder_models.WorkoutBuilder;
import ru.kadei.diaryworkouts.database.Database;
import ru.kadei.diaryworkouts.database.DatabaseClient;
import ru.kadei.diaryworkouts.models.db.Cortege;
import ru.kadei.diaryworkouts.models.workouts.DescriptionProgram;
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
                new ClientForLoadDescriptionProgram(client));
    }

    public void loadAllDescriptionWorkout() {
        database.load("SELECT * FROM descriptionWorkout", descriptionWorkoutBuilder, dbClient);
    }

    public void loadAllDescriptionExercise() {
        database.load("SELECT * FROM descriptionExercise", descriptionExerciseBuilder, dbClient);
    }

    public void loadAllHistory() {
        database.load("SELECT * FROM historyWorkout ORDER BY startDate DECK", workoutBuilder, dbClient);
    }

    public void loadLastWorkout() {
        database.load("SELECT * FROM historyWorkout ORDER BY startDate LIMIT 1", workoutBuilder, dbClient);
    }

    public void loadHistoryFor(Workout workout) {
        long idPrg = workout.getWorkout().id;
        String query = "SELECT * FROM historyWorkout WHERE idProgram = " + idPrg +
                " AND posWorkout = " + workout.posCurrentWorkout;
        database.load(query, workoutBuilder, dbClient);
    }

    private final DatabaseClient dbClient = new DatabaseClient() {
        @Override
        public void loaded(DefaultBuilder builder) {

        }

        @Override
        public void saved(Cortege cortege) {

        }

        @Override
        public void fail(Throwable throwable) {

        }
    };
}
