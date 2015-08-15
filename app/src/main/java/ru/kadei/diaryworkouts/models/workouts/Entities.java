package ru.kadei.diaryworkouts.models.workouts;

import java.util.ArrayList;

/**
 * Created by kadei on 15.08.15.
 */
public class Entities<T extends EntityWorkout> {

    private ArrayList<T> entities;

    public Entities() {
        this(0);
    }

    public Entities(int capacity) {
        entities = new ArrayList<>(capacity);
    }

    public T get(int pos) {
        return entities.get(pos);
    }

    public T getByID(long id) {
        final int pos = getPositionByID(id);
        return pos < 0 ? null : get(pos);
    }

    public int getPositionByID(long id) {
        for (int i = 0, end = entities.size(); i < end; ++i) {
            if(entities.get(i).id == id)
                return i;
        }
        return -1;
    }

    public boolean isEmpty() {
        return entities.isEmpty();
    }
}
