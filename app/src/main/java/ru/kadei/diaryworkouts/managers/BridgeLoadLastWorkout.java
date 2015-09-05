package ru.kadei.diaryworkouts.managers;

import java.util.ArrayList;

import ru.kadei.diaryworkouts.builder_models.DefaultBuilder;
import ru.kadei.diaryworkouts.models.workouts.Workout;

/**
 * Created by kadei on 06.09.15.
 */
public class BridgeLoadLastWorkout extends BridgeLoad {

    public BridgeLoadLastWorkout(WorkoutManagerClient client) {
        super(client);
    }

    @SuppressWarnings("unchecked")
    @Override
    public void loaded(DefaultBuilder builder) {
        ArrayList<Workout> workouts = (ArrayList<Workout>) builder.getObjects();
        client.lastWorkoutLoaded(workouts.isEmpty() ? null : workouts.get(0));
    }
}
