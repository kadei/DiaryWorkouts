package ru.kadei.diaryworkouts.util.primitive_collection;

import static java.lang.System.*;
import static ru.kadei.diaryworkouts.util.primitive_collection.ArrayUtil.exists;
import static ru.kadei.diaryworkouts.util.primitive_collection.ArrayUtil.expand;

/**
 * Simple array dynamic size.
 */
public class LongArray {

    private int mSize;
    private long[] mValues;
    private static final long[] EMPTY = new long[]{};

    public LongArray() {
        mValues = EMPTY;
        mSize = 0;
    }

    public LongArray(int capacity) {
        mValues = new long[capacity];
        mSize = 0;
    }

    public LongArray(long[] array) {
        wrap(array);
    }

    public LongArray(LongArray list) {
        mValues = list.getArray();
        mSize = list.size();
    }

    public void add(long value) {
        if(mSize >= mValues.length) mValues = expand(mValues, 1);
        mValues[mSize++] = value;
    }

    public void add(long[] values) {
        int newSize = mSize + values.length;
        if(newSize >= mValues.length)
            mValues = expand(mValues, newSize - mValues.length);

        for(int i = 0; i < values.length; ++i)
            mValues[mSize++] = values[i];
    }

    public void add(LongArray list) {
        add(list.getArray());
    }

    public void addIfNotHas(long value) {
        if(exists(mValues, 0, mSize, value) == -1) add(value);
    }

    public void addIfNotHas(long[] values) {
        for(int i = 0; i < values.length; ++i) addIfNotHas(values[i]);
    }

    public void addIfNotHas(LongArray values) {
        addIfNotHas(values.getArray());
    }

    public void insert(int position, long value) {
        if(mSize >= mValues.length) mValues = expand(mValues, 1);

        arraycopy(mValues, position, mValues, position + 1, mSize - position);
        mValues[position] = value;
        ++mSize;
    }

    public void insert(int position, long[] values) {
        int newSize = mSize + values.length;
        if(newSize >= mValues.length)
            mValues = expand(mValues, newSize - mValues.length);

        arraycopy(mValues, position, mValues, position + values.length, mSize - position);
        arraycopy(values, 0, mValues, position, values.length);
        mSize += values.length;
    }

    public void insert(int position, LongArray list) {
        insert(position, list.getArray());
    }

    public void removeAt(int position) {
        arraycopy(mValues, position + 1, mValues, position, --mSize - position);
    }

    public void remove(long value) {
        for(int i = 0; i < mSize;) {
            if(mValues[i] == value) removeAt(i);
            else ++i;
        }
    }

    public void remove(long[] values) {
        for(int i = 0; i < values.length; ++i) remove(values[i]);
    }

    public void remove(LongArray values) {
        remove(values.getArray());
    }

    public void swap(int indexOne, int indexTwo) {
        long tmp = mValues[indexOne];
        mValues[indexOne] = mValues[indexTwo];
        mValues[indexTwo] = tmp;
    }

    public void wrap(long value) {
        mValues = new long[]{value};
        mSize = 1;
    }

    public void wrap(long[] values) {
        if(values == null) {
            mValues = EMPTY;
            mSize = 0;
        }
        else {
            mValues = values;
            mSize = values.length;
        }
    }

    public long get(int position) {
        return mValues[position];
    }

    public long getFirst() {
        return mValues[0];
    }

    public long getLast() {
        return mValues[mSize-1];
    }

    public long[] getArray() {
        long[] result = new long[mSize];
        arraycopy(mValues, 0, result, 0, mSize);
        return result;
    }

    public int size() {
        return mSize;
    }

    public boolean isEmpty() {
        return mSize <= 0;
    }

    public boolean contains(long value) {
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
