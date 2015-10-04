package ru.kadei.diaryworkouts.util.primitive_collection;

import java.util.Arrays;

/**
 * Created by kadei on 04.10.15.
 */
public class SimpleSparseLongArray {

    private long[] keys;
    private long[] values;
    private int size;

    private static final long[] EMPTY = new long[]{};

    public SimpleSparseLongArray() {
        this(8);
    }

    public SimpleSparseLongArray(int initialCapacity) {
        if (initialCapacity == 0) {
            keys = EMPTY;
            values = EMPTY;
        } else {
            keys = new long[initialCapacity];
            values = new long[initialCapacity];
        }
        size = 0;
    }

    public long get(long key, long valueIfKeyNotFound) {
        int i = Arrays.binarySearch(keys, 0, size, key);

        if (i < 0) {
            return valueIfKeyNotFound;
        } else {
            return values[i];
        }
    }

    public void delete(long key) {
        int i = Arrays.binarySearch(keys, 0, size, key);

        if (i >= 0) {
            removeAt(i);
        }
    }

    public void removeAt(int index) {
        System.arraycopy(keys, index + 1, keys, index, size - (index + 1));
        System.arraycopy(values, index + 1, values, index, size - (index + 1));
        size--;
    }

    public void put(long key, long value) {
        int i = Arrays.binarySearch(keys, 0, size, key);

        if (i >= 0) {
            values[i] = value;
        } else {
            i = ~i;

            keys = insert(keys, size, i, key);
            values = insert(values, size, i, value);
            size++;
        }
    }

    private static long[] insert(long[] array, int currentSize, int index, long element) {
//        assert currentSize <= array.length;

        if (currentSize + 1 <= array.length) {
            System.arraycopy(array, index, array, index + 1, currentSize - index);
            array[index] = element;
            return array;
        }

        long[] newArray = new long[growSize(currentSize)];
        System.arraycopy(array, 0, newArray, 0, index);
        newArray[index] = element;
        System.arraycopy(array, index, newArray, index + 1, array.length - index);
        return newArray;
    }

    private static int growSize(int size) {
        return size <= 4 ? 8 : size * 2;
    }

    public int size() {
        return size;
    }

    public long keyAt(int index) {
        return keys[index];
    }

    public long valueAt(int index) {
        return values[index];
    }

    public int indexOfKey(long key) {
        return Arrays.binarySearch(keys, 0, size, key);
    }

    public int indexOfValue(long value) {
        for (int i = 0; i < size; i++)
            if (values[i] == value)
                return i;

        return -1;
    }

    public void clear() {
        size = 0;
    }

    @Override
    public String toString() {
        if (size() <= 0) {
            return "{}";
        }

        StringBuilder buffer = new StringBuilder(size * 28);
        buffer.append('{');
        for (int i = 0; i < size; i++) {
            if (i > 0) {
                buffer.append(", ");
            }
            long key = keyAt(i);
            buffer.append(key);
            buffer.append('=');
            long value = valueAt(i);
            buffer.append(value);
        }
        buffer.append('}');
        return buffer.toString();
    }
}
