package com.jgoodies.forms.factories;

import com.jgoodies.common.base.Preconditions;
import com.jgoodies.common.internal.Messages;
import com.jgoodies.forms.builder.ButtonBarBuilder;
import com.jgoodies.forms.builder.ButtonStackBuilder;
import com.jgoodies.forms.builder.PanelBuilder;
import com.jgoodies.forms.internal.FocusTraversalUtilsAccessor;
import com.jgoodies.forms.layout.FormLayout;
import java.awt.Component;
import javax.swing.AbstractButton;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JRadioButton;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;

public final class Forms {
    private Forms() {
    }

    public static JComponent single(String columnSpec, String rowSpec, JComponent component) {
        Preconditions.checkNotBlank(columnSpec, Messages.MUST_NOT_BE_BLANK, "column specification");
        Preconditions.checkNotBlank(rowSpec, Messages.MUST_NOT_BE_BLANK, "row specification");
        Preconditions.checkNotNull(component, Messages.MUST_NOT_BE_NULL, "component");
        PanelBuilder builder = new PanelBuilder(new FormLayout(columnSpec, rowSpec));
        builder.add((Component) component, CC.xy(1, 1));
        return builder.build();
    }

    public static JComponent centered(JComponent component) {
        return single("center:pref:grow", "c:p:g", component);
    }

    public static JComponent border(Border border, JComponent component) {
        JComponent container = single("fill:pref", "f:p", component);
        container.setBorder(border);
        return container;
    }

    @Deprecated
    public static JComponent border(String emptyBorderSpec, JComponent component) {
        return padding(component, emptyBorderSpec, new Object[0]);
    }

    public static JComponent padding(JComponent component, EmptyBorder padding) {
        JComponent container = single("fill:pref", "f:p", component);
        container.setBorder(padding);
        return container;
    }

    public static JComponent padding(JComponent component, String paddingSpec, Object... args) {
        return padding(component, Paddings.createPadding(paddingSpec, args));
    }

    public static JComponent horizontal(String gapColSpec, JComponent... components) {
        boolean z = false;
        Preconditions.checkNotBlank(gapColSpec, Messages.MUST_NOT_BE_BLANK, "gap column specification");
        Preconditions.checkNotNull(components, Messages.MUST_NOT_BE_NULL, "component array");
        if (components.length > 1) {
            z = true;
        }
        Preconditions.checkArgument(z, "You must provide more than one component.");
        PanelBuilder builder = new PanelBuilder(new FormLayout((components.length - 1) + "*(pref, " + gapColSpec + "), pref", "p"));
        int column = 1;
        for (JComponent component : components) {
            builder.add((Component) component, CC.xy(column, 1));
            column += 2;
        }
        return builder.build();
    }

    public static JComponent vertical(String gapRowSpec, JComponent... components) {
        boolean z = false;
        Preconditions.checkNotBlank(gapRowSpec, Messages.MUST_NOT_BE_BLANK, "gap row specification");
        Preconditions.checkNotNull(components, Messages.MUST_NOT_BE_NULL, "component array");
        if (components.length > 1) {
            z = true;
        }
        Preconditions.checkArgument(z, "You must provide more than one component.");
        PanelBuilder builder = new PanelBuilder(new FormLayout("pref", (components.length - 1) + "*(p, " + gapRowSpec + "), p"));
        int row = 1;
        for (JComponent component : components) {
            builder.add((Component) component, CC.xy(1, row));
            row += 2;
        }
        return builder.build();
    }

    public static JComponent buttonBar(JComponent... buttons) {
        return ButtonBarBuilder.create().addButton(buttons).build();
    }

    public static JComponent buttonStack(JComponent... buttons) {
        return ButtonStackBuilder.create().addButton(buttons).build();
    }

    public static JComponent checkBoxBar(JCheckBox... checkBoxes) {
        return buildGroupedButtonBar(checkBoxes);
    }

    public static JComponent checkBoxStack(JCheckBox... checkBoxes) {
        return buildGroupedButtonStack(checkBoxes);
    }

    public static JComponent radioButtonBar(JRadioButton... radioButtons) {
        return buildGroupedButtonBar(radioButtons);
    }

    public static JComponent radioButtonStack(JRadioButton... radioButtons) {
        return buildGroupedButtonStack(radioButtons);
    }

    private static JComponent buildGroupedButtonBar(AbstractButton... buttons) {
        Preconditions.checkArgument(buttons.length > 1, "You must provide more than one button.");
        PanelBuilder builder = new PanelBuilder(new FormLayout(String.format("pref, %s*($rgap, pref)", Integer.valueOf(buttons.length - 1)), "p"));
        int column = 1;
        for (AbstractButton button : buttons) {
            builder.add((Component) button, CC.xy(column, 1));
            column += 2;
        }
        FocusTraversalUtilsAccessor.tryToBuildAFocusGroup(buttons);
        return builder.build();
    }

    private static JComponent buildGroupedButtonStack(AbstractButton... buttons) {
        Preconditions.checkArgument(buttons.length > 1, "You must provide more than one button.");
        PanelBuilder builder = new PanelBuilder(new FormLayout("pref", String.format("p, %s*(0, p)", Integer.valueOf(buttons.length - 1))));
        int row = 1;
        for (AbstractButton button : buttons) {
            builder.add((Component) button, CC.xy(1, row));
            row += 2;
        }
        FocusTraversalUtilsAccessor.tryToBuildAFocusGroup(buttons);
        return builder.build();
    }
}
