package ru.kadei.diaryworkouts.builders;

import ru.kadei.diaryworkouts.database.DatabaseReader;

/**
 * Created by kadei on 30.08.15.
 */
public abstract class DescriptionReader extends DatabaseReader {

    protected final BufferDescriptions bufferDescriptions;

    public DescriptionReader(BufferDescriptions bufferDescriptions, StringBuilder sb) {
        super(sb);
        this.bufferDescriptions = bufferDescriptions;
    }

    BufferDescriptions getBufferDescriptions() {
        return bufferDescriptions;
    }
}
