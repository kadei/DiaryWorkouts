package ru.kadei.diaryworkouts.util.PrimitiveCollection;

import static java.lang.System.*;
import static ru.kadei.diaryworkouts.util.PrimitiveCollection.ArrayUtil.exists;
import static ru.kadei.diaryworkouts.util.PrimitiveCollection.ArrayUtil.expand;

/**
 * Created by kadei on 06.06.2015.
 */
public class IntegerArray {

    private int mSize;
    private int[] mValues;
    private static final int[] EMPTY = new int[]{};

    public IntegerArray() {
        mValues = EMPTY;
        mSize = 0;
    }

    public IntegerArray(int capacity) {
        mValues = new int[capacity];
        mSize = 0;
    }

    public IntegerArray(int[] array) {
        wrap(array);
    }

    public IntegerArray(IntegerArray list) {
        mValues = list.getArray();
        mSize = list.size();
    }

    public void add(int value) {
        if(mSize >= mValues.length) mValues = expand(mValues, 1);
        mValues[mSize++] = value;
    }

    public void add(int[] values) {
        int newSize = mSize + values.length;
        if(newSize >= mValues.length)
            mValues = expand(mValues, newSize - mValues.length);

        for (int value : values) mValues[mSize++] = value;
    }

    public void add(IntegerArray list) {
        add(list.getArray());
    }

    public void addIfNotHas(int value) {
        if(exists(mValues, 0, mSize, value) == -1) add(value);
    }

    public void addIfNotHas(int[] values) {
        for (int value : values) addIfNotHas(value);
    }

    public void addIfNotHas(IntegerArray values) {
        addIfNotHas(values.getArray());
    }

    public void insert(int position, int value) {
        if(mSize >= mValues.length) mValues = expand(mValues, 1);

        arraycopy(mValues, position, mValues, position + 1, mSize - position);
        mValues[position] = value;
        ++mSize;
    }

    public void insert(int position, int[] values) {
        int newSize = mSize + values.length;
        if(newSize >= mValues.length)
            mValues = expand(mValues, newSize - mValues.length);

        arraycopy(mValues, position, mValues, position + values.length, mSize - position);
        arraycopy(values, 0, mValues, position, values.length);
        mSize += values.length;
    }

    public void insert(int position, IntegerArray list) {
        insert(position, list.getArray());
    }

    public void removeAt(int position) {
        arraycopy(mValues, position + 1, mValues, position, --mSize - position);
    }

    public void remove(int value) {
        for(int i = 0; i < mSize;) {
            if(mValues[i] == value) removeAt(i);
            else ++i;
        }
    }

    public void remove(int[] values) {
        for(int i = 0; i < values.length; ++i) remove(values[i]);
    }

    public void remove(IntegerArray values) {
        remove(values.getArray());
    }

    public void swap(int indexOne, int indexTwo) {
        int tmp = mValues[indexOne];
        mValues[indexOne] = mValues[indexTwo];
        mValues[indexTwo] = tmp;
    }

    public void wrap(int value) {
        mValues = new int[]{value};
        mSize = 1;
    }

    public void wrap(int[] values) {
        if(values == null) {
            mValues = EMPTY;
            mSize = 0;
        }
        else {
            mValues = values;
            mSize = values.length;
        }
    }

    public int get(int position) {
        return mValues[position];
    }

    public int getFirst() {
        return mValues[0];
    }

    public int getLast() {
        return mValues[mSize-1];
    }

    public int[] getArray() {
        int[] result = new int[mSize];
        arraycopy(mValues, 0, result, 0, mSize);
        return result;
    }

    public int size() {
        return mSize;
    }

    public int capacity() {
        return mValues.length;
    }

    public boolean isEmpty() {
        return mSize <= 0;
    }

    public boolean contains(int value) {
        return exists(mValues, 0, mSize, value) >= 0;
    }

    public void reset() {
        mSize = 0;
    }

    public void clear() {
        mSize = 0;
        mValues = EMPTY;
    }
}
