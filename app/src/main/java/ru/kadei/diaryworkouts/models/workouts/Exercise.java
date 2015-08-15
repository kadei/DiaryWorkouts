package ru.kadei.diaryworkouts.models.workouts;

/**
 * Created by kadei on 15.08.15.
 */
public class Exercise extends EntityWorkout {

    public static final int BASE =      0xf;
    public static final int ISOLATED =  0xf0;
    public static final int SUPERSET =  0xf00;
    public static final int CARDIO =    0xf000;

    public int type = BASE;
}
