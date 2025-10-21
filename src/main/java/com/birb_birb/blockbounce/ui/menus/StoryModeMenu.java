package com.birb_birb.blockbounce.ui.menus;

import com.almasb.fxgl.app.scene.MenuType;
import com.birb_birb.blockbounce.constants.GameConstants;
import com.birb_birb.blockbounce.core.BlockBounceApp;
import com.birb_birb.blockbounce.utils.ButtonManager;
import com.birb_birb.blockbounce.utils.MenuManager;
import com.birb_birb.blockbounce.utils.SoundManager;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

import java.util.Objects;

import static com.almasb.fxgl.dsl.FXGL.*;

/**
 * Story Mode menu.
 */
public class StoryModeMenu extends MenuManager {

    private int currentLevel = 1;
    private ImageView selectImageView;

    private static final String[] SELECT_IMAGES = {
        GameConstants.SELECT_1,
        GameConstants.SELECT_2,
        GameConstants.SELECT_3,
        GameConstants.SELECT_4,
        GameConstants.SELECT_5,
        GameConstants.SELECT_6,
        GameConstants.SELECT_7,
        GameConstants.SELECT_8
    };

    public StoryModeMenu() {
        super(MenuType.GAME_MENU);
    }

    @Override
    protected String getBackgroundImagePath() {
        return GameConstants.STORY_MODE_BACKGROUND;
    }

    @Override
    protected void setupContent() {
        // Display levels selection
        setupLevelsDisplay();

        // New game button
        Button newGameButton = createMenuButton("Enter Game");
        newGameButton.setLayoutX(getButtonX());
        newGameButton.setLayoutY(getAppHeight() * 0.911);
        newGameButton.setOnAction(e -> {
            BlockBounceApp.setGameMode(BlockBounceApp.GameMode.STORY);
            getGameController().startNewGame();
        });
        root.getChildren().add(newGameButton);

        // Setting and Back
        VBox buttonPanel = createButtonPanel();
        root.getChildren().add(buttonPanel);

        // Input handling
        setupKeyHandling();
    }

    private void setupLevelsDisplay() {
        try {
            // Base layer - Display multiple levels
            // TODO: add locked and unlocked levels
            Image levelsImage = new Image(Objects.requireNonNull(
                getClass().getResourceAsStream(GameConstants.STORY_MODE_LEVELS)));
            ImageView levelsView = new ImageView(levelsImage);
            levelsView.setFitWidth(getAppWidth());
            levelsView.setFitHeight(getAppHeight());
            levelsView.setPreserveRatio(false);
            root.getChildren().add(levelsView);

            // Select layer - Indicator (arrow) for level selection
            Image selectImage = new Image(Objects.requireNonNull(
                getClass().getResourceAsStream(SELECT_IMAGES[0])));
            selectImageView = new ImageView(selectImage);
            selectImageView.setFitWidth(getAppWidth());
            selectImageView.setFitHeight(getAppHeight());
            selectImageView.setPreserveRatio(false);
            root.getChildren().add(selectImageView);

        } catch (Exception e) {
            System.out.println("Failed to load level images: " + e.getMessage());
        }
    }

    private void updateSelectImage() {
        try {
            Image selectImage = new Image(Objects.requireNonNull(
                getClass().getResourceAsStream(SELECT_IMAGES[currentLevel - 1])));
            selectImageView.setImage(selectImage);
            SoundManager.playHoverSound();
        } catch (Exception e) {
            System.out.println("Failed to update select image: " + e.getMessage());
        }
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

        // Arrow key navigation for level selection - RIGHT arrow
        getInput().addAction(new com.almasb.fxgl.input.UserAction("NEXT_LEVEL_RIGHT") {
            @Override
            protected void onActionBegin() {
                currentLevel++;
                if (currentLevel > 8) {
                    currentLevel = 1;
                }
                updateSelectImage();
            }
        }, KeyCode.D);

        // Arrow key navigation for level selection - LEFT arrow
        getInput().addAction(new com.almasb.fxgl.input.UserAction("PREV_LEVEL_LEFT") {
            @Override
            protected void onActionBegin() {
                currentLevel--;
                if (currentLevel < 1) {
                    currentLevel = 8;
                }
                updateSelectImage();
            }
        }, KeyCode.A);
    }
}