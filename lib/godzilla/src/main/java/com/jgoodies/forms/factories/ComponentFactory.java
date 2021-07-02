package com.jgoodies.forms.factories;

import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;

public interface ComponentFactory {
    JButton createButton(Action action);

    JLabel createHeaderLabel(String str);

    JLabel createLabel(String str);

    JLabel createReadOnlyLabel(String str);

    JComponent createSeparator(String str, int i);

    JLabel createTitle(String str);
}
