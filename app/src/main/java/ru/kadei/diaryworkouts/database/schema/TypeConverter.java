package ru.kadei.diaryworkouts.database.schema;

import java.io.Serializable;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

import ru.kadei.diaryworkouts.database.DatabaseManager;
import ru.kadei.diaryworkouts.database.annotations.Column;

import static ru.kadei.diaryworkouts.database.utils.AnnotationUtils.getAnnotation;

/**
 * Created by kadei on 22.08.15.
 */
public class TypeConverter {


    private DatabaseManager dbManager;

    public enum TYPE {
        ID,
        INTEGER,
        REAL,
        TEXT,
        ENTITY,
        SERIALIZABLE
    }

    public TypeConverter(DatabaseManager dbManager) {
        this.dbManager = dbManager;
    }

    public TYPE getType(Field field) {
        final Class type = field.getType();
        if(type.isPrimitive()) {
            if (isId(field))
                return TYPE.ID;
            else if (isInteger(type))
                return TYPE.INTEGER;
            else if (isReal(type))
                return TYPE.REAL;
            else if (isText(type))
                return TYPE.TEXT;
        }
        else {
            if(isText(type))
                return TYPE.TEXT;
            else if(isEntity(type))
                return TYPE.ENTITY;
            else if(isSerializable(type))
                return TYPE.SERIALIZABLE;
        }

        throw new RuntimeException("Unexpected type [" + type.getName() + "]");
    }

    private static boolean isId(Field field) {
        Annotation annotation = getAnnotation(field.getDeclaredAnnotations(), Column.class);
        String name = annotation == null ? field.getName() : ((Column)annotation).name();
        return name.equals("_id");
    }

    private static boolean isInteger(Class type) {
        return type == byte.class || type == short.class || type == int.class
                || type == long.class || type == boolean.class;
    }

    private static boolean isReal(Class type) {
        return type == float.class || type == double.class;
    }

    private static boolean isText(Class type) {
        return type == char.class || type == String.class;
    }

    private boolean isEntity(Class type) {
        return dbManager.isEntity(type);
    }

    private static boolean isSerializable(Class type) {
        Class[] interfaces = type.getInterfaces();
        if(interfaces != null) {
            for(Class i : interfaces)
                if(i == Serializable.class)
                    return true;
        }
        return false;
    }
}
