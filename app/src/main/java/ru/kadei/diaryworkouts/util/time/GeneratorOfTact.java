package ru.kadei.diaryworkouts.util.time;

import android.support.annotation.NonNull;

import static java.lang.Thread.sleep;

/**
 * Created by kadei on 26.06.2015.
 */
public class GeneratorOfTact implements Runnable {

    private Thread mMyThread;
    private Callback mCallback;
    private int mState;
    private long mCurrentTick;

    public static final int STOPPED = 0x00f;
    public static final int RUNNING = 0x0f0;
    public static final int PAUSED = 0xf00;

    public GeneratorOfTact(@NonNull Callback callback) {
        this(callback, 1000L);
    }

    public GeneratorOfTact(Callback callback, long currentTick) {
        mCallback = callback;
        mCurrentTick = currentTick;
        mState = PAUSED;

        mMyThread = new Thread(this);
        mMyThread.start();
    }

    public synchronized void wakeIfNeed() {
        if(mState == PAUSED) {
            mState = RUNNING;
            mMyThread.interrupt();
        }
    }

    public synchronized void pause() {
        if(mState == RUNNING) {
            mState = PAUSED;
        }
    }

    public synchronized void stop() {
        if(mState == PAUSED) {
            mState = STOPPED;
            mMyThread.interrupt();
        }
        else mState = STOPPED;
    }

    public synchronized void setTick(long tick) {
        mCurrentTick = tick;
    }

    public synchronized long getTick() {
        return mCurrentTick;
    }

    public synchronized int currentState() {
        return mState;
    }

    @Override
    public void run() {
        while (currentState() != STOPPED) {
            try {
                if (currentState() == PAUSED) sleep(Long.MAX_VALUE);
                else sleep(getTick());

                if (currentState() == RUNNING) mCallback.tick();

            } catch (InterruptedException e) {
                // ignore
            }
        }
    }

    public interface Callback {
        void tick();
    }
}
