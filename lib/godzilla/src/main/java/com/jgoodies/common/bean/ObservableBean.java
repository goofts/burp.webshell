package com.jgoodies.common.bean;

import java.beans.PropertyChangeListener;

public interface ObservableBean {
    void addPropertyChangeListener(PropertyChangeListener propertyChangeListener);

    void removePropertyChangeListener(PropertyChangeListener propertyChangeListener);
}
