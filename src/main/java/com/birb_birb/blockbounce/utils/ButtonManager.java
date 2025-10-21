package com.birb_birb.blockbounce.utils;

import com.birb_birb.blockbounce.constants.GameConstants;
import com.birb_birb.blockbounce.ui.menus.ScoreModeMenu;
import com.birb_birb.blockbounce.ui.menus.StoryModeMenu;
import com.birb_birb.blockbounce.ui.menus.VersusModeMenu;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.util.Objects;

import static com.almasb.fxgl.dsl.FXGL.*;

public class ButtonManager {

    private ButtonManager() {}

    public static Button createButton() {
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

    public static void navigateToMainMenu() {
        getSceneService().popSubScene();
    }

    public static void exitToMainMenu() {
        getGameController().gotoMainMenu();
    }
}