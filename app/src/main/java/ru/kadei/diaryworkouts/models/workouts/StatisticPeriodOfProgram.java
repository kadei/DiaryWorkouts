package ru.kadei.diaryworkouts.models.workouts;

/**
 * Created by kadei on 04.10.15.
 */
public class StatisticPeriodOfProgram {

    public final DescriptionProgram descriptionProgram;
    public final long begin;
    public final long end;
    public final int totalAmountWorkout;
    public final int monthAmountWorkout;

    public StatisticPeriodOfProgram(DescriptionProgram descriptionProgram, long begin, long end, int totalAmountWorkout, int monthAmountWorkout) {
        this.descriptionProgram = descriptionProgram;
        this.begin = begin;
        this.end = end;
        this.totalAmountWorkout = totalAmountWorkout;
        this.monthAmountWorkout = monthAmountWorkout;
    }
}
