package ru.kadei.diaryworkouts.models.db;

import android.content.ContentValues;

import java.util.ArrayList;

/**
 * Created by kadei on 03.09.15.
 */
public class Relation {

    public String nameTable;
    public long idTarget;
    public String columnIdTarget;
    public ArrayList<ContentValues> values;
}
