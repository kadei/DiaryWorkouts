package ru.kadei.diaryworkouts.models.workouts;

/**
 * Created by kadei on 15.08.15.
 */
public class DescriptionProgram extends Description {

    public final Entities<DescriptionWorkout> workouts;

    public DescriptionProgram(long id, String name, String description) {
        super(id, name, description);
        workouts = new Entities<>(4);
    }
}
