package com.formdev.flatlaf;

import com.formdev.flatlaf.util.SystemInfo;
import java.awt.Component;
import java.awt.Container;
import java.awt.KeyEventPostProcessor;
import java.awt.KeyboardFocusManager;
import java.awt.Window;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowListener;
import java.lang.ref.WeakReference;
import javax.swing.AbstractButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JRootPane;
import javax.swing.JTabbedPane;
import javax.swing.MenuElement;
import javax.swing.MenuSelectionManager;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/* access modifiers changed from: package-private */
public class MnemonicHandler implements KeyEventPostProcessor, ChangeListener {
    private static int altPressedEventCount;
    private static WeakReference<Window> lastShowMnemonicWindow;
    private static boolean selectMenuOnAltReleased;
    private static boolean showMnemonics;
    private static WindowListener windowListener;

    MnemonicHandler() {
    }

    static boolean isShowMnemonics() {
        return showMnemonics || !UIManager.getBoolean("Component.hideMnemonics");
    }

    /* access modifiers changed from: package-private */
    public void install() {
        KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventPostProcessor(this);
        MenuSelectionManager.defaultManager().addChangeListener(this);
    }

    /* access modifiers changed from: package-private */
    public void uninstall() {
        KeyboardFocusManager.getCurrentKeyboardFocusManager().removeKeyEventPostProcessor(this);
        MenuSelectionManager.defaultManager().removeChangeListener(this);
    }

    public boolean postProcessKeyEvent(KeyEvent e) {
        int keyCode = e.getKeyCode();
        if (SystemInfo.isMacOS) {
            if (keyCode != 17 && keyCode != 18) {
                return false;
            }
            showMnemonics(shouldShowMnemonics(e) && e.isControlDown() && e.isAltDown(), e.getComponent());
            return false;
        } else if (SystemInfo.isWindows) {
            return processKeyEventOnWindows(e);
        } else {
            if (keyCode != 18) {
                return false;
            }
            showMnemonics(shouldShowMnemonics(e), e.getComponent());
            return false;
        }
    }

    private boolean shouldShowMnemonics(KeyEvent e) {
        return e.getID() == 401 || MenuSelectionManager.defaultManager().getSelectedPath().length > 0;
    }

    private boolean processKeyEventOnWindows(KeyEvent e) {
        Window window;
        JMenuBar menuBar;
        JMenu firstMenu = null;
        boolean z = false;
        if (e.getKeyCode() != 18) {
            selectMenuOnAltReleased = false;
            return false;
        } else if (e.getID() == 401) {
            altPressedEventCount++;
            if (altPressedEventCount == 1 && !e.isConsumed()) {
                MenuSelectionManager menuSelectionManager = MenuSelectionManager.defaultManager();
                if (menuSelectionManager.getSelectedPath().length == 0) {
                    z = true;
                }
                selectMenuOnAltReleased = z;
                if (!selectMenuOnAltReleased) {
                    menuSelectionManager.clearSelectedPath();
                }
            }
            showMnemonics(shouldShowMnemonics(e), e.getComponent());
            e.consume();
            return true;
        } else if (e.getID() != 402) {
            return false;
        } else {
            altPressedEventCount = 0;
            boolean mnemonicsShown = false;
            if (selectMenuOnAltReleased && !e.isConsumed()) {
                MenuSelectionManager menuSelectionManager2 = MenuSelectionManager.defaultManager();
                if (menuSelectionManager2.getSelectedPath().length == 0) {
                    Component c = e.getComponent();
                    JRootPane rootPane = SwingUtilities.getRootPane(c);
                    if (rootPane != null) {
                        window = SwingUtilities.getWindowAncestor(rootPane);
                    } else {
                        window = null;
                    }
                    if (rootPane != null) {
                        menuBar = rootPane.getJMenuBar();
                    } else {
                        menuBar = null;
                    }
                    if (menuBar == null && (window instanceof JFrame)) {
                        menuBar = ((JFrame) window).getJMenuBar();
                    }
                    if (menuBar != null) {
                        firstMenu = menuBar.getMenu(0);
                    }
                    if (firstMenu != null) {
                        menuSelectionManager2.setSelectedPath(new MenuElement[]{menuBar, firstMenu});
                        showMnemonics(true, c);
                        mnemonicsShown = true;
                    }
                }
            }
            selectMenuOnAltReleased = false;
            if (mnemonicsShown) {
                return false;
            }
            showMnemonics(shouldShowMnemonics(e), e.getComponent());
            return false;
        }
    }

    public void stateChanged(ChangeEvent e) {
        if (MenuSelectionManager.defaultManager().getSelectedPath().length == 0 && altPressedEventCount == 0) {
            showMnemonics(false, null);
        }
    }

    /* access modifiers changed from: package-private */
    public static void showMnemonics(boolean show, Component c) {
        Window window;
        if (show != showMnemonics) {
            showMnemonics = show;
            if (!UIManager.getBoolean("Component.hideMnemonics")) {
                return;
            }
            if (show) {
                JRootPane rootPane = SwingUtilities.getRootPane(c);
                if (rootPane != null && (window = SwingUtilities.getWindowAncestor(rootPane)) != null) {
                    repaintMnemonics(window);
                    windowListener = new WindowAdapter() {
                        /* class com.formdev.flatlaf.MnemonicHandler.AnonymousClass1 */

                        /* JADX WARN: Type inference failed for: r0v1, types: [boolean, java.lang.Runnable] */
                        /* JADX WARNING: Unknown variable types count: 1 */
                        /* Code decompiled incorrectly, please refer to instructions dump. */
                        public void windowDeactivated(java.awt.event.WindowEvent r2) {
                            /*
                                r1 = this;
                                r0 = 0
                                com.formdev.flatlaf.MnemonicHandler.access$002(r0)
                                boolean r0 = com.formdev.flatlaf.MnemonicHandler.access$102(r0)
                                java.awt.EventQueue.invokeLater(r0)
                                return
                            */
                            throw new UnsupportedOperationException("Method not decompiled: com.formdev.flatlaf.MnemonicHandler.AnonymousClass1.windowDeactivated(java.awt.event.WindowEvent):void");
                        }
                    };
                    window.addWindowListener(windowListener);
                    lastShowMnemonicWindow = new WeakReference<>(window);
                }
            } else if (lastShowMnemonicWindow != null) {
                Window window2 = lastShowMnemonicWindow.get();
                if (window2 != null) {
                    repaintMnemonics(window2);
                    if (windowListener != null) {
                        window2.removeWindowListener(windowListener);
                        windowListener = null;
                    }
                }
                lastShowMnemonicWindow = null;
            }
        }
    }

    private static void repaintMnemonics(Container container) {
        Component[] components = container.getComponents();
        for (Component c : components) {
            if (c.isVisible()) {
                if (hasMnemonic(c)) {
                    c.repaint();
                }
                if (c instanceof Container) {
                    repaintMnemonics((Container) c);
                }
            }
        }
    }

    private static boolean hasMnemonic(Component c) {
        if ((c instanceof JLabel) && ((JLabel) c).getDisplayedMnemonicIndex() >= 0) {
            return true;
        }
        if ((c instanceof AbstractButton) && ((AbstractButton) c).getDisplayedMnemonicIndex() >= 0) {
            return true;
        }
        if (c instanceof JTabbedPane) {
            JTabbedPane tabPane = (JTabbedPane) c;
            int tabCount = tabPane.getTabCount();
            for (int i = 0; i < tabCount; i++) {
                if (tabPane.getDisplayedMnemonicIndexAt(i) >= 0) {
                    return true;
                }
            }
        }
        return false;
    }
}
