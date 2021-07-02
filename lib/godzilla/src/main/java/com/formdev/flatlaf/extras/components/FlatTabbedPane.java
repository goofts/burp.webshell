package com.formdev.flatlaf.extras.components;

import com.formdev.flatlaf.FlatClientProperties;
import java.awt.Component;
import java.awt.Insets;
import java.util.function.BiConsumer;
import javax.swing.JTabbedPane;

public class FlatTabbedPane extends JTabbedPane implements FlatComponentExtension {

    public enum ScrollButtonsPlacement {
        both,
        trailing
    }

    public enum ScrollButtonsPolicy {
        never,
        asNeeded,
        asNeededSingle
    }

    public enum TabAlignment {
        leading,
        trailing,
        center
    }

    public enum TabAreaAlignment {
        leading,
        trailing,
        center,
        fill
    }

    public enum TabWidthMode {
        preferred,
        equal,
        compact
    }

    public enum TabsPopupPolicy {
        never,
        asNeeded
    }

    public boolean isShowTabSeparators() {
        return getClientPropertyBoolean(FlatClientProperties.TABBED_PANE_SHOW_TAB_SEPARATORS, "TabbedPane.showTabSeparators");
    }

    public void setShowTabSeparators(boolean showTabSeparators) {
        putClientProperty(FlatClientProperties.TABBED_PANE_SHOW_TAB_SEPARATORS, Boolean.valueOf(showTabSeparators));
    }

    public boolean isShowContentSeparators() {
        return getClientPropertyBoolean(FlatClientProperties.TABBED_PANE_SHOW_CONTENT_SEPARATOR, true);
    }

    public void setShowContentSeparators(boolean showContentSeparators) {
        putClientPropertyBoolean(FlatClientProperties.TABBED_PANE_SHOW_CONTENT_SEPARATOR, showContentSeparators, true);
    }

    public boolean isHasFullBorder() {
        return getClientPropertyBoolean(FlatClientProperties.TABBED_PANE_HAS_FULL_BORDER, "TabbedPane.hasFullBorder");
    }

    public void setHasFullBorder(boolean hasFullBorder) {
        putClientProperty(FlatClientProperties.TABBED_PANE_HAS_FULL_BORDER, Boolean.valueOf(hasFullBorder));
    }

    public boolean isHideTabAreaWithOneTab() {
        return getClientPropertyBoolean(FlatClientProperties.TABBED_PANE_HIDE_TAB_AREA_WITH_ONE_TAB, false);
    }

    public void setHideTabAreaWithOneTab(boolean hideTabAreaWithOneTab) {
        putClientPropertyBoolean(FlatClientProperties.TABBED_PANE_HIDE_TAB_AREA_WITH_ONE_TAB, hideTabAreaWithOneTab, false);
    }

    public int getMinimumTabWidth() {
        return getClientPropertyInt(FlatClientProperties.TABBED_PANE_MINIMUM_TAB_WIDTH, "TabbedPane.minimumTabWidth");
    }

    public void setMinimumTabWidth(int minimumTabWidth) {
        putClientProperty(FlatClientProperties.TABBED_PANE_MINIMUM_TAB_WIDTH, minimumTabWidth >= 0 ? Integer.valueOf(minimumTabWidth) : null);
    }

    public int getMinimumTabWidth(int tabIndex) {
        return FlatClientProperties.clientPropertyInt(getComponentAt(tabIndex), FlatClientProperties.TABBED_PANE_MINIMUM_TAB_WIDTH, 0);
    }

    public void setMinimumTabWidth(int tabIndex, int minimumTabWidth) {
        getComponentAt(tabIndex).putClientProperty(FlatClientProperties.TABBED_PANE_MINIMUM_TAB_WIDTH, minimumTabWidth >= 0 ? Integer.valueOf(minimumTabWidth) : null);
    }

    public int getMaximumTabWidth() {
        return getClientPropertyInt(FlatClientProperties.TABBED_PANE_MAXIMUM_TAB_WIDTH, "TabbedPane.maximumTabWidth");
    }

    public void setMaximumTabWidth(int maximumTabWidth) {
        putClientProperty(FlatClientProperties.TABBED_PANE_MAXIMUM_TAB_WIDTH, maximumTabWidth >= 0 ? Integer.valueOf(maximumTabWidth) : null);
    }

    public int getMaximumTabWidth(int tabIndex) {
        return FlatClientProperties.clientPropertyInt(getComponentAt(tabIndex), FlatClientProperties.TABBED_PANE_MAXIMUM_TAB_WIDTH, 0);
    }

    public void setMaximumTabWidth(int tabIndex, int maximumTabWidth) {
        getComponentAt(tabIndex).putClientProperty(FlatClientProperties.TABBED_PANE_MAXIMUM_TAB_WIDTH, maximumTabWidth >= 0 ? Integer.valueOf(maximumTabWidth) : null);
    }

    public int getTabHeight() {
        return getClientPropertyInt(FlatClientProperties.TABBED_PANE_TAB_HEIGHT, "TabbedPane.tabHeight");
    }

    public void setTabHeight(int tabHeight) {
        putClientProperty(FlatClientProperties.TABBED_PANE_TAB_HEIGHT, tabHeight >= 0 ? Integer.valueOf(tabHeight) : null);
    }

    public Insets getTabInsets() {
        return getClientPropertyInsets(FlatClientProperties.TABBED_PANE_TAB_INSETS, "TabbedPane.tabInsets");
    }

    public void setTabInsets(Insets tabInsets) {
        putClientProperty(FlatClientProperties.TABBED_PANE_TAB_INSETS, tabInsets);
    }

    public Insets getTabInsets(int tabIndex) {
        return (Insets) getComponentAt(tabIndex).getClientProperty(FlatClientProperties.TABBED_PANE_TAB_INSETS);
    }

    public void setTabInsets(int tabIndex, Insets tabInsets) {
        getComponentAt(tabIndex).putClientProperty(FlatClientProperties.TABBED_PANE_TAB_INSETS, tabInsets);
    }

    public Insets getTabAreaInsets() {
        return getClientPropertyInsets(FlatClientProperties.TABBED_PANE_TAB_AREA_INSETS, "TabbedPane.tabAreaInsets");
    }

    public void setTabAreaInsets(Insets tabAreaInsets) {
        putClientProperty(FlatClientProperties.TABBED_PANE_TAB_AREA_INSETS, tabAreaInsets);
    }

    public boolean isTabsClosable() {
        return getClientPropertyBoolean(FlatClientProperties.TABBED_PANE_TAB_CLOSABLE, false);
    }

    public void setTabsClosable(boolean tabClosable) {
        putClientPropertyBoolean(FlatClientProperties.TABBED_PANE_TAB_CLOSABLE, tabClosable, false);
    }

    public Boolean isTabClosable(int tabIndex) {
        Object value = getComponentAt(tabIndex).getClientProperty(FlatClientProperties.TABBED_PANE_TAB_CLOSABLE);
        return Boolean.valueOf(value instanceof Boolean ? ((Boolean) value).booleanValue() : isTabsClosable());
    }

    public void setTabClosable(int tabIndex, boolean tabClosable) {
        getComponentAt(tabIndex).putClientProperty(FlatClientProperties.TABBED_PANE_TAB_CLOSABLE, Boolean.valueOf(tabClosable));
    }

    public String getTabCloseToolTipText() {
        return (String) getClientProperty(FlatClientProperties.TABBED_PANE_TAB_CLOSE_TOOLTIPTEXT);
    }

    public void setTabCloseToolTipText(String tabCloseToolTipText) {
        putClientProperty(FlatClientProperties.TABBED_PANE_TAB_CLOSE_TOOLTIPTEXT, tabCloseToolTipText);
    }

    public String getTabCloseToolTipText(int tabIndex) {
        return (String) getComponentAt(tabIndex).getClientProperty(FlatClientProperties.TABBED_PANE_TAB_CLOSE_TOOLTIPTEXT);
    }

    public void setTabCloseToolTipText(int tabIndex, String tabCloseToolTipText) {
        getComponentAt(tabIndex).putClientProperty(FlatClientProperties.TABBED_PANE_TAB_CLOSE_TOOLTIPTEXT, tabCloseToolTipText);
    }

    public BiConsumer<JTabbedPane, Integer> getTabCloseCallback() {
        return (BiConsumer) getClientProperty(FlatClientProperties.TABBED_PANE_TAB_CLOSE_CALLBACK);
    }

    public void setTabCloseCallback(BiConsumer<JTabbedPane, Integer> tabCloseCallback) {
        putClientProperty(FlatClientProperties.TABBED_PANE_TAB_CLOSE_CALLBACK, tabCloseCallback);
    }

    public BiConsumer<JTabbedPane, Integer> getTabCloseCallback(int tabIndex) {
        return (BiConsumer) getComponentAt(tabIndex).getClientProperty(FlatClientProperties.TABBED_PANE_TAB_CLOSE_CALLBACK);
    }

    public void setTabCloseCallback(int tabIndex, BiConsumer<JTabbedPane, Integer> tabCloseCallback) {
        getComponentAt(tabIndex).putClientProperty(FlatClientProperties.TABBED_PANE_TAB_CLOSE_CALLBACK, tabCloseCallback);
    }

    public TabsPopupPolicy getTabsPopupPolicy() {
        return (TabsPopupPolicy) getClientPropertyEnumString(FlatClientProperties.TABBED_PANE_TABS_POPUP_POLICY, TabsPopupPolicy.class, "TabbedPane.tabsPopupPolicy", TabsPopupPolicy.asNeeded);
    }

    public void setTabsPopupPolicy(TabsPopupPolicy tabsPopupPolicy) {
        putClientPropertyEnumString(FlatClientProperties.TABBED_PANE_TABS_POPUP_POLICY, tabsPopupPolicy);
    }

    public ScrollButtonsPolicy getScrollButtonsPolicy() {
        return (ScrollButtonsPolicy) getClientPropertyEnumString(FlatClientProperties.TABBED_PANE_SCROLL_BUTTONS_POLICY, ScrollButtonsPolicy.class, "TabbedPane.scrollButtonsPolicy", ScrollButtonsPolicy.asNeededSingle);
    }

    public void setScrollButtonsPolicy(ScrollButtonsPolicy scrollButtonsPolicy) {
        putClientPropertyEnumString(FlatClientProperties.TABBED_PANE_SCROLL_BUTTONS_POLICY, scrollButtonsPolicy);
    }

    public ScrollButtonsPlacement getScrollButtonsPlacement() {
        return (ScrollButtonsPlacement) getClientPropertyEnumString(FlatClientProperties.TABBED_PANE_SCROLL_BUTTONS_PLACEMENT, ScrollButtonsPlacement.class, "TabbedPane.scrollButtonsPlacement", ScrollButtonsPlacement.both);
    }

    public void setScrollButtonsPlacement(ScrollButtonsPlacement scrollButtonsPlacement) {
        putClientPropertyEnumString(FlatClientProperties.TABBED_PANE_SCROLL_BUTTONS_PLACEMENT, scrollButtonsPlacement);
    }

    public TabAreaAlignment getTabAreaAlignment() {
        return (TabAreaAlignment) getClientPropertyEnumString(FlatClientProperties.TABBED_PANE_TAB_AREA_ALIGNMENT, TabAreaAlignment.class, "TabbedPane.tabAreaAlignment", TabAreaAlignment.leading);
    }

    public void setTabAreaAlignment(TabAreaAlignment tabAreaAlignment) {
        putClientPropertyEnumString(FlatClientProperties.TABBED_PANE_TAB_AREA_ALIGNMENT, tabAreaAlignment);
    }

    public TabAlignment getTabAlignment() {
        return (TabAlignment) getClientPropertyEnumString(FlatClientProperties.TABBED_PANE_TAB_ALIGNMENT, TabAlignment.class, "TabbedPane.tabAlignment", TabAlignment.center);
    }

    public void setTabAlignment(TabAlignment tabAlignment) {
        putClientPropertyEnumString(FlatClientProperties.TABBED_PANE_TAB_ALIGNMENT, tabAlignment);
    }

    public TabWidthMode getTabWidthMode() {
        return (TabWidthMode) getClientPropertyEnumString(FlatClientProperties.TABBED_PANE_TAB_WIDTH_MODE, TabWidthMode.class, "TabbedPane.tabWidthMode", TabWidthMode.preferred);
    }

    public void setTabWidthMode(TabWidthMode tabWidthMode) {
        putClientPropertyEnumString(FlatClientProperties.TABBED_PANE_TAB_WIDTH_MODE, tabWidthMode);
    }

    public int getTabIconPlacement() {
        return getClientPropertyInt(FlatClientProperties.TABBED_PANE_TAB_ICON_PLACEMENT, 10);
    }

    public void setTabIconPlacement(int tabIconPlacement) {
        putClientProperty(FlatClientProperties.TABBED_PANE_TAB_ICON_PLACEMENT, tabIconPlacement >= 0 ? Integer.valueOf(tabIconPlacement) : null);
    }

    public Component getLeadingComponent() {
        return (Component) getClientProperty(FlatClientProperties.TABBED_PANE_LEADING_COMPONENT);
    }

    public void setLeadingComponent(Component leadingComponent) {
        putClientProperty(FlatClientProperties.TABBED_PANE_LEADING_COMPONENT, leadingComponent);
    }

    public Component getTrailingComponent() {
        return (Component) getClientProperty(FlatClientProperties.TABBED_PANE_TRAILING_COMPONENT);
    }

    public void setTrailingComponent(Component trailingComponent) {
        putClientProperty(FlatClientProperties.TABBED_PANE_TRAILING_COMPONENT, trailingComponent);
    }
}
