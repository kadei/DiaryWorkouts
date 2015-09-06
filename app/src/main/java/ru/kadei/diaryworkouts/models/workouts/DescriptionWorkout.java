package ru.kadei.diaryworkouts.models.workouts;

import java.util.ArrayList;

/**
 * Created by kadei on 15.08.15.
 */
public class DescriptionWorkout extends Description {

    public ArrayList<DescriptionExercise> exercises;

    public static DescriptionWorkout newWorkout(long id, String name, String description, int capacity) {
        DescriptionWorkout dw = new DescriptionWorkout();
        dw.id = id;
        dw.name = name;
        dw.description = description;
        dw.exercises = new ArrayList<>(capacity);
        return dw;
    }
}
