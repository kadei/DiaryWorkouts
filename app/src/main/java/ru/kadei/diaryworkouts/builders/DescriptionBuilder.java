package ru.kadei.diaryworkouts.builders;

import ru.kadei.diaryworkouts.database.ObjectBuilder;
import ru.kadei.diaryworkouts.managers.BufferDescriptions;

/**
 * Created by kadei on 30.08.15.
 */
public abstract class DescriptionBuilder extends ObjectBuilder {

    protected final BufferDescriptions bufferDescriptions;

    public DescriptionBuilder(BufferDescriptions bufferDescriptions) {
        this.bufferDescriptions = bufferDescriptions;
    }
}
