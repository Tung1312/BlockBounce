package com.birb_birb.blockbounce.ui.menus;

import com.almasb.fxgl.app.scene.MenuType;
import com.birb_birb.blockbounce.constants.GameConstants;
import com.birb_birb.blockbounce.core.BlockBounceApp;
import com.birb_birb.blockbounce.utils.ButtonManager;
import com.birb_birb.blockbounce.utils.MenuManager;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

import static com.almasb.fxgl.dsl.FXGL.*;

/**
 * Story Mode menu.
 */
public class StoryModeMenu extends MenuManager {

    public StoryModeMenu() {
        super(MenuType.GAME_MENU);
    }

    @Override
    protected String getBackgroundImagePath() {
        return GameConstants.STORY_MODE_BACKGROUND;
    }

    @Override
    protected void setupContent() {
        Button newGameButton = createMenuButton("Enter Game");
        newGameButton.setLayoutX(getButtonX());
        newGameButton.setLayoutY(getAppHeight() * 0.911);
        newGameButton.setOnAction(e -> {
            BlockBounceApp.setGameMode(BlockBounceApp.GameMode.STORY);
            getGameController().startNewGame();
        });
        root.getChildren().add(newGameButton);

        VBox buttonPanel = createButtonPanel();
        root.getChildren().add(buttonPanel);

        setupKeyHandling();
    }

    public static VBox createButtonPanel() {
        Button settingButton = ButtonManager.createButton();
        settingButton.setOnAction(e -> ButtonManager.openSettings());
        Button homeButton = ButtonManager.createButton();
        homeButton.setOnAction(e -> ButtonManager.navigateToMainMenu());

        Region spacer = new Region();
        spacer.setPrefHeight(601);
        spacer.setMinHeight(601);
        spacer.setMaxHeight(601);

        VBox buttonPanel = new VBox();
        buttonPanel.setAlignment(Pos.TOP_LEFT);
        buttonPanel.setPadding(new Insets(6.5, 0, 0, 7)); // top, right, bottom, left
        buttonPanel.getChildren().addAll(settingButton, spacer, homeButton);

        return buttonPanel;
    }

    private void setupKeyHandling() {
        getInput().addAction(new com.almasb.fxgl.input.UserAction("ESC_BACK") {
            @Override
            protected void onActionBegin() {
                getSceneService().popSubScene();
            }
        }, KeyCode.ESCAPE);
    }
}
