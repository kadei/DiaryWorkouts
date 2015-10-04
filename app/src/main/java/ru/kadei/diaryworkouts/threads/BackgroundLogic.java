package ru.kadei.diaryworkouts.threads;

import android.os.Handler;
import android.os.Message;
import android.util.Pair;

import java.util.ArrayDeque;
import java.util.Queue;

import static java.lang.Long.MAX_VALUE;
import static java.lang.Thread.sleep;

/**
 * Created by kadei on 04.09.15.
 */
public class BackgroundLogic {

    private final Queue<Pair<Task, Object[]>> tasks = new ArrayDeque<>();
    private boolean stop = false;
    private boolean pause = true;

    private final boolean thisThread;

    private final Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            Task task = (Task) msg.obj;
            msg.obj = null;

            if (task.isSuccessful())
                task.noticeCompletion();
            else
                task.noticeFail();

            return false;
        }
    });

    private final Thread thread = new Thread(new Runnable() {
        @Override
        public void run() {
            runBackgroundThread();
        }
    });

    public BackgroundLogic() {
        this(false);
    }

    public BackgroundLogic(boolean thisThread) {
        this.thisThread = thisThread;
        if (!thisThread)
            thread.start();
    }

    private void runBackgroundThread() {
        Task active = null;
        while (true) {
            try {
                Pair<Task, Object[]> pair = scheduleNext();
                active = pair != null ? pair.first : null;

                if (active != null) {
                    if (isStop()) return;

                    active.execute(pair.second);
                    if (isStop()) return;

                    sendTask(active);
                } else {
                    pause(true);
                    sleep(MAX_VALUE);
                }
            } catch (InterruptedException e) {
                if (isStop()) return;
                else pause(false);
            } catch (TaskException e) {
                if (isStop()) return;
                else {
                    active.exception = e.getOriginalException();
                    sendTask(active);
                }
            }
        }
    }

    private void sendTask(Task task) {
        Message msg = handler.obtainMessage();
        msg.obj = task;
        handler.sendMessage(msg);
    }

    private synchronized Pair<Task, Object[]> scheduleNext() {
        return tasks.poll();
    }

    private synchronized boolean isStop() {
        return stop;
    }

    private synchronized void pause(boolean value) {
        pause = value;
    }

    public synchronized void execute(Task task, Object... parameters) {
        if (thisThread)
            executeInThisThread(task, parameters);
        else {
            tasks.offer(new Pair<>(task, parameters));
            if (pause)
                thread.interrupt();
        }
    }

    private void executeInThisThread(Task task, Object... parameters) {
        try {
            task.execute(parameters);
            task.noticeCompletion();
        } catch (TaskException e) {
            task.exception = e.getOriginalException();
            task.noticeFail();
        }
    }

    public synchronized void stop() {
        stop = true;
        if (pause)
            thread.interrupt();
    }

    public boolean isThisThread() {
        return thisThread;
    }
}
