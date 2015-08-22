package ru.kadei.diaryworkouts.models.workouts;

/**
 * Created by kadei on 15.08.15.
 */
public class DescriptionStandardExercise extends DescriptionExercise {

    public final Spec muscleGroupSpec;
    public final Spec measureSpec;

    private DescriptionStandardExercise(long id, String name, String description, int type) {
        super(id, name, description, type);
        muscleGroupSpec = new Spec();
        measureSpec = new Spec();
    }

    public static DescriptionExercise baseExercise(long id, String name, String description) {
        return new DescriptionStandardExercise(id, name, description, BASE);
    }

    public static DescriptionExercise isolatedExercise(long id, String name, String description) {
        return new DescriptionStandardExercise(id, name, description, ISOLATED);
    }

    public static DescriptionExercise cardioExercise(long id, String name, String description) {
        return new DescriptionStandardExercise(id, name, description, CARDIO);
    }
}
