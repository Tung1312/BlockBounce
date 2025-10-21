package com.birb_birb.blockbounce.gamemode.versus;

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

public class VersusModeGame {

    private static Text scoreText;
    private static Text livesText;

    public static void initialize() {

        if (BlockBounceApp.getCurrentGameMode() != BlockBounceApp.GameMode.VERSUS) {
            System.err.println("Warning: VersusModeGame.initialize() called but current mode is "
                    + BlockBounceApp.getCurrentGameMode());
        }

        // Clear all
        getGameWorld().getEntitiesCopy().forEach(Entity::removeFromWorld);
        getPhysicsWorld().clear();
        getGameTimer().clear();
        getWorldProperties().clear();

        // Khởi tạo game properties
        getWorldProperties().setValue("score", 0);
        getWorldProperties().setValue("lives", 5); // Versus mode có nhiều mạng hơn
        getWorldProperties().setValue("gameOver", false);

        // Set up new game
        GameFactory.createBackground();
        GameFactory.createWalls();
        GameFactory.createBricks();
        GameFactory.createPaddle();
        GameFactory.createBall();
        GameFactory.createVersusModeFrame();

        // UI elements
        VBox buttonPanel = GameplayButtons.createButtonPanel();
        buttonPanel.setTranslateX((GameConstants.WINDOW_WIDTH / 2.0) - 26);
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

        // Versus mode: khi hết brick, tự động tạo lại
        getGameTimer().runAtInterval(() -> {
            if (getGameWorld().getEntitiesByType(com.birb_birb.blockbounce.constants.EntityType.BRICK).isEmpty()
                && !getb("gameOver")) {
                respawnBricks();
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
            livesText = new Text("Lives: 5");
            livesText.setFont(gameFont);
            livesText.setFill(Color.WHITE);
            livesText.setTranslateX(GameConstants.OFFSET_LEFT + 20);
            livesText.setTranslateY(60);
            getGameScene().addUINode(livesText);

            // Bind text to properties
            getWorldProperties().addListener("score", (prev, now) -> {
                scoreText.setText("Score: " + now);
            });

            getWorldProperties().addListener("lives", (prev, now) -> {
                livesText.setText("Lives: " + now);
            });

        } catch (Exception e) {
            System.err.println("Failed to setup game UI: " + e.getMessage());
        }
    }

    private static void handleGameOver() {
        // Hiển thị màn hình game over
        Text gameOverText = new Text("GAME OVER");
        Text finalScoreText = new Text("Score: " + geti("score"));

        try {
            Font gameFontBig = Font.loadFont(
                getAssetLoader().getURL("fonts/Daydream.ttf").toExternalForm(), 48);
            Font gameFontSmall = Font.loadFont(
                getAssetLoader().getURL("fonts/Daydream.ttf").toExternalForm(), 32);
            gameOverText.setFont(gameFontBig);
            finalScoreText.setFont(gameFontSmall);
        } catch (Exception e) {
            gameOverText.setFont(Font.font(48));
            finalScoreText.setFont(Font.font(32));
        }

        gameOverText.setFill(Color.RED);
        gameOverText.setTranslateX(GameConstants.WINDOW_WIDTH / 2 - 150);
        gameOverText.setTranslateY(GameConstants.WINDOW_HEIGHT / 2 - 30);

        finalScoreText.setFill(Color.WHITE);
        finalScoreText.setTranslateX(GameConstants.WINDOW_WIDTH / 2 - 100);
        finalScoreText.setTranslateY(GameConstants.WINDOW_HEIGHT / 2 + 20);

        getGameScene().addUINode(gameOverText);
        getGameScene().addUINode(finalScoreText);

        // Quay về menu sau 3 giây
        getGameTimer().runOnceAfter(() -> {
            getGameController().gotoMainMenu();
        }, javafx.util.Duration.seconds(3));
    }

    private static void respawnBricks() {
        // Tạo lại bricks cho versus mode
        GameFactory.createBricks();
    }
}
