package ru.kadei.diaryworkouts.database.models;

import ru.kadei.diaryworkouts.database.annotations.Column;

/**
 * Created by kadei on 22.08.15.
 */
public class Record {

    @Column(name = "_id")
    public long id;
}
