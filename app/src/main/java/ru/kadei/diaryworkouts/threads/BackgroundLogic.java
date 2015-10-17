package ru.kadei.diaryworkouts.threads;

import android.os.Handler;
import android.os.Message;

import java.util.ArrayDeque;
import java.util.Queue;

import static java.lang.Long.MAX_VALUE;
import static java.lang.Thread.sleep;

/**
 * Created by kadei on 04.09.15.
 */
public class BackgroundLogic {

    private final Queue<Task> tasks;

    private boolean stop;
    private boolean pause;

    private final Thread thread;
    private final boolean thisThread;

    private static final int EXECUTE_SUCCESSFULLY = 0;
    private static final int EXECUTE_FAIL = -1;

    public BackgroundLogic() {
        this(false);
    }

    public BackgroundLogic(boolean thisThread) {
        tasks = new ArrayDeque<>();
        pause = false;
        stop = false;

        this.thisThread = thisThread;
        if (!thisThread) {
            thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    runBackgroundThread();
                }
            });
            thread.start();
        } else
            thread = null;
    }

    private void runBackgroundThread() {
        while (!isStop()) {
            Task task = scheduleNext();

            if (task == null)
                _sleep();
            else
                _execute(task);
        }
    }

    private void _sleep() {
        try {
            pause(true);
            sleep(MAX_VALUE);
        } catch (InterruptedException e) {
            pause(false);
        }
    }

    private void _execute(Task t) {
        int statusExecution = 0;
        try {
            t.execute();
            statusExecution = EXECUTE_SUCCESSFULLY;
        } catch (Exception e) {
            t.setThrowable(e);
            statusExecution = EXECUTE_FAIL;
        } finally {
            if (!isStop())
                sendTask(statusExecution, t);
        }
    }

    private void sendTask(int status, Task t) {
        final Message m = handler.obtainMessage(status, t);
        handler.sendMessage(m);
    }

    private final Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            final int what = msg.what;
            final Task t = (Task) msg.obj;

            if (what == EXECUTE_SUCCESSFULLY)
                t.successfully();
            else if (what == EXECUTE_FAIL)
                t.fail();
            else
                throw new RuntimeException("BackgroundLogic unexpected code = " + what);

            return false;
        }
    });

    private synchronized Task scheduleNext() {
        return tasks.poll();
    }

    private synchronized boolean isStop() {
        return stop;
    }

    private synchronized void pause(boolean value) {
        pause = value;
    }

    public synchronized void execute(Task task) {
        if (thisThread) {
            try {
                task.execute();
                task.successfully();
            } catch (Exception e) {
                task.setThrowable(e);
                task.fail();
            }
        } else {
            tasks.offer(task);
            if (pause)
                thread.interrupt();
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
