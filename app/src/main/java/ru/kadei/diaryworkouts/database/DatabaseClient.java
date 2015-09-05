package ru.kadei.diaryworkouts.database;

import ru.kadei.diaryworkouts.builder_models.DefaultBuilder;
import ru.kadei.diaryworkouts.models.db.Cortege;

/**
 * Created by kadei on 05.09.15.
 */
public interface DatabaseClient {
    void loaded(DefaultBuilder builder);
    void saved(Cortege cortege);
    void fail(Throwable throwable);
}
