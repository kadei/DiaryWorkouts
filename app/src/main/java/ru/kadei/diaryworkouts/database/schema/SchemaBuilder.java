package ru.kadei.diaryworkouts.database.schema;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

import ru.kadei.diaryworkouts.database.DatabaseManager;
import ru.kadei.diaryworkouts.database.annotations.Column;
import ru.kadei.diaryworkouts.database.annotations.Ignore;
import ru.kadei.diaryworkouts.database.annotations.Table;

import static ru.kadei.diaryworkouts.database.utils.ReflectionUtils.getAnnotationFor;
import static ru.kadei.diaryworkouts.database.utils.StringUtils.extractNameClass;

/**
 * Created by kadei on 22.08.15.
 */
public class SchemaBuilder {

    private TypeConverter typeConverter;

    private Class target;

    public SchemaBuilder(DatabaseManager dbManager) {
        typeConverter = new TypeConverter(dbManager);
    }

    public Schema buildSchemaFor(Class target) {
        final Schema schema = new Schema();
        this.target = target;
        schema.nameEntity = target.getName();
        schema.nameTable = getNameTableFor(target);
        schema.schemaCortege = getSchemaCortegeFor(target);
        return schema;
    }

    private String getNameTableFor(Class target) {
        Annotation a = getAnnotationFor(target, Table.class);
        return a == null ? extractNameClass(target) : ((Table)a).name();
    }

    private SchemaCortege getSchemaCortegeFor(Class target) {
        final TypeConverter converter = typeConverter;
        final SchemaCortege schemaCortege = new SchemaCortege();
        do {
            for (Field field : target.getDeclaredFields()) {
                if (!ignore(field)) {
                    if(converter.getType(field) == TypeConverter.TYPE.TO_MANY) {

                    }
                    else {
                        String nameField = field.getName();
                        String nameColumn = getNameColumnFor(field);
                        String typeColumn = getTypeFor(field);
                        schemaCortege.addElement(nameField, nameColumn, typeColumn);
                    }
                }
            }
            target = target.getSuperclass();
        } while (target != null && target != Object.class);
        return schemaCortege;
    }

    private boolean ignore(Field field) {
        return getAnnotationFor(field, Ignore.class) != null;
    }

    private String getNameColumnFor(Field field) {
        Annotation a = getAnnotationFor(field, Column.class);
        return a == null ? field.getName() : ((Column) a).name();
    }

    private String getTypeFor(Field field) {
        switch (typeConverter.getType(field)) {
            case ID:
                return "INTEGER PRIMARY KEY AUTOINCREMENT";
            case INTEGER:
                return "INTEGER";
            case REAL:
                return "REAL";
            case TEXT:
                return "TEXT";
            case TO_ONE:
                return "INTEGER";
            case SERIALIZABLE:
                return "BLOB";
            case TO_MANY:
                return "";

            default:
                throw new RuntimeException("Unexpected type field ["+field.getType().getName()+"]");
        }
    }
}
