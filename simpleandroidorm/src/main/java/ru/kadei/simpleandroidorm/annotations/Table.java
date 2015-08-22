package ru.kadei.simpleandroidorm.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by kadei on 20.08.15.
 */
@Retention(value = RetentionPolicy.RUNTIME)
public @interface Table {
    String name();
}
