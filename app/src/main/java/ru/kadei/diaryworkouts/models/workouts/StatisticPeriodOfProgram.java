package ru.kadei.diaryworkouts.models.workouts;

/**
 * Created by kadei on 04.10.15.
 */
public class StatisticPeriodOfProgram {

    public final long begin;
    public final long end;
    public final int amountWorkout;

    public StatisticPeriodOfProgram(long begin,
                                    long end,
                                    int amountWorkout) {
        this.begin = begin;
        this.end = end;
        this.amountWorkout = amountWorkout;
    }
}
