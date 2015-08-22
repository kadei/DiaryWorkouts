package ru.kadei.simpleandroidorm.main;

import android.content.Context;
import android.support.annotation.NonNull;

import java.util.ArrayList;

/**
 * Created by kadei on 20.08.15.
 */
public class Database {

    private final Context context;

    private ArrayList<String> entityNames;

    public Database(@NonNull Context context) {
        this.context = context;
        entityNames = new ArrayList<>(8);
    }

    public void save(Record entity) {
    }

    public Object get(Class entity) {
        return null;
    }
}
