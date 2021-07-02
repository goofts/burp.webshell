package com.formdev.flatlaf.extras.components;

import com.formdev.flatlaf.FlatClientProperties;
import com.formdev.flatlaf.FlatLaf;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ItemEvent;
import javax.swing.JCheckBox;
import javax.swing.JToggleButton;
import javax.swing.LookAndFeel;
import javax.swing.UIManager;

public class FlatTriStateCheckBox extends JCheckBox {
    private boolean allowIndeterminate;
    private boolean altStateCycleOrder;
    private State state;

    public enum State {
        UNSELECTED,
        INDETERMINATE,
        SELECTED
    }

    public FlatTriStateCheckBox() {
        this(null);
    }

    public FlatTriStateCheckBox(String text) {
        this(text, State.INDETERMINATE);
    }

    public FlatTriStateCheckBox(String text, State initialState) {
        super(text);
        this.allowIndeterminate = true;
        this.altStateCycleOrder = UIManager.getBoolean("FlatTriStateCheckBox.altStateCycleOrder");
        setModel(new JToggleButton.ToggleButtonModel() {
            /* class com.formdev.flatlaf.extras.components.FlatTriStateCheckBox.AnonymousClass1 */

            public boolean isSelected() {
                return FlatTriStateCheckBox.this.state != State.UNSELECTED;
            }

            public void setSelected(boolean b) {
                FlatTriStateCheckBox.this.setState(FlatTriStateCheckBox.this.nextState(FlatTriStateCheckBox.this.state));
                fireStateChanged();
                fireItemStateChanged(new ItemEvent(this, 701, this, isSelected() ? 1 : 2));
            }
        });
        setState(initialState);
    }

    public State getState() {
        return this.state;
    }

    public void setState(State state2) {
        if (this.state != state2) {
            State oldState = this.state;
            this.state = state2;
            putClientProperty(FlatClientProperties.SELECTED_STATE, state2 == State.INDETERMINATE ? FlatClientProperties.SELECTED_STATE_INDETERMINATE : null);
            firePropertyChange("state", oldState, state2);
            repaint();
        }
    }

    /* access modifiers changed from: protected */
    public State nextState(State state2) {
        if (!this.altStateCycleOrder) {
            switch (state2) {
                case INDETERMINATE:
                    return State.SELECTED;
                case SELECTED:
                    return State.UNSELECTED;
                default:
                    return this.allowIndeterminate ? State.INDETERMINATE : State.SELECTED;
            }
        } else {
            switch (state2) {
                case INDETERMINATE:
                    return State.UNSELECTED;
                case SELECTED:
                    return this.allowIndeterminate ? State.INDETERMINATE : State.UNSELECTED;
                default:
                    return State.SELECTED;
            }
        }
    }

    public Boolean getChecked() {
        switch (this.state) {
            case INDETERMINATE:
                return null;
            case SELECTED:
                return true;
            default:
                return false;
        }
    }

    public void setChecked(Boolean value) {
        setState(value == null ? State.INDETERMINATE : value.booleanValue() ? State.SELECTED : State.UNSELECTED);
    }

    public void setSelected(boolean b) {
        setState(b ? State.SELECTED : State.UNSELECTED);
    }

    public boolean isIndeterminate() {
        return this.state == State.INDETERMINATE;
    }

    public void setIndeterminate(boolean indeterminate) {
        if (indeterminate) {
            setState(State.INDETERMINATE);
        } else if (this.state == State.INDETERMINATE) {
            setState(State.UNSELECTED);
        }
    }

    public boolean isAllowIndeterminate() {
        return this.allowIndeterminate;
    }

    public void setAllowIndeterminate(boolean allowIndeterminate2) {
        this.allowIndeterminate = allowIndeterminate2;
    }

    public boolean isAltStateCycleOrder() {
        return this.altStateCycleOrder;
    }

    public void setAltStateCycleOrder(boolean altStateCycleOrder2) {
        this.altStateCycleOrder = altStateCycleOrder2;
    }

    /* access modifiers changed from: protected */
    public void paintComponent(Graphics g) {
        FlatTriStateCheckBox.super.paintComponent(g);
        if (this.state == State.INDETERMINATE && !isIndeterminateStateSupported()) {
            paintIndeterminateState(g);
        }
    }

    /* access modifiers changed from: protected */
    public void paintIndeterminateState(Graphics g) {
        g.setColor(Color.magenta);
        g.drawRect(0, 0, getWidth() - 1, getHeight() - 1);
    }

    /* access modifiers changed from: protected */
    public boolean isIndeterminateStateSupported() {
        LookAndFeel laf = UIManager.getLookAndFeel();
        return (laf instanceof FlatLaf) || laf.getClass().getName().equals("com.apple.laf.AquaLookAndFeel");
    }
}
