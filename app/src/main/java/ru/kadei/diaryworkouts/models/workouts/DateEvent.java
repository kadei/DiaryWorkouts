package ru.kadei.diaryworkouts.models.workouts;

import ru.kadei.diaryworkouts.database.Record;

/**
 * Created by kadei on 04.10.15.
 */
public class DateEvent extends Record {

    public final long date;

    public DateEvent(long date) {
        this.date = date;
    }
}
