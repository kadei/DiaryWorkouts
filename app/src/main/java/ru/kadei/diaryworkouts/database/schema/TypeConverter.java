package ru.kadei.diaryworkouts.database.schema;

import java.io.Serializable;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

import ru.kadei.diaryworkouts.database.DatabaseManager;
import ru.kadei.diaryworkouts.database.annotations.Column;

import static ru.kadei.diaryworkouts.database.utils.ReflectionUtils.getAnnotationFor;
import static ru.kadei.diaryworkouts.database.utils.ReflectionUtils.implementsInterface;

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
        TO_ONE,
        TO_MANY,
        SERIALIZABLE,
        SERIALIZABLE_ARRAY
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
            else if(isToOne(type))
                return TYPE.TO_ONE;
            else if(isToMany(type))
                return TYPE.TO_MANY;
            else if(isSerializable(type))
                return TYPE.SERIALIZABLE;
            else if(isSerializableArray(type))
                return TYPE.SERIALIZABLE_ARRAY;
        }
        throw new RuntimeException("Unexpected type ["+type.getName()+"]");
    }

    private boolean isId(Field field) {
        Annotation annotation = getAnnotationFor(field, Column.class);
        String name = annotation == null ? field.getName() : ((Column)annotation).name();
        return name.equals("_id");
    }

    private boolean isInteger(Class type) {
        return type == byte.class || type == short.class || type == int.class
                || type == long.class || type == boolean.class;
    }

    private boolean isReal(Class type) {
        return type == float.class || type == double.class;
    }

    private boolean isText(Class type) {
        return type == char.class || type == String.class;
    }

    private boolean isToMany(Class type) {
        return isIterable(type) && isToOne(type.getComponentType());
    }

    private boolean isIterable(Class type) {
        return type.isArray() || implementsInterface(type, Iterable.class);
    }

    private boolean isToOne(Class type) {
        return dbManager.isEntity(type);
    }

    private boolean isSerializable(Class type) {
        return implementsInterface(type, Serializable.class);
    }

    private boolean isSerializableArray(Class type) {
        return isIterable(type) && isSerializable(type);
    }
}
