package ru.kadei.diaryworkouts.managers;

import java.util.ArrayList;

import ru.kadei.diaryworkouts.builder_models.DefaultBuilder;
import ru.kadei.diaryworkouts.models.workouts.DescriptionWorkout;

/**
 * Created by kadei on 06.09.15.
 */
public class BridgeLoadAllWorkouts extends BridgeLoad {

    public BridgeLoadAllWorkouts(WorkoutManagerClient client) {
        super(client);
    }

    @SuppressWarnings("unchecked")
    @Override
    public void loaded(DefaultBuilder builder) {
        ArrayList<DescriptionWorkout> workouts = (ArrayList<DescriptionWorkout>) builder.getObjects();
        client.allWorkoutsLoaded(workouts);
    }
}
