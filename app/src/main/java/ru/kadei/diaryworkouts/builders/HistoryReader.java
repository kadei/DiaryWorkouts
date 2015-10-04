package ru.kadei.diaryworkouts.builders;

import android.database.Cursor;
import android.util.SparseArray;

import java.util.ArrayList;

import ru.kadei.diaryworkouts.database.DatabaseReader;
import ru.kadei.diaryworkouts.models.workouts.DescriptionExercise;
import ru.kadei.diaryworkouts.models.workouts.DescriptionProgram;
import ru.kadei.diaryworkouts.models.workouts.Exercise;
import ru.kadei.diaryworkouts.models.workouts.Measure;
import ru.kadei.diaryworkouts.models.workouts.Set;
import ru.kadei.diaryworkouts.models.workouts.StandardExercise;
import ru.kadei.diaryworkouts.models.workouts.SupersetExercise;
import ru.kadei.diaryworkouts.models.workouts.Workout;
import ru.kadei.diaryworkouts.util.primitive_collection.SimpleSparseLongArray;

import static ru.kadei.diaryworkouts.database.Database.TRUE;
import static ru.kadei.diaryworkouts.models.workouts.Measure.MEASURE_AMOUNT;
import static ru.kadei.diaryworkouts.models.workouts.Set.computeIndex;

/**
 * Created by kadei on 08.09.15.
 */
public class HistoryReader extends DatabaseReader {

    private final DescriptionReader programReader;

    private final Measure measure = new Measure();
    private SparseArray<String[]> bufferNameColumns = new SparseArray<>(4);

    public HistoryReader(ProgramReader programReader) {
        this.programReader = programReader;
    }

    @Override
    public ArrayList<Workout> buildFromCursor(Cursor c) {
        final ArrayList<Workout> list = new ArrayList<>(c.getCount());

        final int indexId = c.getColumnIndex("_id");
        final int indexIdProgram = c.getColumnIndex("idProgram");
        final int indexPosWorkout = c.getColumnIndex("posWorkout");
        final int indexIdDateEvent = c.getColumnIndex("idDateEvent");
        final int indexDuration = c.getColumnIndex("duration");
        final int indexComment = c.getColumnIndex("comment");

        final SimpleSparseLongArray dates = getAllDates(c, indexIdDateEvent);

        do {
            long idPrg = c.getLong(indexIdProgram);
            DescriptionProgram descriptionProgram = getDescriptionProgram(idPrg);

            int posWorkout = c.getInt(indexPosWorkout);

            long idHistory = c.getLong(indexId);
            ArrayList<Exercise> exercises = getHistoryExercise(idHistory);

            Workout workout = new Workout(descriptionProgram, posWorkout, exercises);
            workout.id = idHistory;
            workout.date = dates.get(c.getLong(indexIdDateEvent), 0L);
            workout.duration = c.getLong(indexDuration);
            workout.comment = c.getString(indexComment);

            list.add(workout);
        } while (c.moveToNext());

        return list;
    }

    private SimpleSparseLongArray getAllDates(Cursor c, int indexIdDateEvent) {
        final String idsSeparatedByComma = getIdsSeparatedByComma(c, indexIdDateEvent);
        final String query = query("SELECT _id, milliseconds FROM dateEvent WHERE _id IN (")
                .append(idsSeparatedByComma).append(") ORDER BY _id").toString();

        final SimpleSparseLongArray dates = new SimpleSparseLongArray(c.getCount());
        final Cursor curDates = db.rawQuery(query, null);

        if(curDates.getCount() != c.getCount())
            throw new RuntimeException("Unexpected case");

        if(curDates.moveToFirst()) {
            final int indexId = curDates.getColumnIndex("_id");
            final int indexMilliseconds = curDates.getColumnIndex("milliseconds");

            do {
                dates.put(curDates.getLong(indexId), curDates.getLong(indexMilliseconds));
            } while (curDates.moveToNext());
        }
        curDates.close();

        return dates;
    }

    private String getIdsSeparatedByComma(Cursor c, int index) {
        final int originalPos = c.getPosition();
        final int size = c.getCount() - originalPos;
        int count = 0;

        final StringBuilder sb = getClearStringBuilder();
        do {
            sb.append(c.getLong(index));
            if(++count < size)
                sb.append(", ");

        } while (c.moveToNext());
        c.moveToPosition(originalPos);
        return sb.toString();
    }

    private DescriptionProgram getDescriptionProgram(long idProgram) {
        final DescriptionReader reader = programReader;
        DescriptionProgram dp = reader.getBufferDescriptions().getProgram(idProgram);
        if (dp == null) {
            reader.setDb(db);
            reader.readObjects("SELECT * FROM descriptionProgram WHERE _id = " + idProgram);
            reader.forgetReferenceDB();
            dp = (DescriptionProgram) reader.getObjects().get(0);
        }
        return dp;
    }

    private ArrayList<Exercise> getHistoryExercise(long idHistory) {
        String query = query("SELECT _id, idExercise, comment ")
                .append("FROM historyExercise WHERE idHistoryWorkout = ").append(idHistory)
                .append(" ORDER BY orderInList").toString();

        Cursor c = db.rawQuery(query, null);
        ArrayList<Exercise> exercises = null;
        if (c.moveToFirst()) {
            exercises = buildHistoryExercise(c);
        }
        c.close();
        return exercises == null ? new ArrayList<Exercise>() : exercises;
    }

    private ArrayList<Exercise> buildHistoryExercise(Cursor c) {
        final ArrayList<Exercise> list = new ArrayList<>(c.getCount());

        final int indexId = c.getColumnIndex("_id");
        final int indexIdExercise = c.getColumnIndex("idExercise");
        final int indexComment = c.getColumnIndex("comment");

        final Measure measure = this.measure;
        final BufferDescriptions buffer = programReader.getBufferDescriptions();
        do {
            long idExercise = c.getLong(indexIdExercise);
            DescriptionExercise descriptionExercise = buffer.getExercise(idExercise);
            measure.extractSpec(descriptionExercise.getMeasureSpec());

            long idHistoryExercise = c.getLong(indexId);
            ArrayList<Set> sets = getHistorySet(idHistoryExercise);

            Exercise exercise = descriptionExercise.isSuperset()
                    ? new SupersetExercise(descriptionExercise, sets)
                    : new StandardExercise(descriptionExercise, sets);

            exercise.id = idHistoryExercise;
            exercise.comment = c.getString(indexComment);
            list.add(exercise);
        } while (c.moveToNext());

        return list;
    }

    private ArrayList<Set> getHistorySet(long idHistoryExercise) {
        String query = createQueryFor(idHistoryExercise);
        Cursor c = db.rawQuery(query, null);

        ArrayList<Set> sets = null;
        if (c.moveToFirst())
            sets = buildHistorySet(c);
        c.close();

        return sets == null ? new ArrayList<Set>() : sets;
    }

    private String createQueryFor(long idHistoryExercise) {
        final String[] nameColumns = getNameColumnsForCurrentMeasure();
        final String columnsSeparatedComma = separate(nameColumns);

        return query("SELECT _id, ").append(columnsSeparatedComma).append("cheat, comment ")
                .append("FROM historySet WHERE idHistoryExercise = ").append(idHistoryExercise)
                .append(" ORDER BY orderInList").toString();
    }

    private String[] getNameColumnsForCurrentMeasure() {
        final Measure m = measure;
        String[] columns = bufferNameColumns.get(m.getSpec());
        if (columns == null) {
            columns = new String[m.getCount()];
            for (int i = 0, end = m.getCount(); i < end; ++i) {
                int index = computeIndex(m.get(i));
                columns[i] = NAME_COLUMNS[index];
            }
            bufferNameColumns.put(m.getSpec(), columns);
        }
        return columns;
    }

    private static final String[] NAME_COLUMNS = new String[]{
            "weight", "repeat", "speed", "distance", "duration"
    };

    private String separate(String[] strings) {
        StringBuilder sb = getClearStringBuilder();
        for (String column : strings)
            sb.append(column).append(", ");
        return sb.toString();
    }

    private ArrayList<Set> buildHistorySet(Cursor c) {
        final Measure measure = this.measure;
        final String[] nameColumns = bufferNameColumns.get(measure.getSpec());
        final ArrayList<Set> sets = new ArrayList<>(c.getCount());

        final int[] indexes = new int[MEASURE_AMOUNT];
        for (int i = 0, end = measure.getCount(); i < end; ++i) {
            indexes[i] = c.getColumnIndex(nameColumns[i]);
        }
        final int indexID = c.getColumnIndex("_id");
        final int indexCheat = c.getColumnIndex("cheat");
        final int indexComment = c.getColumnIndex("comment");

        do {
            Set set = new Set();
            for (int i = 0, end = measure.getCount(); i < end; ++i) {
                set.setValueOfMeasure(c.getFloat(indexes[i]), measure.get(i));
            }
            set.id = c.getLong(indexID);
            set.cheat = c.getInt(indexCheat) == TRUE;
            set.comment = c.getString(indexComment);

            sets.add(set);
        } while (c.moveToNext());

        return sets;
    }
}
