package com.birb_birb.blockbounce.ui.menus;

import com.almasb.fxgl.app.scene.MenuType;
import com.birb_birb.blockbounce.constants.GameConstants;
import com.birb_birb.blockbounce.utils.MenuManager;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;

import java.util.Objects;

import static com.almasb.fxgl.dsl.FXGL.*;

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
        storyModeButton.setOnAction(e -> navigateToStoryMode());
        scoreModeButton.setOnAction(e -> navigateToScoreMode());
        versusModeButton.setOnAction(e -> navigateToVersusMode());

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
        Button helpButton = createButton();
        helpButton.setOnAction(e -> openSettings());

        // How to play button
        Button howToPlayButton = createButton();
        howToPlayButton.setOnAction(e -> showHowToPlay());

        hbox.getChildren().addAll(helpButton, howToPlayButton);

        return hbox;
    }

    /**
     * Create button
     */
    private Button createButton() {
        Button button = new Button();
        button.setPrefWidth(ICON_BUTTON_SIZE);
        button.setPrefHeight(ICON_BUTTON_SIZE);

        String baseStyle = "-fx-background-color: rgba(255, 255, 255, 0);";

        String hoverStyle = "-fx-background-color: rgba(255, 255, 255, 0.36);";

        button.setStyle(baseStyle);

        button.setOnMouseEntered(e -> {
            if (!button.isPressed()) {
                button.setStyle(hoverStyle);
            }
        });

        button.setOnMouseExited(e -> {
            if (!button.isPressed()) {
                button.setStyle(baseStyle);
            }
        });

        button.setOnMousePressed(e -> {
            button.setStyle(baseStyle);
        });

        button.setOnMouseReleased(e -> {
            if (button.isHover()) {
                button.setStyle(hoverStyle);
            } else {
                button.setStyle(baseStyle);
            }
        });

        return button;
    }

    /**
     * Open FXGL settings menu
     */
    private void openSettings() {
        // Open the game menu (settings)
        getController().gotoGameMenu();
    }

    /**
     * Open How to Play window
     */
    private void showHowToPlay() {
        Stage howToPlayStage = new Stage();
        howToPlayStage.setTitle("How to Play");

        StackPane layout = new StackPane();
        layout.setStyle("-fx-background-color: #2c3e50;");

        try {
            Image howToPlayImage = new Image(Objects.requireNonNull(getClass().getResourceAsStream(GameConstants.HOW_TO_PLAY)));
            ImageView imageView = new ImageView(howToPlayImage);
            imageView.setPreserveRatio(true);
            imageView.setFitWidth(800);
            layout.getChildren().add(imageView);
        } catch (Exception e) {
            // Fallback
            javafx.scene.text.Text text = new javafx.scene.text.Text("How to Play image not found");
            text.setFill(Color.WHITE);
            text.setStyle("-fx-font-size: 24px;");
            layout.getChildren().add(text);
        }

        Scene scene = new Scene(layout, 800, 600);
        howToPlayStage.setScene(scene);
        howToPlayStage.show();
    }

    private void navigateToStoryMode() {
        getSceneService().pushSubScene(new StoryModeMenu());
    }

    private void navigateToScoreMode() {
        getSceneService().pushSubScene(new ScoreModeMenu());
    }

    private void navigateToVersusMode() {
        getSceneService().pushSubScene(new VersusModeMenu());
    }
}
