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
import ru.kadei.diaryworkouts.database.DatabaseWriter;
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
    private final BufferDescriptions buffer;

    public WorkoutManager(Database database) {
        super(createStringBuilder());
        this.database = database;
        buffer = new BufferDescriptions();
    }

    public void loadAllDescriptionPrograms(WorkoutManagerClient client) {
        final DatabaseReader reader = new ProgramReader(buffer);
        reader.setQuery("SELECT * FROM descriptionProgram");

        database.load(reader, new StubDatabaseClient(client) {
            @SuppressWarnings("unchecked")
            @Override
            public void loaded(DatabaseReader reader) {
                getClient().allProgramsLoaded((ArrayList<DescriptionProgram>) reader.getObjects());
            }
        });
    }

    public void loadAllDescriptionWorkout(WorkoutManagerClient client) {
        final DatabaseReader reader = new WorkoutReader(buffer);
        reader.setQuery("SELECT * FROM descriptionWorkout");

        database.load(reader, new StubDatabaseClient(client) {
            @SuppressWarnings("unchecked")
            @Override
            public void loaded(DatabaseReader reader) {
                getClient().allWorkoutsLoaded((ArrayList<DescriptionWorkout>) reader.getObjects());
            }
        });
    }

    public void loadAllDescriptionExercise(WorkoutManagerClient client) {
        final DatabaseReader reader = new ExerciseReader(buffer);
        reader.setQuery("SELECT * FROM descriptionExercise");

        database.load(reader, new StubDatabaseClient(client) {
            @SuppressWarnings("unchecked")
            @Override
            public void loaded(DatabaseReader reader) {
                getClient().allExercisesLoaded((ArrayList<DescriptionExercise>) reader.getObjects());
            }
        });
    }

    public void loadAllHistory(WorkoutManagerClient client) {
        final String query = "SELECT " + allColumnsHistoryWorkout + ", dateEvent.milliseconds " +
                "FROM historyWorkout, dateEvent " +
                "WHERE historyWorkout.idDateEvent = dateEvent._id " +
                "ORDER BY dateEvent.milliseconds DESC";

        final DatabaseReader reader = new HistoryReader(buffer);
        reader.setQuery(query);

        database.load(reader, new StubDatabaseClient(client) {
            @SuppressWarnings("unchecked")
            @Override
            public void loaded(DatabaseReader reader) {
                getClient().allHistoryLoaded((ArrayList<Workout>) reader.getObjects());
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

        final DatabaseReader reader = new HistoryReader(buffer);
        reader.setQuery(query);

        database.load(reader, new StubDatabaseClient(client) {
            @SuppressWarnings("unchecked")
            @Override
            public void loaded(DatabaseReader reader) {
                ArrayList<Workout> workouts = (ArrayList<Workout>) reader.getObjects();
                getClient().lastWorkoutLoaded(workouts.isEmpty() ? null : workouts.get(0));
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

        final DatabaseReader reader = new HistoryReader(buffer);
        reader.setQuery(query);

        database.load(reader, new StubDatabaseClient(client) {
            @SuppressWarnings("unchecked")
            @Override
            public void loaded(DatabaseReader reader) {
                ArrayList<Workout> history = (ArrayList<Workout>) reader.getObjects();
                getClient().allHistoryLoadedFor(workout, history);
            }
        });
    }

    public void loadStatisticLastProgram(WorkoutManagerClient client) {
        database.executeTask(new LastPeriodReader(), new StubDatabaseClient(client) {
            @Override
            public void executed(DatabaseExecutor executor) {
                getClient().statisticPeriodsLoaded((StatisticPeriodOfProgram) executor.getResult());
            }
        });
    }

    public void saveDescriptionProgram(DescriptionProgram program, WorkoutManagerClient client) {
        DatabaseWriter writer = new ProgramWriter();
        writer.setRecord(program);

        database.save(writer, new StubDatabaseClient(client) {
            @Override
            public void saved(DatabaseWriter writer) {
                getClient().descriptionSaved((Description) writer.getRecord());
            }
        });
    }

    public void saveDescriptionWorkout(DescriptionWorkout workout, WorkoutManagerClient client) {
        final DatabaseWriter writer = new WorkoutWriter();
        writer.setRecord(workout);

        database.save(writer, new StubDatabaseClient(client) {
            @Override
            public void saved(DatabaseWriter writer) {
                getClient().descriptionSaved((Description) writer.getRecord());
            }
        });
    }

    public void saveDescriptionExercise(DescriptionExercise exercise, WorkoutManagerClient client) {
        final DatabaseWriter writer = new ExerciseWriter();
        writer.setRecord(exercise);

        database.save(writer, new StubDatabaseClient(client) {
            @Override
            public void saved(DatabaseWriter writer) {
                getClient().descriptionSaved((Description) writer.getRecord());
            }
        });
    }

    public void saveWorkout(Workout workout, WorkoutManagerClient client) {
        final DatabaseWriter writer = new HistoryWriter();
        writer.setRecord(workout);

        database.save(writer, new StubDatabaseClient(client) {
            @Override
            public void saved(DatabaseWriter writer) {
                getClient().workoutSaved((Workout) writer.getRecord());
            }
        });
    }
}
