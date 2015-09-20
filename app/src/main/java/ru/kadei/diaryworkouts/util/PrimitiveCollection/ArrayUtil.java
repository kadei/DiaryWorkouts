package ru.kadei.diaryworkouts.util.PrimitiveCollection;

import static java.lang.System.arraycopy;

public class ArrayUtil {

	private static final int BIT_IN_BYTE =  8;
	private static final int BYTE_IN_LONG = 8; // Long.SIZE / BIT_IN_BYTE;
    private static final int BYTE_IN_INT =  4; // Integer.SIZE / BIT_IN_BYTE;

    public static void sortDescend(int[] array, int start, int end) {
        for(int i = start, endI = end - 1; i < endI; ++i) {
            int tempI = array[i];
            for(int j = i + 1; j < end; ++j) {
                int tempJ = array[j];
                if(tempI < tempJ) {
                    array[j] = tempI;
                    array[i] = tempJ;
                    tempI = tempJ;
                }
            }
        }
    }

    public static boolean inRange(int value, int start, int end) {
        if(start > end) throw new IllegalArgumentException("start = " + start + ", end = " + end);

        return value >= start && value <= end;
    }

    /**
     * Compare two arrays
     * @param first array
     * @param start start index for first array
     * @param length length first array
     * @param second array
     * @param shift value which append with startIndex,
     *                       and serve beginning index for second array
     * @return index where values not equals, or -1
     */
    public static int compare(int[] first, int start, int length, int[] second, int shift) {
        for(int i = start; i < length; ++i)
            if(first[i] != second[i + shift]) return i;

        return -1;
    }

    /** <b>SEE:</b> {@link ArrayUtil#removeFrom(char[], char)} */
    public static int removeFrom(int[] array, int value) {
        int size = array.length;
        for(int i = 0; i < size;) {
            if(array[i] == value) {
                arraycopy(array, i + 1, array, i, --size - i);
                continue;
            }
            ++i;
        }
        return size;
    }

    /** Removed all supplied chars from array.
     * @return array size after removed chars */
    public static int removeFrom(char[] array, char ch) {
        int size = array.length;
        for(int i = 0; i < size;) {
            if(array[i] == ch) {
                arraycopy(array, i + 1, array, i, --size - i);
                continue;
            }
            ++i;
        }
        return size;
    }

    public static long[] expand(long[] arr, int expandNum) {
        if(expandNum <= 0) return arr;

        long[] newArr = new long[arr.length + expandNum];
        arraycopy(arr, 0, newArr, 0, arr.length);
        return newArr;
    }

    public static float[] expand(float[] arr, int expandNum) {
        if(expandNum <= 0) return arr;

        float[] newArr = new float[arr.length + expandNum];
        arraycopy(arr, 0, newArr, 0, arr.length);
        return newArr;
    }

    public static int[] expand(int[] arr, int expandNum) {
        if(expandNum <= 0) return arr;

        int[] newArr = new int[arr.length + expandNum];
        arraycopy(arr, 0, newArr, 0, arr.length);
        return newArr;
    }

    public static Object[] expand(Object[] arr, int expandNum) {
        if(expandNum <= 0) return arr;

        Object[] newArr = new Object[arr.length + expandNum];
        arraycopy(arr, 0, newArr, 0, arr.length);
        return newArr;
    }

    public static float sum(float[] arr, int start, int end) {
        float s = 0f;
        for(int i = start; i < end; ++i) s += arr[i];

        return s;
    }

    /** @return position value in array or -1 */
    public static int exists(char[] source, int start, int end, char ch) {
        for(int i = start; i < end; ++i)
            if(source[i] == ch) return i;

        return -1;
    }

    /** @return position value in array or -1 */
    public static int exists(int[] source, int start, int end, int val) {
        for(int i = start; i < end; ++i)
            if(source[i] == val) return i;

        return -1;
    }

    /** @return position value in array or -1 */
    public static int exists(long[] source, int start, int end, long val) {
        for(int i = start; i < end; ++i)
            if(source[i] == val) return i;

        return -1;
    }
	
	/** Convert long array, to byte array */
	public static byte[] toByteArray(long[] array) {
		byte[] b = new byte[array.length * BYTE_IN_LONG];
		long tmp;
		int  count = 0;
		int  startShift = Long.SIZE - BIT_IN_BYTE;

        for (long anArray : array) {
            for (int shift = startShift; shift >= 0; shift -= BIT_IN_BYTE) {
                tmp = anArray;
                tmp >>>= shift;
                b[count] = (byte) tmp;
                ++count;
            }
        }
		return b;
	}

    /** Convert byte array, to long array */
    public static long[] toLongArray(byte[] byteArr) {
        long[] lArr = new long[byteArr.length / BYTE_IN_LONG];
        int count = 0;

        for(int i = 0; i < lArr.length; ++i){
            lArr[i] = (long) byteArr[count];

            for(int j = 1; j < BYTE_IN_LONG; ++j){
                lArr[i] <<= 8;

                ++count;
                long tmp = (long) byteArr[count];
                tmp &= 0x00000000000000ffL;
                lArr[i] = lArr[i] | tmp;
            }
            ++count;
        }
        return lArr;
    }

    public static void longToByte(byte[] arr, int start, long value) {
        for(int i = 64 - 8; i >= 0; i -= 8) {
            long tmp = value;
            tmp >>>= i;
            arr[start++] = (byte) tmp;
        }
    }

    public static long byteToLong(byte[] arr, int start) {
        long result = arr[start++];
        for(int i = 1; i < 8; ++i) {
            result <<= 8;
            long tmp = arr[start++];
            tmp &= 0x00000000000000ffL;
            result |= tmp;
        }
        return result;
    }

    /** convert integer array, to byte array */
    public static byte[] toByteArray(int[] array) {
        byte[] b = new byte[array.length * BYTE_IN_INT];
        int tmp;
        int count = 0;
        int startShift = Integer.SIZE - BIT_IN_BYTE;

        for (int anArray : array) {
            for (int shift = startShift; shift >= 0; shift -= BIT_IN_BYTE) {
                tmp = anArray;
                tmp >>>= shift;
                b[count] = (byte) tmp;
                ++count;
            }
        }
        return b;
    }

    /** convert byte array to integer array */
    public static int[] toIntArray(byte[] byteArr) {
        int[] arr = new int[byteArr.length / BYTE_IN_INT];
        int tmp;
        int count = 0;

        for(int i = 0; i < arr.length; ++i){
            arr[i] = (int) byteArr[count];
            for(int j = 1; j < BYTE_IN_INT; j += 1){
                arr[i] <<= 8;

                ++ count;
                tmp = (int) byteArr[count];
                tmp &= 0x000000ff;

                arr[i] = arr[i] | tmp;
            }
            ++count;
        }
        return arr;
    }
}
