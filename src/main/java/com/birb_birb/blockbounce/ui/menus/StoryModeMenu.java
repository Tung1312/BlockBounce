package com.birb_birb.blockbounce.ui.menus;

import com.almasb.fxgl.app.scene.MenuType;
import com.almasb.fxgl.input.UserAction;
import com.birb_birb.blockbounce.constants.GameConstants;
import com.birb_birb.blockbounce.constants.GameMode;
import com.birb_birb.blockbounce.core.gamemode.story.StoryModeGame;
import com.birb_birb.blockbounce.utils.ButtonManager;
import com.birb_birb.blockbounce.utils.MenuManager;
import com.birb_birb.blockbounce.utils.SoundManager;
import com.birb_birb.blockbounce.utils.saveload.ProgressLoader;
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

/**Story Mode menu*/
public class StoryModeMenu extends MenuManager {

    private final ProgressLoader progressManager;
    private int currentLevel = 1;
    private ImageView selectImageView;
    private ImageView levelsView;

    public StoryModeMenu() {
        super(MenuType.GAME_MENU);
        this.progressManager = ProgressLoader.getInstance();
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
            // Check if selected level is unlocked
            if (progressManager != null && !progressManager.isLevelUnlocked(currentLevel)) {
                return;
            }

            GameMode.setCurrentGameMode(GameMode.STORY);
            StoryModeGame.setStartingLevel(currentLevel);
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
            // Get instance and reload progress before loading/rendering level displays
            ProgressLoader loader = ProgressLoader.getInstance();
            loader.reloadProgress();

            // Base layer - Display unlocked levels based on progress
            int unlockedLevels = loader.getUnlockedLevels();
            String levelsImagePath = GameConstants.UNLOCKED_LEVEL_IMAGES[Math.min(unlockedLevels - 1, 7)];

            // Set current level to the highest unlocked level (capped at 8)
            currentLevel = Math.min(unlockedLevels, 8);

            Image levelsImage = new Image(Objects.requireNonNull(
                getClass().getResourceAsStream(levelsImagePath)));
            levelsView = new ImageView(levelsImage);
            levelsView.setFitWidth(getAppWidth());
            levelsView.setFitHeight(getAppHeight());
            levelsView.setPreserveRatio(false);
            root.getChildren().add(levelsView);

            // Select layer - Indicator (arrow) for level selection
            Image selectImage = new Image(Objects.requireNonNull(
                getClass().getResourceAsStream(GameConstants.SELECT_IMAGES[currentLevel - 1])));
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
        if (selectImageView == null) {
            System.out.println("SelectImageView not initialized yet");
            return;
        }
        try {
            Image selectImage = new Image(Objects.requireNonNull(
                getClass().getResourceAsStream(GameConstants.SELECT_IMAGES[currentLevel - 1])));
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
        getInput().addAction(new UserAction("ESC_BACK") {
            @Override
            protected void onActionBegin() {
                getSceneService().popSubScene();
            }
        }, KeyCode.ESCAPE);

        getInput().addAction(new UserAction("NEXT_LEVEL_RIGHT") {
            @Override
            protected void onActionBegin() {
                if (progressManager == null) return;
                int maxLevel = Math.min(8, progressManager.getUnlockedLevels());
                currentLevel++;
                if (currentLevel > maxLevel) {
                    currentLevel = 1;
                }
                updateSelectImage();
            }
        }, KeyCode.D);

        getInput().addAction(new UserAction("PREV_LEVEL_LEFT") {
            @Override
            protected void onActionBegin() {
                if (progressManager == null) return;
                int maxLevel = Math.min(8, progressManager.getUnlockedLevels());
                currentLevel--;
                if (currentLevel < 1) {
                    currentLevel = maxLevel;
                }
                updateSelectImage();
            }
        }, KeyCode.A);
    }
}