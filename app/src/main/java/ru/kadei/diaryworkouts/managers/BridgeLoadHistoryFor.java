package ru.kadei.diaryworkouts.managers;

import java.util.ArrayList;

import ru.kadei.diaryworkouts.builder_models.DefaultBuilder;
import ru.kadei.diaryworkouts.models.workouts.Workout;

/**
 * Created by kadei on 06.09.15.
 */
public class BridgeLoadHistoryFor extends BridgeLoad {

    private final Workout target;

    public BridgeLoadHistoryFor(WorkoutManagerClient client, Workout target) {
        super(client);
        this.target = target;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void loaded(DefaultBuilder builder) {
        ArrayList<Workout> history = (ArrayList<Workout>) builder.getObjects();
        client.allHistoryLoadedFor(target, history);
    }
}
