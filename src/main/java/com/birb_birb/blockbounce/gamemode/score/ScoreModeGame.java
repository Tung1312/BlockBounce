package com.birb_birb.blockbounce.gamemode.score;

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

public class ScoreModeGame {

    private static Text scoreText;
    private static Text livesText;
    private static Text highScoreText;

    public static void initialize() {

        if (BlockBounceApp.getCurrentGameMode() != BlockBounceApp.GameMode.ENDLESS) {
            System.err.println("Warning: ScoreModeGame.initialize() called but current mode is "
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
        getWorldProperties().setValue("highScore", 0);
        getWorldProperties().setValue("gameOver", false);

        // Set up new game
        GameFactory.createBackground();
        GameFactory.createWalls();
        GameFactory.createBricks();
        GameFactory.createPaddle();
        GameFactory.createBall();
        GameFactory.createScoreModeFrame();

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

        // Tự động tạo thêm brick khi hết (endless mode)
        getGameTimer().runAtInterval(() -> {
            if (getGameWorld().getEntitiesByType(com.birb_birb.blockbounce.constants.EntityType.BRICK).isEmpty()
                && !getb("gameOver")) {
                spawnMoreBricks();
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

            // High score text
            highScoreText = new Text("High: 0");
            highScoreText.setFont(gameFont);
            highScoreText.setFill(Color.YELLOW);
            highScoreText.setTranslateX(GameConstants.WINDOW_WIDTH / 2 - 60);
            highScoreText.setTranslateY(30);
            getGameScene().addUINode(highScoreText);

            // Bind text to properties
            getWorldProperties().addListener("score", (prev, now) -> {
                scoreText.setText("Score: " + now);
                // Cập nhật high score
                if ((Integer) now > geti("highScore")) {
                    set("highScore", now);
                }
            });

            getWorldProperties().addListener("lives", (prev, now) -> {
                livesText.setText("Lives: " + now);
            });

            getWorldProperties().addListener("highScore", (prev, now) -> {
                highScoreText.setText("High: " + now);
            });

        } catch (Exception e) {
            System.err.println("Failed to setup game UI: " + e.getMessage());
        }
    }

    private static void handleGameOver() {
        // Hiển thị màn hình game over với điểm số cuối
        Text gameOverText = new Text("GAME OVER");
        Text finalScoreText = new Text("Final Score: " + geti("score"));

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
        finalScoreText.setTranslateX(GameConstants.WINDOW_WIDTH / 2 - 150);
        finalScoreText.setTranslateY(GameConstants.WINDOW_HEIGHT / 2 + 20);

        getGameScene().addUINode(gameOverText);
        getGameScene().addUINode(finalScoreText);

        // Quay về menu sau 3 giây
        getGameTimer().runOnceAfter(() -> {
            getGameController().gotoMainMenu();
        }, javafx.util.Duration.seconds(3));
    }

    private static void spawnMoreBricks() {
        // Tạo lại bricks cho endless mode
        GameFactory.createBricks();

        // Hiển thị text thông báo
        Text continueText = new Text("CONTINUE!");
        try {
            Font gameFont = Font.loadFont(
                getAssetLoader().getURL("fonts/Daydream.ttf").toExternalForm(), 36);
            continueText.setFont(gameFont);
        } catch (Exception e) {
            continueText.setFont(Font.font(36));
        }
        continueText.setFill(Color.LIGHTGREEN);
        continueText.setTranslateX(GameConstants.WINDOW_WIDTH / 2 - 100);
        continueText.setTranslateY(GameConstants.WINDOW_HEIGHT / 2);
        getGameScene().addUINode(continueText);

        // Xóa text sau 1.5 giây
        getGameTimer().runOnceAfter(() -> {
            getGameScene().removeUINode(continueText);
        }, javafx.util.Duration.seconds(1.5));
    }
}
