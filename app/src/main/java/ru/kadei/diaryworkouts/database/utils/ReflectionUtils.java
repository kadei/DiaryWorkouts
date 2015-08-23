package ru.kadei.diaryworkouts.database.utils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

/**
 * Created by kadei on 22.08.15.
 */
public class ReflectionUtils {

    public static Annotation getAnnotationFor(Class type, Class annotation) {
        return getAnnotation(type.getDeclaredAnnotations(), annotation);
    }

    public static Annotation getAnnotationFor(Field field, Class annotation) {
        return getAnnotation(field.getDeclaredAnnotations(), annotation);
    }

    public static Annotation getAnnotation(Annotation[] annotations, Class target) {
        for (Annotation a : annotations) {
            if(a.annotationType() == target)
                return a;
        }
        return null;
    }

    public static boolean implementsInterface(Class type, Class classInterface) {
        Class[] interfaces = type.getInterfaces();
        if(interfaces != null) {
            for(Class i : interfaces)
                if(i == classInterface)
                    return true;
        }
        return false;
    }
}
