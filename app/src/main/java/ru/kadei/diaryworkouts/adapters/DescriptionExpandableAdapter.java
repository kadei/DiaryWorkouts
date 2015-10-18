package ru.kadei.diaryworkouts.adapters;

import android.content.Context;
import android.text.TextUtils;
import android.util.LongSparseArray;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.TextView;

import java.util.ArrayList;

import ru.kadei.diaryworkouts.R;
import ru.kadei.diaryworkouts.fragments.CustomFragment;
import ru.kadei.diaryworkouts.models.workouts.Description;

/**
 * Created by kadei on 18.10.15.
 */
public class DescriptionExpandableAdapter extends BaseExpandableListAdapter implements View.OnClickListener {

    private final LayoutInflater inflater;

    private Listener listener;

    private static final ArrayList<? extends Description> EMPTY = new ArrayList<>();
    private ArrayList<? extends Description> data = EMPTY;

    private final LongSparseArray<String> bufferString = new LongSparseArray<>(8);

    public DescriptionExpandableAdapter(CustomFragment frg) {
        inflater = (LayoutInflater) frg.getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public void setData(ArrayList<? extends Description> data) {
        if (data == null)
            this.data = EMPTY;
        else
            this.data = data;

        notifyDataSetChanged();
    }

    public void setListener(Listener listener) {
        this.listener = listener;
    }

    public void clearData() {
        data.clear();
        notifyDataSetChanged();
    }

    @Override
    public int getGroupCount() {
        return data.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return 1;
    }

    @Override
    public Object getGroup(int groupPosition) {
        return data.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return null;
    }

    @Override
    public long getGroupId(int groupPosition) {
        return data.get(groupPosition).id;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return 0L;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.expandable_group, parent, false);
            convertView.findViewById(android.R.id.button1).setOnClickListener(this);
        }

        final Description d = data.get(groupPosition);
        TextView tv = (TextView) convertView.findViewById(android.R.id.text1);
        tv.setText(d.name);

        convertView.findViewById(android.R.id.button1).setTag(d);

        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.expandable_child_description, parent, false);
        }

        final Description d = data.get(groupPosition);
        TextView tv = (TextView) convertView.findViewById(android.R.id.text1);
        tv.setText(createDescription(d));

        return convertView;
    }

    private String createDescription(Description description) {
        final long id = description.id;
        final ArrayList<? extends Description> list = description.getContent();

        String result = bufferString.get(id, null);
        if (result == null) {
            final String strDescription = description.description;
            final StringBuilder sb = new StringBuilder(64);

            if (!TextUtils.isEmpty(strDescription))
                sb.append(strDescription).append("\n\n");

            for (int i = 0, end = list.size(); i < end; ++i) {
                sb.append(i + 1).append(") ").append(list.get(i).name);
                if (i < end - 1)
                    sb.append("\n");
            }
            result = sb.toString();
            bufferString.put(id, result);
        }
        return result;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }

    @Override
    public void onClick(View v) {
        if(listener != null)
            listener.select((Description) v.getTag());
    }

    public interface Listener {
        void select(Description description);
    }
}
