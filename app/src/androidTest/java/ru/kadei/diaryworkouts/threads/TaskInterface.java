package ru.kadei.diaryworkouts.threads;

/**
 * Created by kadei on 05.09.15.
 */
public interface TaskInterface {
    String[] exe();
    void end(String[] strings);
    void oops(Throwable t);
}
