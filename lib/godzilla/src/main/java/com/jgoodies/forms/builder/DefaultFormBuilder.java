package com.jgoodies.forms.builder;

import com.jgoodies.common.internal.StringResourceAccessor;
import com.jgoodies.forms.layout.ConstantSize;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.FormSpecs;
import com.jgoodies.forms.layout.RowSpec;
import java.awt.Color;
import java.awt.Component;
import java.awt.LayoutManager;
import java.util.ResourceBundle;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;

@Deprecated
public final class DefaultFormBuilder extends I15dPanelBuilder {
    private RowSpec defaultRowSpec;
    private int leadingColumnOffset;
    private RowSpec lineGapSpec;
    private RowSpec paragraphGapSpec;
    private boolean rowGroupingEnabled;

    public DefaultFormBuilder(FormLayout layout) {
        this(layout, new JPanel((LayoutManager) null));
    }

    public DefaultFormBuilder(FormLayout layout, JPanel container) {
        this(layout, (StringResourceAccessor) null, container);
    }

    public DefaultFormBuilder(FormLayout layout, ResourceBundle bundle) {
        super(layout, bundle);
        this.defaultRowSpec = FormSpecs.PREF_ROWSPEC;
        this.lineGapSpec = FormSpecs.LINE_GAP_ROWSPEC;
        this.paragraphGapSpec = FormSpecs.PARAGRAPH_GAP_ROWSPEC;
        this.leadingColumnOffset = 0;
        this.rowGroupingEnabled = false;
    }

    public DefaultFormBuilder(FormLayout layout, ResourceBundle bundle, JPanel container) {
        super(layout, bundle, container);
        this.defaultRowSpec = FormSpecs.PREF_ROWSPEC;
        this.lineGapSpec = FormSpecs.LINE_GAP_ROWSPEC;
        this.paragraphGapSpec = FormSpecs.PARAGRAPH_GAP_ROWSPEC;
        this.leadingColumnOffset = 0;
        this.rowGroupingEnabled = false;
    }

    public DefaultFormBuilder(FormLayout layout, StringResourceAccessor localizer) {
        super(layout, localizer);
        this.defaultRowSpec = FormSpecs.PREF_ROWSPEC;
        this.lineGapSpec = FormSpecs.LINE_GAP_ROWSPEC;
        this.paragraphGapSpec = FormSpecs.PARAGRAPH_GAP_ROWSPEC;
        this.leadingColumnOffset = 0;
        this.rowGroupingEnabled = false;
    }

    public DefaultFormBuilder(FormLayout layout, StringResourceAccessor localizer, JPanel container) {
        super(layout, localizer, container);
        this.defaultRowSpec = FormSpecs.PREF_ROWSPEC;
        this.lineGapSpec = FormSpecs.LINE_GAP_ROWSPEC;
        this.paragraphGapSpec = FormSpecs.PARAGRAPH_GAP_ROWSPEC;
        this.leadingColumnOffset = 0;
        this.rowGroupingEnabled = false;
    }

    @Override // com.jgoodies.forms.builder.I15dPanelBuilder, com.jgoodies.forms.builder.I15dPanelBuilder, com.jgoodies.forms.internal.AbstractBuilder
    public DefaultFormBuilder background(Color background) {
        super.background(background);
        return this;
    }

    @Override // com.jgoodies.forms.builder.I15dPanelBuilder, com.jgoodies.forms.builder.I15dPanelBuilder, com.jgoodies.forms.internal.AbstractBuilder
    public DefaultFormBuilder border(Border border) {
        super.border(border);
        return this;
    }

    @Override // com.jgoodies.forms.builder.I15dPanelBuilder, com.jgoodies.forms.builder.I15dPanelBuilder, com.jgoodies.forms.internal.AbstractBuilder
    public DefaultFormBuilder border(String emptyBorderSpec) {
        super.border(emptyBorderSpec);
        return this;
    }

    @Override // com.jgoodies.forms.builder.I15dPanelBuilder, com.jgoodies.forms.builder.I15dPanelBuilder, com.jgoodies.forms.internal.AbstractBuilder
    public DefaultFormBuilder padding(EmptyBorder padding) {
        super.padding(padding);
        return this;
    }

    @Override // com.jgoodies.forms.builder.I15dPanelBuilder, com.jgoodies.forms.builder.I15dPanelBuilder, com.jgoodies.forms.internal.AbstractBuilder
    public DefaultFormBuilder padding(String paddingSpec, Object... args) {
        super.padding(paddingSpec, new Object[0]);
        return this;
    }

    @Override // com.jgoodies.forms.builder.I15dPanelBuilder, com.jgoodies.forms.builder.I15dPanelBuilder, com.jgoodies.forms.internal.AbstractBuilder
    public DefaultFormBuilder opaque(boolean b) {
        super.opaque(b);
        return this;
    }

    public DefaultFormBuilder defaultRowSpec(RowSpec defaultRowSpec2) {
        this.defaultRowSpec = defaultRowSpec2;
        return this;
    }

    public DefaultFormBuilder lineGapSize(ConstantSize lineGapSize) {
        this.lineGapSpec = RowSpec.createGap(lineGapSize);
        return this;
    }

    public DefaultFormBuilder paragraphGapSize(ConstantSize paragraphGapSize) {
        this.paragraphGapSpec = RowSpec.createGap(paragraphGapSize);
        return this;
    }

    public DefaultFormBuilder leadingColumnOffset(int columnOffset) {
        this.leadingColumnOffset = columnOffset;
        return this;
    }

    public DefaultFormBuilder rowGroupingEnabled(boolean enabled) {
        this.rowGroupingEnabled = enabled;
        return this;
    }

    public final void appendLineGapRow() {
        appendRow(this.lineGapSpec);
    }

    public void append(Component component) {
        append(component, 1);
    }

    public void append(Component component, int columnSpan) {
        ensureCursorColumnInGrid();
        ensureHasGapRow(this.lineGapSpec);
        ensureHasComponentLine();
        add(component, createLeftAdjustedConstraints(columnSpan));
        nextColumn(columnSpan + 1);
    }

    public void append(Component c1, Component c2) {
        append(c1);
        append(c2);
    }

    public void append(Component c1, Component c2, Component c3) {
        append(c1);
        append(c2);
        append(c3);
    }

    public JLabel append(String textWithMnemonic) {
        JLabel label = getComponentFactory().createLabel(textWithMnemonic);
        append((Component) label);
        return label;
    }

    public JLabel append(String textWithMnemonic, Component component) {
        return append(textWithMnemonic, component, 1);
    }

    public JLabel append(String textWithMnemonic, Component c, boolean nextLine) {
        JLabel label = append(textWithMnemonic, c);
        if (nextLine) {
            nextLine();
        }
        return label;
    }

    public JLabel append(String textWithMnemonic, Component c, int columnSpan) {
        JLabel label = append(textWithMnemonic);
        label.setLabelFor(c);
        append(c, columnSpan);
        return label;
    }

    public JLabel append(String textWithMnemonic, Component c1, Component c2) {
        JLabel label = append(textWithMnemonic, c1);
        append(c2);
        return label;
    }

    public JLabel append(String textWithMnemonic, Component c1, Component c2, int colSpan) {
        JLabel label = append(textWithMnemonic, c1);
        append(c2, colSpan);
        return label;
    }

    public JLabel append(String textWithMnemonic, Component c1, Component c2, Component c3) {
        JLabel label = append(textWithMnemonic, c1, c2);
        append(c3);
        return label;
    }

    public JLabel append(String textWithMnemonic, Component c1, Component c2, Component c3, Component c4) {
        JLabel label = append(textWithMnemonic, c1, c2, c3);
        append(c4);
        return label;
    }

    public JLabel appendI15d(String resourceKey) {
        return append(getResourceString(resourceKey));
    }

    public JLabel appendI15d(String resourceKey, Component component) {
        return append(getResourceString(resourceKey), component, 1);
    }

    public JLabel appendI15d(String resourceKey, Component component, boolean nextLine) {
        return append(getResourceString(resourceKey), component, nextLine);
    }

    public JLabel appendI15d(String resourceKey, Component c, int columnSpan) {
        return append(getResourceString(resourceKey), c, columnSpan);
    }

    public JLabel appendI15d(String resourceKey, Component c1, Component c2) {
        return append(getResourceString(resourceKey), c1, c2);
    }

    public JLabel appendI15d(String resourceKey, Component c1, Component c2, int colSpan) {
        return append(getResourceString(resourceKey), c1, c2, colSpan);
    }

    public JLabel appendI15d(String resourceKey, Component c1, Component c2, Component c3) {
        return append(getResourceString(resourceKey), c1, c2, c3);
    }

    public JLabel appendI15d(String resourceKey, Component c1, Component c2, Component c3, Component c4) {
        return append(getResourceString(resourceKey), c1, c2, c3, c4);
    }

    public JLabel appendTitle(String textWithMnemonic) {
        JLabel titleLabel = getComponentFactory().createTitle(textWithMnemonic);
        append((Component) titleLabel);
        return titleLabel;
    }

    public JLabel appendI15dTitle(String resourceKey) {
        return appendTitle(getResourceString(resourceKey));
    }

    public JComponent appendSeparator() {
        return appendSeparator("");
    }

    public JComponent appendSeparator(String text) {
        ensureCursorColumnInGrid();
        ensureHasGapRow(this.paragraphGapSpec);
        ensureHasComponentLine();
        setColumn(super.getLeadingColumn());
        int columnSpan = getColumnCount();
        setColumnSpan(getColumnCount());
        JComponent titledSeparator = addSeparator(text);
        setColumnSpan(1);
        nextColumn(columnSpan);
        return titledSeparator;
    }

    public JComponent appendI15dSeparator(String resourceKey) {
        return appendSeparator(getResourceString(resourceKey));
    }

    /* access modifiers changed from: protected */
    @Override // com.jgoodies.forms.internal.AbstractFormBuilder
    public int getLeadingColumn() {
        return (this.leadingColumnOffset * getColumnIncrementSign()) + super.getLeadingColumn();
    }

    private void ensureCursorColumnInGrid() {
        if ((isLeftToRight() && getColumn() > getColumnCount()) || (!isLeftToRight() && getColumn() < 1)) {
            nextLine();
        }
    }

    private void ensureHasGapRow(RowSpec gapRowSpec) {
        if (getRow() != 1 && getRow() > getRowCount()) {
            if (getRow() > getRowCount() || getCursorRowSpec() != gapRowSpec) {
                appendRow(gapRowSpec);
                nextLine();
            }
        }
    }

    private void ensureHasComponentLine() {
        if (getRow() > getRowCount()) {
            appendRow(this.defaultRowSpec);
            if (this.rowGroupingEnabled) {
                getLayout().addGroupedRow(getRow());
            }
        }
    }

    private RowSpec getCursorRowSpec() {
        return getLayout().getRowSpec(getRow());
    }
}
