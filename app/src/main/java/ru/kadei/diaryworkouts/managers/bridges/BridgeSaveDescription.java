package ru.kadei.diaryworkouts.managers.bridges;

import ru.kadei.diaryworkouts.database.Record;
import ru.kadei.diaryworkouts.managers.WorkoutManagerClient;
import ru.kadei.diaryworkouts.models.workouts.Description;

/**
 * Created by kadei on 06.09.15.
 */
public class BridgeSaveDescription extends BridgeSave {

    public BridgeSaveDescription(WorkoutManagerClient client) {
        super(client);
    }

    @Override
    public void saved(Record record) {
        client.descriptionSaved((Description) record);
    }
}
