package ru.kadei.diaryworkouts.models.workouts;

import java.util.ArrayList;

import ru.kadei.diaryworkouts.database.Record;

/**
 * Created by kadei on 15.08.15.
 */
public abstract class Exercise extends Record {

    public String comment;
    public final DescriptionExercise info;
    protected final ArrayList<Set> sets;

    public Exercise(DescriptionExercise info, ArrayList<Set> sets) {
        this.info = info;
        this.sets = sets;
    }

    public void addSet(ArrayList<Set> sets) {
        ArrayList<Set> dest = this.sets;
        for(Set set : sets)
            dest.add(set);
    }

    public abstract ArrayList<Set> getSet(int pos);
    public abstract void setSet(int pos, ArrayList<Set> set);
    public abstract int getCountSet();
}
