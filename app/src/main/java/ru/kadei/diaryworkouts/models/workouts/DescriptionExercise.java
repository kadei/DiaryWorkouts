package ru.kadei.diaryworkouts.models.workouts;

import java.util.ArrayList;

/**
 * Created by kadei on 15.08.15.
 */
public abstract class DescriptionExercise extends Description {

    public static final int BASE =      0xf;
    public static final int ISOLATED =  0xf0;
    public static final int SUPERSET =  0xf00;
    public static final int CARDIO =    0xf000;

    public int type;

    public static DescriptionStandardExercise newStandardExercise(long id, String name, String description,
                                                          int type, int measureSpec, int muscleGroupSpec) {
        DescriptionStandardExercise de = new DescriptionStandardExercise();
        de.id = id;
        de.name = name;
        de.description = description;
        de.type = type;
        de.setMeasureSpec(measureSpec);
        de.setMuscleGroupSpec(muscleGroupSpec);
        return de;
    }

    public static DescriptionSupersetExercise newSupersetExercise(long id, String name, String description, int capacity) {
        DescriptionSupersetExercise de = new DescriptionSupersetExercise();
        de.id = id;
        de.name = name;
        de.description = description;
        de.type = SUPERSET;
        de.exercises = new ArrayList<>(capacity);
        return de;
    }

    public boolean isSuperset() {
        return type == SUPERSET;
    }

    public abstract int getMeasureSpec();
    public abstract int getMuscleGroupSpec();
    public abstract int getExerciseCount();

    @Override
    public boolean equals(Object o) {
        if(o != null && o instanceof DescriptionExercise) {
            DescriptionExercise de = (DescriptionExercise) o;
            return de.id == id && de.type == type;
        }
        else return false;
    }
}
