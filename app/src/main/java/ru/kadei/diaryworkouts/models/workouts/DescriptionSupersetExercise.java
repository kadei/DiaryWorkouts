package ru.kadei.diaryworkouts.models.workouts;

import java.util.ArrayList;

/**
 * Created by kadei on 15.08.15.
 */
public class DescriptionSupersetExercise extends DescriptionExercise {

    public ArrayList<DescriptionStandardExercise> exercises;

    @Override
    public int getMeasureSpec() {
        int measureSpec = 0;
        for(DescriptionStandardExercise stdExe : exercises) {
            measureSpec |= stdExe.getMeasureSpec();
        }
        return measureSpec;
    }

    @Override
    public int getMuscleGroupSpec() {
        int muscleGroupSpec = 0;
        for(DescriptionStandardExercise stdExe : exercises) {
            muscleGroupSpec |= stdExe.getMuscleGroupSpec();
        }
        return muscleGroupSpec;
    }

    @Override
    public int getExerciseCount() {
        return exercises.size();
    }

    @Override
    public DescriptionExercise getExercise(int pos) {
        return exercises.get(pos);
    }
}
