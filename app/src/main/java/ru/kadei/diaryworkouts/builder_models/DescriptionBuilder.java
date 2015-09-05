package ru.kadei.diaryworkouts.builder_models;

/**
 * Created by kadei on 30.08.15.
 */
public abstract class DescriptionBuilder extends DefaultBuilder {

    protected final BufferDescriptions bufferDescriptions;

    public DescriptionBuilder(BufferDescriptions bufferDescriptions) {
        this.bufferDescriptions = bufferDescriptions;
    }
}
