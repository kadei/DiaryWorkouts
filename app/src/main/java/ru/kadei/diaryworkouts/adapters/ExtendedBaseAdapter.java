package ru.kadei.diaryworkouts.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.BaseAdapter;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;

public abstract class ExtendedBaseAdapter extends BaseAdapter {

    protected final LayoutInflater inflater;

    protected ExtendedBaseAdapter(Context context) {
        inflater = (LayoutInflater) context.getSystemService(LAYOUT_INFLATER_SERVICE);
    }
}
