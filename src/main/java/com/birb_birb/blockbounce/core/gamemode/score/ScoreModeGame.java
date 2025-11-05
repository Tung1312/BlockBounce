package com.birb_birb.blockbounce.core.gamemode.score;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.birb_birb.blockbounce.constants.BrickType;
import com.birb_birb.blockbounce.constants.GameMode;
import com.birb_birb.blockbounce.constants.GameConstants;
import com.birb_birb.blockbounce.constants.EntityType;
import com.birb_birb.blockbounce.core.GameFactory;
import com.birb_birb.blockbounce.core.GameManager;
import com.birb_birb.blockbounce.entities.BallComponent;
import com.birb_birb.blockbounce.entities.BrickComponent;
import com.birb_birb.blockbounce.utils.SoundManager;
import com.birb_birb.blockbounce.utils.saveload.SaveData;
import com.birb_birb.blockbounce.utils.saveload.StateCapture;
import com.birb_birb.blockbounce.utils.saveload.SaveGameManager;
import com.birb_birb.blockbounce.utils.saveload.RandomLevelLoader;
import com.birb_birb.blockbounce.utils.saveload.LevelData;
import com.birb_birb.blockbounce.utils.highscore.HighScoreManager;
import com.birb_birb.blockbounce.ui.HighScoreInput;
import com.birb_birb.blockbounce.utils.MenuManager;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

import static com.almasb.fxgl.dsl.FXGLForKtKt.*;

public class ScoreModeGame extends GameManager {

    private static final ScoreModeGame INSTANCE = new ScoreModeGame();
    private Text highScoreText;
    private Text timerText;
    private double elapsedTime = 0;
    private boolean timerStarted = false;
    private int currentSaveSlot = 1; // Default save slot
    private RandomLevelLoader randomLevelLoader; // For infinite level generation

    private ScoreModeGame() {
        randomLevelLoader = new RandomLevelLoader();
    }

    public static void startGame() {
        if (GameMode.getCurrentGameMode() != GameMode.ENDLESS) {
            System.err.println("Warning: ScoreModeGame.start() called but current mode is " + GameMode.getCurrentGameMode());
        }
        INSTANCE.initialize();

        // Check if we should load save after initialization
        if (GameMode.shouldLoadSave()) {
            GameMode.setShouldLoadSave(false); // Reset flag

            // Add dimming overlay immediately after initialization
            javafx.scene.Node loadingOverlay = MenuManager.createDimmingOverlay(1);
            getGameScene().addUINode(loadingOverlay);

            // Show loading message immediately
            INSTANCE.displayMessage("Loading...", Color.CYAN, 0.5, null);

            // Delay load to ensure game is fully initialized and loading message is visible
            getGameTimer().runOnceAfter(() -> {
                INSTANCE.loadGame(1);
                // Remove the loading overlay after load completes
                getGameScene().removeUINode(loadingOverlay);
            }, javafx.util.Duration.millis(500));
        }
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
        // Do not allow saving if game is over or lives <= 0
        if (getb("gameOver") || geti("lives") <= 0) {
            displayMessage("Cannot save after Game Over", Color.ORANGE, 1.5, null);
            return false;
        }
        SaveData saveData =
            StateCapture.captureScoreModeState(elapsedTime);
        boolean success = SaveGameManager.saveScoreGame(slot, saveData);

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
        SaveData saveData =
            SaveGameManager.loadScoreGame(slot);

        if (saveData != null) {
            // Do not load saves that represent a finished game
            if (saveData.getLives() <= 0) {
                displayMessage("Cannot load: save is Game Over", Color.ORANGE, 1.8, null);
                return false;
            }
            StateCapture.RestoreResult result =
                StateCapture.restoreScoreModeState(saveData);

            if (result != null) {
                elapsedTime = result.getElapsedTime();
                timerStarted = result.isTimerStarted();
                updateTimerDisplay();
                currentSaveSlot = slot;

                // If ball was launched when saved, pause it and show countdown
                if (result.isTimerStarted()) {
                    // Add dimming overlay for countdown (loading overlay was already added)
                    javafx.scene.Node dimmingOverlay = MenuManager.createDimmingOverlay();
                    getGameScene().addUINode(dimmingOverlay);

                    // Temporarily pause ball movement
                    var balls = getGameWorld().getEntitiesByType(EntityType.BALL);
                    if (!balls.isEmpty()) {
                        BallComponent ballComponent =
                            balls.get(0).getComponent(BallComponent.class);
                        if (ballComponent != null) {
                            // Save current velocity
                            final javafx.geometry.Point2D savedVelocity = ballComponent.getVelocity();
                            // Temporarily stop the ball
                            ballComponent.setVelocity(new javafx.geometry.Point2D(0, 0));

                            // Show countdown 3-2-1-GO
                            displayCountdown(() -> {
                                // After countdown, restore ball velocity and remove overlay
                                if (ballComponent != null) {
                                    ballComponent.setVelocity(savedVelocity);
                                }
                                getGameScene().removeUINode(dimmingOverlay);
                            });
                        } else {
                            getGameScene().removeUINode(dimmingOverlay);
                        }
                    } else {
                        getGameScene().removeUINode(dimmingOverlay);
                    }
                } else {
                    // Ball wasn't launched, no countdown needed
                    // Loading overlay will be removed by the caller
                }

                return true;
            }
        }

        displayMessage("Load Failed!", Color.RED, 1.5, null);
        return false;
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
        highScoreText = new Text("0");
        highScoreText.setFont(gameFont);
        highScoreText.setFill(Color.YELLOW);
        highScoreText.setTranslateX(GameConstants.WINDOW_WIDTH - 200);
        highScoreText.setTranslateY(56);
        getGameScene().addUINode(highScoreText);

        // Bind high score text to property
        getWorldProperties().addListener("highScore", (prev, now) -> {
            highScoreText.setText(String.valueOf(now));
        });
    }

    private void createTimerDisplay() {
        timerText = new Text("Time: 00:00");
        timerText.setFont(gameFont);
        timerText.setFill(Color.WHITE);
        timerText.setTranslateX(GameConstants.WINDOW_WIDTH / 2.0 - 126);
        timerText.setTranslateY(56);
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
            long destructibleBricks = getGameWorld()
                .getEntitiesByType(EntityType.BRICK)
                .stream()
                .filter(brick -> {
                    BrickComponent brickComp = brick.getComponent(BrickComponent.class);
                    return brickComp != null && brickComp.getBrickType() != BrickType.OBSIDIAN;
                })
                .count();

            if (destructibleBricks == 0 && !getb("gameOver")) {
                // Before spawning next level, attach the ball to the paddle
                var balls = getGameWorld().getEntitiesByType(EntityType.BALL);
                Entity mainBall;
                if (balls.isEmpty()) {
                    mainBall = GameFactory.createBall();
                } else {
                    mainBall = balls.getFirst();
                    // remove any extra balls from DOUBLE_BALL, keep gameplay predictable between levels
                    for (int i = 1; i < balls.size(); i++) {
                        balls.get(i).removeFromWorld();
                    }
                }
                if (mainBall != null) {
                    BallComponent ballComp = mainBall.getComponent(BallComponent.class);
                    if (ballComp != null) {
                        ballComp.attachToPaddle();
                    }
                }

                // Load a random level from storage
                LevelData randomLevel = randomLevelLoader.loadRandomLevel();
                if (randomLevel != null) {
                    // Create bricks based on the loaded level data
                    GameFactory.createBricksFromData(randomLevel);

                    // Show continue message (no completion callback)
                    displayMessage("CONTINUE!", Color.LIGHTGREEN, 1.5, null);
                } else {
                    // Fallback to default behavior if no random level is found
                    GameFactory.createBricks();
                    displayMessage("CONTINUE!", Color.LIGHTGREEN, 1.5, null);
                }
            }
        }, javafx.util.Duration.seconds(0.5));
    }

    private void updateTimerDisplay() {
        int totalSeconds = (int) elapsedTime;
        int minutes = totalSeconds / 60;
        int seconds = totalSeconds % 60;
        timerText.setText(String.format("Time: %02d:%02d", minutes, seconds));
    }

    @Override
    protected void handleGameOver() {
        SoundManager.playCompleteSound();

        // Add dimming overlay
        getGameScene().addUINode(MenuManager.createDimmingOverlay());

        // Display final time in game over screen
        int totalSeconds = (int) elapsedTime;
        int minutes = totalSeconds / 60;
        int seconds = totalSeconds % 60;

        int finalScore = geti("score");

        Text gameOverText = new Text("GAME OVER");
        Text finalScoreText = new Text("Final Score: " + finalScore);
        Text finalTimeText = new Text(String.format("Time: %02d:%02d", minutes, seconds));

        gameOverText.setFont(displayFont);
        finalScoreText.setFont(displayFont);
        finalTimeText.setFont(displayFont);

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

        getGameController().pauseEngine();

        // If it's a high score, prompt for name; otherwise return to menu after a delay
        if (HighScoreManager.isHighScore(finalScore)) {
            int rank = HighScoreManager.getScoreRank(finalScore);

            // display score for 3s then open dialog to enter name
            new Thread(() -> {
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }

                javafx.application.Platform.runLater(() -> {
                    HighScoreInput dialog = new HighScoreInput(
                        finalScore,
                        rank,
                        name -> {
                            HighScoreManager.addHighScore(name, finalScore);
                            FXGL.getSceneService().popSubScene();

                            Text savedText = new Text("High Score Saved!");
                            savedText.setFont(displayFont);
                            savedText.setFill(Color.LIGHTGREEN);
                            savedText.setTranslateX((double) GameConstants.WINDOW_WIDTH / 2 - 150);
                            savedText.setTranslateY((double) GameConstants.WINDOW_HEIGHT / 2 + 120);
                            getGameScene().addUINode(savedText);

                            // wait 3s then return to menu
                            new Thread(() -> {
                                try {
                                    Thread.sleep(3000);
                                } catch (InterruptedException e) {
                                    Thread.currentThread().interrupt();
                                }

                                javafx.application.Platform.runLater(() -> {
                                    getGameController().resumeEngine();
                                    getGameController().gotoMainMenu();
                                });
                            }).start();
                        },
                        () -> {
                            // if cancelled: close dialog and wait 1s before going back
                            FXGL.getSceneService().popSubScene();

                            new Thread(() -> {
                                try {
                                    Thread.sleep(1000);
                                } catch (InterruptedException e) {
                                    Thread.currentThread().interrupt();
                                }

                                javafx.application.Platform.runLater(() -> {
                                    getGameController().resumeEngine();
                                    getGameController().gotoMainMenu();
                                });
                            }).start();
                        }
                    );

                    FXGL.getSceneService().pushSubScene(dialog);
                });
            }).start();
        } else {
            // Non-high score: wait 3 seconds then return to menu
            new Thread(() -> {
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }

                javafx.application.Platform.runLater(() -> {
                    getGameController().resumeEngine();
                    getGameController().gotoMainMenu();
                });
            }).start();
        }
    }
}

