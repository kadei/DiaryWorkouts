package ru.kadei.diaryworkouts.database;

/**
 * Created by kadei on 13.09.15.
 */
public class SQLCreator {
    private final StringBuilder sb = new StringBuilder(512);

    protected final StringBuilder query(String s) {
        sb.delete(0, sb.length());
        sb.append(s);
        return sb;
    }

    protected final StringBuilder getClearStringBuilder() {
        sb.delete(0, sb.length());
        return sb;
    }
}
