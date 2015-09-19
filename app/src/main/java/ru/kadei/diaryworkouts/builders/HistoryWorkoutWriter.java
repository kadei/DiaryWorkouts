package ru.kadei.diaryworkouts.builders;

import android.content.ContentValues;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.TimeZone;

import ru.kadei.diaryworkouts.database.Cortege;
import ru.kadei.diaryworkouts.database.DatabaseWriter;
import ru.kadei.diaryworkouts.models.workouts.Exercise;
import ru.kadei.diaryworkouts.models.workouts.Set;
import ru.kadei.diaryworkouts.models.workouts.Workout;

import static java.lang.String.valueOf;
import static java.util.Calendar.DATE;
import static java.util.Calendar.HOUR_OF_DAY;
import static java.util.Calendar.MONTH;
import static java.util.Calendar.YEAR;
import static ru.kadei.diaryworkouts.database.Database.FALSE;
import static ru.kadei.diaryworkouts.database.Database.TRUE;
import static ru.kadei.diaryworkouts.models.workouts.Measure.DISTANCE;
import static ru.kadei.diaryworkouts.models.workouts.Measure.DURATION;
import static ru.kadei.diaryworkouts.models.workouts.Measure.REPEAT;
import static ru.kadei.diaryworkouts.models.workouts.Measure.SPEED;
import static ru.kadei.diaryworkouts.models.workouts.Measure.WEIGHT;

/**
 * Created by kadei on 12.09.15.
 */
public class HistoryWorkoutWriter extends DatabaseWriter {

    private Cortege cortegeWorkout;
    private Cortege cortegeExercise;
    private Cortege cortegeSet;

    @Override
    public void writeObject(Object object) {
        if (object instanceof Workout)
            saveWorkout((Workout) object);
        else
            oops(object);
    }

    private void saveWorkout(Workout workout) {
        initCorteges();

        final ContentValues cv = cortegeWorkout.values;
        cv.put("idProgram", workout.getDescriptionProgram().id);
        cv.put("idWorkout", workout.getDescriptionWorkout().id);
        cv.put("posWorkout", workout.getPosCurrentWorkout());
        cv.put("startDate", workout.date);
        cv.put("duration", workout.duration);
        cv.put("comment", workout.comment);


        final long idHistory = save(cortegeWorkout, workout);
        saveDate(workout.date, idHistory);
        saveHistoryExercise(workout, idHistory);

        releaseCorteges();
    }

    private void saveDate(long millisecond, long idHistory) {
        if (existsDateFor(idHistory))
            deleteDateFor(idHistory);

        Calendar calendar = new GregorianCalendar(TimeZone.getTimeZone("GMT+3"));
        calendar.setTimeInMillis(millisecond);

        ContentValues cv = new ContentValues(5);
        cv.put("idHistoryWorkout", idHistory);
        cv.put("year", calendar.get(YEAR));
        cv.put("month", calendar.get(MONTH));
        cv.put("day", calendar.get(DATE));
        cv.put("hour", calendar.get(HOUR_OF_DAY));

        insertInto("dateWorkout", cv);
        cv.clear();
    }

    private boolean existsDateFor(long idHistory) {
        return existsColumnIn("dateWorkout", "idHistoryWorkout", valueOf(idHistory));
    }

    private void deleteDateFor(long idHistory) {
        db.delete("dateWorkout", query("idHistoryWorkout = ").append(idHistory).toString(), null);
    }

    private void saveHistoryExercise(Workout workout, long idHistory) {
        final Cortege cortege = cortegeExercise;
        final ContentValues cv = cortege.values;

        final int originalPos = workout.posCurrentExercise;

        for (int i = 0, end = workout.getCountExercises(); i < end; ++i) {
            workout.posCurrentExercise = i;
            Exercise exercise = workout.getCurrentExercise();

            cv.put("idHistoryWorkout", idHistory);
            cv.put("idExercise", exercise.info.id);
            cv.put("orderInList", i);
            cv.put("comment", exercise.comment);

            long idHistoryExercise = save(cortege, exercise);
            saveHistorySet(exercise, idHistoryExercise);
        }

        workout.posCurrentExercise = originalPos;
    }

    private void saveHistorySet(Exercise exercise, long idHistoryExercise) {
        final Cortege cortege = cortegeSet;
        final ContentValues cv = cortege.values;

        int count = 0;
        for (int i = 0, end = exercise.getCountSet(); i < end; ++i) {
            for (Set set : exercise.getSet(i)) {
                cv.put("idHistoryExercise", idHistoryExercise);
                cv.put("orderInList", count++);
                cv.put("cheat", set.cheat ? TRUE : FALSE);
                cv.put("weight", set.getValueOfMeasure(WEIGHT));
                cv.put("repeat", set.getValueOfMeasure(REPEAT));
                cv.put("speed", set.getValueOfMeasure(SPEED));
                cv.put("distance", set.getValueOfMeasure(DISTANCE));
                cv.put("duration", set.getValueOfMeasure(DURATION));
                cv.put("comment", set.comment);

                save(cortege, set);
            }
        }
    }

    private void initCorteges() {
        Cortege cortege = new Cortege();
        cortege.nameTable = "historyWorkout";
        cortege.values = new ContentValues(6);
        cortegeWorkout = cortege;

        cortege = new Cortege();
        cortege.nameTable = "historyExercise";
        cortege.values = new ContentValues(4);
        cortegeExercise = cortege;

        cortege = new Cortege();
        cortege.nameTable = "historySet";
        cortege.values = new ContentValues(8);
        cortegeSet = cortege;
    }

    private void releaseCorteges() {
        cortegeWorkout.values.clear();
        cortegeWorkout = null;

        cortegeExercise.values.clear();
        cortegeExercise = null;

        cortegeSet.values.clear();
        cortegeSet = null;
    }
}
