package com.birb_birb.blockbounce.core;

import com.almasb.fxgl.entity.Entity;
import com.birb_birb.blockbounce.constants.GameConstants;
import com.birb_birb.blockbounce.constants.GameMode;
import com.birb_birb.blockbounce.ui.GameplayButtons;
import com.birb_birb.blockbounce.utils.FontManager;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.FontWeight;

import static com.almasb.fxgl.dsl.FXGLForKtKt.*;

public abstract class GameManager {

    protected Text scoreText;
    protected Text livesText;
    protected Font gameFont = FontManager.getCustomFont();

    private final java.util.ArrayDeque<Message> messageQueue = new java.util.ArrayDeque<>();
    private boolean messageActive = false;

    private static class Message {
        final String text;
        final Color color;
        final double durationSeconds;
        final Runnable onComplete;

        Message(String text, Color color, double durationSeconds, Runnable onComplete) {
            this.text = text;
            this.color = color;
            this.durationSeconds = durationSeconds;
            this.onComplete = onComplete;
        }
    }

    public void initialize() {
        clearAll();
        setupProperties();
        setupNewGame();
        setupUI();
        setupGameplayButtons();
        setupGameLogic();
    }

    protected void clearAll() {
        getGameWorld().getEntitiesCopy().forEach(Entity::removeFromWorld);
        getPhysicsWorld().clear();
        getGameTimer().clear();
        getWorldProperties().clear();
    }

    protected void setupProperties() {
        getWorldProperties().setValue("score", 0);
        getWorldProperties().setValue("lives", 3);
        getWorldProperties().setValue("gameOver", false);
    }

    protected void setupNewGame() {
        GameFactory.createBackground();
        GameFactory.createWalls();
        GameFactory.createBricks();
        GameFactory.createPaddle();
        GameFactory.createBall();
    }

    protected abstract void createFrame();

    protected void setupUI() {
        createFrame();

        // Don't show the single-player global score/lives UI in Versus mode.
        if (GameMode.getCurrentGameMode() == GameMode.VERSUS) {
            return;
        }

        // Score display
        scoreText = new Text("Score: 0");
        scoreText.setFont(gameFont);
        scoreText.setFill(Color.WHITE);
        scoreText.setTranslateX(GameConstants.OFFSET_LEFT + 20);
        scoreText.setTranslateY(30);
        getGameScene().addUINode(scoreText);

        // Lives display
        livesText = new Text("Lives: 3");
        livesText.setFont(gameFont);
        livesText.setFill(Color.WHITE);
        livesText.setTranslateX(GameConstants.OFFSET_LEFT + 20);
        livesText.setTranslateY(60);
        getGameScene().addUINode(livesText);

        // Bind UI to properties
        getWorldProperties().addListener("score", (prev, now) -> {
            scoreText.setText("Score: " + now);
            onScoreChanged((Integer) prev, (Integer) now);
        });

        getWorldProperties().addListener("lives", (prev, now) -> {
            livesText.setText("Lives: " + now);
            onLivesChanged((Integer) prev, (Integer) now);
        });

        getWorldProperties().addListener("gameOver", (prev, now) -> {
            if ((Boolean) now) {
                handleGameOver();
            }
        });
    }

    protected void setupGameplayButtons() {
        VBox buttonPanel = GameplayButtons.createButtonPanel(GameMode.getCurrentGameMode());
        buttonPanel.setTranslateX(GameConstants.WINDOW_WIDTH - 60);
        buttonPanel.setTranslateY(10);
        getGameScene().addUINode(buttonPanel);
    }

    protected abstract void setupGameLogic();

    protected void onScoreChanged(int oldScore, int newScore) {
        // does nothing
    }

    protected void onLivesChanged(int oldLives, int newLives) {
        // does nothing
    }

    /**
     * Handler for Game Over state.
     */
    protected void handleGameOver() {
        Text gameOverText = new Text("GAME OVER");
        Text finalScoreText = new Text("Final Score: " + geti("score"));

        gameOverText.setFont(gameFont);
        finalScoreText.setFont(gameFont);

        gameOverText.setFill(Color.RED);
        gameOverText.setTranslateX((double) GameConstants.WINDOW_WIDTH / 2 - 150);
        gameOverText.setTranslateY((double) GameConstants.WINDOW_HEIGHT / 2 - 30);

        finalScoreText.setFill(Color.WHITE);
        finalScoreText.setTranslateX((double) GameConstants.WINDOW_WIDTH / 2 - 150);
        finalScoreText.setTranslateY((double) GameConstants.WINDOW_HEIGHT / 2 + 20);

        getGameScene().addUINode(gameOverText);
        getGameScene().addUINode(finalScoreText);

        // Return to menu after 3 seconds
        getGameTimer().runOnceAfter(() -> {
            getGameController().gotoMainMenu();
        }, javafx.util.Duration.seconds(3));
    }

    /**
     * Enqueue a message and optional completion callback that runs after the
     * message is displayed and the 3s gap elapses.
     */
    protected void displayMessage(String message, Color color, double durationSeconds, Runnable onComplete) {
        // Enqueue the message and start processing if idle
        messageQueue.addLast(new Message(message, color, durationSeconds, onComplete));
        if (!messageActive) {
            processNextMessage();
        }
    }

    /**
     * Display countdown 3-2-1-GO before running onComplete callback
     */
    protected void displayCountdown(Runnable onComplete) {
        displayCountdownNumber(3, () -> {
            displayCountdownNumber(2, () -> {
                displayCountdownNumber(1, () -> {
                    // After showing 1, show GO! then run callback
                    displayCountdownGo(onComplete);
                });
            });
        });
    }

    private void displayCountdownNumber(int number, Runnable onComplete) {
        Text countdownText = new Text(String.valueOf(number));
        try {
            Font messageFont = Font.loadFont(
                getAssetLoader().getURL("fonts/Daydream.ttf").toExternalForm(), 80);
            countdownText.setFont(Font.font(messageFont.getFamily(), FontWeight.BOLD, 80));
        } catch (Exception e) {
            countdownText.setFont(Font.font("System", FontWeight.BOLD, 80));
        }

        countdownText.setFill(Color.YELLOW);
        countdownText.applyCss();
        double textWidth = countdownText.getLayoutBounds().getWidth();
        double centerX = ((double) GameConstants.WINDOW_WIDTH - textWidth) / 2.0;
        countdownText.setTranslateX(centerX);
        countdownText.setTranslateY((double) GameConstants.WINDOW_HEIGHT / 2);
        getGameScene().addUINode(countdownText);

        // Show for 0.8 seconds then remove and call next
        getGameTimer().runOnceAfter(() -> {
            getGameScene().removeUINode(countdownText);
            if (onComplete != null) {
                onComplete.run();
            }
        }, javafx.util.Duration.millis(800));
    }

    private void displayCountdownGo(Runnable onComplete) {
        Text goText = new Text("GO!");
        try {
            Font messageFont = Font.loadFont(
                getAssetLoader().getURL("fonts/Daydream.ttf").toExternalForm(), 80);
            goText.setFont(Font.font(messageFont.getFamily(), FontWeight.BOLD, 80));
        } catch (Exception e) {
            goText.setFont(Font.font("System", FontWeight.BOLD, 80));
        }

        goText.setFill(Color.LIGHTGREEN);
        goText.applyCss();
        double textWidth = goText.getLayoutBounds().getWidth();
        double centerX = ((double) GameConstants.WINDOW_WIDTH - textWidth) / 2.0;
        goText.setTranslateX(centerX);
        goText.setTranslateY((double) GameConstants.WINDOW_HEIGHT / 2);
        getGameScene().addUINode(goText);

        // Show for 0.5 seconds then remove and run callback
        getGameTimer().runOnceAfter(() -> {
            getGameScene().removeUINode(goText);
            if (onComplete != null) {
                onComplete.run();
            }
        }, javafx.util.Duration.millis(500));
    }

    // Process next message from the queue (internal)
    private void processNextMessage() {
        Message msg = messageQueue.pollFirst();
        if (msg == null) {
            messageActive = false;
            return;
        }

        messageActive = true;

        Text messageText = new Text(msg.text);
        try {
            // Load custom font if available, otherwise use default
            Font messageFont = Font.loadFont(
                getAssetLoader().getURL("fonts/Daydream.ttf").toExternalForm(), 48);
            messageText.setFont(Font.font(messageFont.getFamily(), FontWeight.BOLD, 48));
        } catch (Exception e) {
            messageText.setFont(Font.font("System", FontWeight.BOLD, 48));
        }

        messageText.setFill(msg.color);
        // Precisely center the message horizontally by measuring text width
        messageText.applyCss();
        double textWidth = messageText.getLayoutBounds().getWidth();
        double centerX = ((double) GameConstants.WINDOW_WIDTH - textWidth) / 2.0;
        messageText.setTranslateX(centerX);
        messageText.setTranslateY((double) GameConstants.WINDOW_HEIGHT / 2);
        getGameScene().addUINode(messageText);

        // Remove the message after the requested display duration, then wait 3s gap
        getGameTimer().runOnceAfter(() -> {
            getGameScene().removeUINode(messageText);

            // After removal, wait 3 seconds before showing the next message
            getGameTimer().runOnceAfter(() -> {
                // If this message has a completion callback, run it now
                try {
                    if (msg.onComplete != null) {
                        msg.onComplete.run();
                    }
                } catch (Exception ignored) {}

                messageActive = false;
                processNextMessage();
            }, javafx.util.Duration.seconds(3.0));

        }, javafx.util.Duration.seconds(msg.durationSeconds));
    }

}
