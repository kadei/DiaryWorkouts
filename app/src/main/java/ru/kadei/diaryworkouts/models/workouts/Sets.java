package ru.kadei.diaryworkouts.models.workouts;

import java.util.ArrayList;

/**
 * Created by kadei on 15.08.15.
 */
public class Sets {

    private ArrayList<Set> sets;

    public Sets() {
        this(0);
    }

    public Sets(int capacity) {
        sets = new ArrayList<>(capacity);
    }

    public Set get(int pos) {
        return sets.get(pos);
    }

    public void add(Set set) {
        sets.add(set);
    }

    public int countSet() {
        return sets.size();
    }

    public boolean isEmpty() {
        return sets.isEmpty();
    }
}
