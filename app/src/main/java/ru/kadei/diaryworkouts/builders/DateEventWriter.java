package ru.kadei.diaryworkouts.builders;

import android.content.ContentValues;

import java.util.Calendar;
import java.util.GregorianCalendar;

import ru.kadei.diaryworkouts.database.DatabaseWriter;
import ru.kadei.diaryworkouts.database.Record;
import ru.kadei.diaryworkouts.models.workouts.DateEvent;

import static java.util.Calendar.DATE;
import static java.util.Calendar.HOUR_OF_DAY;
import static java.util.Calendar.MONTH;
import static java.util.Calendar.YEAR;
import static java.util.TimeZone.getTimeZone;

/**
 * Created by kadei on 04.10.15.
 */
public class DateEventWriter extends DatabaseWriter {

    public static final int GMT = 3;

    @Override
    public void writeObject(Record object) {
        if (object instanceof DateEvent)
            saveDate((DateEvent) object);
        else
            oops(object);
    }

    private void saveDate(DateEvent dateEvent) {
        if (existsDateEventWithMilliseconds(dateEvent.date))
            deleteDateEventWhereMilliseconds(dateEvent.date);

        Calendar calendar = new GregorianCalendar(getTimeZone(getGMT()));
        calendar.setTimeInMillis(dateEvent.date);

        ContentValues cv = new ContentValues(5);
        cv.put("year", calendar.get(YEAR));
        cv.put("month", calendar.get(MONTH));
        cv.put("day", calendar.get(DATE));
        cv.put("hour", calendar.get(HOUR_OF_DAY));
        cv.put("milliseconds", dateEvent.date);

        dateEvent.id = insertInto("dateEvent", cv);
        cv.clear();
    }

    private boolean existsDateEventWithMilliseconds(long milliseconds) {
        return existsColumnIn("dateEvent", "milliseconds", Long.toString(milliseconds));
    }

    private void deleteDateEventWhereMilliseconds(long milliseconds) {
        db.delete("dateEvent", query("milliseconds = ").append(milliseconds).toString(), null);
    }

    static String getGMT() {
        return GMT >= 0 ? "GMT+" + GMT : "GMT" + GMT;
    }
}
