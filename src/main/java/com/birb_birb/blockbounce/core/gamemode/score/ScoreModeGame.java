package com.birb_birb.blockbounce.core.gamemode.score;

import com.birb_birb.blockbounce.constants.GameMode;
import com.birb_birb.blockbounce.constants.GameConstants;
import com.birb_birb.blockbounce.constants.EntityType;
import com.birb_birb.blockbounce.core.GameFactory;
import com.birb_birb.blockbounce.core.GameManager;
import com.birb_birb.blockbounce.entities.BallComponent;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

import static com.almasb.fxgl.dsl.FXGLForKtKt.*;
import static javafx.scene.paint.Color.rgb;

public class ScoreModeGame extends GameManager {

    private static final ScoreModeGame INSTANCE = new ScoreModeGame();
    private Text highScoreText;
    private Text timerText;
    private double elapsedTime = 0;
    private boolean timerStarted = false;
    private int currentSaveSlot = 1; // Default save slot

    private ScoreModeGame() {}

    public static void startGame() {
        if (GameMode.getCurrentGameMode() != GameMode.ENDLESS) {
            System.err.println("Warning: ScoreModeGame.start() called but current mode is " + GameMode.getCurrentGameMode());
        }
        INSTANCE.initialize();

        // Check if we should load save after initialization
        if (GameMode.shouldLoadSave()) {
            GameMode.setShouldLoadSave(false); // Reset flag

            // Show loading message immediately
            INSTANCE.displayMessage("Loading...", javafx.scene.paint.Color.CYAN, 0.5, null);

            // Delay load to ensure game is fully initialized and loading message is visible
            getGameTimer().runOnceAfter(() -> {
                INSTANCE.loadGame(1);
            }, javafx.util.Duration.millis(500));
        }
    }

    /**
     * Start game from a saved state
     * @param slot Save slot to load from (1-3)
     */
    public static void startFromSave(int slot) {
        if (GameMode.getCurrentGameMode() != GameMode.ENDLESS) {
            System.err.println("Warning: ScoreModeGame.startFromSave() called but current mode is " + GameMode.getCurrentGameMode());
        }
        INSTANCE.currentSaveSlot = slot;
        INSTANCE.initialize();
        INSTANCE.loadGame(slot);
    }

    /**
     * Get the singleton instance
     */
    public static ScoreModeGame getInstance() {
        return INSTANCE;
    }

    /**
     * Save current game state to a slot
     * @param slot Save slot number (1-3)
     */
    public boolean saveGame(int slot) {
        com.birb_birb.blockbounce.saveload.GameSaveData saveData =
            com.birb_birb.blockbounce.saveload.GameStateCapture.captureScoreModeState(elapsedTime);
        boolean success = com.birb_birb.blockbounce.saveload.SaveGameManager.saveScoreGame(slot, saveData);

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
        com.birb_birb.blockbounce.saveload.GameSaveData saveData =
            com.birb_birb.blockbounce.saveload.SaveGameManager.loadScoreGame(slot);

        if (saveData != null) {
            com.birb_birb.blockbounce.saveload.GameStateCapture.RestoreResult result =
                com.birb_birb.blockbounce.saveload.GameStateCapture.restoreScoreModeState(saveData);

            if (result != null) {
                elapsedTime = result.getElapsedTime();
                timerStarted = result.isTimerStarted();
                updateTimerDisplay();
                currentSaveSlot = slot;

                // If ball was launched when saved, pause it and show countdown
                if (result.isTimerStarted()) {
                    // Temporarily pause ball movement
                    var balls = getGameWorld().getEntitiesByType(EntityType.BALL);
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
            }
        }

        displayMessage("Load Failed!", Color.RED, 1.5, null);
        return false;
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
        getWorldProperties().setValue("highScore", 0);
        elapsedTime = 0;
        timerStarted = false;
    }

    @Override
    protected void createFrame() {
        GameFactory.createScoreModeFrame();
    }

    @Override
    protected void setupUI() {
        super.setupUI();
        createHighScoreDisplay();
        createTimerDisplay();
    }

    private void createHighScoreDisplay() {
        highScoreText = new Text("High: 0");
        highScoreText.setFont(gameFont);
        highScoreText.setFill(Color.YELLOW);
        highScoreText.setTranslateX(GameConstants.WINDOW_WIDTH / 2.0 - 60);
        highScoreText.setTranslateY(30);
        getGameScene().addUINode(highScoreText);

        // Bind high score text to property
        getWorldProperties().addListener("highScore", (prev, now) -> {
            highScoreText.setText("High: " + now);
        });
    }

    private void createTimerDisplay() {
        timerText = new Text("00:00");
        timerText.setFont(gameFont);
        timerText.setFill(rgb(62, 32, 31));
        timerText.setTranslateX(GameConstants.WINDOW_WIDTH - 200);
        timerText.setTranslateY(30);
        getGameScene().addUINode(timerText);
    }

    @Override
    protected void onScoreChanged(int oldScore, int newScore) {
        // Update high score if current score exceeds it
        if (newScore > geti("highScore")) {
            set("highScore", newScore);
        }
    }

    @Override
    protected void setupGameLogic() {
        // Update timer every frame - only if ball has been launched
        getGameTimer().runAtInterval(() -> {
            if (!getb("gameOver")) {
                // Check if ball has been launched
                if (!timerStarted) {
                    var balls = getGameWorld().getEntitiesByType(EntityType.BALL);
                    if (!balls.isEmpty()) {
                        BallComponent ballComponent = balls.get(0).getComponent(BallComponent.class);
                        if (ballComponent != null && ballComponent.hasLaunched()) {
                            timerStarted = true;
                        }
                    }
                }

                // Only update elapsed time if timer has started
                if (timerStarted) {
                    elapsedTime += 0.016; // Approximately 60 FPS (1/60 second)
                    updateTimerDisplay();
                }
            }
        }, javafx.util.Duration.millis(16));

        // Automatically spawn more bricks when all are destroyed (endless mode)
        getGameTimer().runAtInterval(() -> {
            if (getGameWorld().getEntitiesByType(EntityType.BRICK).isEmpty()
                && !getb("gameOver")) {
                spawnMoreBricks();
                // Show continue message (no completion callback)
                displayMessage("CONTINUE!", Color.LIGHTGREEN, 1.5, null);
            }
        }, javafx.util.Duration.seconds(0.5));
    }

    private void updateTimerDisplay() {
        int totalSeconds = (int) elapsedTime;
        int minutes = totalSeconds / 60;
        int seconds = totalSeconds % 60;
        timerText.setText(String.format("Time: %02d:%02d", minutes, seconds));
    }

    private void spawnMoreBricks() {
        // Create new bricks for endless mode
        GameFactory.createBricks();

        // Show continue message
        displayMessage("CONTINUE!", Color.LIGHTGREEN, 1.5, null);
    }

    @Override
    protected void handleGameOver() {
        // Display final time in game over screen
        int totalSeconds = (int) elapsedTime;
        int minutes = totalSeconds / 60;
        int seconds = totalSeconds % 60;

        Text gameOverText = new Text("GAME OVER");
        Text finalScoreText = new Text("Final Score: " + geti("score"));
        Text finalTimeText = new Text(String.format("Time: %02d:%02d", minutes, seconds));

        gameOverText.setFont(gameFont);
        finalScoreText.setFont(gameFont);
        finalTimeText.setFont(gameFont);

        gameOverText.setFill(Color.RED);
        finalScoreText.setFill(Color.WHITE);
        finalTimeText.setFill(Color.CYAN);

        gameOverText.setTranslateX((double) GameConstants.WINDOW_WIDTH / 2 - 150);
        gameOverText.setTranslateY((double) GameConstants.WINDOW_HEIGHT / 2 - 50);

        finalScoreText.setTranslateX((double) GameConstants.WINDOW_WIDTH / 2 - 150);
        finalScoreText.setTranslateY((double) GameConstants.WINDOW_HEIGHT / 2);

        finalTimeText.setTranslateX((double) GameConstants.WINDOW_WIDTH / 2 - 150);
        finalTimeText.setTranslateY((double) GameConstants.WINDOW_HEIGHT / 2 + 50);

        getGameScene().addUINode(gameOverText);
        getGameScene().addUINode(finalScoreText);
        getGameScene().addUINode(finalTimeText);

        // Return to menu after 3 seconds
        getGameTimer().runOnceAfter(() -> {
            getGameController().gotoMainMenu();
        }, javafx.util.Duration.seconds(3));
    }
}
