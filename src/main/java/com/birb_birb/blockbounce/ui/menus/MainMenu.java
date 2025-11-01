package com.birb_birb.blockbounce.ui.menus;

import com.almasb.fxgl.app.scene.MenuType;
import com.birb_birb.blockbounce.constants.GameConstants;
import com.birb_birb.blockbounce.utils.ButtonManager;
import com.birb_birb.blockbounce.utils.MenuManager;
import com.birb_birb.blockbounce.utils.SoundManager;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;

/**
 * Main menu of the game with options for different game modes.
 */
public class MainMenu extends MenuManager {

    private static final double ICON_BUTTON_SPACING = 228;
    private static final double ICON_BUTTON_SIZE = 52;

    public MainMenu() {
        super(MenuType.MAIN_MENU);
    }

    @Override
    protected String getBackgroundImagePath() {
        return GameConstants.MAIN_MENU_BACKGROUND;
    }

    @Override
    protected void setupContent() {
        // Calculate button positions
        double buttonX = getButtonX();
        double startY = getStartY();

        // Create menu buttons
        Button storyModeButton = createMenuButton("Story Mode");
        Button scoreModeButton = createMenuButton("Score Mode");
        Button versusModeButton = createMenuButton("Versus Mode");

        // Position buttons
        storyModeButton.setLayoutX(buttonX);
        storyModeButton.setLayoutY(startY);

        scoreModeButton.setLayoutX(buttonX);
        scoreModeButton.setLayoutY(startY + GameConstants.BUTTON_SPACING);

        versusModeButton.setLayoutX(buttonX);
        versusModeButton.setLayoutY(startY + (GameConstants.BUTTON_SPACING * 2));

        // Set button actions
        storyModeButton.setOnAction(e -> ButtonManager.navigateToStoryMode());
        scoreModeButton.setOnAction(e -> ButtonManager.navigateToScoreMode());
        versusModeButton.setOnAction(e -> ButtonManager.navigateToVersusMode());

        // Add buttons to the root pane
        root.getChildren().addAll(storyModeButton, scoreModeButton, versusModeButton);

        HBox secondaryButtonBox = createSecondaryButtons();
        root.getChildren().add(secondaryButtonBox);
    }

    /**
     * Create HBox containing Help and How to Play  buttons
     */
    private HBox createSecondaryButtons() {
        HBox hbox = new HBox(ICON_BUTTON_SPACING);
        hbox.setAlignment(Pos.CENTER);

        // Position
        double totalWidth = ICON_BUTTON_SIZE + ICON_BUTTON_SPACING + ICON_BUTTON_SIZE; // button + spacing + button
        hbox.setLayoutX((getAppWidth() - totalWidth) / 2);
        hbox.setLayoutY(getAppHeight() - 60);

        // Help button
        Button settingButton = createButton();
        settingButton.setOnAction(e -> ButtonManager.openSettings());

        // How to play button
        Button howToPlayButton = createButton();
        howToPlayButton.setOnAction(e -> ButtonManager.showGithub());

        hbox.getChildren().addAll(settingButton, howToPlayButton);

        return hbox;
    }

    /**
     * Create button
     */
    private Button createButton() {
        Button button = new Button();
        button.setPrefWidth(ICON_BUTTON_SIZE);
        button.setPrefHeight(ICON_BUTTON_SIZE);

        button.getStyleClass().add("game-icon-button");

        button.setOnMouseEntered(e -> {
            if (!button.isPressed()) {
                SoundManager.playHoverSound();
            }
        });

        button.setOnMousePressed(e -> {
            SoundManager.playClickSound();
        });

        return button;
    }
}
