package ru.kadei.diaryworkouts.database;

import android.content.Context;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

import static org.xmlpull.v1.XmlPullParser.END_DOCUMENT;
import static org.xmlpull.v1.XmlPullParser.END_TAG;
import static org.xmlpull.v1.XmlPullParser.START_TAG;
import static org.xmlpull.v1.XmlPullParser.TEXT;

/**
 * Created by kadei on 21.08.15.
 */
public class ParserOfEntities {

    public static final String ENTITY_TAG = "entity";

    private final Context context;
    private final XmlPullParserFactory factory;

    private InputStreamReader reader;

    private ParserOfEntities(Context context) throws XmlPullParserException {
        this.context = context;
        factory = XmlPullParserFactory.newInstance();
        factory.setNamespaceAware(true);
    }

    public static ParserOfEntities newParser(Context context) throws XmlPullParserException {
        return new ParserOfEntities(context);
    }

    public ArrayList<String> getNameEntitiesFrom(String nameFile) throws XmlPullParserException, IOException {
        XmlPullParser xpp = getParserFor(nameFile);
        ArrayList<String> list = parse(xpp);
        reader.close();
        reader = null;
        return list;
    }

    private XmlPullParser getParserFor(String nameFile) throws XmlPullParserException, IOException {
        XmlPullParser xpp = factory.newPullParser();
        reader = new InputStreamReader(context.getAssets().open(nameFile));
        xpp.setInput(reader);
        return xpp;
    }

    private ArrayList<String> parse(XmlPullParser parser) throws XmlPullParserException, IOException {
        final ArrayList<String> list = new ArrayList<>(8);
        boolean tagFlag = false;
        int event = parser.getEventType();

        while (event != END_DOCUMENT) {
            if(event == START_TAG) {
                if(parser.getName().equals(ENTITY_TAG))
                    tagFlag = true;
            }
            else if(event == TEXT) {
                if (tagFlag)
                    list.add(parser.getText());
            }
            else if(event == END_TAG) {
                if(parser.getName().equals(ENTITY_TAG))
                    tagFlag = false;
            }
            event = parser.next();
        }
        return list;
    }
}