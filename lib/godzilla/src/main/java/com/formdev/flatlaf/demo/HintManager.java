package com.formdev.flatlaf.demo;

import com.formdev.flatlaf.ui.FlatPopupMenuBorder;
import com.formdev.flatlaf.util.UIScale;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.JRootPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import net.miginfocom.swing.MigLayout;

/* access modifiers changed from: package-private */
public class HintManager {
    private static final List<HintPanel> hintPanels = new ArrayList();

    HintManager() {
    }

    static void showHint(Hint hint) {
        if (!DemoPrefs.getState().getBoolean(hint.prefsKey, false)) {
            HintPanel hintPanel = new HintPanel(hint);
            hintPanel.showHint();
            hintPanels.add(hintPanel);
        } else if (hint.nextHint != null) {
            showHint(hint.nextHint);
        }
    }

    static void hideAllHints() {
        for (HintPanel hintPanel : (HintPanel[]) hintPanels.toArray(new HintPanel[hintPanels.size()])) {
            hintPanel.hideHint();
        }
    }

    /* access modifiers changed from: package-private */
    public static class Hint {
        private final String message;
        private final Hint nextHint;
        private final Component owner;
        private final int position;
        private final String prefsKey;

        Hint(String message2, Component owner2, int position2, String prefsKey2, Hint nextHint2) {
            this.message = message2;
            this.owner = owner2;
            this.position = position2;
            this.prefsKey = prefsKey2;
            this.nextHint = nextHint2;
        }
    }

    /* access modifiers changed from: private */
    public static class HintPanel extends JPanel {
        private JButton gotItButton;
        private final Hint hint;
        private JLabel hintLabel;
        private JPanel popup;

        private HintPanel(Hint hint2) {
            this.hint = hint2;
            initComponents();
            this.hintLabel.setText("<html>" + hint2.message + "</html>");
            addMouseListener(new MouseAdapter() {
                /* class com.formdev.flatlaf.demo.HintManager.HintPanel.AnonymousClass1 */
            });
        }

        public void updateUI() {
            HintManager.super.updateUI();
            setBackground(UIManager.getColor("HintPanel.backgroundColor"));
            setBorder(new FlatPopupMenuBorder());
        }

        /* access modifiers changed from: package-private */
        public void showHint() {
            JRootPane rootPane = SwingUtilities.getRootPane(this.hint.owner);
            if (rootPane != null) {
                JLayeredPane layeredPane = rootPane.getLayeredPane();
                this.popup = new JPanel(new BorderLayout()) {
                    /* class com.formdev.flatlaf.demo.HintManager.HintPanel.AnonymousClass2 */

                    /* JADX WARN: Type inference failed for: r0v1, types: [void, java.lang.Runnable] */
                    /* JADX WARNING: Unknown variable types count: 1 */
                    /* Code decompiled incorrectly, please refer to instructions dump. */
                    public void updateUI() {
                        /*
                            r5 = this;
                            com.formdev.flatlaf.demo.HintManager.HintPanel.super.updateUI()
                            com.formdev.flatlaf.ui.FlatDropShadowBorder r0 = new com.formdev.flatlaf.ui.FlatDropShadowBorder
                            java.lang.String r1 = "Popup.dropShadowColor"
                            java.awt.Color r1 = javax.swing.UIManager.getColor(r1)
                            java.lang.String r2 = "Popup.dropShadowInsets"
                            java.awt.Insets r2 = javax.swing.UIManager.getInsets(r2)
                            java.lang.String r3 = "Popup.dropShadowOpacity"
                            r4 = 1056964608(0x3f000000, float:0.5)
                            float r3 = com.formdev.flatlaf.ui.FlatUIUtils.getUIFloat(r3, r4)
                            r0.<init>(r1, r2, r3)
                            void r0 = r5.setBorder(r0)
                            java.awt.EventQueue.invokeLater(r0)
                            return
                        */
                        throw new UnsupportedOperationException("Method not decompiled: com.formdev.flatlaf.demo.HintManager.HintPanel.AnonymousClass2.updateUI():void");
                    }

                    private /* synthetic */ void lambda$updateUI$0() {
                        validate();
                        setSize(getPreferredSize());
                    }
                };
                this.popup.setOpaque(false);
                this.popup.add(this);
                Point pt = SwingUtilities.convertPoint(this.hint.owner, 0, 0, layeredPane);
                int x = pt.x;
                int y = pt.y;
                Dimension size = this.popup.getPreferredSize();
                int gap = UIScale.scale(6);
                switch (this.hint.position) {
                    case 1:
                        y -= size.height + gap;
                        break;
                    case 2:
                        x -= size.width + gap;
                        break;
                    case 3:
                        y += this.hint.owner.getHeight() + gap;
                        break;
                    case 4:
                        x += this.hint.owner.getWidth() + gap;
                        break;
                }
                this.popup.setBounds(x, y, size.width, size.height);
                layeredPane.add(this.popup, JLayeredPane.POPUP_LAYER);
            }
        }

        /* access modifiers changed from: package-private */
        public void hideHint() {
            Container parent;
            if (!(this.popup == null || (parent = this.popup.getParent()) == null)) {
                parent.remove(this.popup);
                parent.repaint(this.popup.getX(), this.popup.getY(), this.popup.getWidth(), this.popup.getHeight());
            }
            HintManager.hintPanels.remove(this);
        }

        private void gotIt() {
            hideHint();
            DemoPrefs.getState().putBoolean(this.hint.prefsKey, true);
            if (this.hint.nextHint != null) {
                HintManager.showHint(this.hint.nextHint);
            }
        }

        private void initComponents() {
            this.hintLabel = new JLabel();
            this.gotItButton = new JButton();
            setLayout(new MigLayout("insets dialog,hidemode 3", "[::200,fill]", "[]para[]"));
            this.hintLabel.setText("hint");
            add(this.hintLabel, "cell 0 0");
            this.gotItButton.setText("Got it!");
            this.gotItButton.setFocusable(false);
            this.gotItButton.addActionListener(
        }
