package com.birb_birb.blockbounce.ui.menus;

import com.almasb.fxgl.app.scene.MenuType;
import com.birb_birb.blockbounce.constants.GameConstants;
import com.birb_birb.blockbounce.constants.GameMode;
import com.birb_birb.blockbounce.utils.saveload.SaveGameManager;
import com.birb_birb.blockbounce.utils.MenuManager;
import com.birb_birb.blockbounce.utils.SoundManager;
import javafx.scene.control.Button;
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
        return GameConstants.MAIN_MENU_BACKGROUND;
    }

    @Override
    protected void setupContent() {
        // Title
        Text titleText = new Text("SCORE MODE");
        titleText.setFont(Font.font("Arial", 48));
        titleText.setFill(Color.CYAN);
        titleText.setLayoutX(getAppWidth() / 2.0 - 150);
        titleText.setLayoutY(150);
        root.getChildren().add(titleText);

        // Calculate button positions
        double buttonX = getButtonX();
        double startY = getStartY();

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
        loadGameButton.setLayoutY(startY + GameConstants.BUTTON_SPACING);

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

        // Back button
        Button backButton = createMenuButton("Back");
        backButton.setLayoutX(buttonX);
        backButton.setLayoutY(startY + (GameConstants.BUTTON_SPACING * 2));
        backButton.setOnAction(e -> {
            SoundManager.playClickSound();
            getGameController().gotoMainMenu();
        });

        root.getChildren().addAll(newGameButton, loadGameButton, backButton);
    }
}
