package ru.kadei.diaryworkouts.models.workouts;

/**
 * Created by kadei on 15.08.15.
 */
public class Description {

    public final long id;
    public final String name;
    public final String description;

    public Description(long id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
    }
}
