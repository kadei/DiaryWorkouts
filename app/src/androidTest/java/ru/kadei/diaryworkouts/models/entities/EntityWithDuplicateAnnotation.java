package ru.kadei.diaryworkouts.models.entities;

import ru.kadei.diaryworkouts.database.models.Record;
import ru.kadei.diaryworkouts.database.annotations.Column;

/**
 * Created by kadei on 23.08.15.
 */
public class EntityWithDuplicateAnnotation extends Record {

    @Column(name = "_id")
    long myId;
}
