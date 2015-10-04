package ru.kadei.diaryworkouts.builders;

import android.database.Cursor;

import java.util.ArrayList;

import ru.kadei.diaryworkouts.database.DatabaseReader;
import ru.kadei.diaryworkouts.models.workouts.StatisticPeriodOfProgram;

/**
 * Created by kadei on 04.10.15.
 */
public class StatisticPeriodReader extends DatabaseReader {
    @Override
    public ArrayList<?> buildFromCursor(Cursor c) {

        final int idPrg = c.getColumnIndex("idProgram");
//        final int

        final ArrayList<StatisticPeriodOfProgram> statistic = new ArrayList<>(4);
        return statistic;
    }
}
