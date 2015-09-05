package ru.kadei.diaryworkouts.models.db;

import android.content.ContentValues;

import java.util.ArrayList;

/**
 * Created by kadei on 03.09.15.
 */
public class Cortege {

    public long id;
    public String nameTable;
    public ContentValues values;
    public final ArrayList<Relation> relations;

    public Cortege() {
        relations = new ArrayList<>(8);
    }
}