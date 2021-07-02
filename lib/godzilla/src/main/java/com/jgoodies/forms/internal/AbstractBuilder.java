package com.jgoodies.forms.internal;

import com.jgoodies.common.base.Preconditions;
import com.jgoodies.common.internal.Messages;
import com.jgoodies.forms.FormsSetup;
import com.jgoodies.forms.factories.ComponentFactory;
import com.jgoodies.forms.factories.Paddings;
import com.jgoodies.forms.internal.AbstractBuilder;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import java.awt.Color;
import java.awt.Container;
import javax.swing.JPanel;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;

public abstract class AbstractBuilder<B extends AbstractBuilder<B>> {
    private ComponentFactory componentFactory;
    protected final CellConstraints currentCellConstraints = new CellConstraints();
    private final FormLayout layout;
    private final JPanel panel;

    public abstract JPanel build();

    protected AbstractBuilder(FormLayout layout2, JPanel panel2) {
        this.layout = (FormLayout) Preconditions.checkNotNull(layout2, Messages.MUST_NOT_BE_NULL, "layout");
        this.panel = (JPanel) Preconditions.checkNotNull(panel2, Messages.MUST_NOT_BE_NULL, "panel");
        panel2.setLayout(layout2);
    }

    public final JPanel getPanel() {
        return this.panel;
    }

    @Deprecated
    public final Container getContainer() {
        return this.panel;
    }

    public final FormLayout getLayout() {
        return this.layout;
    }

    public final int getColumnCount() {
        return getLayout().getColumnCount();
    }

    public final int getRowCount() {
        return getLayout().getRowCount();
    }

    public B background(Color background) {
        getPanel().setBackground(background);
        opaque(true);
        return this;
    }

    public B border(Border border) {
        getPanel().setBorder(border);
        return this;
    }

    @Deprecated
    public B border(String paddingSpec) {
        padding(Paddings.createPadding(paddingSpec, new Object[0]));
        return this;
    }

    public B padding(EmptyBorder padding) {
        getPanel().setBorder(padding);
        return this;
    }

    public B padding(String paddingSpec, Object... args) {
        padding(Paddings.createPadding(paddingSpec, args));
        return this;
    }

    public B opaque(boolean b) {
        getPanel().setOpaque(b);
        return this;
    }

    public final ComponentFactory getComponentFactory() {
        if (this.componentFactory == null) {
            this.componentFactory = createComponentFactory();
        }
        return this.componentFactory;
    }

    public final void setComponentFactory(ComponentFactory newFactory) {
        this.componentFactory = newFactory;
    }

    /* access modifiers changed from: protected */
    public ComponentFactory createComponentFactory() {
        return FormsSetup.getComponentFactoryDefault();
    }
}
