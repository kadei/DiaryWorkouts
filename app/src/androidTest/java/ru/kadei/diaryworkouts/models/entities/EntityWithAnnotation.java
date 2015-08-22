package ru.kadei.diaryworkouts.models.entities;

import ru.kadei.diaryworkouts.database.annotations.Column;
import ru.kadei.diaryworkouts.database.annotations.Table;

/**
 * Created by kadei on 22.08.15.
 */
@Table(name = "table_for_entity")
public class EntityWithAnnotation {

    @Column(name = "_id")
    long id;

    boolean isExists;

    @Column(name = "summa")
    float sum;
}
