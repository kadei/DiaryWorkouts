package ru.kadei.diaryworkouts.models.entities;

import ru.kadei.diaryworkouts.database.annotations.Column;

/**
 * Created by kadei on 22.08.15.
 */
public class EntityWithSerializable {

    float x;
    float y;

    String name;

    @Column(name = "serial")
    SerializableObject serializableObject;
}
