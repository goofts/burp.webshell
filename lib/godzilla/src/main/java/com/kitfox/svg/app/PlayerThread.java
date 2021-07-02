package com.kitfox.svg.app;

import java.util.HashSet;
import java.util.Iterator;

public class PlayerThread implements Runnable {
    public static final int PS_PLAY_BACK = 2;
    public static final int PS_PLAY_FWD = 1;
    public static final int PS_STOP = 0;
    double curTime = 0.0d;
    HashSet<PlayerThreadListener> listeners = new HashSet<>();
    int playState = 0;
    Thread thread = new Thread(this);
    double timeStep = 0.2d;

    public PlayerThread() {
        this.thread.start();
    }

    public void run() {
        while (this.thread != null) {
            synchronized (this) {
                switch (this.playState) {
                    case 1:
                        this.curTime += this.timeStep;
                        break;
                    case 2:
                        this.curTime -= this.timeStep;
                        if (this.curTime < 0.0d) {
                            this.curTime = 0.0d;
                            break;
                        }
                        break;
                }
                fireTimeUpdateEvent();
            }
            try {
                Thread.sleep((long) (this.timeStep * 1000.0d));
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void exit() {
        this.thread = null;
    }

    public synchronized void addListener(PlayerThreadListener listener) {
        this.listeners.add(listener);
    }

    public synchronized double getCurTime() {
        return this.curTime;
    }

    public synchronized void setCurTime(double time) {
        this.curTime = time;
    }

    public synchronized double getTimeStep() {
        return this.timeStep;
    }

    public synchronized void setTimeStep(double time) {
        this.timeStep = time;
        if (this.timeStep < 0.01d) {
            this.timeStep = 0.01d;
        }
    }

    public synchronized int getPlayState() {
        return this.playState;
    }

    public synchronized void setPlayState(int playState2) {
        this.playState = playState2;
    }

    private void fireTimeUpdateEvent() {
        Iterator<PlayerThreadListener> it = this.listeners.iterator();
        while (it.hasNext()) {
            it.next().updateTime(this.curTime, this.timeStep, this.playState);
        }
    }
}
