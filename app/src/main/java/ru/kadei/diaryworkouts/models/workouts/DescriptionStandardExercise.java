package ru.kadei.diaryworkouts.models.workouts;

/**
 * Created by kadei on 15.08.15.
 */
public class DescriptionStandardExercise extends DescriptionExercise {

    private int measureSpec;
    private int muscleGroupSpec;

    @Override
    public int getMeasureSpec() {
        return measureSpec;
    }

    @Override
    public int getMuscleGroupSpec() {
        return muscleGroupSpec;
    }

    public void setMeasureSpec(int measureSpec) {
        this.measureSpec = measureSpec;
    }

    public void setMuscleGroupSpec(int muscleGroupSpec) {
        this.muscleGroupSpec = muscleGroupSpec;
    }
}
