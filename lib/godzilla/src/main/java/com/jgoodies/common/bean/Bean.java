package com.jgoodies.common.bean;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.beans.PropertyVetoException;
import java.beans.VetoableChangeListener;
import java.beans.VetoableChangeSupport;
import java.io.Serializable;

public abstract class Bean implements Serializable, ObservableBean2 {
    protected transient PropertyChangeSupport changeSupport;
    private transient VetoableChangeSupport vetoSupport;

    @Override // com.jgoodies.common.bean.ObservableBean
    public final synchronized void addPropertyChangeListener(PropertyChangeListener listener) {
        if (listener != null) {
            if (this.changeSupport == null) {
                this.changeSupport = createPropertyChangeSupport(this);
            }
            this.changeSupport.addPropertyChangeListener(listener);
        }
    }

    @Override // com.jgoodies.common.bean.ObservableBean
    public final synchronized void removePropertyChangeListener(PropertyChangeListener listener) {
        if (listener != null) {
            if (this.changeSupport != null) {
                this.changeSupport.removePropertyChangeListener(listener);
            }
        }
    }

    @Override // com.jgoodies.common.bean.ObservableBean2
    public final synchronized void addPropertyChangeListener(String propertyName, PropertyChangeListener listener) {
        if (listener != null) {
            if (this.changeSupport == null) {
                this.changeSupport = createPropertyChangeSupport(this);
            }
            this.changeSupport.addPropertyChangeListener(propertyName, listener);
        }
    }

    @Override // com.jgoodies.common.bean.ObservableBean2
    public final synchronized void removePropertyChangeListener(String propertyName, PropertyChangeListener listener) {
        if (listener != null) {
            if (this.changeSupport != null) {
                this.changeSupport.removePropertyChangeListener(propertyName, listener);
            }
        }
    }

    public final synchronized void addVetoableChangeListener(VetoableChangeListener listener) {
        if (listener != null) {
            if (this.vetoSupport == null) {
                this.vetoSupport = new VetoableChangeSupport(this);
            }
            this.vetoSupport.addVetoableChangeListener(listener);
        }
    }

    public final synchronized void removeVetoableChangeListener(VetoableChangeListener listener) {
        if (listener != null) {
            if (this.vetoSupport != null) {
                this.vetoSupport.removeVetoableChangeListener(listener);
            }
        }
    }

    public final synchronized void addVetoableChangeListener(String propertyName, VetoableChangeListener listener) {
        if (listener != null) {
            if (this.vetoSupport == null) {
                this.vetoSupport = new VetoableChangeSupport(this);
            }
            this.vetoSupport.addVetoableChangeListener(propertyName, listener);
        }
    }

    public final synchronized void removeVetoableChangeListener(String propertyName, VetoableChangeListener listener) {
        if (listener != null) {
            if (this.vetoSupport != null) {
                this.vetoSupport.removeVetoableChangeListener(propertyName, listener);
            }
        }
    }

    @Override // com.jgoodies.common.bean.ObservableBean2
    public final synchronized PropertyChangeListener[] getPropertyChangeListeners() {
        PropertyChangeListener[] propertyChangeListeners;
        if (this.changeSupport == null) {
            propertyChangeListeners = new PropertyChangeListener[0];
        } else {
            propertyChangeListeners = this.changeSupport.getPropertyChangeListeners();
        }
        return propertyChangeListeners;
    }

    @Override // com.jgoodies.common.bean.ObservableBean2
    public final synchronized PropertyChangeListener[] getPropertyChangeListeners(String propertyName) {
        PropertyChangeListener[] propertyChangeListeners;
        if (this.changeSupport == null) {
            propertyChangeListeners = new PropertyChangeListener[0];
        } else {
            propertyChangeListeners = this.changeSupport.getPropertyChangeListeners(propertyName);
        }
        return propertyChangeListeners;
    }

    public final synchronized VetoableChangeListener[] getVetoableChangeListeners() {
        VetoableChangeListener[] vetoableChangeListeners;
        if (this.vetoSupport == null) {
            vetoableChangeListeners = new VetoableChangeListener[0];
        } else {
            vetoableChangeListeners = this.vetoSupport.getVetoableChangeListeners();
        }
        return vetoableChangeListeners;
    }

    public final synchronized VetoableChangeListener[] getVetoableChangeListeners(String propertyName) {
        VetoableChangeListener[] vetoableChangeListeners;
        if (this.vetoSupport == null) {
            vetoableChangeListeners = new VetoableChangeListener[0];
        } else {
            vetoableChangeListeners = this.vetoSupport.getVetoableChangeListeners(propertyName);
        }
        return vetoableChangeListeners;
    }

    /* access modifiers changed from: protected */
    public PropertyChangeSupport createPropertyChangeSupport(Object bean) {
        return new PropertyChangeSupport(bean);
    }

    /* access modifiers changed from: protected */
    public final void firePropertyChange(PropertyChangeEvent event) {
        PropertyChangeSupport aChangeSupport = this.changeSupport;
        if (aChangeSupport != null) {
            aChangeSupport.firePropertyChange(event);
        }
    }

    /* access modifiers changed from: protected */
    public final void firePropertyChange(String propertyName, Object oldValue, Object newValue) {
        PropertyChangeSupport aChangeSupport = this.changeSupport;
        if (aChangeSupport != null) {
            aChangeSupport.firePropertyChange(propertyName, oldValue, newValue);
        }
    }

    /* access modifiers changed from: protected */
    public final void firePropertyChange(String propertyName, boolean oldValue, boolean newValue) {
        PropertyChangeSupport aChangeSupport = this.changeSupport;
        if (aChangeSupport != null) {
            aChangeSupport.firePropertyChange(propertyName, oldValue, newValue);
        }
    }

    /* access modifiers changed from: protected */
    public final void firePropertyChange(String propertyName, double oldValue, double newValue) {
        firePropertyChange(propertyName, Double.valueOf(oldValue), Double.valueOf(newValue));
    }

    /* access modifiers changed from: protected */
    public final void firePropertyChange(String propertyName, float oldValue, float newValue) {
        firePropertyChange(propertyName, Float.valueOf(oldValue), Float.valueOf(newValue));
    }

    /* access modifiers changed from: protected */
    public final void firePropertyChange(String propertyName, int oldValue, int newValue) {
        PropertyChangeSupport aChangeSupport = this.changeSupport;
        if (aChangeSupport != null) {
            aChangeSupport.firePropertyChange(propertyName, Integer.valueOf(oldValue), Integer.valueOf(newValue));
        }
    }

    /* access modifiers changed from: protected */
    public final void firePropertyChange(String propertyName, long oldValue, long newValue) {
        firePropertyChange(propertyName, Long.valueOf(oldValue), Long.valueOf(newValue));
    }

    /* access modifiers changed from: protected */
    public final void fireMultiplePropertiesChanged() {
        firePropertyChange((String) null, (Object) null, (Object) null);
    }

    /* access modifiers changed from: protected */
    public final void fireIndexedPropertyChange(String propertyName, int index, Object oldValue, Object newValue) {
        PropertyChangeSupport aChangeSupport = this.changeSupport;
        if (aChangeSupport != null) {
            aChangeSupport.fireIndexedPropertyChange(propertyName, index, oldValue, newValue);
        }
    }

    /* access modifiers changed from: protected */
    public final void fireIndexedPropertyChange(String propertyName, int index, int oldValue, int newValue) {
        if (oldValue != newValue) {
            fireIndexedPropertyChange(propertyName, index, Integer.valueOf(oldValue), Integer.valueOf(newValue));
        }
    }

    /* access modifiers changed from: protected */
    public final void fireIndexedPropertyChange(String propertyName, int index, boolean oldValue, boolean newValue) {
        if (oldValue != newValue) {
            fireIndexedPropertyChange(propertyName, index, Boolean.valueOf(oldValue), Boolean.valueOf(newValue));
        }
    }

    /* access modifiers changed from: protected */
    public final void fireVetoableChange(PropertyChangeEvent event) throws PropertyVetoException {
        VetoableChangeSupport aVetoSupport = this.vetoSupport;
        if (aVetoSupport != null) {
            aVetoSupport.fireVetoableChange(event);
        }
    }

    /* access modifiers changed from: protected */
    public final void fireVetoableChange(String propertyName, Object oldValue, Object newValue) throws PropertyVetoException {
        VetoableChangeSupport aVetoSupport = this.vetoSupport;
        if (aVetoSupport != null) {
            aVetoSupport.fireVetoableChange(propertyName, oldValue, newValue);
        }
    }

    /* access modifiers changed from: protected */
    public final void fireVetoableChange(String propertyName, boolean oldValue, boolean newValue) throws PropertyVetoException {
        VetoableChangeSupport aVetoSupport = this.vetoSupport;
        if (aVetoSupport != null) {
            aVetoSupport.fireVetoableChange(propertyName, oldValue, newValue);
        }
    }

    /* access modifiers changed from: protected */
    public final void fireVetoableChange(String propertyName, double oldValue, double newValue) throws PropertyVetoException {
        fireVetoableChange(propertyName, Double.valueOf(oldValue), Double.valueOf(newValue));
    }

    /* access modifiers changed from: protected */
    public final void fireVetoableChange(String propertyName, int oldValue, int newValue) throws PropertyVetoException {
        VetoableChangeSupport aVetoSupport = this.vetoSupport;
        if (aVetoSupport != null) {
            aVetoSupport.fireVetoableChange(propertyName, Integer.valueOf(oldValue), Integer.valueOf(newValue));
        }
    }

    /* access modifiers changed from: protected */
    public final void fireVetoableChange(String propertyName, float oldValue, float newValue) throws PropertyVetoException {
        fireVetoableChange(propertyName, Float.valueOf(oldValue), Float.valueOf(newValue));
    }

    /* access modifiers changed from: protected */
    public final void fireVetoableChange(String propertyName, long oldValue, long newValue) throws PropertyVetoException {
        fireVetoableChange(propertyName, Long.valueOf(oldValue), Long.valueOf(newValue));
    }
}
