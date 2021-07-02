package com.formdev.flatlaf.ui;

import java.awt.Color;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import javax.swing.JComponent;
import javax.swing.UIManager;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicListUI;

public class FlatListUI extends BasicListUI {
    protected Color selectionBackground;
    protected Color selectionForeground;
    protected Color selectionInactiveBackground;
    protected Color selectionInactiveForeground;

    public static ComponentUI createUI(JComponent c) {
        return new FlatListUI();
    }

    /* access modifiers changed from: protected */
    public void installDefaults() {
        FlatListUI.super.installDefaults();
        this.selectionBackground = UIManager.getColor("List.selectionBackground");
        this.selectionForeground = UIManager.getColor("List.selectionForeground");
        this.selectionInactiveBackground = UIManager.getColor("List.selectionInactiveBackground");
        this.selectionInactiveForeground = UIManager.getColor("List.selectionInactiveForeground");
        toggleSelectionColors();
    }

    /* access modifiers changed from: protected */
    public void uninstallDefaults() {
        FlatListUI.super.uninstallDefaults();
        this.selectionBackground = null;
        this.selectionForeground = null;
        this.selectionInactiveBackground = null;
        this.selectionInactiveForeground = null;
    }

    /* access modifiers changed from: protected */
    public FocusListener createFocusListener() {
        return new BasicListUI.FocusHandler() {
            /* class com.formdev.flatlaf.ui.FlatListUI.AnonymousClass1 */

            public void focusGained(FocusEvent e) {
                FlatListUI.super.focusGained(e);
                FlatListUI.this.toggleSelectionColors();
            }

            /* JADX WARN: Type inference failed for: r0v0, types: [void, java.lang.Runnable] */
            /* JADX WARNING: Unknown variable types count: 1 */
            /* Code decompiled incorrectly, please refer to instructions dump. */
            public void focusLost(java.awt.event.FocusEvent r2) {
                /*
                    r1 = this;
                    void r0 = com.formdev.flatlaf.ui.FlatListUI.super.focusLost(r2)
                    java.awt.EventQueue.invokeLater(r0)
                    return
                */
                throw new UnsupportedOperationException("Method not decompiled: com.formdev.flatlaf.ui.FlatListUI.AnonymousClass1.focusLost(java.awt.event.FocusEvent):void");
            }

            private /* synthetic */ void lambda$focusLost$0() {
                FlatListUI.this.toggleSelectionColors();
            }
        };
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private void toggleSelectionColors() {
        if (this.list != null) {
            if (FlatUIUtils.isPermanentFocusOwner(this.list)) {
                if (this.list.getSelectionBackground() == this.selectionInactiveBackground) {
                    this.list.setSelectionBackground(this.selectionBackground);
                }
                if (this.list.getSelectionForeground() == this.selectionInactiveForeground) {
                    this.list.setSelectionForeground(this.selectionForeground);
                    return;
                }
                return;
            }
            if (this.list.getSelectionBackground() == this.selectionBackground) {
                this.list.setSelectionBackground(this.selectionInactiveBackground);
            }
            if (this.list.getSelectionForeground() == this.selectionForeground) {
                this.list.setSelectionForeground(this.selectionInactiveForeground);
            }
        }
    }
}
