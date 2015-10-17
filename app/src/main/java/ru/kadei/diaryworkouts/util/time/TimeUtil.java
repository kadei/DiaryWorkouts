package ru.kadei.diaryworkouts.util.time;

import static ru.kadei.diaryworkouts.util.time.TimeElapsedConfig.INDEX_DAY;
import static ru.kadei.diaryworkouts.util.time.TimeElapsedConfig.INDEX_HOUR;
import static ru.kadei.diaryworkouts.util.time.TimeElapsedConfig.INDEX_MINUTE;
import static ru.kadei.diaryworkouts.util.time.TimeElapsedConfig.INDEX_MONTH;
import static ru.kadei.diaryworkouts.util.time.TimeElapsedConfig.INDEX_SECOND;
import static ru.kadei.diaryworkouts.util.time.TimeElapsedConfig.INDEX_WEEK;
import static ru.kadei.diaryworkouts.util.time.TimeElapsedConfig.INDEX_YEAR;

/**
 * Created by kadei on 31.01.2015.
 */
public class TimeUtil {

    public static final long SEC = 1000L;
    public static final long MIN = SEC * 60L;
    public static final long HOUR = MIN * 60L;
    public static final long DAY = HOUR * 24L;
    public static final long WEEK = DAY * 7L;
    public static final long MONTH = DAY * 30L;
    public static final long YEAR = DAY * 365L;

    public static void timeForTimer(StringBuilder sb, long time) {

        long amount = time / MIN;
        if (amount < 10)
            sb.append("0");

        time %= MIN;
        sb.append(amount).append(":");

        amount = time / SEC;
        if (amount < 10)
            sb.append("0");

        sb.append(amount);
    }

    public static void timeElapsed(TimeElapsedConfig config) {

        long val = config.timeElapsed;
        final long min = config.minimum;
        final String[][] words = config.words;
        final StringBuilder sb = config.sb;
        final int LEN = sb.length();

        String ending;
        long amount = val / YEAR;
        if (amount > 0 && val >= min) {
            ending = ending(amount, words[INDEX_YEAR]);
            sb.append(amount).append(" ").append(ending).append(", ");
        }

        val %= YEAR;

        amount = val / MONTH;
        if (amount > 0 && val >= min) {
            ending = ending(amount, words[INDEX_MONTH]);
            sb.append(amount).append(" ").append(ending).append(", ");
        }

        val %= MONTH;

        amount = val / WEEK;
        if (amount > 0 && val >= min) {
            ending = ending(amount, words[INDEX_WEEK]);
            sb.append(amount).append(" ").append(ending).append(", ");
        }

        val %= WEEK;

        amount = val / DAY;
        if (amount > 0 && val >= min) {
            ending = ending(amount, words[INDEX_DAY]);
            sb.append(amount).append(" ").append(ending).append(", ");
        }

        val %= DAY;

        amount = val / HOUR;
        if (amount > 0 && val >= min) {
            ending = ending(amount, words[INDEX_HOUR]);
            sb.append(amount).append(" ").append(ending).append(", ");
        }

        val %= HOUR;

        amount = val / MIN;
        if (amount > 0 && val >= min) {
            ending = ending(amount, words[INDEX_MINUTE]);
            sb.append(amount).append(" ").append(ending).append(", ");
        }

        val %= MIN;

        amount = val / SEC;
        if (amount > 0 && val >= min) {
            ending = ending(amount, words[INDEX_SECOND]);
            sb.append(amount).append(" ").append(ending).append(", ");
        }

        if (sb.length() > LEN)
            sb.delete(sb.length() - 2, sb.length()); // unnecessary comma
    }

    public static long solveMin(long value) {
        if (value >= YEAR) return MONTH;
        if (value >= MONTH) return WEEK;
        if (value >= WEEK) return DAY;
        if (value >= DAY) return HOUR;
        if (value >= HOUR) return MIN;
        return SEC;
    }

    // подборка окончания для значения, в массивае должны быть названия периодов
    // в формате {"день" "дня" "дней"}, {"месяц" "месяца" "месяцев"} и т.п.
    private static String ending(long value, String[] words) {
        if (value >= 10 && value <= 14)
            return words[2];

        value %= 10;
        if (value == 1)
            return words[0];

        if (value >= 2 && value <= 4)
            return words[1];

        return words[2];
    }
}
