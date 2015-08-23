package ru.kadei.diaryworkouts.database.utils;

import java.util.Arrays;
import java.util.List;

/**
 * Created by kadei on 23.08.15.
 */
public class ArrayUtils {

    public static int containsDuplicate(Object[] objects) {
        return containsDuplicate(Arrays.asList(objects));
    }

    public static int containsDuplicate(List<?> list) {
        for(int i = 0, endI = list.size()-1; i < endI; ++i) {
            Object objI = list.get(i);
            for(int j = i + 1, endJ = list.size(); j < endJ; ++j) {
                if (objI.equals(list.get(j)))
                    return i;
            }
        }
        return -1;
    }
}
