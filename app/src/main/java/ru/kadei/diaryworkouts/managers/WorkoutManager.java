package ru.kadei.diaryworkouts.managers;


import java.util.ArrayList;

import ru.kadei.diaryworkouts.builders.BufferDescriptions;
import ru.kadei.diaryworkouts.builders.ExerciseReader;
import ru.kadei.diaryworkouts.builders.ExerciseWriter;
import ru.kadei.diaryworkouts.builders.HistoryReader;
import ru.kadei.diaryworkouts.builders.HistoryWriter;
import ru.kadei.diaryworkouts.builders.LastPeriodReader;
import ru.kadei.diaryworkouts.builders.ProgramReader;
import ru.kadei.diaryworkouts.builders.ProgramWriter;
import ru.kadei.diaryworkouts.builders.WorkoutReader;
import ru.kadei.diaryworkouts.builders.WorkoutWriter;
import ru.kadei.diaryworkouts.database.Database;
import ru.kadei.diaryworkouts.database.DatabaseExecutor;
import ru.kadei.diaryworkouts.database.DatabaseReader;
import ru.kadei.diaryworkouts.database.Record;
import ru.kadei.diaryworkouts.database.SQLCreator;
import ru.kadei.diaryworkouts.models.workouts.Description;
import ru.kadei.diaryworkouts.models.workouts.DescriptionExercise;
import ru.kadei.diaryworkouts.models.workouts.DescriptionProgram;
import ru.kadei.diaryworkouts.models.workouts.DescriptionWorkout;
import ru.kadei.diaryworkouts.models.workouts.StatisticPeriodOfProgram;
import ru.kadei.diaryworkouts.models.workouts.Workout;
import ru.kadei.diaryworkouts.util.stubs.StubDatabaseClient;

/**
 * Created by kadei on 02.09.15.
 */
public class WorkoutManager extends SQLCreator {

    private final Database database;

    private ExerciseReader exerciseReader;
    private WorkoutReader workoutReader;
    private ProgramReader programReader;
    private HistoryReader historyReader;

    public WorkoutManager(Database database) {
        this.database = database;

        BufferDescriptions buffer = new BufferDescriptions();
        exerciseReader = new ExerciseReader(buffer);
        workoutReader = new WorkoutReader(buffer, exerciseReader);
        programReader = new ProgramReader(buffer, workoutReader);
        historyReader = new HistoryReader(programReader);
    }

    public void loadAllDescriptionPrograms(WorkoutManagerClient client) {
        database.load("SELECT * FROM descriptionProgram", programReader,
                new StubDatabaseClient(client) {
                    @SuppressWarnings("unchecked")
                    @Override
                    public void loaded(DatabaseReader reader) {
                        client.allProgramsLoaded((ArrayList<DescriptionProgram>) reader.getObjects());
                    }
                });
    }

    public void loadAllDescriptionWorkout(WorkoutManagerClient client) {
        database.load("SELECT * FROM descriptionWorkout", workoutReader,
                new StubDatabaseClient(client) {
                    @SuppressWarnings("unchecked")
                    @Override
                    public void loaded(DatabaseReader reader) {
                        client.allWorkoutsLoaded((ArrayList<DescriptionWorkout>) reader.getObjects());
                    }
                });
    }

    public void loadAllDescriptionExercise(WorkoutManagerClient client) {
        database.load("SELECT * FROM descriptionExercise", exerciseReader,
                new StubDatabaseClient(client) {
                    @SuppressWarnings("unchecked")
                    @Override
                    public void loaded(DatabaseReader reader) {
                        client.allExercisesLoaded((ArrayList<DescriptionExercise>) reader.getObjects());
                    }
                });
    }

    public void loadAllHistory(WorkoutManagerClient client) {
        final String query = "SELECT " + allColumnsHistoryWorkout + ", dateEvent.milliseconds " +
                "FROM historyWorkout, dateEvent " +
                "WHERE historyWorkout.idDateEvent = dateEvent._id " +
                "ORDER BY dateEvent.milliseconds DESC";

        database.load(query, historyReader, new StubDatabaseClient(client) {
            @SuppressWarnings("unchecked")
            @Override
            public void loaded(DatabaseReader reader) {
                client.allHistoryLoaded((ArrayList<Workout>) reader.getObjects());
            }
        });
    }

    private static final String allColumnsHistoryWorkout = "historyWorkout._id, " +
            "historyWorkout.idProgram, " +
            "historyWorkout.idWorkout, " +
            "historyWorkout.posWorkout, " +
            "historyWorkout.idDateEvent, " +
            "historyWorkout.duration, " +
            "historyWorkout.comment";

    public void loadLastWorkout(WorkoutManagerClient client) {
        final String query = "SELECT " + allColumnsHistoryWorkout + ", dateEvent.milliseconds " +
                "FROM historyWorkout, dateEvent " +
                "WHERE historyWorkout.idDateEvent = dateEvent._id " +
                "ORDER BY dateEvent.milliseconds DESC LIMIT 1";

        database.load(query, historyReader, new StubDatabaseClient(client) {
            @SuppressWarnings("unchecked")
            @Override
            public void loaded(DatabaseReader reader) {
                ArrayList<Workout> workouts = (ArrayList<Workout>) reader.getObjects();
                client.lastWorkoutLoaded(workouts.isEmpty() ? null : workouts.get(0));
            }
        });
    }

    public void loadHistoryFor(final Workout workout, WorkoutManagerClient client) {
        loadHistoryFor(workout, 0, client);
    }

    public void loadHistoryFor(final Workout workout, int limit, WorkoutManagerClient client) {
        String strLimit = limit <= 0 ? "" : " LIMIT " + Integer.toString(limit);
        String query = query("SELECT ").append(allColumnsHistoryWorkout).append(", dateEvent.milliseconds ")
                .append("FROM historyWorkout, dateEvent WHERE historyWorkout.idDateEvent = dateEvent._id")
                .append(" AND historyWorkout.idProgram = ").append(workout.getIdProgram())
                .append(" AND historyWorkout.posWorkout = ").append(workout.getPosCurrentWorkout())
                .append(" ORDER BY dateEvent.milliseconds DESC").append(strLimit).toString();

        database.load(query, historyReader, new StubDatabaseClient(client) {
            @SuppressWarnings("unchecked")
            @Override
            public void loaded(DatabaseReader reader) {
                ArrayList<Workout> history = (ArrayList<Workout>) reader.getObjects();
                client.allHistoryLoadedFor(workout, history);
            }
        });
    }

    public void loadStatisticLastProgram(WorkoutManagerClient client) {
        database.executeTask(new LastPeriodReader(), new StubDatabaseClient(client) {
            @Override
            public void executed(DatabaseExecutor executor) {
                client.statisticPeriodsLoaded((StatisticPeriodOfProgram) executor.getResult());
            }
        });
    }

    public void saveDescriptionProgram(DescriptionProgram program, WorkoutManagerClient client) {
        database.save(program, new ProgramWriter(), new StubDatabaseClient(client) {
            @Override
            public void saved(Record record) {
                client.descriptionSaved((Description) record);
            }
        });
    }

    public void saveDescriptionWorkout(DescriptionWorkout workout, WorkoutManagerClient client) {
        database.save(workout, new WorkoutWriter(), new StubDatabaseClient(client) {
            @Override
            public void saved(Record record) {
                client.descriptionSaved((Description) record);
            }
        });
    }

    public void saveDescriptionExercise(DescriptionExercise exercise, WorkoutManagerClient client) {
        database.save(exercise, new ExerciseWriter(), new StubDatabaseClient(client) {
            @Override
            public void saved(Record record) {
                client.descriptionSaved((Description) record);
            }
        });
    }

    public void saveWorkout(Workout workout, WorkoutManagerClient client) {
        database.save(workout, new HistoryWriter(), new StubDatabaseClient(client) {
            @Override
            public void saved(Record record) {
                client.workoutSaved((Workout) record);
            }
        });
    }
}
