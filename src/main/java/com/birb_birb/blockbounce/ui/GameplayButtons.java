package com.birb_birb.blockbounce.ui;

import com.birb_birb.blockbounce.constants.GameConstants;
import com.birb_birb.blockbounce.utils.ButtonManager;
import com.birb_birb.blockbounce.utils.SoundManager;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.scene.layout.Region;

public class GameplayButtons {

    private GameplayButtons() {}

    public static VBox createButtonPanel() {
        Button settingButton = createButton();
        settingButton.setOnAction(e -> ButtonManager.openSettings());
        Button backButton = createButton();
        backButton.setOnAction(e -> ButtonManager.navigateToStoryMode());
        Button homeButton = createButton();
        homeButton.setOnAction(e -> ButtonManager.exitToMainMenu());

        Region spacer1 = new Region();
        spacer1.setPrefHeight(8);
        spacer1.setMinHeight(8);
        spacer1.setMaxHeight(8);

        Region spacer2 = new Region();
        spacer2.setPrefHeight(541);
        spacer2.setMinHeight(541);
        spacer2.setMaxHeight(541);

        VBox buttonPanel = new VBox();
        buttonPanel.setAlignment(Pos.TOP_RIGHT);
        buttonPanel.setPadding(new Insets(-3, 10, 0, 0)); // top, right, bottom, left
        buttonPanel.getChildren().addAll(settingButton, spacer1, backButton, spacer2, homeButton);

        return buttonPanel;
    }

    private static Button createButton() {
        Button button = new Button();
        button.setPrefSize(52, 52); // Icon button size
        button.setMinSize(52, 52);
        button.setMaxSize(52, 52);

        button.setStyle(GameConstants.BASE_STYLE);

        button.setOnMouseEntered(e -> {
            if (!button.isPressed()) {
                SoundManager.playHoverSound();
                button.setStyle(GameConstants.HOVER_STYLE);
            }
        });

        button.setOnMouseExited(e -> {
            if (!button.isPressed()) {
                button.setStyle(GameConstants.BASE_STYLE);
            }
        });

        button.setOnMousePressed(e -> {
            SoundManager.playClickSound();
            button.setStyle(GameConstants.BASE_STYLE);
        });

        button.setOnMouseReleased(e -> {
            if (button.isHover()) {
                button.setStyle(GameConstants.HOVER_STYLE);
            } else {
                button.setStyle(GameConstants.BASE_STYLE);
            }
        });

        return button;
    }
}

