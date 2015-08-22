package ru.kadei.diaryworkouts.database.schema;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

import ru.kadei.diaryworkouts.database.DatabaseManager;
import ru.kadei.diaryworkouts.database.annotations.Column;
import ru.kadei.diaryworkouts.database.annotations.Ignore;
import ru.kadei.diaryworkouts.database.annotations.Table;

import static ru.kadei.diaryworkouts.utils.StringUtils.extractNameClass;

/**
 * Created by kadei on 22.08.15.
 */
public class SchemaBuilder {

    private DatabaseManager dbManager;

    public SchemaBuilder(DatabaseManager dbManager) {
        this.dbManager = dbManager;
    }

    public Schema buildSchemaFor(Class target) {
        final Schema schema = new Schema();
        schema.nameTable = getNameTableFor(target);
        schema.schemaRecord = getSchemaRecordFor(target);
        return schema;
    }

    private String getNameTableFor(Class target) {
        Annotation a = getAnnotation(target.getDeclaredAnnotations(), Table.class);
        return a == null ? extractNameClass(target) : ((Table)a).name();
    }

    private SchemaRecord getSchemaRecordFor(Class target) {
        final SchemaRecord schemaRecord = new SchemaRecord();
        do {
            final Field[] fields = target.getDeclaredFields();
            for (Field f : fields) {
                if (!ignore(f)) {
                    String nameField = f.getName();
                    String nameColumn = getNameColumnFor(f);
                    String typeColumn = getTypeFor(f);
                    schemaRecord.addElement(nameField, nameColumn, typeColumn);
                }
            }
            target = target.getSuperclass();
        } while (target != null && target != Object.class);
        return schemaRecord;
    }

    private boolean ignore(Field field) {
        return getAnnotation(field.getDeclaredAnnotations(), Ignore.class) != null;
    }

    private String getNameColumnFor(Field field) {
        Annotation a = getAnnotation(field.getDeclaredAnnotations(), Column.class);
        return a == null ? field.getName() : ((Column) a).name();
    }

    private Annotation getAnnotation(Annotation[] annotations, Class target) {
        for (Annotation a : annotations) {
            if(a.annotationType() == target)
                return a;
        }
        return null;
    }

    private String getTypeFor(Field field) {
        Class typeField = field.getType();
        if(isId(field))
            return getTypeForId();
        else if(typeField.isPrimitive())
            return getTypeForPrimitive(typeField);
        else if(isEntity(typeField))
            return getTypeForEntity();

        return null;
    }

    private boolean isId(Field field) {
        return getNameColumnFor(field).equals("_id");
    }

    private String getTypeForId() {
        return "INTEGER PRIMARY KEY AUTOINCREMENT";
    }

    private String getTypeForPrimitive(Class type) {
        if(isInteger(type))
            return "INTEGER";
        else if(isReal(type))
            return "REAL";
        else // if(type == char.class)
            return "TEXT";
    }

    private static boolean isInteger(Class type) {
        return type == byte.class || type == short.class || type == int.class
                || type == long.class || type == boolean.class;
    }

    private static boolean isReal(Class type) {
        return type == float.class || type == double.class;
    }

    private boolean isEntity(Class type) {
        return dbManager.isEntity(type);
    }

    private String getTypeForEntity() {
        return "INTEGER"; // for _id
    }
}
