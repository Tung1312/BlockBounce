package com.birb_birb.blockbounce.core.gamemode.story;

import com.almasb.fxgl.entity.Entity;
import com.birb_birb.blockbounce.constants.GameConstants;
import com.birb_birb.blockbounce.constants.GameMode;
import com.birb_birb.blockbounce.core.GameFactory;
import com.birb_birb.blockbounce.core.GameManager;
import com.birb_birb.blockbounce.utils.MenuManager;
import com.birb_birb.blockbounce.utils.SoundManager;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

import static com.almasb.fxgl.dsl.FXGLForKtKt.*;

public class StoryModeGame extends GameManager {

    private static final StoryModeGame INSTANCE = new StoryModeGame();
    private Text levelText;

    private StoryModeGame() {}

    public static void startGame() {
        if (GameMode.getCurrentGameMode() != GameMode.STORY) {
            System.err.println("Warning: StoryModeGame.start() called but current mode is " + GameMode.getCurrentGameMode());
        }
        INSTANCE.initialize();
    }

    public static StoryModeGame getInstance() {
        return INSTANCE;
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

        // Play complete sound when finishing a story mode level
        SoundManager.playCompleteSound();

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

    @Override
    protected void handleGameOver() {
        SoundManager.playLooseSound();
        getGameScene().addUINode(MenuManager.createDimmingOverlay());

        javafx.scene.text.Text gameOverText = new javafx.scene.text.Text("GAME OVER");
        javafx.scene.text.Text finalScoreText = new javafx.scene.text.Text("Final Score: " + geti("score"));
        javafx.scene.text.Text finalLevelText = new javafx.scene.text.Text("Level Reached: " + geti("level"));

        gameOverText.setFont(displayFont);
        finalScoreText.setFont(displayFont);
        finalLevelText.setFont(displayFont);

        gameOverText.setFill(Color.RED);
        finalScoreText.setFill(Color.WHITE);
        finalLevelText.setFill(Color.CYAN);

        gameOverText.setTranslateX((double) GameConstants.WINDOW_WIDTH / 2 - 150);
        gameOverText.setTranslateY((double) GameConstants.WINDOW_HEIGHT / 2 - 50);

        finalScoreText.setTranslateX((double) GameConstants.WINDOW_WIDTH / 2 - 150);
        finalScoreText.setTranslateY((double) GameConstants.WINDOW_HEIGHT / 2);

        finalLevelText.setTranslateX((double) GameConstants.WINDOW_WIDTH / 2 - 150);
        finalLevelText.setTranslateY((double) GameConstants.WINDOW_HEIGHT / 2 + 50);

        getGameScene().addUINode(gameOverText);
        getGameScene().addUINode(finalScoreText);
        getGameScene().addUINode(finalLevelText);

        getGameController().pauseEngine();

        // wait 3s before return to menu
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
