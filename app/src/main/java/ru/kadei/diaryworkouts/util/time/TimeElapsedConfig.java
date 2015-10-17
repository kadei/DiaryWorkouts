package ru.kadei.diaryworkouts.util.time;

/**
 * Created by kadei on 17.10.15.
 */
public class TimeElapsedConfig {

    public static final int INDEX_YEAR = 0;
    public static final int INDEX_MONTH = 1;
    public static final int INDEX_WEEK = 2;
    public static final int INDEX_DAY = 3;
    public static final int INDEX_HOUR = 4;
    public static final int INDEX_MINUTE = 5;
    public static final int INDEX_SECOND = 6;

    public final String[][] words;
    public long timeElapsed;
    public long minimum;

    public final StringBuilder sb = new StringBuilder(64);

    public TimeElapsedConfig() {
        this.words = new String[7][];
    }

    public void calcTimeElapsedAndMin(long currentTime, long pastTime) {
        timeElapsed = currentTime - pastTime;
        minimum = TimeUtil.solveMin(timeElapsed);
    }

    public String result() {
        final String str = sb.toString();
        sb.delete(0, sb.length());
        return str;
    }


}
