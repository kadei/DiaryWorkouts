package ru.kadei.diaryworkouts.managers.bridges;

import java.util.ArrayList;

import ru.kadei.diaryworkouts.database.ObjectBuilder;
import ru.kadei.diaryworkouts.managers.WorkoutManagerClient;
import ru.kadei.diaryworkouts.models.workouts.Workout;

/**
 * Created by kadei on 06.09.15.
 */
public class BridgeLoadAllHistory extends BridgeLoad {

    public BridgeLoadAllHistory(WorkoutManagerClient client) {
        super(client);
    }

    @SuppressWarnings("unchecked")
    @Override
    public void loaded(ObjectBuilder builder) {
        ArrayList<Workout> workouts = (ArrayList<Workout>) builder.getObjects();
        client.allHistoryLoaded(workouts);
    }
}
