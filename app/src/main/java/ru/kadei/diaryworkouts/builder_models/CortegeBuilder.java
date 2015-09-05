package ru.kadei.diaryworkouts.builder_models;

import ru.kadei.diaryworkouts.models.db.Cortege;

/**
 * Created by kadei on 03.09.15.
 */
public abstract class CortegeBuilder {

    protected Cortege cortege;

    public abstract void buildCortegeFor(Object object);

    public Cortege getCortege() {
        Cortege c = cortege;
        cortege = null;
        return c;
    }

    protected final void oops(Object object) {
        throw new RuntimeException("Unexpected object ["+object+"]");
    }
}
