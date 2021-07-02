package com.jgoodies.forms.builder;

import com.jgoodies.common.base.Preconditions;
import com.jgoodies.common.base.Strings;
import com.jgoodies.common.internal.Messages;
import com.jgoodies.forms.FormsSetup;
import com.jgoodies.forms.debug.FormDebugPanel;
import com.jgoodies.forms.factories.CC;
import com.jgoodies.forms.factories.ComponentFactory;
import com.jgoodies.forms.factories.Forms;
import com.jgoodies.forms.factories.Paddings;
import com.jgoodies.forms.internal.FocusTraversalUtilsAccessor;
import com.jgoodies.forms.internal.InternalFocusSetupUtils;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.LayoutMap;
import com.jgoodies.forms.layout.RowSpec;
import com.jgoodies.forms.util.FocusTraversalType;
import java.awt.Color;
import java.awt.Component;
import java.awt.ComponentOrientation;
import java.awt.FocusTraversalPolicy;
import java.awt.LayoutManager;
import java.lang.ref.WeakReference;
import javax.swing.AbstractButton;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTree;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;

public class FormBuilder {
    private static final String LABELED_BY_PROPERTY = "labeledBy";
    private ColumnSpec[] columnSpecs;
    private boolean debug;
    private LabelType defaultLabelType = LabelType.DEFAULT;
    private ComponentFactory factory;
    private FocusTraversalPolicy focusTraversalPolicy;
    private FocusTraversalType focusTraversalType;
    private JComponent initialComponent;
    private boolean labelForFeatureEnabled;
    private FormLayout layout;
    private LayoutMap layoutMap;
    private WeakReference mostRecentlyAddedLabelReference = null;
    private int offsetX = 0;
    private int offsetY = 0;
    private JPanel panel;
    private RowSpec[] rowSpecs;

    public interface FormBuildingView {
        void buildInto(FormBuilder formBuilder);
    }

    public enum LabelType {
        DEFAULT,
        READ_ONLY
    }

    protected FormBuilder() {
        labelForFeatureEnabled(FormsSetup.getLabelForFeatureEnabledDefault());
        this.offsetX = 0;
        this.offsetY = 0;
    }

    public static FormBuilder create() {
        return new FormBuilder();
    }

    public JPanel build() {
        return getPanel();
    }

    public FormBuilder layoutMap(LayoutMap layoutMap2) {
        this.layoutMap = layoutMap2;
        return this;
    }

    public FormBuilder columns(String encodedColumnSpecs, Object... args) {
        this.columnSpecs = ColumnSpec.decodeSpecs(Strings.get(encodedColumnSpecs, args), getLayoutMap());
        return this;
    }

    public FormBuilder appendColumns(String encodedColumnSpecs, Object... args) {
        for (ColumnSpec columnSpec : ColumnSpec.decodeSpecs(Strings.get(encodedColumnSpecs, args), getLayoutMap())) {
            getLayout().appendColumn(columnSpec);
        }
        return this;
    }

    public FormBuilder rows(String encodedRowSpecs, Object... args) {
        this.rowSpecs = RowSpec.decodeSpecs(Strings.get(encodedRowSpecs, args), getLayoutMap());
        return this;
    }

    public FormBuilder appendRows(String encodedRowSpecs, Object... args) {
        for (RowSpec rowSpec : RowSpec.decodeSpecs(Strings.get(encodedRowSpecs, args), getLayoutMap())) {
            getLayout().appendRow(rowSpec);
        }
        return this;
    }

    public FormBuilder columnGroup(int... columnIndices) {
        getLayout().setColumnGroup(columnIndices);
        return this;
    }

    public FormBuilder columnGroups(int[]... multipleColumnGroups) {
        getLayout().setColumnGroups(multipleColumnGroups);
        return this;
    }

    public FormBuilder rowGroup(int... rowIndices) {
        getLayout().setRowGroup(rowIndices);
        return this;
    }

    public FormBuilder rowGroups(int[]... multipleRowGroups) {
        getLayout().setRowGroups(multipleRowGroups);
        return this;
    }

    public FormBuilder honorsVisibility(boolean b) {
        getLayout().setHonorsVisibility(b);
        return this;
    }

    public FormBuilder honorsVisibility(JComponent c, boolean b) {
        getLayout().setHonorsVisibility(c, Boolean.valueOf(b));
        return this;
    }

    public FormBuilder layout(FormLayout layout2) {
        this.layout = (FormLayout) Preconditions.checkNotNull(layout2, Messages.MUST_NOT_BE_NULL, "layout");
        return this;
    }

    public FormBuilder panel(JPanel panel2) {
        this.panel = (JPanel) Preconditions.checkNotNull(panel2, Messages.MUST_NOT_BE_NULL, "panel");
        this.panel.setLayout(getLayout());
        return this;
    }

    public FormBuilder debug(boolean b) {
        this.debug = b;
        return this;
    }

    public FormBuilder name(String panelName) {
        getPanel().setName(panelName);
        return this;
    }

    public FormBuilder background(Color background) {
        getPanel().setBackground(background);
        opaque(true);
        return this;
    }

    public FormBuilder border(Border border) {
        getPanel().setBorder(border);
        return this;
    }

    @Deprecated
    public FormBuilder border(String paddingSpec) {
        return padding(paddingSpec, new Object[0]);
    }

    public FormBuilder padding(EmptyBorder padding) {
        getPanel().setBorder(padding);
        return this;
    }

    public FormBuilder padding(String paddingSpec, Object... args) {
        padding(Paddings.createPadding(paddingSpec, args));
        return this;
    }

    public FormBuilder opaque(boolean b) {
        getPanel().setOpaque(b);
        return this;
    }

    public FormBuilder initialComponent(JComponent initialComponent2) {
        Preconditions.checkState(this.initialComponent == null, "The initial component must be set once only.");
        checkValidFocusTraversalSetup();
        this.initialComponent = initialComponent2;
        setupFocusTraversalPolicyAndProvider();
        return this;
    }

    public FormBuilder focusTraversalType(FocusTraversalType focusTraversalType2) {
        boolean z = true;
        Preconditions.checkNotNull(focusTraversalType2, Messages.MUST_NOT_BE_NULL, "focus traversal type");
        if (this.focusTraversalType != null) {
            z = false;
        }
        Preconditions.checkState(z, "The focus traversal type must be set once only.");
        checkValidFocusTraversalSetup();
        this.focusTraversalType = focusTraversalType2;
        setupFocusTraversalPolicyAndProvider();
        return this;
    }

    public FormBuilder focusTraversalPolicy(FocusTraversalPolicy policy) {
        boolean z = true;
        Preconditions.checkNotNull(policy, Messages.MUST_NOT_BE_NULL, "focus traversal policy");
        if (this.focusTraversalPolicy != null) {
            z = false;
        }
        Preconditions.checkState(z, "The focus traversal policy must be set once only.");
        checkValidFocusTraversalSetup();
        this.focusTraversalPolicy = policy;
        setupFocusTraversalPolicyAndProvider();
        return this;
    }

    public FormBuilder focusGroup(AbstractButton... buttons) {
        FocusTraversalUtilsAccessor.tryToBuildAFocusGroup(buttons);
        return this;
    }

    public JPanel getPanel() {
        if (this.panel == null) {
            this.panel = this.debug ? new FormDebugPanel() : new JPanel((LayoutManager) null);
            this.panel.setOpaque(FormsSetup.getOpaqueDefault());
        }
        return this.panel;
    }

    public FormBuilder factory(ComponentFactory factory2) {
        this.factory = factory2;
        return this;
    }

    public FormBuilder labelForFeatureEnabled(boolean b) {
        this.labelForFeatureEnabled = b;
        return this;
    }

    public FormBuilder offset(int offsetX2, int offsetY2) {
        this.offsetX = offsetX2;
        this.offsetY = offsetY2;
        return this;
    }

    public FormBuilder translate(int dX, int dY) {
        this.offsetX += dX;
        this.offsetY += dY;
        return this;
    }

    public FormBuilder defaultLabelType(LabelType newValue) {
        this.defaultLabelType = newValue;
        return this;
    }

    public ComponentAdder add(Component c) {
        return add(true, c);
    }

    public ComponentAdder addRaw(Component c) {
        return addRaw(true, c);
    }

    public ComponentAdder addScrolled(Component c) {
        return addScrolled(true, c);
    }

    public ComponentAdder addBar(JButton... buttons) {
        return addBar(true, buttons);
    }

    public ComponentAdder addBar(JCheckBox... checkBoxes) {
        return addBar(true, checkBoxes);
    }

    public ComponentAdder addBar(JRadioButton... radioButtons) {
        return addBar(true, radioButtons);
    }

    public ComponentAdder addStack(JButton... buttons) {
        return addStack(true, buttons);
    }

    public ComponentAdder addStack(JCheckBox... checkBoxes) {
        return addStack(true, checkBoxes);
    }

    public ComponentAdder addStack(JRadioButton... radioButtons) {
        return addStack(true, radioButtons);
    }

    public ViewAdder add(FormBuildingView view) {
        return add(true, view);
    }

    public ComponentAdder add(String markedLabelText, Object... args) {
        return add(true, markedLabelText, args);
    }

    public ComponentAdder addLabel(String markedText, Object... args) {
        return addLabel(true, markedText, args);
    }

    public ComponentAdder addROLabel(String markedText, Object... args) {
        return addROLabel(true, markedText, args);
    }

    public ComponentAdder addTitle(String markedText, Object... args) {
        return addTitle(true, markedText, args);
    }

    public ComponentAdder addSeparator(String markedText, Object... args) {
        return addSeparator(true, markedText, args);
    }

    public ComponentAdder add(Icon image) {
        return add(true, image);
    }

    public ComponentAdder add(boolean expression, Component c) {
        if (!expression || c == null) {
            return new NoOpComponentAdder(this);
        }
        if ((c instanceof JTable) || (c instanceof JList) || (c instanceof JTree)) {
            return addScrolled(expression, c);
        }
        return addRaw(expression, c);
    }

    public ComponentAdder addRaw(boolean expression, Component c) {
        if (!expression || c == null) {
            return new NoOpComponentAdder(this);
        }
        return addImpl(c);
    }

    public ComponentAdder addScrolled(boolean expression, Component c) {
        if (!expression || c == null) {
            return new NoOpComponentAdder(this);
        }
        return addImpl(new JScrollPane(c));
    }

    public ComponentAdder addBar(boolean expression, JButton... buttons) {
        if (!expression || buttons == null) {
            return new NoOpComponentAdder(this);
        }
        return addImpl(Forms.buttonBar(buttons));
    }

    public ComponentAdder addBar(boolean expression, JCheckBox... checkBoxes) {
        if (!expression) {
            return new NoOpComponentAdder(this);
        }
        return addImpl(Forms.checkBoxBar(checkBoxes));
    }

    public ComponentAdder addBar(boolean expression, JRadioButton... radioButtons) {
        if (!expression) {
            return new NoOpComponentAdder(this);
        }
        return addImpl(Forms.radioButtonBar(radioButtons));
    }

    public ComponentAdder addStack(boolean expression, JButton... buttons) {
        if (!expression || buttons == null) {
            return new NoOpComponentAdder(this);
        }
        return addImpl(Forms.buttonStack(buttons));
    }

    public ComponentAdder addStack(boolean expression, JCheckBox... checkBoxes) {
        if (!expression) {
            return new NoOpComponentAdder(this);
        }
        return addImpl(Forms.checkBoxStack(checkBoxes));
    }

    public ComponentAdder addStack(boolean expression, JRadioButton... radioButtons) {
        if (!expression || radioButtons == null) {
            return new NoOpComponentAdder(this);
        }
        return addImpl(Forms.radioButtonStack(radioButtons));
    }

    public ViewAdder add(boolean expression, FormBuildingView view) {
        return new ViewAdder(this, expression, view);
    }

    public ComponentAdder add(boolean expression, String markedLabelText, Object... args) {
        return this.defaultLabelType == LabelType.DEFAULT ? addLabel(expression, markedLabelText, args) : addROLabel(expression, markedLabelText, args);
    }

    public ComponentAdder addLabel(boolean expression, String markedText, Object... args) {
        return addRaw(expression, getFactory().createLabel(Strings.get(markedText, args)));
    }

    public ComponentAdder addROLabel(boolean expression, String markedText, Object... args) {
        return addRaw(expression, getFactory().createReadOnlyLabel(Strings.get(markedText, args)));
    }

    public ComponentAdder addTitle(boolean expression, String markedText, Object... args) {
        return addRaw(expression, getFactory().createTitle(Strings.get(markedText, args)));
    }

    public ComponentAdder addSeparator(boolean expression, String markedText, Object... args) {
        return addRaw(expression, getFactory().createSeparator(Strings.get(markedText, args), isLeftToRight() ? 2 : 4));
    }

    public ComponentAdder add(boolean expression, Icon image) {
        if (!expression || image == null) {
            return new NoOpComponentAdder(this);
        }
        return addImpl(new JLabel(image));
    }

    /* access modifiers changed from: protected */
    public LayoutMap getLayoutMap() {
        if (this.layoutMap == null) {
            this.layoutMap = LayoutMap.getRoot();
        }
        return this.layoutMap;
    }

    /* access modifiers changed from: protected */
    public FormLayout getLayout() {
        if (this.layout != null) {
            return this.layout;
        }
        Preconditions.checkNotNull(this.columnSpecs, "The layout columns must be specified.");
        Preconditions.checkNotNull(this.rowSpecs, "The layout rows must be specified.");
        this.layout = new FormLayout(this.columnSpecs, this.rowSpecs);
        return this.layout;
    }

    /* access modifiers changed from: protected */
    public ComponentFactory getFactory() {
        if (this.factory == null) {
            this.factory = FormsSetup.getComponentFactoryDefault();
        }
        return this.factory;
    }

    /* access modifiers changed from: protected */
    public ComponentAdder addImpl(Component c) {
        if (getPanel().getLayout() == null) {
            this.panel.setLayout(getLayout());
        }
        return new ComponentAdder(this, c);
    }

    /* access modifiers changed from: package-private */
    public void addImpl(Component component, CellConstraints rawConstraints) {
        getPanel().add(component, rawConstraints.translate(this.offsetX, this.offsetY));
        manageLabelsAndComponents(component);
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

    private static boolean isLabelForApplicable(JLabel label, Component component) {
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

    private static void setLabelFor(JLabel label, Component component) {
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

    private boolean isLeftToRight() {
        ComponentOrientation orientation = getPanel().getComponentOrientation();
        return orientation.isLeftToRight() || !orientation.isHorizontal();
    }

    private void checkValidFocusTraversalSetup() {
        InternalFocusSetupUtils.checkValidFocusTraversalSetup(this.focusTraversalPolicy, this.focusTraversalType, this.initialComponent);
    }

    private void setupFocusTraversalPolicyAndProvider() {
        InternalFocusSetupUtils.setupFocusTraversalPolicyAndProvider(getPanel(), this.focusTraversalPolicy, this.focusTraversalType, this.initialComponent);
    }

    public static final class ViewAdder {
        private final FormBuilder builder;
        private final boolean expression;
        private final FormBuildingView view;

        ViewAdder(FormBuilder builder2, boolean expression2, FormBuildingView view2) {
            this.builder = builder2;
            this.expression = expression2;
            this.view = view2;
        }

        public FormBuilder xy(int col, int row) {
            if (this.expression && this.view != null) {
                this.builder.translate(col, row);
                this.view.buildInto(this.builder);
                this.builder.translate(-col, -row);
            }
            return this.builder;
        }
    }

    public static class ComponentAdder {
        protected final FormBuilder builder;
        private final Component component;
        private boolean labelForSet = false;

        ComponentAdder(FormBuilder builder2, Component component2) {
            this.builder = builder2;
            this.component = component2;
        }

        public final ComponentAdder labelFor(Component c) {
            Preconditions.checkArgument(this.component instanceof JLabel, "#labelFor is applicable only to JLabels");
            Preconditions.checkArgument(!this.labelForSet, "You must set the label-for-relation only once.");
            this.component.setLabelFor(c);
            this.labelForSet = true;
            return this;
        }

        public final FormBuilder at(CellConstraints constraints) {
            return add(constraints);
        }

        public final FormBuilder xy(int col, int row) {
            return at(CC.xy(col, row));
        }

        public final FormBuilder xy(int col, int row, String encodedAlignments) {
            return at(CC.xy(col, row, encodedAlignments));
        }

        public final FormBuilder xy(int col, int row, CellConstraints.Alignment colAlign, CellConstraints.Alignment rowAlign) {
            return at(CC.xy(col, row, colAlign, rowAlign));
        }

        public final FormBuilder xyw(int col, int row, int colSpan) {
            return at(CC.xyw(col, row, colSpan));
        }

        public final FormBuilder xyw(int col, int row, int colSpan, String encodedAlignments) {
            return at(CC.xyw(col, row, colSpan, encodedAlignments));
        }

        public final FormBuilder xyw(int col, int row, int colSpan, CellConstraints.Alignment colAlign, CellConstraints.Alignment rowAlign) {
            return at(CC.xyw(col, row, colSpan, colAlign, rowAlign));
        }

        public final FormBuilder xywh(int col, int row, int colSpan, int rowSpan) {
            return at(CC.xywh(col, row, colSpan, rowSpan));
        }

        public final FormBuilder xywh(int col, int row, int colSpan, int rowSpan, String encodedAlignments) {
            return at(CC.xywh(col, row, colSpan, rowSpan, encodedAlignments));
        }

        public final FormBuilder xywh(int col, int row, int colSpan, int rowSpan, CellConstraints.Alignment colAlign, CellConstraints.Alignment rowAlign) {
            return at(CC.xywh(col, row, colSpan, rowSpan, colAlign, rowAlign));
        }

        public final FormBuilder rc(int row, int col) {
            return at(CC.rc(row, col));
        }

        public final FormBuilder rc(int row, int col, String encodedAlignments) {
            return at(CC.rc(row, col, encodedAlignments));
        }

        public final FormBuilder rc(int row, int col, CellConstraints.Alignment rowAlign, CellConstraints.Alignment colAlign) {
            return at(CC.rc(row, col, rowAlign, colAlign));
        }

        public final FormBuilder rcw(int row, int col, int colSpan) {
            return at(CC.rcw(row, col, colSpan));
        }

        public final FormBuilder rcw(int row, int col, int colSpan, String encodedAlignments) {
            return at(CC.rcw(row, col, colSpan, encodedAlignments));
        }

        public final FormBuilder rcw(int row, int col, int colSpan, CellConstraints.Alignment rowAlign, CellConstraints.Alignment colAlign) {
            return at(CC.rcw(row, col, colSpan, rowAlign, colAlign));
        }

        public final FormBuilder rchw(int row, int col, int rowSpan, int colSpan) {
            return at(CC.rchw(row, col, rowSpan, colSpan));
        }

        public final FormBuilder rchw(int row, int col, int rowSpan, int colSpan, String encodedAlignments) {
            return at(CC.rchw(row, col, rowSpan, colSpan, encodedAlignments));
        }

        public final FormBuilder rchw(int row, int col, int rowSpan, int colSpan, CellConstraints.Alignment rowAlign, CellConstraints.Alignment colAlign) {
            return at(CC.rchw(col, row, rowSpan, colSpan, colAlign, rowAlign));
        }

        /* access modifiers changed from: protected */
        public FormBuilder add(CellConstraints constraints) {
            this.builder.addImpl(this.component, constraints);
            return this.builder;
        }
    }

    /* access modifiers changed from: private */
    public static final class NoOpComponentAdder extends ComponentAdder {
        NoOpComponentAdder(FormBuilder builder) {
            super(builder, null);
        }

        /* access modifiers changed from: protected */
        @Override // com.jgoodies.forms.builder.FormBuilder.ComponentAdder
        public FormBuilder add(CellConstraints constraints) {
            return this.builder;
        }
    }
}
