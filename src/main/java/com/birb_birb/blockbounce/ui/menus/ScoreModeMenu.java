package com.birb_birb.blockbounce.ui.menus;

import com.almasb.fxgl.app.scene.MenuType;
import com.birb_birb.blockbounce.constants.GameConstants;
import com.birb_birb.blockbounce.constants.GameMode;
import com.birb_birb.blockbounce.utils.ButtonManager;
import com.birb_birb.blockbounce.utils.saveload.SaveGameManager;
import com.birb_birb.blockbounce.utils.MenuManager;
import com.birb_birb.blockbounce.utils.SoundManager;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

import static com.almasb.fxgl.dsl.FXGL.*;

/**
 * Score Mode submenu for selecting New Game or Load Game
 */
public class ScoreModeMenu extends MenuManager {

    public ScoreModeMenu() {
        super(MenuType.MAIN_MENU);
    }

    @Override
    protected String getBackgroundImagePath() {
        return GameConstants.SCORE_MODE_BACKGROUND;
    }

    @Override
    protected void setupContent() {

        // Calculate button positions
        double buttonX = getAppWidth() / 2.0 + 148;
        double startY = getAppHeight() * 0.284;

        // New Game button
        Button newGameButton = createMenuButton("New Game");
        newGameButton.setLayoutX(buttonX);
        newGameButton.setLayoutY(startY);
        newGameButton.setOnAction(e -> {
            SoundManager.playClickSound();
            GameMode.setCurrentGameMode(GameMode.ENDLESS);
            fireNewGame();
        });

        // Load Game button
        Button loadGameButton = createMenuButton("Load Game");
        loadGameButton.setLayoutX(buttonX);
        loadGameButton.setLayoutY(startY + 293);

        // Check if any save exists
        boolean hasSave = false;
        for (int slot = 1; slot <= 3; slot++) {
            if (SaveGameManager.hasScoreSaveData(slot)) {
                hasSave = true;
                break;
            }
        }

        if (!hasSave) {
            loadGameButton.setDisable(true);
            loadGameButton.setOpacity(0.5);
        } else {
            loadGameButton.setOnAction(e -> {
                SoundManager.playClickSound();
                GameMode.setCurrentGameMode(GameMode.ENDLESS);
                GameMode.setShouldLoadSave(true); // Set flag to load save
                fireNewGame();
            });
        }

        VBox buttonPanel = createButtonPanel();
        root.getChildren().add(buttonPanel);

        root.getChildren().addAll(newGameButton, loadGameButton);
    }

    public static VBox createButtonPanel() {
        Button settingButton = ButtonManager.createButton();
        settingButton.setOnAction(e -> ButtonManager.openSettings());
        Button homeButton = ButtonManager.createButton();
        homeButton.setOnAction(e -> ButtonManager.exitToMainMenu());

        Region spacer = new Region();
        spacer.setPrefHeight(601);
        spacer.setMinHeight(601);
        spacer.setMaxHeight(601);

        VBox buttonPanel = new VBox();
        buttonPanel.setAlignment(Pos.TOP_RIGHT);
        buttonPanel.setPadding(new Insets(6.5, 0, 0, GameConstants.WINDOW_WIDTH - 60)); // top, right, bottom, left
        buttonPanel.getChildren().addAll(settingButton, spacer, homeButton);

        return buttonPanel;
    }
}
