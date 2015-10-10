package ru.kadei.diaryworkouts.builders;

import android.database.Cursor;

import ru.kadei.diaryworkouts.database.DatabaseExecutor;
import ru.kadei.diaryworkouts.models.workouts.StatisticPeriodOfProgram;

/**
 * Created by kadei on 04.10.15.
 */
public class LastPeriodReader extends DatabaseExecutor {

    private static final int BEGIN = 0;
    private static final int END = 1;
    private static final long INVALID_DATE = -1L;

    @Override
    public void execute() {

        StatisticPeriodOfProgram statistic = getDefaultStatistic();

        final long[] dates = getBeginEndDates();
        int count;
        if(valid(dates)) {
            count = getCountWorkoutsBetween(dates);
            statistic = getStatistic(dates[BEGIN], dates[END], count);
        }
        object = statistic;
    }

    private long[] getBeginEndDates() {
        String queryForObtainDates =
                "SELECT min(millis) as beginDate, max(millis) as endDate " +
                        "FROM (SELECT dateEvent.milliseconds as millis " +
                        "FROM (SELECT distinct historyWorkout.idProgram as idPrg " +
                        "FROM historyWorkout, dateEvent " +
                        "WHERE historyWorkout.idDateEvent = dateEvent._id " +
                        "ORDER BY dateEvent.milliseconds DESC LIMIT 1), historyWorkout, dateEvent " +
                        "WHERE historyWorkout.idDateEvent = dateEvent._id " +
                        "AND historyWorkout.idProgram = idPrg " +
                        "ORDER BY dateEvent.milliseconds DESC)";

        final long[] result = new long[]{INVALID_DATE, INVALID_DATE};

        Cursor c = db.rawQuery(queryForObtainDates, null);
        if(c.moveToFirst()) {
            final int indexBegin = c.getColumnIndex("beginDate");
            final int indexEnd = c.getColumnIndex("endDate");

            if(!c.isNull(indexBegin) && !c.isNull(indexEnd)) {
                result[BEGIN] = c.getLong(indexBegin);
                result[END] = c.getLong(indexEnd);
            }
        }
        c.close();
        return result;
    }

    private boolean valid(long[] dates) {
        return dates[BEGIN] != INVALID_DATE && dates[END] != INVALID_DATE;
    }

    private int getCountWorkoutsBetween(long[] dates) {
        String queryForCountWorkouts = query(
                "SELECT count(*) as countWorkouts " +
                "FROM historyWorkout, dateEvent " +
                "WHERE historyWorkout.idDateEvent = dateEvent._id " +
                "AND dateEvent.milliseconds >= ").append(dates[BEGIN]).append(
                " AND dateEvent.milliseconds <= ").append(dates[END]).toString();

        int count = 0;

        Cursor c = db.rawQuery(queryForCountWorkouts, null);
        if(c.moveToFirst()) {
            final int index = c.getColumnIndex("countWorkouts");
            if(!c.isNull(index))
                count = c.getInt(index);
        }
        c.close();

        return count;
    }

    private StatisticPeriodOfProgram getDefaultStatistic() {
        return getStatistic(0L, 0L, 0);
    }

    private StatisticPeriodOfProgram getStatistic(long begin, long end, int count) {
        return new StatisticPeriodOfProgram(begin, end, count);
    }
}
