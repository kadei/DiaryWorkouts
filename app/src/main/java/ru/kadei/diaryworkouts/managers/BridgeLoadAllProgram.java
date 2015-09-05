package ru.kadei.diaryworkouts.managers;

import java.util.ArrayList;

import ru.kadei.diaryworkouts.builder_models.DefaultBuilder;
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
    public void loaded(DefaultBuilder builder) {
        ArrayList<DescriptionProgram> programs = (ArrayList<DescriptionProgram>) builder.getObjects();
        client.allProgramsLoaded(programs);
    }
}
