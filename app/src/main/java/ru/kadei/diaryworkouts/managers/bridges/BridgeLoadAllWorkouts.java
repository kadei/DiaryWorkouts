package ru.kadei.diaryworkouts.managers.bridges;

import java.util.ArrayList;

import ru.kadei.diaryworkouts.database.ObjectBuilder;
import ru.kadei.diaryworkouts.managers.WorkoutManagerClient;
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
    public void loaded(ObjectBuilder builder) {
        ArrayList<DescriptionWorkout> workouts = (ArrayList<DescriptionWorkout>) builder.getObjects();
        client.allWorkoutsLoaded(workouts);
    }
}
