package ru.kadei.diaryworkouts.models.workouts;

import java.util.ArrayList;

import ru.kadei.diaryworkouts.database.Record;

/**
 * Created by kadei on 15.08.15.
 */
public abstract class Description extends Record {

    public String name;
    public String description;

    public static boolean contains(ArrayList<? extends Record> list, long id) {
        return getPosition(list, id) >= 0;
    }

    public static int getPosition(ArrayList<? extends Record> list, long id) {
        for (int i = 0, end = list.size(); i < end; ++i) {
            if (list.get(i).id == id)
                return i;
        }
        return -1;
    }

    public abstract ArrayList<? extends Description> getContent();
}
