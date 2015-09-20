package ru.kadei.diaryworkouts.adapters;

import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;
import android.util.Pair;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import ru.kadei.diaryworkouts.R;
import ru.kadei.diaryworkouts.activities.MainActivity;
import ru.kadei.diaryworkouts.managers.ResourceManager;
import ru.kadei.diaryworkouts.util.PrimitiveCollection.IntegerArray;


public class NavigationAdapter extends ExtendedBaseAdapter {

    private int selectedPos = -1;

    private ArrayList<Pair<Drawable, String>> items;
    private IntegerArray disabledItem;

    private ColorStateList originalTextColor;
    private ColorStateList disabledTextColor;

    public NavigationAdapter(MainActivity activity, ArrayList<Pair<Drawable, String>> items) {
        super(activity);
        disabledItem = new IntegerArray(4);
        this.items = items;

        ResourceManager res = activity.getResourceManager();
        originalTextColor = res.getColorStateList(R.color.primary_text);
        disabledTextColor = res.getColorStateList(R.color.disabled_text);
    }

    @Override
    public int getCount() {
        return items == null ? 0 : items.size();
    }

    @Override
    public Object getItem(int position) {
        return items == null ? null : items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView == null)
            convertView = inflater.inflate(R.layout.navigation_item, parent, false);

        Pair<Drawable, String> pair = items.get(position);

        View v = convertView.findViewById(android.R.id.icon);
        v.setBackground(pair.first);

        TextView tv = (TextView) convertView.findViewById(android.R.id.text1);
        tv.setText(pair.second);

        if(disabledItem.contains(position))
            setTextColor(tv, disabledTextColor);
        else
            setTextColor(tv, originalTextColor);

        return convertView;
    }

    private static void setTextColor(TextView tv, ColorStateList color) {
        if(tv.getTextColors() != color) {
            tv.setTextColor(color);
        }
    }

    public IntegerArray getDisabledItem() {
        return disabledItem;
    }

    public void disableItem(int pos) {
        disabledItem.addIfNotHas(pos);
    }

    public void enableItem(int pos) {
        disabledItem.remove(pos);
    }

    @Override
    public boolean isEnabled(int position) {
        return !disabledItem.contains(position);
    }
}