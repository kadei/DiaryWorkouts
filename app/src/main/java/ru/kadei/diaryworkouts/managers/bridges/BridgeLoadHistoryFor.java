package ru.kadei.diaryworkouts.managers.bridges;

import java.util.ArrayList;

import ru.kadei.diaryworkouts.database.ObjectBuilder;
import ru.kadei.diaryworkouts.managers.WorkoutManagerClient;
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
    public void loaded(ObjectBuilder builder) {
        ArrayList<Workout> history = (ArrayList<Workout>) builder.getObjects();
        client.allHistoryLoadedFor(target, history);
    }
}
