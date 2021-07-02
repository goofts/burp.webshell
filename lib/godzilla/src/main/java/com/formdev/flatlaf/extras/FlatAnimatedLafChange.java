package com.formdev.flatlaf.extras;

import com.formdev.flatlaf.FlatSystemProperties;
import com.formdev.flatlaf.util.Animator;
import java.util.Map;
import java.util.WeakHashMap;
import javassist.bytecode.Opcode;
import javax.swing.JComponent;
import javax.swing.JLayeredPane;

public class FlatAnimatedLafChange {
    private static float alpha;
    private static Animator animator;
    public static int duration = Opcode.IF_ICMPNE;
    private static boolean inShowSnapshot;
    private static final Map<JLayeredPane, JComponent> newUIsnapshots = new WeakHashMap();
    private static final Map<JLayeredPane, JComponent> oldUIsnapshots = new WeakHashMap();
    public static int resolution = 40;

    public static void showSnapshot() {
        if (FlatSystemProperties.getBoolean("flatlaf.animatedLafChange", true)) {
            if (animator != null) {
                animator.stop();
            }
            alpha = 1.0f;
            showSnapshot(true, oldUIsnapshots);
        }
    }

    /* Code decompiled incorrectly, please refer to instructions dump. */
    private static void showSnapshot(boolean r11, java.util.Map<javax.swing.JLayeredPane, javax.swing.JComponent> r12) {
        /*
            r8 = 0
            r6 = 1
            com.formdev.flatlaf.extras.FlatAnimatedLafChange.inShowSnapshot = r6
            java.awt.Window[] r4 = java.awt.Window.getWindows()
            int r9 = r4.length
            r7 = r8
        L_0x000a:
            if (r7 >= r9) goto L_0x005f
            r3 = r4[r7]
            boolean r5 = r3 instanceof javax.swing.RootPaneContainer
            if (r5 == 0) goto L_0x0018
            boolean r5 = r3.isShowing()
            if (r5 != 0) goto L_0x001c
        L_0x0018:
            int r5 = r7 + 1
            r7 = r5
            goto L_0x000a
        L_0x001c:
            int r5 = r3.getWidth()
            int r10 = r3.getHeight()
            java.awt.image.VolatileImage r1 = r3.createVolatileImage(r5, r10)
            if (r1 == 0) goto L_0x0018
            javax.swing.RootPaneContainer r3 = (javax.swing.RootPaneContainer) r3
            javax.swing.JLayeredPane r0 = r3.getLayeredPane()
            java.awt.Graphics r5 = r1.getGraphics()
            r0.paint(r5)
            com.formdev.flatlaf.extras.FlatAnimatedLafChange$1 r2 = new com.formdev.flatlaf.extras.FlatAnimatedLafChange$1
            r2.<init>(r1, r11)
            if (r11 != 0) goto L_0x0041
            r2.setOpaque(r6)
        L_0x0041:
            java.awt.Dimension r5 = r0.getSize()
            r2.setSize(r5)
            java.lang.Integer r5 = javax.swing.JLayeredPane.DRAG_LAYER
            int r10 = r5.intValue()
            if (r11 == 0) goto L_0x005d
            r5 = 2
        L_0x0051:
            int r5 = r5 + r10
            java.lang.Integer r5 = java.lang.Integer.valueOf(r5)
            r0.add(r2, r5)
            r12.put(r0, r2)
            goto L_0x0018
        L_0x005d:
            r5 = r6
            goto L_0x0051
        L_0x005f:
            com.formdev.flatlaf.extras.FlatAnimatedLafChange.inShowSnapshot = r8
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.formdev.flatlaf.extras.FlatAnimatedLafChange.showSnapshot(boolean, java.util.Map):void");
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
        	at jadx.core.codegen.InsnGen.makeInsnBody(InsnGen.java:439)
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
    public static void hideSnapshotWithAnimation() {
        /*
            java.lang.String r0 = "flatlaf.animatedLafChange"
            r1 = 1
            boolean r0 = com.formdev.flatlaf.FlatSystemProperties.getBoolean(r0, r1)
            if (r0 != 0) goto L_0x000a
        L_0x0009:
            return
        L_0x000a:
            java.util.Map<javax.swing.JLayeredPane, javax.swing.JComponent> r0 = com.formdev.flatlaf.extras.FlatAnimatedLafChange.oldUIsnapshots
            boolean r0 = r0.isEmpty()
            if (r0 != 0) goto L_0x0009
            r0 = 0
            java.util.Map<javax.swing.JLayeredPane, javax.swing.JComponent> r1 = com.formdev.flatlaf.extras.FlatAnimatedLafChange.newUIsnapshots
            showSnapshot(r0, r1)
            com.formdev.flatlaf.util.Animator r0 = new com.formdev.flatlaf.util.Animator
            int r1 = com.formdev.flatlaf.extras.FlatAnimatedLafChange.duration
            r2 = move-result
            r3 = move-result
            r0.<init>(r1, r2, r3)
            com.formdev.flatlaf.extras.FlatAnimatedLafChange.animator = r0
            com.formdev.flatlaf.util.Animator r0 = com.formdev.flatlaf.extras.FlatAnimatedLafChange.animator
            int r1 = com.formdev.flatlaf.extras.FlatAnimatedLafChange.resolution
            r0.setResolution(r1)
            com.formdev.flatlaf.util.Animator r0 = com.formdev.flatlaf.extras.FlatAnimatedLafChange.animator
            r0.start()
            goto L_0x0009
        */
        throw new UnsupportedOperationException("Method not decompiled: com.formdev.flatlaf.extras.FlatAnimatedLafChange.hideSnapshotWithAnimation():void");
    }

    private static /* synthetic */ void lambda$hideSnapshotWithAnimation$0(float fraction) {
        if (((double) fraction) >= 0.1d && ((double) fraction) <= 0.9d) {
            alpha = 1.0f - fraction;
            for (Map.Entry<JLayeredPane, JComponent> e : oldUIsnapshots.entrySet()) {
                if (e.getKey().isShowing()) {
                    e.getValue().repaint();
                }
            }
        }
    }

    private static /* synthetic */ void lambda$hideSnapshotWithAnimation$1() {
        hideSnapshot();
        animator = null;
    }

    private static void hideSnapshot() {
        hideSnapshot(oldUIsnapshots);
        hideSnapshot(newUIsnapshots);
    }

    private static void hideSnapshot(Map<JLayeredPane, JComponent> map) {
        for (Map.Entry<JLayeredPane, JComponent> e : map.entrySet()) {
            e.getKey().remove(e.getValue());
            e.getKey().repaint();
        }
        map.clear();
    }

    public static void stop() {
        if (animator != null) {
            animator.stop();
        } else {
            hideSnapshot();
        }
    }
}
