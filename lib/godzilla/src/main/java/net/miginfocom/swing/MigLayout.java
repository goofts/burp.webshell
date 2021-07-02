package net.miginfocom.swing;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.LayoutManager;
import java.awt.LayoutManager2;
import java.awt.Point;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.io.ObjectStreamException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.IdentityHashMap;
import java.util.Iterator;
import java.util.Map;
import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.JEditorPane;
import javax.swing.JPopupMenu;
import javax.swing.JTextArea;
import javax.swing.OverlayLayout;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import net.miginfocom.layout.AC;
import net.miginfocom.layout.BoundSize;
import net.miginfocom.layout.CC;
import net.miginfocom.layout.ComponentWrapper;
import net.miginfocom.layout.ConstraintParser;
import net.miginfocom.layout.ContainerWrapper;
import net.miginfocom.layout.Grid;
import net.miginfocom.layout.LC;
import net.miginfocom.layout.LayoutCallback;
import net.miginfocom.layout.LayoutUtil;
import net.miginfocom.layout.PlatformDefaults;
import net.miginfocom.layout.UnitValue;

public class MigLayout implements LayoutManager2, Externalizable {
    private transient ContainerWrapper cacheParentW;
    private transient ArrayList<LayoutCallback> callbackList;
    private final transient Map<ComponentWrapper, CC> ccMap;
    private Object colConstraints;
    private transient AC colSpecs;
    private transient Timer debugTimer;
    private transient boolean dirty;
    private transient Grid grid;
    private transient int lastHash;
    private transient Dimension lastInvalidSize;
    private transient int lastModCount;
    private transient Dimension lastParentSize;
    private long lastSize;
    private transient boolean lastWasInvalid;
    private Object layoutConstraints;
    private transient LC lc;
    private Object rowConstraints;
    private transient AC rowSpecs;
    private final Map<Component, Object> scrConstrMap;

    public MigLayout() {
        this("", "", "");
    }

    public MigLayout(String layoutConstraints2) {
        this(layoutConstraints2, "", "");
    }

    public MigLayout(String layoutConstraints2, String colConstraints2) {
        this(layoutConstraints2, colConstraints2, "");
    }

    public MigLayout(String layoutConstraints2, String colConstraints2, String rowConstraints2) {
        this.scrConstrMap = new IdentityHashMap(8);
        this.layoutConstraints = "";
        this.colConstraints = "";
        this.rowConstraints = "";
        this.cacheParentW = null;
        this.ccMap = new HashMap(8);
        this.debugTimer = null;
        this.lc = null;
        this.colSpecs = null;
        this.rowSpecs = null;
        this.grid = null;
        this.lastModCount = PlatformDefaults.getModCount();
        this.lastHash = -1;
        this.lastInvalidSize = null;
        this.lastWasInvalid = false;
        this.lastParentSize = null;
        this.callbackList = null;
        this.dirty = true;
        this.lastSize = 0;
        setLayoutConstraints(layoutConstraints2);
        setColumnConstraints(colConstraints2);
        setRowConstraints(rowConstraints2);
    }

    public MigLayout(LC layoutConstraints2) {
        this(layoutConstraints2, (AC) null, (AC) null);
    }

    public MigLayout(LC layoutConstraints2, AC colConstraints2) {
        this(layoutConstraints2, colConstraints2, (AC) null);
    }

    public MigLayout(LC layoutConstraints2, AC colConstraints2, AC rowConstraints2) {
        this.scrConstrMap = new IdentityHashMap(8);
        this.layoutConstraints = "";
        this.colConstraints = "";
        this.rowConstraints = "";
        this.cacheParentW = null;
        this.ccMap = new HashMap(8);
        this.debugTimer = null;
        this.lc = null;
        this.colSpecs = null;
        this.rowSpecs = null;
        this.grid = null;
        this.lastModCount = PlatformDefaults.getModCount();
        this.lastHash = -1;
        this.lastInvalidSize = null;
        this.lastWasInvalid = false;
        this.lastParentSize = null;
        this.callbackList = null;
        this.dirty = true;
        this.lastSize = 0;
        setLayoutConstraints(layoutConstraints2);
        setColumnConstraints(colConstraints2);
        setRowConstraints(rowConstraints2);
    }

    public Object getLayoutConstraints() {
        return this.layoutConstraints;
    }

    public void setLayoutConstraints(Object constr) {
        if (constr == null || (constr instanceof String)) {
            constr = ConstraintParser.prepare((String) constr);
            this.lc = ConstraintParser.parseLayoutConstraint((String) constr);
        } else if (constr instanceof LC) {
            this.lc = (LC) constr;
        } else {
            throw new IllegalArgumentException("Illegal constraint type: " + constr.getClass().toString());
        }
        this.layoutConstraints = constr;
        this.dirty = true;
    }

    public Object getColumnConstraints() {
        return this.colConstraints;
    }

    public void setColumnConstraints(Object constr) {
        if (constr == null || (constr instanceof String)) {
            constr = ConstraintParser.prepare((String) constr);
            this.colSpecs = ConstraintParser.parseColumnConstraints((String) constr);
        } else if (constr instanceof AC) {
            this.colSpecs = (AC) constr;
        } else {
            throw new IllegalArgumentException("Illegal constraint type: " + constr.getClass().toString());
        }
        this.colConstraints = constr;
        this.dirty = true;
    }

    public Object getRowConstraints() {
        return this.rowConstraints;
    }

    public void setRowConstraints(Object constr) {
        if (constr == null || (constr instanceof String)) {
            constr = ConstraintParser.prepare((String) constr);
            this.rowSpecs = ConstraintParser.parseRowConstraints((String) constr);
        } else if (constr instanceof AC) {
            this.rowSpecs = (AC) constr;
        } else {
            throw new IllegalArgumentException("Illegal constraint type: " + constr.getClass().toString());
        }
        this.rowConstraints = constr;
        this.dirty = true;
    }

    public Map<Component, Object> getConstraintMap() {
        return new IdentityHashMap(this.scrConstrMap);
    }

    public void setConstraintMap(Map<Component, Object> map) {
        this.scrConstrMap.clear();
        this.ccMap.clear();
        for (Map.Entry<Component, Object> e : map.entrySet()) {
            setComponentConstraintsImpl(e.getKey(), e.getValue(), true);
        }
    }

    public Object getComponentConstraints(Component comp) {
        Object obj;
        synchronized (comp.getParent().getTreeLock()) {
            obj = this.scrConstrMap.get(comp);
        }
        return obj;
    }

    public void setComponentConstraints(Component comp, Object constr) {
        setComponentConstraintsImpl(comp, constr, false);
    }

    private void setComponentConstraintsImpl(Component comp, Object constr, boolean noCheck) {
        Container parent = comp.getParent();
        synchronized ((parent != null ? parent.getTreeLock() : new Object())) {
            if (!noCheck) {
                if (!this.scrConstrMap.containsKey(comp)) {
                    throw new IllegalArgumentException("Component must already be added to parent!");
                }
            }
            ComponentWrapper cw = new SwingComponentWrapper(comp);
            if (constr == null || (constr instanceof String)) {
                String cStr = ConstraintParser.prepare((String) constr);
                this.scrConstrMap.put(comp, constr);
                this.ccMap.put(cw, ConstraintParser.parseComponentConstraint(cStr));
            } else if (constr instanceof CC) {
                this.scrConstrMap.put(comp, constr);
                this.ccMap.put(cw, (CC) constr);
            } else {
                throw new IllegalArgumentException("Constraint must be String or ComponentConstraint: " + constr.getClass().toString());
            }
            this.dirty = true;
        }
    }

    public boolean isManagingComponent(Component c) {
        return this.scrConstrMap.containsKey(c);
    }

    public void addLayoutCallback(LayoutCallback callback) {
        if (callback == null) {
            throw new NullPointerException();
        }
        if (this.callbackList == null) {
            this.callbackList = new ArrayList<>(1);
        }
        this.callbackList.add(callback);
        this.grid = null;
    }

    public void removeLayoutCallback(LayoutCallback callback) {
        if (this.callbackList != null) {
            this.callbackList.remove(callback);
        }
    }

    private void setDebug(ComponentWrapper parentW, boolean b) {
        final Component parent;
        if (b && (this.debugTimer == null || this.debugTimer.getDelay() != getDebugMillis())) {
            if (this.debugTimer != null) {
                this.debugTimer.stop();
            }
            ContainerWrapper pCW = parentW.getParent();
            if (pCW != null) {
                parent = (Component) pCW.getComponent();
            } else {
                parent = null;
            }
            this.debugTimer = new Timer(getDebugMillis(), new MyDebugRepaintListener());
            if (parent != null) {
                SwingUtilities.invokeLater(new Runnable() {
                    /* class net.miginfocom.swing.MigLayout.AnonymousClass1 */

                    public void run() {
                        Container p = parent.getParent();
                        if (p == null) {
                            return;
                        }
                        if (p instanceof JComponent) {
                            ((JComponent) p).revalidate();
                            return;
                        }
                        parent.invalidate();
                        p.validate();
                    }
                });
            }
            this.debugTimer.setInitialDelay(100);
            this.debugTimer.start();
        } else if (!b && this.debugTimer != null) {
            this.debugTimer.stop();
            this.debugTimer = null;
        }
    }

    private boolean getDebug() {
        return this.debugTimer != null;
    }

    private int getDebugMillis() {
        int globalDebugMillis = LayoutUtil.getGlobalDebugMillis();
        return globalDebugMillis > 0 ? globalDebugMillis : this.lc.getDebugMillis();
    }

    private void checkCache(Container parent) {
        boolean z = true;
        if (parent != null) {
            if (this.dirty) {
                this.grid = null;
            }
            cleanConstraintMaps(parent);
            int mc = PlatformDefaults.getModCount();
            if (this.lastModCount != mc) {
                this.grid = null;
                this.lastModCount = mc;
            }
            if (parent.isValid()) {
                this.lastWasInvalid = false;
            } else if (!this.lastWasInvalid) {
                this.lastWasInvalid = true;
                int hash = 0;
                boolean resetLastInvalidOnParent = false;
                for (ComponentWrapper wrapper : this.ccMap.keySet()) {
                    Object component = wrapper.getComponent();
                    if ((component instanceof JTextArea) || (component instanceof JEditorPane)) {
                        resetLastInvalidOnParent = true;
                    }
                    hash = (hash ^ wrapper.getLayoutHashCode()) + 285134905;
                }
                if (resetLastInvalidOnParent) {
                    resetLastInvalidOnParent(parent);
                }
                if (hash != this.lastHash) {
                    this.grid = null;
                    this.lastHash = hash;
                }
                Dimension ps = parent.getSize();
                if (this.lastInvalidSize == null || !this.lastInvalidSize.equals(ps)) {
                    this.grid = null;
                    this.lastInvalidSize = ps;
                }
            }
            ContainerWrapper par = checkParent(parent);
            if (getDebugMillis() <= 0) {
                z = false;
            }
            setDebug(par, z);
            if (this.grid == null) {
                this.grid = new Grid(par, this.lc, this.rowSpecs, this.colSpecs, this.ccMap, this.callbackList);
            }
            this.dirty = false;
        }
    }

    private void cleanConstraintMaps(Container parent) {
        HashSet<Component> parentCompSet = new HashSet<>(Arrays.asList(parent.getComponents()));
        Iterator<Map.Entry<ComponentWrapper, CC>> it = this.ccMap.entrySet().iterator();
        while (it.hasNext()) {
            Component c = (Component) it.next().getKey().getComponent();
            if (!parentCompSet.contains(c)) {
                it.remove();
                this.scrConstrMap.remove(c);
            }
        }
    }

    private void resetLastInvalidOnParent(Container parent) {
        while (parent != null) {
            LayoutManager layoutManager = parent.getLayout();
            if (layoutManager instanceof MigLayout) {
                ((MigLayout) layoutManager).lastWasInvalid = false;
            }
            parent = parent.getParent();
        }
    }

    private ContainerWrapper checkParent(Container parent) {
        if (parent == null) {
            return null;
        }
        if (this.cacheParentW == null || this.cacheParentW.getComponent() != parent) {
            this.cacheParentW = new SwingContainerWrapper(parent);
        }
        return this.cacheParentW;
    }

    public void layoutContainer(Container parent) {
        synchronized (parent.getTreeLock()) {
            checkCache(parent);
            Insets i = parent.getInsets();
            int[] b = {i.left, i.top, (parent.getWidth() - i.left) - i.right, (parent.getHeight() - i.top) - i.bottom};
            if (this.grid.layout(b, this.lc.getAlignX(), this.lc.getAlignY(), getDebug())) {
                this.grid = null;
                checkCache(parent);
                this.grid.layout(b, this.lc.getAlignX(), this.lc.getAlignY(), getDebug());
            }
            long newSize = ((long) this.grid.getHeight()[1]) + (((long) this.grid.getWidth()[1]) << 32);
            if (this.lastSize != newSize) {
                this.lastSize = newSize;
                final ContainerWrapper containerWrapper = checkParent(parent);
                Window win = SwingUtilities.getAncestorOfClass(Window.class, (Component) containerWrapper.getComponent());
                if (win != null) {
                    if (win.isVisible()) {
                        SwingUtilities.invokeLater(new Runnable() {
                            /* class net.miginfocom.swing.MigLayout.AnonymousClass2 */

                            public void run() {
                                MigLayout.this.adjustWindowSize(containerWrapper);
                            }
                        });
                    } else {
                        adjustWindowSize(containerWrapper);
                    }
                }
            }
            this.lastInvalidSize = null;
        }
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private void adjustWindowSize(ContainerWrapper parent) {
        Container packable;
        BoundSize wBounds = this.lc.getPackWidth();
        BoundSize hBounds = this.lc.getPackHeight();
        if (!((wBounds == BoundSize.NULL_SIZE && hBounds == BoundSize.NULL_SIZE) || (packable = getPackable((Component) parent.getComponent())) == null)) {
            Container pc = (Component) parent.getComponent();
            for (Container c = pc instanceof Container ? pc : pc.getParent(); c != null; c = c.getParent()) {
                LayoutManager layout = c.getLayout();
                if ((layout instanceof BoxLayout) || (layout instanceof OverlayLayout)) {
                    ((LayoutManager2) layout).invalidateLayout(c);
                }
            }
            Dimension prefSize = packable.getPreferredSize();
            int targW = constrain(checkParent(packable), packable.getWidth(), prefSize.width, wBounds);
            int targH = constrain(checkParent(packable), packable.getHeight(), prefSize.height, hBounds);
            Point p = packable.isShowing() ? packable.getLocationOnScreen() : packable.getLocation();
            int x = Math.round(((float) p.x) - (((float) (targW - packable.getWidth())) * (1.0f - this.lc.getPackWidthAlign())));
            int y = Math.round(((float) p.y) - (((float) (targH - packable.getHeight())) * (1.0f - this.lc.getPackHeightAlign())));
            if (packable instanceof JPopupMenu) {
                JPopupMenu popupMenu = (JPopupMenu) packable;
                popupMenu.setVisible(false);
                popupMenu.setPopupSize(targW, targH);
                Component invoker = popupMenu.getInvoker();
                Point popPoint = new Point(x, y);
                SwingUtilities.convertPointFromScreen(popPoint, invoker);
                ((JPopupMenu) packable).show(invoker, popPoint.x, popPoint.y);
                packable.setPreferredSize((Dimension) null);
                return;
            }
            packable.setBounds(x, y, targW, targH);
        }
    }

    private Container getPackable(Component comp) {
        Container popup = (JPopupMenu) findType(JPopupMenu.class, comp);
        if (popup == null) {
            return (Container) findType(Window.class, comp);
        }
        for (Container popupComp = popup; popupComp != null; popupComp = popupComp.getParent()) {
            if (popupComp.getClass().getName().contains("HeavyWeightWindow")) {
                return popupComp;
            }
        }
        return popup;
    }

    public static <E> E findType(Class<E> clazz, Component comp) {
        while (comp != null && !clazz.isInstance(comp)) {
            comp = (E) comp.getParent();
        }
        return (E) comp;
    }

    private int constrain(ContainerWrapper parent, int winSize, int prefSize, BoundSize constrain) {
        if (constrain == null) {
            return winSize;
        }
        int retSize = winSize;
        UnitValue wUV = constrain.getPreferred();
        if (wUV != null) {
            retSize = wUV.getPixels((float) prefSize, parent, parent);
        }
        int retSize2 = constrain.constrain(retSize, (float) prefSize, parent);
        if (constrain.getGapPush()) {
            retSize2 = Math.max(winSize, retSize2);
        }
        return retSize2;
    }

    public Dimension minimumLayoutSize(Container parent) {
        Dimension sizeImpl;
        synchronized (parent.getTreeLock()) {
            sizeImpl = getSizeImpl(parent, 0);
        }
        return sizeImpl;
    }

    public Dimension preferredLayoutSize(Container parent) {
        Dimension sizeImpl;
        synchronized (parent.getTreeLock()) {
            if (this.lastParentSize == null || !parent.getSize().equals(this.lastParentSize)) {
                Iterator<ComponentWrapper> it = this.ccMap.keySet().iterator();
                while (true) {
                    if (it.hasNext()) {
                        if (it.next().getContentBias() != -1) {
                            layoutContainer(parent);
                            break;
                        }
                    } else {
                        break;
                    }
                }
            }
            this.lastParentSize = parent.getSize();
            sizeImpl = getSizeImpl(parent, 1);
        }
        return sizeImpl;
    }

    public Dimension maximumLayoutSize(Container parent) {
        return new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE);
    }

    private Dimension getSizeImpl(Container parent, int sizeType) {
        int[] iArr;
        int[] iArr2 = null;
        checkCache(parent);
        Insets i = parent.getInsets();
        if (this.grid != null) {
            iArr = this.grid.getWidth();
        } else {
            iArr = null;
        }
        int w = LayoutUtil.getSizeSafe(iArr, sizeType) + i.left + i.right;
        if (this.grid != null) {
            iArr2 = this.grid.getHeight();
        }
        return new Dimension(w, LayoutUtil.getSizeSafe(iArr2, sizeType) + i.top + i.bottom);
    }

    public float getLayoutAlignmentX(Container parent) {
        if (this.lc == null || this.lc.getAlignX() == null) {
            return 0.0f;
        }
        return (float) this.lc.getAlignX().getPixels(1.0f, checkParent(parent), null);
    }

    public float getLayoutAlignmentY(Container parent) {
        if (this.lc == null || this.lc.getAlignY() == null) {
            return 0.0f;
        }
        return (float) this.lc.getAlignY().getPixels(1.0f, checkParent(parent), null);
    }

    public void addLayoutComponent(String s, Component comp) {
        addLayoutComponent(comp, s);
    }

    public void addLayoutComponent(Component comp, Object constraints) {
        synchronized (comp.getParent().getTreeLock()) {
            setComponentConstraintsImpl(comp, constraints, true);
        }
    }

    public void removeLayoutComponent(Component comp) {
        synchronized (comp.getParent().getTreeLock()) {
            this.scrConstrMap.remove(comp);
            this.ccMap.remove(new SwingComponentWrapper(comp));
            this.grid = null;
        }
    }

    public void invalidateLayout(Container target) {
        this.dirty = true;
    }

    private Object readResolve() throws ObjectStreamException {
        return LayoutUtil.getSerializedObject(this);
    }

    @Override // java.io.Externalizable
    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        LayoutUtil.setSerializedObject(this, LayoutUtil.readAsXML(in));
    }

    @Override // java.io.Externalizable
    public void writeExternal(ObjectOutput out) throws IOException {
        if (getClass() == MigLayout.class) {
            LayoutUtil.writeAsXML(out, this);
        }
    }

    /* access modifiers changed from: private */
    public class MyDebugRepaintListener implements ActionListener {
        private MyDebugRepaintListener() {
        }

        public void actionPerformed(ActionEvent e) {
            if (MigLayout.this.grid == null || !((Component) MigLayout.this.grid.getContainer().getComponent()).isShowing()) {
                MigLayout.this.debugTimer.stop();
                MigLayout.this.debugTimer = null;
                return;
            }
            MigLayout.this.grid.paintDebug();
        }
    }
}
