package ru.kadei.diaryworkouts.database.schema;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

import ru.kadei.diaryworkouts.database.DatabaseManager;
import ru.kadei.diaryworkouts.database.annotations.Column;
import ru.kadei.diaryworkouts.database.annotations.Ignore;
import ru.kadei.diaryworkouts.database.annotations.Table;

import static ru.kadei.diaryworkouts.database.utils.AnnotationUtils.getAnnotation;
import static ru.kadei.diaryworkouts.database.utils.StringUtils.extractNameClass;

/**
 * Created by kadei on 22.08.15.
 */
public class SchemaBuilder {

    private TypeConverter typeConverter;

    public SchemaBuilder(DatabaseManager dbManager) {
        typeConverter = new TypeConverter(dbManager);
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
            case ENTITY:
                return "INTEGER";
            case SERIALIZABLE:
                return "BLOB";

            default:
                throw new RuntimeException("Unexpected type field [" + field.getType().getName() + "]");
        }
    }
}
