package ru.kadei.diaryworkouts.managers;

import java.util.ArrayList;

import ru.kadei.diaryworkouts.builders.DescriptionReader;
import ru.kadei.diaryworkouts.builders.DescriptionExerciseReader;
import ru.kadei.diaryworkouts.builders.DescriptionExerciseWriter;
import ru.kadei.diaryworkouts.builders.DescriptionProgramReader;
import ru.kadei.diaryworkouts.builders.DescriptionProgramWriter;
import ru.kadei.diaryworkouts.builders.DescriptionWorkoutReader;
import ru.kadei.diaryworkouts.builders.DescriptionWorkoutWriter;
import ru.kadei.diaryworkouts.builders.HistoryWorkoutReader;
import ru.kadei.diaryworkouts.builders.HistoryWorkoutWriter;
import ru.kadei.diaryworkouts.database.Database;
import ru.kadei.diaryworkouts.database.DatabaseReader;
import ru.kadei.diaryworkouts.database.Record;
import ru.kadei.diaryworkouts.managers.bridges.BridgeLoad;
import ru.kadei.diaryworkouts.managers.bridges.BridgeSave;
import ru.kadei.diaryworkouts.managers.bridges.BridgeSaveDescription;
import ru.kadei.diaryworkouts.models.workouts.DescriptionExercise;
import ru.kadei.diaryworkouts.models.workouts.DescriptionProgram;
import ru.kadei.diaryworkouts.models.workouts.DescriptionWorkout;
import ru.kadei.diaryworkouts.models.workouts.Workout;

/**
 * Created by kadei on 02.09.15.
 */
public class WorkoutManager {

    private final Database database;

    private BufferDescriptions bufferDescriptions;
    private DescriptionReader descriptionExerciseBuilder;
    private DescriptionReader descriptionWorkoutBuilder;
    private DescriptionReader descriptionProgramBuilder;
    private HistoryWorkoutReader workoutBuilder;

    public WorkoutManager(Database database) {
        this.database = database;

        bufferDescriptions = new BufferDescriptions();
        descriptionExerciseBuilder = new DescriptionExerciseReader(bufferDescriptions);
        descriptionWorkoutBuilder = new DescriptionWorkoutReader(bufferDescriptions, descriptionExerciseBuilder);
        descriptionProgramBuilder = new DescriptionProgramReader(bufferDescriptions, descriptionWorkoutBuilder);

        workoutBuilder = new HistoryWorkoutReader(descriptionProgramBuilder);
    }

    public void loadAllDescriptionPrograms(WorkoutManagerClient client) {
        database.load("SELECT * FROM descriptionProgram", descriptionProgramBuilder,
                new BridgeLoad(client) {
                    @SuppressWarnings("unchecked")
                    @Override
                    public void loaded(DatabaseReader builder) {
                        client.allProgramsLoaded(
                                (ArrayList<DescriptionProgram>) builder.getObjects());
                    }
                });
    }

    public void loadAllDescriptionWorkout(WorkoutManagerClient client) {
        database.load("SELECT * FROM descriptionWorkout", descriptionWorkoutBuilder,
                new BridgeLoad(client) {
                    @SuppressWarnings("unchecked")
                    @Override
                    public void loaded(DatabaseReader builder) {
                        client.allWorkoutsLoaded(
                                (ArrayList<DescriptionWorkout>) builder.getObjects());
                    }
                });
    }

    public void loadAllDescriptionExercise(WorkoutManagerClient client) {
        database.load("SELECT * FROM descriptionExercise", descriptionExerciseBuilder,
                new BridgeLoad(client) {
                    @SuppressWarnings("unchecked")
                    @Override
                    public void loaded(DatabaseReader builder) {
                        client.allExercisesLoaded(
                                (ArrayList<DescriptionExercise>) builder.getObjects());
                    }
                });
    }

    public void loadAllHistory(WorkoutManagerClient client) {
        database.load("SELECT * FROM historyWorkout ORDER BY startDate DESC", workoutBuilder,
                new BridgeLoad(client) {
                    @SuppressWarnings("unchecked")
                    @Override
                    public void loaded(DatabaseReader builder) {
                        client.allHistoryLoaded((ArrayList<Workout>) builder.getObjects());
                    }
                });
    }

    public void loadLastWorkout(WorkoutManagerClient client) {
        database.load("SELECT * FROM historyWorkout ORDER BY startDate DESC LIMIT 1", workoutBuilder,
                new BridgeLoad(client) {
                    @SuppressWarnings("unchecked")
                    @Override
                    public void loaded(DatabaseReader builder) {
                        ArrayList<Workout> workouts = (ArrayList<Workout>) builder.getObjects();
                        client.lastWorkoutLoaded(workouts.isEmpty() ? null : workouts.get(0));
                    }
                });
    }

    public void loadHistoryFor(final Workout workout, WorkoutManagerClient client) {
        String query = "SELECT * FROM historyWorkout WHERE idProgram = " + workout.getIdProgram() +
                " AND posWorkout = " + workout.getPosCurrentWorkout();

        database.load(query, workoutBuilder, new BridgeLoad(client) {
            @SuppressWarnings("unchecked")
            @Override
            public void loaded(DatabaseReader builder) {
                ArrayList<Workout> history = (ArrayList<Workout>) builder.getObjects();
                client.allHistoryLoadedFor(workout, history);
            }
        });
    }

    public void saveDescriptionProgram(DescriptionProgram program, WorkoutManagerClient client) {
        database.save(program, new DescriptionProgramWriter(), new BridgeSaveDescription(client));
    }

    public void saveDescriptionWorkout(DescriptionWorkout workout, WorkoutManagerClient client) {
        database.save(workout, new DescriptionWorkoutWriter(), new BridgeSaveDescription(client));
    }

    public void saveDescriptionExercise(DescriptionExercise exercise, WorkoutManagerClient client) {
        database.save(exercise, new DescriptionExerciseWriter(), new BridgeSaveDescription(client));
    }

    public void saveWorkout(Workout workout, WorkoutManagerClient client) {
        database.save(workout, new HistoryWorkoutWriter(), new BridgeSave(client) {
            @Override
            public void saved(Record record) {
                client.workoutSaved((Workout) record);
            }
        });
    }
}
