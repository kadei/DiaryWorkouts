package ru.kadei.diaryworkouts.models.entities;

import ru.kadei.diaryworkouts.database.annotations.Column;
import ru.kadei.diaryworkouts.database.annotations.Ignore;
import ru.kadei.diaryworkouts.database.annotations.Table;

/**
 * Created by kadei on 22.08.15.
 */
@Table(name = "ignoreFields")
public class EntityWithIgnoreFields {

    @Column(name = "_id")
    long id;

    @Ignore
    @Column(name = "y")
    float yPosition;

    float xPosition;

    @Column(name = "distance")
    double d;

    @Ignore
    char symbol;
}
