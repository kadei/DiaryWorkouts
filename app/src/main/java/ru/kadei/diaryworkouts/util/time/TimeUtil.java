package ru.kadei.diaryworkouts.util.time;

/**
 * Created by kadei on 31.01.2015.
 */
public class TimeUtil {

    public static final long SEC =  1000L;
    public static final long MIN =  SEC * 60L;
    public static final long HOUR = MIN * 60L;
    public static final long DAY =  HOUR * 24L;
    public static final long WEEK = DAY * 7L;
    public static final long MONTH = DAY * 30L;
    public static final long YEAR = DAY * 365L;

    private static final String[] year =    {" год",    " года",    " лет"};
    private static final String[] month =   {" месяц",  " месяца",  " месяцев"};
    private static final String[] week =    {" неделя", " недели",  " недель"};
    private static final String[] day =     {" день",   " дня",     " дней"};
    private static final String[] hour =    {" час",    " часа",    " часов"};
    private static final String[] minute =  {" минута", " минуты",  " минут"};
    private static final String[] second =  {" секунда"," секунды", " секунд"};

    private static final String[][] words = new String[][] {
            year, month, week, day, hour, minute, second
    };

    public static void timeForTimer(StringBuilder sb, long time) {

        long amount = time / MIN;
        if(amount < 10)
            sb.append("0");

        time %= MIN;
        sb.append(amount).append(":");

        amount = time / SEC;
        if(amount < 10)
            sb.append("0");

        sb.append(amount);
    }

    /**
     * convert <b>val</b> (milliseconds) into string format "amount_year, amount_month, ..."
     * @param sb
     * @param val
     * @param min
     */
    public static void timeElapsed(StringBuilder sb, long val, long min) {

        final int LEN = sb.length();
        long amount = val / YEAR;
        if(amount > 0 && val >= min)
            sb.append(amount).append(ending(amount, words[0])).append(", ");

        val %= YEAR;

        amount = val / MONTH;
        if(amount > 0 && val >= min)
            sb.append(amount).append(ending(amount, words[1])).append(", ");

        val %= MONTH;

        amount = val / WEEK;
        if(amount > 0 && val >= min)
            sb.append(amount).append(ending(amount, words[2])).append(", ");

        val %= WEEK;

        amount = val / DAY;
        if(amount > 0 && val >= min)
            sb.append(amount).append(ending(amount, words[3])).append(", ");

        val %= DAY;

        amount = val / HOUR;
        if(amount > 0 && val >= min)
            sb.append(amount).append(ending(amount, words[4])).append(", ");

        val %= HOUR;

        amount = val / MIN;
        if(amount > 0 && val >= min)
            sb.append(amount).append(ending(amount, words[5])).append(", ");

        val %= MIN;

        amount = val / SEC;
        if(amount > 0 && val >= min)
            sb.append(amount).append(ending(amount, words[6])).append(", ");

        if(sb.length() > LEN)
            sb.delete(sb.length() - 2, sb.length()); // unnecessary comma
    }

    public static long solveMin(long value) {
        if(value >= YEAR) return MONTH;
        if(value >= MONTH) return WEEK;
        if(value >= WEEK) return DAY;
        if(value >= DAY) return HOUR;
        if(value >= HOUR) return MIN;
        return SEC;
    }

    // подборка окончания для значения, в массивае должны быть названия периодов
    // в формате {"день" "дня" "дней"}, {"месяц" "месяца" "месяцев"} и т.п.
    private static String ending(long value, String[] words) {
        if(value >= 10 && value <= 14)
            return words[2];

        value %= 10;
        if(value == 1)
            return words[0];

        if(value >= 2 && value <= 4)
            return words[1];

        return words[2];
    }
}
