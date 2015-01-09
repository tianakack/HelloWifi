package com.superball.hellowifi;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by TIAN on 2015/1/9.
 */
public abstract class TimerHelper {

    private Timer mTimer = null;

    public TimerHelper() {

        mTimer = null;
    }

    public void start(long delay, long period) {
        stop();

        mTimer = new Timer();
        mTimer.schedule(new TimerTask() {
            @Override
            public void run() {

                TimerHelper.this.run();
            }
        }, delay, period);
    }

    public void stop() {
        if (mTimer != null) {

            mTimer.cancel();
            mTimer = null;
        }
    }

    public abstract void run();
}
