package ru.kadei.diaryworkouts.managers;

import java.util.ArrayList;

import ru.kadei.diaryworkouts.builder_models.DefaultBuilder;
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
    public void loaded(DefaultBuilder builder) {
        ArrayList<DescriptionExercise> exercises = (ArrayList<DescriptionExercise>) builder.getObjects();
        client.allExercisesLoaded(exercises);
    }
}
