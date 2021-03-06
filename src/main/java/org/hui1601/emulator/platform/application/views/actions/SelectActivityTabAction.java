package org.hui1601.emulator.platform.application.views.actions;

/*

    Hide
    1. Show 상태에서 이미 선택된 탭을 다시 선택할 경우
    2. Show 상태에서 일정 이하 너비로 줄일 경우

    Show
    1. Hide 상태에서 탭을 선택할 경우
    2. Hide 상태에서 일정 이상 너비로 늘릴 경우

    Change
    1. Show 상태에서 이미 선택된 탭과 다른 탭을 탭을 선택할 경우

*/

import javafx.scene.control.ToggleButton;
import javafx.scene.layout.VBox;
import org.hui1601.emulator.platform.application.views.parts.ActiveAreaPart;

public class SelectActivityTabAction {
    // Activity Bar
    private static VBox vbox;

    public static void initialize() {
        vbox = ActiveAreaPart.getActivityBar();
    }

    public static void update(int index) {
        ToggleButton toggle = (ToggleButton) vbox.getChildren().get(index);

        // Hide
        if (HideSideBarAction.isHided()) {
            // 숨겨진 상태에서 탭을 선택할 경우
            HideSideBarAction.update(false);
        }
        // Show
        else {
            if (!toggle.isSelected()) {
                HideSideBarAction.update(true);
            }
        }

        ChangeActivityTabAction.update(index);
    }
}