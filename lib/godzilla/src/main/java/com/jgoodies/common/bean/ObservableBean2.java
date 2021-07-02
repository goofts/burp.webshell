package com.jgoodies.common.bean;

import java.beans.PropertyChangeListener;

public interface ObservableBean2 extends ObservableBean {
    void addPropertyChangeListener(String str, PropertyChangeListener propertyChangeListener);

    PropertyChangeListener[] getPropertyChangeListeners();

    PropertyChangeListener[] getPropertyChangeListeners(String str);

    void removePropertyChangeListener(String str, PropertyChangeListener propertyChangeListener);
}
