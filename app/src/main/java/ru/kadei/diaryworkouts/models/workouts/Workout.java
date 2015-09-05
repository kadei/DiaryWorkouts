package ru.kadei.diaryworkouts.models.workouts;

/**
 * Created by kadei on 15.08.15.
 */
public class Workout {

    private final DescriptionProgram descriptionProgram;
    public final int posCurrentWorkout;

    public long id;
    public long date;
    public long duration;
    public String comment;

    private int posCurrentExercise;

    public Workout(DescriptionProgram descriptionProgram, int posCurrentWorkout) {
        this.descriptionProgram = descriptionProgram;
        this.posCurrentWorkout = posCurrentWorkout;
    }

    public String getName() {
        return getWorkout().name;
    }

    public DescriptionWorkout getWorkout() {
        return descriptionProgram.workouts.get(posCurrentWorkout);
    }

    public DescriptionExercise getExercise() {
        return getWorkout().exercises.get(posCurrentExercise);
    }
}
