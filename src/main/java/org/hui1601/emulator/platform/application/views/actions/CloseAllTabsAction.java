package org.hui1601.emulator.platform.application.views.actions;

import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;

public class CloseAllTabsAction {
    public static void update(Tab tab) {
        update(tab.getTabPane());
    }

    public static void update(TabPane pane) {
        CloseTabAction.update(pane.getTabs());
    }
}