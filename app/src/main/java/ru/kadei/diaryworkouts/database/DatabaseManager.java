package ru.kadei.diaryworkouts.database;

import android.content.Context;
import android.support.annotation.NonNull;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.ArrayList;

import static ru.kadei.diaryworkouts.database.ParserOfEntities.newParser;

/**
 * Created by kadei on 21.08.15.
 */
public class DatabaseManager {

    public static final String FILE_ENTITIES = "entities.xml";

    private final Context context;

    private final String nameDatabase;
    private ArrayList<String> nameEntities;

    public DatabaseManager(@NonNull Context context, @NonNull String nameDatabase)
            throws IOException, XmlPullParserException {
        this.context = context;
        this.nameDatabase = nameDatabase;
        checkSchemas();
    }

    private void checkSchemas() throws XmlPullParserException, IOException {
        loadNameEntities();
    }

    private void loadNameEntities() throws XmlPullParserException, IOException {
        nameEntities = newParser(context).getNameEntitiesFrom(FILE_ENTITIES);
        if(nameEntities.isEmpty())
            throw new RuntimeException("no registered entities, check file " + FILE_ENTITIES);
    }

    public boolean isEntity(Class c) {
        final String name = c.getName();
        for(String ne : nameEntities)
            if(name.equals(ne)) return true;

        return false;
    }

    public ArrayList<String> getNameEntities() {
        return nameEntities;
    }
}
