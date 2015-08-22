package ru.kadei.diaryworkouts.models.workouts;

/**
 * Created by kadei on 15.08.15.
 */
public class DescriptionWorkout extends Description {

    public final Entities<DescriptionExercise> exercises;

    public DescriptionWorkout(long id, String name, String description) {
        super(id, name, description);
        exercises = new Entities<>(8);
    }
}
