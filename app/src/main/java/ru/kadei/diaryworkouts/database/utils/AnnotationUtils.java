package ru.kadei.diaryworkouts.database.utils;

import java.lang.annotation.Annotation;

/**
 * Created by kadei on 22.08.15.
 */
public class AnnotationUtils {

    public static Annotation getAnnotation(Annotation[] annotations, Class target) {
        for (Annotation a : annotations) {
            if(a.annotationType() == target)
                return a;
        }
        return null;
    }
}
