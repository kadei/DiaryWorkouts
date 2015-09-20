package ru.kadei.diaryworkouts.adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import ru.kadei.diaryworkouts.R;
import ru.kadei.diaryworkouts.models.workouts.Description;
import ru.kadei.diaryworkouts.util.PrimitiveCollection.IntegerArray;
import ru.kadei.diaryworkouts.view.DynamicListView;

/**
 * Created by kadei on 26.05.2015.
 */
public class SortListAdapter extends ExtendedBaseAdapter implements DynamicListView.Swaper {

    ArrayList<Description> mList;
    IntegerArray mIDs = new IntegerArray(10);

    public SortListAdapter(Context context) {
        super(context);
    }

    public void setData(ArrayList<Description> list) {
        mList = list;
        if(list != null) {
            mIDs.reset();
            for(int i = 0, end = list.size(); i < end; ++i) mIDs.add(i);
        }
    }

    public void removeById(int id) {
        for(int i = 0; i < mIDs.size(); ++i) {
            if(mIDs.get(i) == id) {
                mIDs.removeAt(i);
                mList.remove(i);
                break;
            }
        }
    }

    @Override
    public void swap(int indexOne, int indexTwo) {
        Description tmp = mList.get(indexOne);
        mList.set(indexOne, mList.get(indexTwo));
        mList.set(indexTwo, tmp);

        mIDs.swap(indexOne, indexTwo);
    }

    @Override
    public int getCount() {
        return mList == null ? 0 : mList.size();
    }

    @Override
    public Object getItem(int position) {
        return mList == null ? null : mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        if(position < 0 || position >= mList.size())
            return -1;

        return mIDs.get(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView == null)
            convertView = inflater.inflate(R.layout.item__sort_list, parent, false);

        Description description = mList.get(position);

        TextView tv = (TextView) convertView.findViewById(android.R.id.text1);
        tv.setText(description.name);

        return convertView;
    }
}
