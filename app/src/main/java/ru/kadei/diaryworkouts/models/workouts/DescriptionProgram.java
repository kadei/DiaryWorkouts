package ru.kadei.diaryworkouts.models.workouts;

import java.util.ArrayList;

/**
 * Created by kadei on 15.08.15.
 */
public class DescriptionProgram extends Description {

    public ArrayList<DescriptionWorkout> workouts;

    public static DescriptionProgram newProgram(long id, String name, String description, int capacity) {
        DescriptionProgram dp = new DescriptionProgram();
        dp.id = id;
        dp.name = name;
        dp.description = description;
        dp.workouts = new ArrayList<>(capacity);
        return dp;
    }

    @Override
    public ArrayList<? extends Description> getContent() {
        return workouts;
    }
}
