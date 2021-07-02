package com.formdev.flatlaf.ui;

import com.formdev.flatlaf.util.UIScale;
import java.awt.Insets;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.function.Function;
import javax.swing.JComponent;

public class MigLayoutVisualPadding {
    public static String VISUAL_PADDING_PROPERTY = "visualPadding";
    private static final FlatMigInsets ZERO = new FlatMigInsets(0, 0, 0, 0);
    private static final boolean migLayoutAvailable;

    /* access modifiers changed from: private */
    public interface FlatMigListener extends PropertyChangeListener {
    }

    static {
        boolean available = false;
        try {
            Class.forName("net.miginfocom.swing.MigLayout");
            available = true;
        } catch (ClassNotFoundException e) {
        }
        migLayoutAvailable = available;
    }

    public static void install(JComponent c, Insets insets) {
        if (migLayoutAvailable) {
            setVisualPadding(c, insets);
        }
    }

    /*  JADX ERROR: JadxRuntimeException in pass: BlockSplitter
        jadx.core.utils.exceptions.JadxRuntimeException: Missing block: 5
        	at jadx.core.dex.visitors.blocksmaker.BlockSplitter.getBlock(BlockSplitter.java:307)
        	at jadx.core.dex.visitors.blocksmaker.BlockSplitter.setupConnections(BlockSplitter.java:236)
        	at jadx.core.dex.visitors.blocksmaker.BlockSplitter.splitBasicBlocks(BlockSplitter.java:129)
        	at jadx.core.dex.visitors.blocksmaker.BlockSplitter.visit(BlockSplitter.java:52)
        */
    public static void install(javax.swing.JComponent r4) {
        /*
            boolean r0 = com.formdev.flatlaf.ui.MigLayoutVisualPadding.migLayoutAvailable
            if (r0 != 0) goto L_0x0005
        L_0x0004:
            return
        L_?:
            r0 = move-result
            r1 = 1
            java.lang.String[] r1 = new java.lang.String[r1]
            r2 = 0
            java.lang.String r3 = "border"
            r1[r2] = r3
            install(r4, r0, r1)
            goto L_0x0004
        */
        throw new UnsupportedOperationException("Method not decompiled: com.formdev.flatlaf.ui.MigLayoutVisualPadding.install(javax.swing.JComponent):void");
    }

    private static /* synthetic */ Insets lambda$install$0(JComponent c2) {
        FlatBorder border = FlatUIUtils.getOutsideFlatBorder(c2);
        if (border == null) {
            return null;
        }
        int focusWidth = border.getFocusWidth(c2);
        return new Insets(focusWidth, focusWidth, focusWidth, focusWidth);
    }

    /* JADX WARN: Type inference failed for: r0v3, types: [java.beans.PropertyChangeListener, void] */
    /* JADX WARNING: Unknown variable types count: 1 */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static void install(javax.swing.JComponent r1, java.util.function.Function<javax.swing.JComponent, java.awt.Insets> r2, java.lang.String... r3) {
        /*
            boolean r0 = com.formdev.flatlaf.ui.MigLayoutVisualPadding.migLayoutAvailable
            if (r0 != 0) goto L_0x0005
        L_0x0004:
            return
        L_0x0005:
            java.lang.Object r0 = r2.apply(r1)
            java.awt.Insets r0 = (java.awt.Insets) r0
            void r0 = setVisualPadding(r1, r0)
            r1.addPropertyChangeListener(r0)
            goto L_0x0004
        */
        throw new UnsupportedOperationException("Method not decompiled: com.formdev.flatlaf.ui.MigLayoutVisualPadding.install(javax.swing.JComponent, java.util.function.Function, java.lang.String[]):void");
    }

    private static /* synthetic */ void lambda$install$1(String[] propertyNames, JComponent c, Function getPaddingFunction, PropertyChangeEvent e) {
        String propertyName = e.getPropertyName();
        for (String name : propertyNames) {
            if (name == propertyName) {
                setVisualPadding(c, (Insets) getPaddingFunction.apply(c));
                return;
            }
        }
    }

    private static void setVisualPadding(JComponent c, Insets visualPadding) {
        Object oldPadding = c.getClientProperty(VISUAL_PADDING_PROPERTY);
        if (oldPadding == null || (oldPadding instanceof FlatMigInsets)) {
            c.putClientProperty(VISUAL_PADDING_PROPERTY, visualPadding != null ? new FlatMigInsets(UIScale.scale2(visualPadding.top), UIScale.scale2(visualPadding.left), UIScale.scale2(visualPadding.bottom), UIScale.scale2(visualPadding.right)) : ZERO);
        }
    }

    public static void uninstall(JComponent c) {
        if (migLayoutAvailable) {
            PropertyChangeListener[] propertyChangeListeners = c.getPropertyChangeListeners();
            int length = propertyChangeListeners.length;
            int i = 0;
            while (true) {
                if (i >= length) {
                    break;
                }
                PropertyChangeListener l = propertyChangeListeners[i];
                if (l instanceof FlatMigListener) {
                    c.removePropertyChangeListener(l);
                    break;
                }
                i++;
            }
            if (c.getClientProperty(VISUAL_PADDING_PROPERTY) instanceof FlatMigInsets) {
                c.putClientProperty(VISUAL_PADDING_PROPERTY, (Object) null);
            }
        }
    }

    /* access modifiers changed from: private */
    public static class FlatMigInsets extends Insets {
        FlatMigInsets(int top, int left, int bottom, int right) {
            super(top, left, bottom, right);
        }
    }
}
