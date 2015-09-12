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

    private final Queue<Task> tasks = new ArrayDeque<>();
    private boolean stop = false;
    private boolean pause = true;

    private final boolean thisThread;

    private final Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            Task task = (Task) msg.obj;
            task.forgetParameters();

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
        if(!thisThread)
            thread.start();
    }

    private void runBackgroundThread() {
        Task active = null;
        while (true) {
            try {
                active = scheduleNext();
                if (active != null) {
                    if (isStop()) return;

                    active.execute();
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
        if(thisThread)
            executeInThisThread(task);
        else {
            tasks.offer(task);
            if (pause)
                thread.interrupt();
        }
    }

    private void executeInThisThread(Task task) {
        try {
            task.execute();
            task.noticeCompletion();
        } catch (TaskException e) {
            task.exception = e.getOriginalException();
            task.noticeFail();
        }
    }

    private synchronized void stop() {
        stop = true;
    }
}
