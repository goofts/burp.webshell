package com.jgoodies.forms.builder;

import com.jgoodies.common.base.Preconditions;
import com.jgoodies.forms.FormsSetup;
import com.jgoodies.forms.internal.AbstractFormBuilder;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import java.awt.Component;
import java.awt.FocusTraversalPolicy;
import java.awt.LayoutManager;
import java.lang.ref.WeakReference;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

@Deprecated
public class PanelBuilder extends AbstractFormBuilder<PanelBuilder> {
    private static final String LABELED_BY_PROPERTY = "labeledBy";
    private boolean labelForFeatureEnabled;
    private WeakReference mostRecentlyAddedLabelReference;

    public PanelBuilder(FormLayout layout) {
        this(layout, new JPanel((LayoutManager) null));
    }

    public PanelBuilder(FormLayout layout, JPanel panel) {
        super(layout, panel);
        this.mostRecentlyAddedLabelReference = null;
        opaque(FormsSetup.getOpaqueDefault());
        this.labelForFeatureEnabled = FormsSetup.getLabelForFeatureEnabledDefault();
    }

    public PanelBuilder focusTraversal(FocusTraversalPolicy policy) {
        getPanel().setFocusTraversalPolicy(policy);
        getPanel().setFocusTraversalPolicyProvider(true);
        return this;
    }

    public PanelBuilder labelForFeatureEnabled(boolean b) {
        this.labelForFeatureEnabled = b;
        return this;
    }

    @Override // com.jgoodies.forms.internal.AbstractBuilder
    public final JPanel build() {
        return getPanel();
    }

    public final JLabel addLabel(String textWithMnemonic) {
        return addLabel(textWithMnemonic, cellConstraints());
    }

    public final JLabel addLabel(String textWithMnemonic, CellConstraints constraints) {
        JLabel label = getComponentFactory().createLabel(textWithMnemonic);
        add((Component) label, constraints);
        return label;
    }

    public final JLabel addLabel(String textWithMnemonic, String encodedConstraints) {
        return addLabel(textWithMnemonic, new CellConstraints(encodedConstraints));
    }

    public final JLabel addLabel(String textWithMnemonic, CellConstraints labelConstraints, Component component, CellConstraints componentConstraints) {
        if (labelConstraints == componentConstraints) {
            throw new IllegalArgumentException("You must provide two CellConstraints instances, one for the label and one for the component.\nConsider using the CC class. See the JavaDocs for details.");
        }
        JLabel label = addLabel(textWithMnemonic, labelConstraints);
        add(component, componentConstraints);
        label.setLabelFor(component);
        return label;
    }

    public final JLabel addROLabel(String textWithMnemonic) {
        return addROLabel(textWithMnemonic, cellConstraints());
    }

    public final JLabel addROLabel(String textWithMnemonic, CellConstraints constraints) {
        JLabel label = getComponentFactory().createReadOnlyLabel(textWithMnemonic);
        add((Component) label, constraints);
        return label;
    }

    public final JLabel addROLabel(String textWithMnemonic, String encodedConstraints) {
        return addROLabel(textWithMnemonic, new CellConstraints(encodedConstraints));
    }

    public final JLabel addROLabel(String textWithMnemonic, CellConstraints labelConstraints, Component component, CellConstraints componentConstraints) {
        checkConstraints(labelConstraints, componentConstraints);
        JLabel label = addROLabel(textWithMnemonic, labelConstraints);
        add(component, componentConstraints);
        label.setLabelFor(component);
        return label;
    }

    public final JLabel addTitle(String textWithMnemonic) {
        return addTitle(textWithMnemonic, cellConstraints());
    }

    public final JLabel addTitle(String textWithMnemonic, CellConstraints constraints) {
        JLabel titleLabel = getComponentFactory().createTitle(textWithMnemonic);
        add((Component) titleLabel, constraints);
        return titleLabel;
    }

    public final JLabel addTitle(String textWithMnemonic, String encodedConstraints) {
        return addTitle(textWithMnemonic, new CellConstraints(encodedConstraints));
    }

    public final JComponent addSeparator(String textWithMnemonic) {
        return addSeparator(textWithMnemonic, getLayout().getColumnCount());
    }

    public final JComponent addSeparator(String textWithMnemonic, CellConstraints constraints) {
        JComponent titledSeparator = getComponentFactory().createSeparator(textWithMnemonic, isLeftToRight() ? 2 : 4);
        add((Component) titledSeparator, constraints);
        return titledSeparator;
    }

    public final JComponent addSeparator(String textWithMnemonic, String encodedConstraints) {
        return addSeparator(textWithMnemonic, new CellConstraints(encodedConstraints));
    }

    public final JComponent addSeparator(String textWithMnemonic, int columnSpan) {
        return addSeparator(textWithMnemonic, createLeftAdjustedConstraints(columnSpan));
    }

    public final JLabel add(JLabel label, CellConstraints labelConstraints, Component component, CellConstraints componentConstraints) {
        checkConstraints(labelConstraints, componentConstraints);
        add((Component) label, labelConstraints);
        add(component, componentConstraints);
        label.setLabelFor(component);
        return label;
    }

    @Override // com.jgoodies.forms.internal.AbstractFormBuilder
    public Component add(Component component, CellConstraints cellConstraints) {
        Component result = super.add(component, cellConstraints);
        manageLabelsAndComponents(component);
        return result;
    }

    private void manageLabelsAndComponents(Component c) {
        if (this.labelForFeatureEnabled) {
            if (c instanceof JLabel) {
                JLabel label = (JLabel) c;
                if (label.getLabelFor() == null) {
                    setMostRecentlyAddedLabel(label);
                } else {
                    clearMostRecentlyAddedLabel();
                }
            } else {
                JLabel mostRecentlyAddedLabel = getMostRecentlyAddedLabel();
                if (mostRecentlyAddedLabel != null && isLabelForApplicable(mostRecentlyAddedLabel, c)) {
                    setLabelFor(mostRecentlyAddedLabel, c);
                    clearMostRecentlyAddedLabel();
                }
            }
        }
    }

    /* access modifiers changed from: protected */
    public boolean isLabelForApplicable(JLabel label, Component component) {
        boolean z = true;
        if (label.getLabelFor() != null || !component.isFocusable()) {
            return false;
        }
        if (!(component instanceof JComponent)) {
            return true;
        }
        if (((JComponent) component).getClientProperty(LABELED_BY_PROPERTY) != null) {
            z = false;
        }
        return z;
    }

    /* access modifiers changed from: protected */
    public void setLabelFor(JLabel label, Component component) {
        Component labeledComponent;
        if (component instanceof JScrollPane) {
            labeledComponent = ((JScrollPane) component).getViewport().getView();
        } else {
            labeledComponent = component;
        }
        label.setLabelFor(labeledComponent);
    }

    private JLabel getMostRecentlyAddedLabel() {
        if (this.mostRecentlyAddedLabelReference == null) {
            return null;
        }
        JLabel label = (JLabel) this.mostRecentlyAddedLabelReference.get();
        if (label == null) {
            return null;
        }
        return label;
    }

    private void setMostRecentlyAddedLabel(JLabel label) {
        this.mostRecentlyAddedLabelReference = new WeakReference(label);
    }

    private void clearMostRecentlyAddedLabel() {
        this.mostRecentlyAddedLabelReference = null;
    }

    private static void checkConstraints(CellConstraints c1, CellConstraints c2) {
        Preconditions.checkArgument(c1 != c2, "You must provide two CellConstraints instances, one for the label and one for the component.\nConsider using the CC factory. See the JavaDocs for details.");
    }
}
