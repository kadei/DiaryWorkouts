package ru.kadei.diaryworkouts.models.workouts;

/**
 * Created by kadei on 15.08.15.
 */
public class DescriptionSupersetExercise extends DescriptionExercise {

    public final Entities<DescriptionStandardExercise> exercises;

    private DescriptionSupersetExercise(long id, String name, String description) {
        super(id, name, description, SUPERSET);
        exercises = new Entities<>(2);
    }

    public static DescriptionExercise supersetExercise(long id, String name, String description) {
        return new DescriptionSupersetExercise(id, name, description);
    }
}
