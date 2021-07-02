package com.jgoodies.forms.builder;

import com.jgoodies.common.base.Preconditions;
import com.jgoodies.common.base.Strings;
import com.jgoodies.common.internal.Messages;
import com.jgoodies.forms.FormsSetup;
import com.jgoodies.forms.factories.ComponentFactory;
import com.jgoodies.forms.factories.Forms;
import com.jgoodies.forms.factories.Paddings;
import com.jgoodies.forms.internal.InternalFocusSetupUtils;
import com.jgoodies.forms.util.FocusTraversalType;
import com.kitfox.svg.Filter;
import java.awt.Component;
import java.awt.FocusTraversalPolicy;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTree;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;

public final class ListViewBuilder {
    private Border border;
    private JComponent detailsView;
    private ComponentFactory factory;
    private JComponent filterView;
    private String filterViewColSpec = "[100dlu, p]";
    private FocusTraversalPolicy focusTraversalPolicy;
    private FocusTraversalType focusTraversalType;
    private boolean honorsVisibility = true;
    private Component initialComponent;
    private JComponent label;
    private JComponent listBarView;
    private JComponent listExtrasView;
    private JComponent listStackView;
    private JComponent listView;
    private String listViewRowSpec = "fill:[100dlu, d]:grow";
    private String namePrefix = "ListView";
    private JComponent panel;

    private ListViewBuilder() {
    }

    public static ListViewBuilder create() {
        return new ListViewBuilder();
    }

    public ListViewBuilder border(Border border2) {
        this.border = border2;
        invalidatePanel();
        return this;
    }

    public ListViewBuilder padding(EmptyBorder padding) {
        border(padding);
        return this;
    }

    public ListViewBuilder padding(String paddingSpec, Object... args) {
        padding(Paddings.createPadding(paddingSpec, args));
        return this;
    }

    public ListViewBuilder initialComponent(JComponent initialComponent2) {
        boolean z = true;
        Preconditions.checkNotNull(initialComponent2, Messages.MUST_NOT_BE_NULL, "initial component");
        if (this.initialComponent != null) {
            z = false;
        }
        Preconditions.checkState(z, "The initial component must be set once only.");
        checkValidFocusTraversalSetup();
        this.initialComponent = initialComponent2;
        return this;
    }

    public ListViewBuilder focusTraversalType(FocusTraversalType focusTraversalType2) {
        boolean z = true;
        Preconditions.checkNotNull(focusTraversalType2, Messages.MUST_NOT_BE_NULL, "focus traversal type");
        if (this.focusTraversalType != null) {
            z = false;
        }
        Preconditions.checkState(z, "The focus traversal type must be set once only.");
        checkValidFocusTraversalSetup();
        this.focusTraversalType = focusTraversalType2;
        return this;
    }

    public ListViewBuilder focusTraversalPolicy(FocusTraversalPolicy policy) {
        boolean z = true;
        Preconditions.checkNotNull(policy, Messages.MUST_NOT_BE_NULL, "focus traversal policy");
        if (this.focusTraversalPolicy != null) {
            z = false;
        }
        Preconditions.checkState(z, "The focus traversal policy must be set once only.");
        checkValidFocusTraversalSetup();
        this.focusTraversalPolicy = policy;
        return this;
    }

    public ListViewBuilder honorVisibility(boolean b) {
        this.honorsVisibility = b;
        invalidatePanel();
        return this;
    }

    public ListViewBuilder namePrefix(String namePrefix2) {
        this.namePrefix = namePrefix2;
        return this;
    }

    public ListViewBuilder factory(ComponentFactory factory2) {
        this.factory = factory2;
        return this;
    }

    public ListViewBuilder label(JComponent labelView) {
        this.label = labelView;
        overrideNameIfBlank(labelView, "label");
        invalidatePanel();
        return this;
    }

    public ListViewBuilder labelText(String markedText, Object... args) {
        label(getFactory().createLabel(Strings.get(markedText, args)));
        return this;
    }

    public ListViewBuilder headerText(String markedText, Object... args) {
        label(getFactory().createHeaderLabel(Strings.get(markedText, args)));
        return this;
    }

    public ListViewBuilder filterView(JComponent filterView2) {
        this.filterView = filterView2;
        overrideNameIfBlank(filterView2, Filter.TAG_NAME);
        invalidatePanel();
        return this;
    }

    public ListViewBuilder filterViewColumn(String colSpec, Object... args) {
        Preconditions.checkNotNull(colSpec, Messages.MUST_NOT_BE_BLANK, "filter view column specification");
        this.filterViewColSpec = Strings.get(colSpec, args);
        invalidatePanel();
        return this;
    }

    public ListViewBuilder listView(JComponent listView2) {
        Preconditions.checkNotNull(listView2, Messages.MUST_NOT_BE_BLANK, "list view");
        if ((listView2 instanceof JTable) || (listView2 instanceof JList) || (listView2 instanceof JTree)) {
            this.listView = new JScrollPane(listView2);
        } else {
            this.listView = listView2;
        }
        overrideNameIfBlank(listView2, "listView");
        invalidatePanel();
        return this;
    }

    public ListViewBuilder listViewRow(String rowSpec, Object... args) {
        Preconditions.checkNotNull(rowSpec, Messages.MUST_NOT_BE_BLANK, "list view row specification");
        this.listViewRowSpec = Strings.get(rowSpec, args);
        invalidatePanel();
        return this;
    }

    public ListViewBuilder listBarView(JComponent listBarView2) {
        this.listBarView = listBarView2;
        overrideNameIfBlank(listBarView2, "listBarView");
        invalidatePanel();
        return this;
    }

    public ListViewBuilder listBar(JComponent... buttons) {
        listBarView(Forms.buttonBar(buttons));
        return this;
    }

    public ListViewBuilder listStackView(JComponent listStackView2) {
        this.listStackView = listStackView2;
        overrideNameIfBlank(listStackView2, "listStackView");
        invalidatePanel();
        return this;
    }

    public ListViewBuilder listStack(JComponent... buttons) {
        listStackView(Forms.buttonStack(buttons));
        return this;
    }

    public ListViewBuilder listExtrasView(JComponent listExtrasView2) {
        this.listExtrasView = listExtrasView2;
        overrideNameIfBlank(listExtrasView2, "listExtrasView");
        invalidatePanel();
        return this;
    }

    public ListViewBuilder detailsView(JComponent detailsView2) {
        this.detailsView = detailsView2;
        overrideNameIfBlank(detailsView2, "detailsView");
        invalidatePanel();
        return this;
    }

    public JComponent build() {
        if (this.panel == null) {
            this.panel = buildPanel();
        }
        return this.panel;
    }

    private ComponentFactory getFactory() {
        if (this.factory == null) {
            this.factory = FormsSetup.getComponentFactoryDefault();
        }
        return this.factory;
    }

    private void invalidatePanel() {
        this.panel = null;
    }

    private JComponent buildPanel() {
        Preconditions.checkNotNull(this.listView, "The list view must be set before #build is invoked.");
        FormBuilder builder = FormBuilder.create().columns("fill:default:grow, %s, p", hasStack() ? "$rg" : "0").rows("p, %1$s, p, %2$s, p", this.listViewRowSpec, hasDetails() ? "14dlu" : "0").honorsVisibility(this.honorsVisibility).border(this.border).add(hasHeader(), (Component) buildHeader()).xy(1, 1).add(true, (Component) this.listView).xy(1, 2).add(hasOperations(), (Component) buildOperations()).xy(1, 3).add(hasStack(), (Component) this.listStackView).xy(3, 2).add(hasDetails(), (Component) this.detailsView).xy(1, 5);
        if (this.label instanceof JLabel) {
            JLabel theLabel = this.label;
            if (theLabel.getLabelFor() == null) {
                theLabel.setLabelFor(this.listView);
            }
        }
        InternalFocusSetupUtils.setupFocusTraversalPolicyAndProvider(builder.getPanel(), this.focusTraversalPolicy, this.focusTraversalType, this.initialComponent);
        return builder.build();
    }

    private JComponent buildHeader() {
        if (!hasHeader()) {
            return null;
        }
        return FormBuilder.create().columns(hasFilter() ? "default:grow, 9dlu, %s" : "default:grow, 0,    0", this.filterViewColSpec).rows("[14dlu, p], $lcg", new Object[0]).labelForFeatureEnabled(false).add(hasLabel(), (Component) this.label).xy(1, 1).add(hasFilter(), (Component) this.filterView).xy(3, 1).build();
    }

    private JComponent buildOperations() {
        if (!hasOperations()) {
            return null;
        }
        return FormBuilder.create().columns("left:default, %s:grow, right:pref", hasListExtras() ? "9dlu" : "0").rows("$rgap, p", new Object[0]).honorsVisibility(this.honorsVisibility).add(hasListBar(), (Component) this.listBarView).xy(1, 2).add(hasListExtras(), (Component) this.listExtrasView).xy(3, 2).build();
    }

    private boolean hasLabel() {
        return this.label != null;
    }

    private boolean hasFilter() {
        return this.filterView != null;
    }

    private boolean hasHeader() {
        return hasLabel() || hasFilter();
    }

    private boolean hasListBar() {
        return this.listBarView != null;
    }

    private boolean hasListExtras() {
        return this.listExtrasView != null;
    }

    private boolean hasOperations() {
        return hasListBar() || hasListExtras();
    }

    private boolean hasStack() {
        return this.listStackView != null;
    }

    private boolean hasDetails() {
        return this.detailsView != null;
    }

    private void overrideNameIfBlank(JComponent component, String suffix) {
        if (component != null && Strings.isBlank(component.getName())) {
            component.setName(this.namePrefix + '.' + suffix);
        }
    }

    private void checkValidFocusTraversalSetup() {
        InternalFocusSetupUtils.checkValidFocusTraversalSetup(this.focusTraversalPolicy, this.focusTraversalType, this.initialComponent);
    }
}
