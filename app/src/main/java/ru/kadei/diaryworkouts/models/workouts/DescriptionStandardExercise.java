package ru.kadei.diaryworkouts.models.workouts;

import java.util.ArrayList;

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

    @Override
    public int getExerciseCount() {
        return 1;
    }

    @Override
    public DescriptionExercise getExercise(int pos) {
        return this;
    }

    public void setMeasureSpec(int measureSpec) {
        this.measureSpec = measureSpec;
    }

    public void setMuscleGroupSpec(int muscleGroupSpec) {
        this.muscleGroupSpec = muscleGroupSpec;
    }

    @Override
    public ArrayList<? extends Description> getContent() {
        return null;
    }
}
