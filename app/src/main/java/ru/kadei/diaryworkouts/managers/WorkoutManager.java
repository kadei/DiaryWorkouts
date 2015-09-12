package ru.kadei.diaryworkouts.managers;

import java.util.ArrayList;

import ru.kadei.diaryworkouts.builders.ExerciseReader;
import ru.kadei.diaryworkouts.builders.ExerciseWriter;
import ru.kadei.diaryworkouts.builders.ProgramReader;
import ru.kadei.diaryworkouts.builders.ProgramWriter;
import ru.kadei.diaryworkouts.builders.WorkoutReader;
import ru.kadei.diaryworkouts.builders.WorkoutWriter;
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

    private ExerciseReader exerciseReader;
    private WorkoutReader workoutReader;
    private ProgramReader programReader;
    private HistoryWorkoutReader historyReader;

    public WorkoutManager(Database database) {
        this.database = database;

        BufferDescriptions buffer = new BufferDescriptions();
        exerciseReader = new ExerciseReader(buffer);
        workoutReader = new WorkoutReader(buffer, exerciseReader);
        programReader = new ProgramReader(buffer, workoutReader);
        historyReader = new HistoryWorkoutReader(programReader);
    }

    public void loadAllDescriptionPrograms(WorkoutManagerClient client) {
        database.load("SELECT * FROM descriptionProgram", programReader,
                new BridgeLoad(client) {
                    @SuppressWarnings("unchecked")
                    @Override
                    public void loaded(DatabaseReader reader) {
                        client.allProgramsLoaded(
                                (ArrayList<DescriptionProgram>) reader.getObjects());
                    }
                });
    }

    public void loadAllDescriptionWorkout(WorkoutManagerClient client) {
        database.load("SELECT * FROM descriptionWorkout", workoutReader,
                new BridgeLoad(client) {
                    @SuppressWarnings("unchecked")
                    @Override
                    public void loaded(DatabaseReader reader) {
                        client.allWorkoutsLoaded(
                                (ArrayList<DescriptionWorkout>) reader.getObjects());
                    }
                });
    }

    public void loadAllDescriptionExercise(WorkoutManagerClient client) {
        database.load("SELECT * FROM descriptionExercise", exerciseReader,
                new BridgeLoad(client) {
                    @SuppressWarnings("unchecked")
                    @Override
                    public void loaded(DatabaseReader reader) {
                        client.allExercisesLoaded(
                                (ArrayList<DescriptionExercise>) reader.getObjects());
                    }
                });
    }

    public void loadAllHistory(WorkoutManagerClient client) {
        database.load("SELECT * FROM historyWorkout ORDER BY startDate DESC", historyReader,
                new BridgeLoad(client) {
                    @SuppressWarnings("unchecked")
                    @Override
                    public void loaded(DatabaseReader reader) {
                        client.allHistoryLoaded((ArrayList<Workout>) reader.getObjects());
                    }
                });
    }

    public void loadLastWorkout(WorkoutManagerClient client) {
        database.load("SELECT * FROM historyWorkout ORDER BY startDate DESC LIMIT 1", historyReader,
                new BridgeLoad(client) {
                    @SuppressWarnings("unchecked")
                    @Override
                    public void loaded(DatabaseReader reader) {
                        ArrayList<Workout> workouts = (ArrayList<Workout>) reader.getObjects();
                        client.lastWorkoutLoaded(workouts.isEmpty() ? null : workouts.get(0));
                    }
                });
    }

    public void loadHistoryFor(final Workout workout, WorkoutManagerClient client) {
        String query = "SELECT * FROM historyWorkout WHERE idProgram = " + workout.getIdProgram() +
                " AND posWorkout = " + workout.getPosCurrentWorkout();

        database.load(query, historyReader, new BridgeLoad(client) {
            @SuppressWarnings("unchecked")
            @Override
            public void loaded(DatabaseReader reader) {
                ArrayList<Workout> history = (ArrayList<Workout>) reader.getObjects();
                client.allHistoryLoadedFor(workout, history);
            }
        });
    }

    public void saveDescriptionProgram(DescriptionProgram program, WorkoutManagerClient client) {
        database.save(program, new ProgramWriter(), new BridgeSaveDescription(client));
    }

    public void saveDescriptionWorkout(DescriptionWorkout workout, WorkoutManagerClient client) {
        database.save(workout, new WorkoutWriter(), new BridgeSaveDescription(client));
    }

    public void saveDescriptionExercise(DescriptionExercise exercise, WorkoutManagerClient client) {
        database.save(exercise, new ExerciseWriter(), new BridgeSaveDescription(client));
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
