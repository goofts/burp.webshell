package com.formdev.flatlaf.demo;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.ComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.MutableComboBoxModel;
import javax.swing.UIManager;

public class LookAndFeelsComboBox extends JComboBox<UIManager.LookAndFeelInfo> {
    private final PropertyChangeListener lafListener;

    /* JADX WARN: Type inference failed for: r0v0, types: [java.beans.PropertyChangeListener, void] */
    /* JADX WARNING: Unknown variable types count: 1 */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public LookAndFeelsComboBox() {
        /*
            r1 = this;
            void r0 = r1.<init>()
            r1.lafListener = r0
            com.formdev.flatlaf.demo.LookAndFeelsComboBox$1 r0 = new com.formdev.flatlaf.demo.LookAndFeelsComboBox$1
            r0.<init>()
            r1.setRenderer(r0)
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.formdev.flatlaf.demo.LookAndFeelsComboBox.<init>():void");
    }

    public void addLookAndFeel(String name, String className) {
        getMutableModel().addElement(new UIManager.LookAndFeelInfo(name, className));
    }

    public String getSelectedLookAndFeel() {
        Object sel = getSelectedItem();
        if (sel instanceof UIManager.LookAndFeelInfo) {
            return ((UIManager.LookAndFeelInfo) sel).getClassName();
        }
        return null;
    }

    public void setSelectedLookAndFeel(String className) {
        setSelectedIndex(getIndexOfLookAndFeel(className));
    }

    public void selectedCurrentLookAndFeel() {
        setSelectedLookAndFeel(UIManager.getLookAndFeel().getClass().getName());
    }

    public void removeLookAndFeel(String className) {
        int index = getIndexOfLookAndFeel(className);
        if (index >= 0) {
            getMutableModel().removeElementAt(index);
        }
    }

    public int getIndexOfLookAndFeel(String className) {
        ComboBoxModel<UIManager.LookAndFeelInfo> model = getModel();
        int size = model.getSize();
        for (int i = 0; i < size; i++) {
            if (className.equals(((UIManager.LookAndFeelInfo) model.getElementAt(i)).getClassName())) {
                return i;
            }
        }
        return -1;
    }

    private MutableComboBoxModel<UIManager.LookAndFeelInfo> getMutableModel() {
        return getModel();
    }

    public void addNotify() {
        LookAndFeelsComboBox.super.addNotify();
        selectedCurrentLookAndFeel();
        UIManager.addPropertyChangeListener(this.lafListener);
    }

    public void removeNotify() {
        LookAndFeelsComboBox.super.removeNotify();
        UIManager.removePropertyChangeListener(this.lafListener);
    }

    /* access modifiers changed from: package-private */
    public void lafChanged(PropertyChangeEvent e) {
        if ("lookAndFeel".equals(e.getPropertyName())) {
            selectedCurrentLookAndFeel();
        }
    }
}
