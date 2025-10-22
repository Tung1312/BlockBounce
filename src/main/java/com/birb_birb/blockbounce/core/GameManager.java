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

import static com.almasb.fxgl.dsl.FXGLForKtKt.*;

public abstract class GameManager {

    protected Text scoreText;
    protected Text livesText;
    protected Font gameFont = FontManager.getCustomFont();

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
     * Displays a message on screen (temporary).
     */
    protected void displayMessage(String message, Color color, double durationSeconds) {
        Text messageText = new Text(message);
        try {
            Font messageFont = Font.loadFont(
                getAssetLoader().getURL("fonts/Daydream.ttf").toExternalForm(), 36);
            messageText.setFont(messageFont);
        } catch (Exception e) {
            messageText.setFont(Font.font(36));
        }
        messageText.setFill(color);
        messageText.setTranslateX((double) GameConstants.WINDOW_WIDTH / 2 - 100);
        messageText.setTranslateY((double) GameConstants.WINDOW_HEIGHT / 2);
        getGameScene().addUINode(messageText);

        getGameTimer().runOnceAfter(() -> {
            getGameScene().removeUINode(messageText);
        }, javafx.util.Duration.seconds(durationSeconds));
    }
}
