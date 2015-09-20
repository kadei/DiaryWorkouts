package ru.kadei.diaryworkouts.util;

import static java.lang.Math.max;
import static java.util.Arrays.binarySearch;
import static ru.kadei.diaryworkouts.util.PrimitiveCollection.ArrayUtil.expand;

/**
 * Created by kadei on 23.06.2015.
 */
public class MapFloatKey<T> {

    private float[] mKeys;
    private Object[] mValues;
    private int mSize;

    private static final float[] EMPTY_KEYS = new float[]{};
    private static final Object[] EMPTY_VALUES = new Object[]{};

    public MapFloatKey() {
        mKeys = EMPTY_KEYS;
        mValues = EMPTY_VALUES;
        mSize = 0;
    }

    public MapFloatKey(int capacity) {
        mKeys = new float[capacity];
        mValues = new Object[capacity];
        mSize = 0;
    }

    /** Return value by specified key. Exception if key not present. */
    public T get(float key, T defaultValue) {
        int pos = binarySearch(mKeys, 0, mSize, key);
        if(pos < 0) return defaultValue;

        return (T) mValues[pos];
    }

    public void reset() {
        mSize = 0;
    }

    public int size() {
        return mSize;
    }

    /** @return Position, if map contains specified key or negative number otherwise. */
    public int containsKey(float key) {
        return binarySearch(mKeys, 0, mSize, key);
    }

    /** @return Position first value, if map contains specified value or -1 otherwise. */
    public int containsValue(T value) {
        for(int i = 0; i < mSize; ++i) if(mValues[i] == value) return i;

        return -1;
    }

    public float keyAt(int index) {
        return mKeys[index];
    }

    public T valueAt(int index) {
        return (T) mValues[index];
    }

    public void put(float key, T value) {
        int pos = binarySearch(mKeys, 0, mSize, key);
        if(pos < 0) {
            if(mSize >= mKeys.length) {
                mKeys = expand(mKeys, max(1, mKeys.length * 2));
                mValues = expand(mValues, max(1, mValues.length * 2));
            }

            pos = ~pos;

            System.arraycopy(mKeys, pos, mKeys, pos + 1, mSize - pos);
            mKeys[pos] = key;
            System.arraycopy(mValues, pos, mValues, pos + 1, mSize - pos);
            mValues[pos] = value;

            ++mSize;
        }
        else { // replace value
            mValues[pos] = value;
        }
    }

    public void remove(int key) {
        int pos = binarySearch(mKeys, 0, mSize, key);
        if(pos < 0) return;

        --mSize;
        System.arraycopy(mKeys, pos + 1, mKeys, pos, mSize - pos);
        System.arraycopy(mValues, pos + 1, mValues, pos, mSize - pos);
    }
}
