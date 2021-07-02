package com.formdev.flatlaf.util;

import com.formdev.flatlaf.FlatSystemProperties;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.Iterator;
import javax.swing.Timer;

public class Animator {
    private int duration;
    private final Runnable endRunnable;
    private boolean hasBegun;
    private Interpolator interpolator;
    private int resolution;
    private boolean running;
    private long startTime;
    private final ArrayList<TimingTarget> targets;
    private boolean timeToStop;
    private Timer timer;

    @FunctionalInterface
    public interface Interpolator {
        float interpolate(float f);
    }

    public static boolean useAnimation() {
        return FlatSystemProperties.getBoolean(FlatSystemProperties.ANIMATION, true);
    }

    public Animator(int duration2) {
        this(duration2, null, null);
    }

    public Animator(int duration2, TimingTarget target) {
        this(duration2, target, null);
    }

    public Animator(int duration2, TimingTarget target, Runnable endRunnable2) {
        this.resolution = 10;
        this.targets = new ArrayList<>();
        setDuration(duration2);
        addTarget(target);
        this.endRunnable = endRunnable2;
    }

    public int getDuration() {
        return this.duration;
    }

    public void setDuration(int duration2) {
        throwExceptionIfRunning();
        if (duration2 <= 0) {
            throw new IllegalArgumentException();
        }
        this.duration = duration2;
    }

    public int getResolution() {
        return this.resolution;
    }

    public void setResolution(int resolution2) {
        throwExceptionIfRunning();
        if (resolution2 <= 0) {
            throw new IllegalArgumentException();
        }
        this.resolution = resolution2;
    }

    public Interpolator getInterpolator() {
        return this.interpolator;
    }

    public void setInterpolator(Interpolator interpolator2) {
        throwExceptionIfRunning();
        this.interpolator = interpolator2;
    }

    public void addTarget(TimingTarget target) {
        if (target != null) {
            synchronized (this.targets) {
                if (!this.targets.contains(target)) {
                    this.targets.add(target);
                }
            }
        }
    }

    public void removeTarget(TimingTarget target) {
        synchronized (this.targets) {
            this.targets.remove(target);
        }
    }

    /*  JADX ERROR: MOVE_RESULT instruction can be used only in fallback mode
        jadx.core.utils.exceptions.CodegenException: MOVE_RESULT instruction can be used only in fallback mode
        	at jadx.core.codegen.InsnGen.fallbackOnlyInsn(InsnGen.java:604)
        	at jadx.core.codegen.InsnGen.makeInsnBody(InsnGen.java:542)
        	at jadx.core.codegen.InsnGen.makeInsn(InsnGen.java:230)
        	at jadx.core.codegen.InsnGen.addWrappedArg(InsnGen.java:119)
        	at jadx.core.codegen.InsnGen.addArg(InsnGen.java:103)
        	at jadx.core.codegen.InsnGen.generateMethodArguments(InsnGen.java:806)
        	at jadx.core.codegen.InsnGen.makeConstructor(InsnGen.java:663)
        	at jadx.core.codegen.InsnGen.makeInsnBody(InsnGen.java:363)
        	at jadx.core.codegen.InsnGen.makeInsn(InsnGen.java:230)
        	at jadx.core.codegen.InsnGen.addWrappedArg(InsnGen.java:119)
        	at jadx.core.codegen.InsnGen.addArg(InsnGen.java:103)
        	at jadx.core.codegen.InsnGen.makeInsnBody(InsnGen.java:428)
        	at jadx.core.codegen.InsnGen.makeInsn(InsnGen.java:249)
        	at jadx.core.codegen.InsnGen.makeInsn(InsnGen.java:217)
        	at jadx.core.codegen.RegionGen.makeSimpleBlock(RegionGen.java:110)
        	at jadx.core.codegen.RegionGen.makeRegion(RegionGen.java:56)
        	at jadx.core.codegen.RegionGen.makeSimpleRegion(RegionGen.java:93)
        	at jadx.core.codegen.RegionGen.makeRegion(RegionGen.java:59)
        	at jadx.core.codegen.RegionGen.makeRegionIndent(RegionGen.java:99)
        	at jadx.core.codegen.RegionGen.makeIf(RegionGen.java:143)
        	at jadx.core.codegen.RegionGen.makeRegion(RegionGen.java:63)
        	at jadx.core.codegen.RegionGen.makeSimpleRegion(RegionGen.java:93)
        	at jadx.core.codegen.RegionGen.makeRegion(RegionGen.java:59)
        	at jadx.core.codegen.MethodGen.addRegionInsns(MethodGen.java:244)
        	at jadx.core.codegen.MethodGen.addInstructions(MethodGen.java:237)
        	at jadx.core.codegen.ClassGen.addMethodCode(ClassGen.java:342)
        	at jadx.core.codegen.ClassGen.addMethod(ClassGen.java:295)
        	at jadx.core.codegen.ClassGen.lambda$addInnerClsAndMethods$2(ClassGen.java:264)
        	at java.util.stream.ForEachOps$ForEachOp$OfRef.accept(ForEachOps.java:184)
        	at java.util.ArrayList.forEach(ArrayList.java:1257)
        	at java.util.stream.SortedOps$RefSortingSink.end(SortedOps.java:390)
        	at java.util.stream.Sink$ChainedReference.end(Sink.java:258)
        */
    public void start() {
        /*
            r5 = this;
            r4 = 0
            r5.throwExceptionIfRunning()
            r0 = 1
            r5.running = r0
            r5.hasBegun = r4
            r5.timeToStop = r4
            long r0 = java.lang.System.nanoTime()
            r2 = 1000000(0xf4240, double:4.940656E-318)
            long r0 = r0 / r2
            r5.startTime = r0
            javax.swing.Timer r0 = r5.timer
            if (r0 != 0) goto L_0x0031
            javax.swing.Timer r0 = new javax.swing.Timer
            int r1 = r5.resolution
            r2 = move-result
            r0.<init>(r1, r2)
            r5.timer = r0
        L_0x0026:
            javax.swing.Timer r0 = r5.timer
            r0.setInitialDelay(r4)
            javax.swing.Timer r0 = r5.timer
            r0.start()
            return
        L_0x0031:
            javax.swing.Timer r0 = r5.timer
            int r1 = r5.resolution
            r0.setDelay(r1)
            goto L_0x0026
        */
        throw new UnsupportedOperationException("Method not decompiled: com.formdev.flatlaf.util.Animator.start():void");
    }

    private /* synthetic */ void lambda$start$0(ActionEvent e) {
        if (!this.hasBegun) {
            begin();
            this.hasBegun = true;
        }
        timingEvent(getTimingFraction());
    }

    public void stop() {
        stop(false);
    }

    public void cancel() {
        stop(true);
    }

    private void stop(boolean cancel) {
        if (this.running) {
            if (this.timer != null) {
                this.timer.stop();
            }
            if (!cancel) {
                end();
            }
            this.running = false;
            this.timeToStop = false;
        }
    }

    public void restart() {
        cancel();
        start();
    }

    public boolean isRunning() {
        return this.running;
    }

    private float getTimingFraction() {
        long elapsedTime = (System.nanoTime() / 1000000) - this.startTime;
        this.timeToStop = elapsedTime >= ((long) this.duration);
        float fraction = clampFraction(((float) elapsedTime) / ((float) this.duration));
        if (this.interpolator != null) {
            return clampFraction(this.interpolator.interpolate(fraction));
        }
        return fraction;
    }

    private float clampFraction(float fraction) {
        if (fraction < 0.0f) {
            return 0.0f;
        }
        if (fraction > 1.0f) {
            return 1.0f;
        }
        return fraction;
    }

    private void timingEvent(float fraction) {
        synchronized (this.targets) {
            Iterator<TimingTarget> it = this.targets.iterator();
            while (it.hasNext()) {
                it.next().timingEvent(fraction);
            }
        }
        if (this.timeToStop) {
            stop();
        }
    }

    private void begin() {
        synchronized (this.targets) {
            Iterator<TimingTarget> it = this.targets.iterator();
            while (it.hasNext()) {
                it.next().begin();
            }
        }
    }

    private void end() {
        synchronized (this.targets) {
            Iterator<TimingTarget> it = this.targets.iterator();
            while (it.hasNext()) {
                it.next().end();
            }
        }
        if (this.endRunnable != null) {
            this.endRunnable.run();
        }
    }

    private void throwExceptionIfRunning() {
        if (isRunning()) {
            throw new IllegalStateException();
        }
    }

    @FunctionalInterface
    public interface TimingTarget {
        void timingEvent(float f);

        default void begin() {
        }

        default void end() {
        }
    }
}
