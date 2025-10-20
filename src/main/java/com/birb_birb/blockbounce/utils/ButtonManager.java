package com.birb_birb.blockbounce.utils;

import com.birb_birb.blockbounce.constants.GameConstants;
import com.birb_birb.blockbounce.ui.menus.ScoreModeMenu;
import com.birb_birb.blockbounce.ui.menus.StoryModeMenu;
import com.birb_birb.blockbounce.ui.menus.VersusModeMenu;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.util.Objects;

import static com.almasb.fxgl.dsl.FXGL.*;

/**
 * Centralized manager for all button actions and navigation in the game.
 * Provides static methods that can be used across different menus and UI components.
 */
public class ButtonManager {

    private ButtonManager() {
        // Private constructor to prevent instantiation
    }

    /**
     * Navigate to Story Mode menu
     */
    public static void navigateToStoryMode() {
        getSceneService().pushSubScene(new StoryModeMenu());
    }

    /**
     * Navigate to Score Mode menu
     */
    public static void navigateToScoreMode() {
        getSceneService().pushSubScene(new ScoreModeMenu());
    }

    /**
     * Navigate to Versus Mode menu
     */
    public static void navigateToVersusMode() {
        getSceneService().pushSubScene(new VersusModeMenu());
    }

    /**
     * Start a new game
     */
    public static void newGame() {
        getGameController().startNewGame();
    }

    /**
     * Open FXGL settings/game menu
     */
    public static void openSettings() {
        getGameController().gotoGameMenu();
    }

    /**
     * Show How to Play window with instructions
     */
    public static void showHowToPlay() {
        Stage howToPlayStage = new Stage();
        howToPlayStage.setTitle("How to Play");

        StackPane layout = new StackPane();
        layout.setStyle("-fx-background-color: #2c3e50;");

        try {
            Image howToPlayImage = new Image(
                Objects.requireNonNull(
                    ButtonManager.class.getResourceAsStream(GameConstants.HOW_TO_PLAY)
                )
            );
            ImageView imageView = new ImageView(howToPlayImage);
            imageView.setPreserveRatio(true);
            imageView.setFitWidth(1080);
            layout.getChildren().add(imageView);
        } catch (Exception e) {
            // Fallback if image not found
            javafx.scene.text.Text text = new javafx.scene.text.Text("How to Play image not found");
            text.setFill(Color.WHITE);
            text.setStyle("-fx-font-size: 24px;");
            layout.getChildren().add(text);
        }

        Scene scene = new Scene(layout, 1080, 720);
        howToPlayStage.setScene(scene);
        howToPlayStage.show();
    }

    /**
     * Navigate back to Main Menu
     * Pops the current sub-scene to return to main menu
     */
    public static void navigateToMainMenu() {
        getSceneService().popSubScene();
    }

    /**
     * Navigate back to Main Menu from game
     * Goes to main menu from the game scene
     */
    public static void exitToMainMenu() {
        getGameController().gotoMainMenu();
    }
}

