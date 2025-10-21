package com.birb_birb.blockbounce.gamemode.story;

import com.almasb.fxgl.entity.Entity;
import com.birb_birb.blockbounce.constants.GameConstants;
import com.birb_birb.blockbounce.core.BlockBounceApp;
import com.birb_birb.blockbounce.core.GameFactory;
import com.birb_birb.blockbounce.ui.GameplayButtons;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

import static com.almasb.fxgl.dsl.FXGLForKtKt.*;
import static com.almasb.fxgl.dsl.FXGLForKtKt.getWorldProperties;

public class StoryModeGame {

    private static Text scoreText;
    private static Text livesText;
    private static Text levelText;

    public static void initialize() {

        if (BlockBounceApp.getCurrentGameMode() != BlockBounceApp.GameMode.STORY) {
            System.err.println("Warning: StoryModeGame.initialize() called but current mode is "
                    + BlockBounceApp.getCurrentGameMode());
        }

        // Clear all
        getGameWorld().getEntitiesCopy().forEach(Entity::removeFromWorld);
        getPhysicsWorld().clear();
        getGameTimer().clear();
        getWorldProperties().clear();

        // Khởi tạo game properties
        getWorldProperties().setValue("score", 0);
        getWorldProperties().setValue("lives", 3);
        getWorldProperties().setValue("level", 1);
        getWorldProperties().setValue("gameOver", false);

        // Set up new game
        GameFactory.createBackground();
        GameFactory.createWalls();
        GameFactory.createBricks();
        GameFactory.createPaddle();
        GameFactory.createBall();
        GameFactory.createStoryModeFrame();

        // UI elements
        VBox buttonPanel = GameplayButtons.createButtonPanel();
        buttonPanel.setTranslateX(GameConstants.WINDOW_WIDTH - 60);
        buttonPanel.setTranslateY(10);
        getGameScene().addUINode(buttonPanel);

        // Thêm UI hiển thị điểm số và lives
        setupGameUI();

        // Theo dõi game over
        getWorldProperties().addListener("gameOver", (prev, now) -> {
            if ((Boolean) now) {
                handleGameOver();
            }
        });

        // Theo dõi khi hết brick để chuyển level
        getGameTimer().runAtInterval(() -> {
            if (getGameWorld().getEntitiesByType(com.birb_birb.blockbounce.constants.EntityType.BRICK).isEmpty()
                && !getb("gameOver")) {
                nextLevel();
            }
        }, javafx.util.Duration.seconds(0.5));
    }

    private static void setupGameUI() {
        try {
            Font gameFont = Font.loadFont(
                getAssetLoader().getURL("fonts/Daydream.ttf").toExternalForm(), 24);

            // Score text
            scoreText = new Text("Score: 0");
            scoreText.setFont(gameFont);
            scoreText.setFill(Color.WHITE);
            scoreText.setTranslateX(GameConstants.OFFSET_LEFT + 20);
            scoreText.setTranslateY(30);
            getGameScene().addUINode(scoreText);

            // Lives text
            livesText = new Text("Lives: 3");
            livesText.setFont(gameFont);
            livesText.setFill(Color.WHITE);
            livesText.setTranslateX(GameConstants.OFFSET_LEFT + 20);
            livesText.setTranslateY(60);
            getGameScene().addUINode(livesText);

            // Level text
            levelText = new Text("Level: 1");
            levelText.setFont(gameFont);
            levelText.setFill(Color.WHITE);
            levelText.setTranslateX(GameConstants.WINDOW_WIDTH / 2 - 50);
            levelText.setTranslateY(30);
            getGameScene().addUINode(levelText);

            // Bind text to properties
            getWorldProperties().addListener("score", (prev, now) -> {
                scoreText.setText("Score: " + now);
            });

            getWorldProperties().addListener("lives", (prev, now) -> {
                livesText.setText("Lives: " + now);
            });

            getWorldProperties().addListener("level", (prev, now) -> {
                levelText.setText("Level: " + now);
            });

        } catch (Exception e) {
            System.err.println("Failed to setup game UI: " + e.getMessage());
        }
    }

    private static void handleGameOver() {
        // Hiển thị màn hình game over
        Text gameOverText = new Text("GAME OVER");
        try {
            Font gameFont = Font.loadFont(
                getAssetLoader().getURL("fonts/Daydream.ttf").toExternalForm(), 48);
            gameOverText.setFont(gameFont);
        } catch (Exception e) {
            gameOverText.setFont(Font.font(48));
        }
        gameOverText.setFill(Color.RED);
        gameOverText.setTranslateX(GameConstants.WINDOW_WIDTH / 2 - 150);
        gameOverText.setTranslateY(GameConstants.WINDOW_HEIGHT / 2);
        getGameScene().addUINode(gameOverText);

        // Quay về menu sau 3 giây
        getGameTimer().runOnceAfter(() -> {
            getGameController().gotoMainMenu();
        }, javafx.util.Duration.seconds(3));
    }

    private static void nextLevel() {
        inc("level", 1);

        // Xóa các entity cũ (trừ frame và walls)
        getGameWorld().getEntitiesByType(com.birb_birb.blockbounce.constants.EntityType.BALL)
            .forEach(Entity::removeFromWorld);
        getGameWorld().getEntitiesByType(com.birb_birb.blockbounce.constants.EntityType.PADDLE)
            .forEach(Entity::removeFromWorld);
        getGameWorld().getEntitiesByType(com.birb_birb.blockbounce.constants.EntityType.BRICK)
            .forEach(Entity::removeFromWorld);

        // Tạo lại bricks, paddle, và ball
        GameFactory.createBricks();
        GameFactory.createPaddle();
        GameFactory.createBall();

        // Hiển thị level text
        Text levelUpText = new Text("LEVEL " + geti("level"));
        try {
            Font gameFont = Font.loadFont(
                getAssetLoader().getURL("fonts/Daydream.ttf").toExternalForm(), 48);
            levelUpText.setFont(gameFont);
        } catch (Exception e) {
            levelUpText.setFont(Font.font(48));
        }
        levelUpText.setFill(Color.YELLOW);
        levelUpText.setTranslateX(GameConstants.WINDOW_WIDTH / 2 - 120);
        levelUpText.setTranslateY(GameConstants.WINDOW_HEIGHT / 2);
        getGameScene().addUINode(levelUpText);

        // Xóa text sau 2 giây
        getGameTimer().runOnceAfter(() -> {
            getGameScene().removeUINode(levelUpText);
        }, javafx.util.Duration.seconds(2));
    }
}
