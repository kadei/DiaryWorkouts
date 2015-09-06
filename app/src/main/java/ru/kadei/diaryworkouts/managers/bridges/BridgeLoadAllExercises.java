package ru.kadei.diaryworkouts.managers.bridges;

import java.util.ArrayList;

import ru.kadei.diaryworkouts.database.ObjectBuilder;
import ru.kadei.diaryworkouts.managers.WorkoutManagerClient;
import ru.kadei.diaryworkouts.models.workouts.DescriptionExercise;

/**
 * Created by kadei on 06.09.15.
 */
public class BridgeLoadAllExercises extends BridgeLoad {

    public BridgeLoadAllExercises(WorkoutManagerClient client) {
        super(client);
    }

    @SuppressWarnings("unchecked")
    @Override
    public void loaded(ObjectBuilder builder) {
        ArrayList<DescriptionExercise> exercises = (ArrayList<DescriptionExercise>) builder.getObjects();
        client.allExercisesLoaded(exercises);
    }
}
