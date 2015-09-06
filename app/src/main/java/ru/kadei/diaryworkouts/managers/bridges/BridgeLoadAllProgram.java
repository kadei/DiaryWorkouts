package ru.kadei.diaryworkouts.managers.bridges;

import java.util.ArrayList;

import ru.kadei.diaryworkouts.database.ObjectBuilder;
import ru.kadei.diaryworkouts.managers.WorkoutManagerClient;
import ru.kadei.diaryworkouts.models.workouts.DescriptionProgram;

/**
 * Created by kadei on 06.09.15.
 */
public class BridgeLoadAllProgram extends BridgeLoad {

    public BridgeLoadAllProgram(WorkoutManagerClient client) {
        super(client);
    }

    @SuppressWarnings("unchecked")
    @Override
    public void loaded(ObjectBuilder builder) {
        ArrayList<DescriptionProgram> programs = (ArrayList<DescriptionProgram>) builder.getObjects();
        client.allProgramsLoaded(programs);
    }
}
