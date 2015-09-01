package ru.kadei.diaryworkouts.builder_models;

import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

import ru.kadei.diaryworkouts.models.workouts.Description;

/**
 * Created by kadei on 30.08.15.
 */
public class DescriptionBuilder extends DefaultBuilder {

    protected ArrayList<? extends Description> descriptions;

    public DescriptionBuilder(SQLiteDatabase db) {
        super(db);
    }

    public void buildDescriptionProgram(String query) {
    }

    public void buildDescriptionWorkout(String query) {
    }

    public void buildDescriptionExercise(String query) {
    }

    public ArrayList<? extends Description> get() {
        ArrayList<? extends Description> tmp = descriptions;
        descriptions = null;
        return tmp;
    }
}
