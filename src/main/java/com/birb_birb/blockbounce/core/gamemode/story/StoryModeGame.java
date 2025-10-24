package com.birb_birb.blockbounce.core.gamemode.story;

import com.almasb.fxgl.entity.Entity;
import com.birb_birb.blockbounce.constants.GameConstants;
import com.birb_birb.blockbounce.constants.GameMode;
import com.birb_birb.blockbounce.core.GameFactory;
import com.birb_birb.blockbounce.core.GameManager;
import com.birb_birb.blockbounce.saveload.GameSaveData;
import com.birb_birb.blockbounce.saveload.GameStateCapture;
import com.birb_birb.blockbounce.saveload.SaveGameManager;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

import static com.almasb.fxgl.dsl.FXGLForKtKt.*;

public class StoryModeGame extends GameManager {

    private static final StoryModeGame INSTANCE = new StoryModeGame();
    private Text levelText;
    private int currentSaveSlot = 1; // Default save slot

    private StoryModeGame() {}

    public static void startGame() {
        if (GameMode.getCurrentGameMode() != GameMode.STORY) {
            System.err.println("Warning: StoryModeGame.start() called but current mode is " + GameMode.getCurrentGameMode());
        }
        INSTANCE.initialize();
    }

    /**
     * Start game from a saved state
     * @param slot Save slot to load from (1-3)
     */
    public static void startFromSave(int slot) {
        if (GameMode.getCurrentGameMode() != GameMode.STORY) {
            System.err.println("Warning: StoryModeGame.startFromSave() called but current mode is " + GameMode.getCurrentGameMode());
        }
        INSTANCE.currentSaveSlot = slot;
        INSTANCE.initialize();
        INSTANCE.loadGame(slot);
    }

    /**
     * Get the singleton instance
     */
    public static StoryModeGame getInstance() {
        return INSTANCE;
    }

    /**
     * Save current game state to a slot
     * @param slot Save slot number (1-3)
     */
    public boolean saveGame(int slot) {
        GameSaveData saveData = GameStateCapture.captureStoryModeState();
        boolean success = SaveGameManager.saveGame(slot, saveData);

        if (success) {
            currentSaveSlot = slot;
            displayMessage("Game Saved!", Color.LIGHTGREEN, 1.5, null);
        } else {
            displayMessage("Save Failed!", Color.RED, 1.5, null);
        }

        return success;
    }

    /**
     * Load game state from a slot
     * @param slot Save slot number (1-3)
     */
    public boolean loadGame(int slot) {
        GameSaveData saveData = SaveGameManager.loadGame(slot);

        if (saveData != null) {
            GameStateCapture.restoreStoryModeState(saveData);
            currentSaveSlot = slot;

            // Check if ball was launched when saved
            if (saveData.isBallLaunched()) {
                // Temporarily pause ball movement
                var balls = getGameWorld().getEntitiesByType(com.birb_birb.blockbounce.constants.EntityType.BALL);
                if (!balls.isEmpty()) {
                    com.birb_birb.blockbounce.entities.BallComponent ballComponent =
                        balls.get(0).getComponent(com.birb_birb.blockbounce.entities.BallComponent.class);
                    if (ballComponent != null) {
                        // Save current velocity
                        final javafx.geometry.Point2D savedVelocity = ballComponent.getVelocity();
                        // Temporarily stop the ball
                        ballComponent.setVelocity(new javafx.geometry.Point2D(0, 0));

                        // Show countdown 3-2-1-GO
                        displayCountdown(() -> {
                            // After countdown, restore ball velocity
                            if (ballComponent != null) {
                                ballComponent.setVelocity(savedVelocity);
                            }
                            displayMessage("Game Loaded!", Color.LIGHTBLUE, 1.0, null);
                        });
                    } else {
                        displayMessage("Game Loaded!", Color.LIGHTBLUE, 1.5, null);
                    }
                } else {
                    displayMessage("Game Loaded!", Color.LIGHTBLUE, 1.5, null);
                }
            } else {
                // Ball wasn't launched, just show normal message
                displayMessage("Game Loaded!", Color.LIGHTBLUE, 1.5, null);
            }

            return true;
        } else {
            displayMessage("Load Failed!", Color.RED, 1.5, null);
            return false;
        }
    }

    /**
     * Auto-save to current slot
     */
    public void autoSave() {
        saveGame(currentSaveSlot);
    }

    /**
     * Get current save slot
     */
    public int getCurrentSaveSlot() {
        return currentSaveSlot;
    }

    /**
     * Set current save slot
     */
    public void setCurrentSaveSlot(int slot) {
        if (slot >= 1 && slot <= 3) {
            this.currentSaveSlot = slot;
        }
    }

    @Override
    protected void setupProperties() {
        super.setupProperties();
        getWorldProperties().setValue("level", 1);
    }

    @Override
    protected void createFrame() {
        GameFactory.createStoryModeFrame();
    }

    @Override
    protected void setupUI() {
        super.setupUI();
        createLevelDisplay();
    }

    private void createLevelDisplay() {
        levelText = new Text("Level: 1");
        levelText.setFont(gameFont);
        levelText.setFill(GameConstants.FONT_COLOR);
        levelText.setTranslateX(GameConstants.WINDOW_WIDTH / 2.0 + 417);
        levelText.setTranslateY(56);
        getGameScene().addUINode(levelText);

        // Bind level text to property
        getWorldProperties().addListener("level", (prev, now) -> {
            levelText.setText("Level: " + now);
        });
    }

    @Override
    protected void setupGameLogic() {
        // Check for level completion when all bricks are destroyed
        getGameTimer().runAtInterval(() -> {
            if (getGameWorld().getEntitiesByType(com.birb_birb.blockbounce.constants.EntityType.BRICK).isEmpty()
                && !getb("gameOver")) {
                nextLevel();
            }
        }, javafx.util.Duration.seconds(0.5));
    }

    private void nextLevel() {
        inc("level", 1);

        // Auto-save when completing a level
        autoSave();

        // Show level up message
        displayMessage("LEVEL " + geti("level") + "!", (Color) GameConstants.FONT_COLOR, 2.0, null);

        // Remove old entities (except frame and walls)
        getGameWorld().getEntitiesByType(com.birb_birb.blockbounce.constants.EntityType.BALL)
            .forEach(Entity::removeFromWorld);
        getGameWorld().getEntitiesByType(com.birb_birb.blockbounce.constants.EntityType.PADDLE)
            .forEach(Entity::removeFromWorld);

        // Create new level
        GameFactory.createBricks();
        GameFactory.createPaddle();
        GameFactory.createBall();
    }
}
