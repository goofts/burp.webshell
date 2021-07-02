package com.kitfox.svg.app.beans;

import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import javassist.bytecode.Opcode;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class ProportionalLayoutPanel extends JPanel {
    public static final long serialVersionUID = 1;
    float bottomMargin;
    private JPanel jPanel1;
    float leftMargin;
    float rightMargin;
    float topMargin;

    public ProportionalLayoutPanel() {
        initComponents();
    }

    public void addNotify() {
        ProportionalLayoutPanel.super.addNotify();
        JOptionPane.showMessageDialog(this, "" + getBounds());
    }

    private void initComponents() {
        this.jPanel1 = new JPanel();
        setLayout(null);
        addComponentListener(new ComponentAdapter() {
            /* class com.kitfox.svg.app.beans.ProportionalLayoutPanel.AnonymousClass1 */

            public void componentResized(ComponentEvent evt) {
                ProportionalLayoutPanel.this.formComponentResized(evt);
            }

            public void componentShown(ComponentEvent evt) {
                ProportionalLayoutPanel.this.formComponentShown(evt);
            }
        });
        add(this.jPanel1);
        this.jPanel1.setBounds(80, 90, 280, (int) Opcode.IF_ICMPNE);
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private void formComponentShown(ComponentEvent evt) {
        JOptionPane.showMessageDialog(this, "" + getWidth() + ", " + getHeight());
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private void formComponentResized(ComponentEvent evt) {
    }
}
