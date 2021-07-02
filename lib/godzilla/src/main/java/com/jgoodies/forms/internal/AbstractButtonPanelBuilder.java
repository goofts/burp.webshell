package com.jgoodies.forms.internal;

import com.jgoodies.common.base.Preconditions;
import com.jgoodies.forms.internal.AbstractButtonPanelBuilder;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.FormSpecs;
import com.jgoodies.forms.layout.RowSpec;
import java.awt.Color;
import java.awt.Component;
import java.awt.ComponentOrientation;
import java.util.ArrayList;
import java.util.List;
import javax.swing.AbstractButton;
import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.border.Border;

public abstract class AbstractButtonPanelBuilder<B extends AbstractButtonPanelBuilder<B>> extends AbstractBuilder<B> {
    protected boolean focusGrouped = false;
    private boolean leftToRight;

    /* access modifiers changed from: protected */
    public abstract AbstractButtonPanelBuilder addButton(JComponent jComponent);

    /* access modifiers changed from: protected */
    public abstract AbstractButtonPanelBuilder addRelatedGap();

    /* access modifiers changed from: protected */
    public abstract AbstractButtonPanelBuilder addUnrelatedGap();

    /* JADX INFO: super call moved to the top of the method (can break code semantics) */
    protected AbstractButtonPanelBuilder(FormLayout layout, JPanel container) {
        super(layout, container);
        boolean z = false;
        opaque(false);
        ComponentOrientation orientation = container.getComponentOrientation();
        this.leftToRight = (orientation.isLeftToRight() || !orientation.isHorizontal()) ? true : z;
    }

    @Override // com.jgoodies.forms.internal.AbstractBuilder
    public final JPanel build() {
        if (!this.focusGrouped) {
            List<AbstractButton> buttons = new ArrayList<>();
            Component[] arr$ = getPanel().getComponents();
            for (Component component : arr$) {
                if (component instanceof AbstractButton) {
                    buttons.add((AbstractButton) component);
                }
            }
            FocusTraversalUtilsAccessor.tryToBuildAFocusGroup((AbstractButton[]) buttons.toArray(new AbstractButton[0]));
            this.focusGrouped = true;
        }
        return getPanel();
    }

    @Deprecated
    public final void setBackground(Color background) {
        getPanel().setBackground(background);
        opaque(true);
    }

    @Deprecated
    public final void setBorder(Border border) {
        getPanel().setBorder(border);
    }

    @Deprecated
    public final void setOpaque(boolean b) {
        getPanel().setOpaque(b);
    }

    public final boolean isLeftToRight() {
        return this.leftToRight;
    }

    public final void setLeftToRight(boolean b) {
        this.leftToRight = b;
    }

    /* access modifiers changed from: protected */
    public final void nextColumn() {
        nextColumn(1);
    }

    private void nextColumn(int columns) {
        this.currentCellConstraints.gridX += getColumnIncrementSign() * columns;
    }

    /* access modifiers changed from: protected */
    public final int getColumn() {
        return this.currentCellConstraints.gridX;
    }

    /* access modifiers changed from: protected */
    public final int getRow() {
        return this.currentCellConstraints.gridY;
    }

    /* access modifiers changed from: protected */
    public final void nextRow() {
        nextRow(1);
    }

    private void nextRow(int rows) {
        this.currentCellConstraints.gridY += rows;
    }

    /* access modifiers changed from: protected */
    public final void appendColumn(ColumnSpec columnSpec) {
        getLayout().appendColumn(columnSpec);
    }

    /* access modifiers changed from: protected */
    public final void appendGlueColumn() {
        appendColumn(FormSpecs.GLUE_COLSPEC);
    }

    /* access modifiers changed from: protected */
    public final void appendRelatedComponentsGapColumn() {
        appendColumn(FormSpecs.RELATED_GAP_COLSPEC);
    }

    /* access modifiers changed from: protected */
    public final void appendUnrelatedComponentsGapColumn() {
        appendColumn(FormSpecs.UNRELATED_GAP_COLSPEC);
    }

    /* access modifiers changed from: protected */
    public final void appendRow(RowSpec rowSpec) {
        getLayout().appendRow(rowSpec);
    }

    /* access modifiers changed from: protected */
    public final void appendGlueRow() {
        appendRow(FormSpecs.GLUE_ROWSPEC);
    }

    /* access modifiers changed from: protected */
    public final void appendRelatedComponentsGapRow() {
        appendRow(FormSpecs.RELATED_GAP_ROWSPEC);
    }

    /* access modifiers changed from: protected */
    public final void appendUnrelatedComponentsGapRow() {
        appendRow(FormSpecs.UNRELATED_GAP_ROWSPEC);
    }

    /* access modifiers changed from: protected */
    public final Component add(Component component) {
        getPanel().add(component, this.currentCellConstraints);
        this.focusGrouped = false;
        return component;
    }

    /* access modifiers changed from: protected */
    public AbstractButtonPanelBuilder addButton(JComponent... buttons) {
        Preconditions.checkNotNull(buttons, "The button array must not be null.");
        Preconditions.checkArgument(buttons.length > 0, "The button array must not be empty.");
        boolean needsGap = false;
        for (JComponent button : buttons) {
            if (button == null) {
                addUnrelatedGap();
                needsGap = false;
            } else {
                if (needsGap) {
                    addRelatedGap();
                }
                addButton(button);
                needsGap = true;
            }
        }
        return this;
    }

    /* access modifiers changed from: protected */
    public AbstractButtonPanelBuilder addButton(Action... actions) {
        Preconditions.checkNotNull(actions, "The Action array must not be null.");
        int length = actions.length;
        Preconditions.checkArgument(length > 0, "The Action array must not be empty.");
        JButton[] buttons = new JButton[length];
        for (int i = 0; i < length; i++) {
            Action action = actions[i];
            buttons[i] = action == null ? null : createButton(action);
        }
        return addButton((JComponent[]) buttons);
    }

    /* access modifiers changed from: protected */
    public JButton createButton(Action action) {
        return getComponentFactory().createButton(action);
    }

    private int getColumnIncrementSign() {
        return isLeftToRight() ? 1 : -1;
    }
}
