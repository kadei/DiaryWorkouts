package ru.kadei.diaryworkouts.models.workouts;

/**
 * Created by kadei on 15.08.15.
 */
public class Workout {

    public long id;
    private final DescriptionProgram descriptionProgram;
    private final int posCurrentWorkout;
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
