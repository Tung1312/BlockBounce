package com.birb_birb.blockbounce.utils;

import com.birb_birb.blockbounce.constants.GameConstants;
import com.birb_birb.blockbounce.constants.GameMode;
import com.birb_birb.blockbounce.ui.menus.ScoreModeMenu;
import com.birb_birb.blockbounce.ui.menus.StoryModeMenu;
import com.birb_birb.blockbounce.ui.menus.VersusModeMenu;
import javafx.scene.control.Button;
import javafx.util.Duration;

import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import static com.almasb.fxgl.dsl.FXGL.*;

public class ButtonManager {

    private ButtonManager() {}

    public static Button createButton() {
        Button button = new Button();
        button.setPrefSize(52, 52); // Icon button size
        button.setMinSize(52, 52);
        button.setMaxSize(52, 52);

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

    public static void navigateToStoryMode() {
        getSceneService().pushSubScene(new StoryModeMenu());
    }

    public static void navigateToScoreMode() {
        getSceneService().pushSubScene(new ScoreModeMenu());
    }

    public static void navigateToVersusMode() {
        getSceneService().pushSubScene(new VersusModeMenu());
    }

    public static void openSettings() {
        getGameController().gotoGameMenu();
    }

    public static void showGithub() {
        // ref: https://codingtechroom.com/question/-javafx-open-url-hyperlink-browser
        String githubUrl = "https://github.com/Tung1312/BlockBounce";

        runOnce(() -> {
            try {
                Desktop.getDesktop().browse(new URI(githubUrl));
            } catch (Exception e) {
                getNotificationService().pushNotification("Could not open link");
            }
        }, Duration.seconds(0));
    }

    public static void goToPreviousScene() {
        getSceneService().popSubScene();
    }

    public static void navigateBackToGameModeMenu(GameMode gameMode) {
        getDialogService().showConfirmationBox("Are you sure you want to go back to previous menu?", answer -> {
            if (answer) {
                getGameController().gotoMainMenu();

                // Push the appropriate menu based on game mode
                switch (gameMode) {
                    case STORY:
                        getSceneService().pushSubScene(new StoryModeMenu());
                        break;
                    case ENDLESS:
                        getSceneService().pushSubScene(new ScoreModeMenu());
                        break;
                    case VERSUS:
                        getSceneService().pushSubScene(new VersusModeMenu());
                        break;
                }
            }
        });
    }

    public static void exitToMainMenu() {
        getGameController().gotoMainMenu();
    }

    public static void exitToMainMenuFromGame() {
        getGameController().pauseEngine();
        getDialogService().showConfirmationBox("Are you sure you want to exit to the main menu?", answer -> {
            if (answer) {
                getGameController().gotoMainMenu();
            }
        });
    }
}