package ru.kadei.diaryworkouts.builders;

import ru.kadei.diaryworkouts.database.DatabaseReader;
import ru.kadei.diaryworkouts.managers.BufferDescriptions;

/**
 * Created by kadei on 30.08.15.
 */
public abstract class DescriptionReader extends DatabaseReader {

    protected final BufferDescriptions bufferDescriptions;

    public DescriptionReader(BufferDescriptions bufferDescriptions) {
        this.bufferDescriptions = bufferDescriptions;
    }

    BufferDescriptions getBufferDescriptions() {
        return bufferDescriptions;
    }
}
