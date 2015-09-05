package ru.kadei.diaryworkouts.managers;

import java.util.ArrayList;

import ru.kadei.diaryworkouts.builder_models.DefaultBuilder;
import ru.kadei.diaryworkouts.database.DatabaseClient;
import ru.kadei.diaryworkouts.models.db.Cortege;
import ru.kadei.diaryworkouts.models.workouts.DescriptionProgram;

/**
 * Created by kadei on 06.09.15.
 */
public class ClientForLoadDescriptionProgram implements DatabaseClient {

    private WorkoutManagerClient client;

    public ClientForLoadDescriptionProgram(WorkoutManagerClient client) {
        this.client = client;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void loaded(DefaultBuilder builder) {
        ArrayList<DescriptionProgram> programs = (ArrayList<DescriptionProgram>) builder.getObjects();
        client.allProgramsLoaded(programs);
    }

    @Override
    public void saved(Cortege cortege) {
    }

    @Override
    public void fail(Throwable throwable) {
    }
}
