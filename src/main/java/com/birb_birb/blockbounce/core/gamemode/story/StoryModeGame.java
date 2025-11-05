package com.birb_birb.blockbounce.core.gamemode.story;

import com.almasb.fxgl.entity.Entity;
import com.birb_birb.blockbounce.constants.BrickType;
import com.birb_birb.blockbounce.constants.EntityType;
import com.birb_birb.blockbounce.constants.GameConstants;
import com.birb_birb.blockbounce.constants.GameMode;
import com.birb_birb.blockbounce.core.GameFactory;
import com.birb_birb.blockbounce.core.GameManager;
import com.birb_birb.blockbounce.entities.BrickComponent;
import com.birb_birb.blockbounce.utils.ButtonManager;
import com.birb_birb.blockbounce.utils.MenuManager;
import com.birb_birb.blockbounce.utils.SoundManager;
import com.birb_birb.blockbounce.utils.saveload.LevelData;
import com.birb_birb.blockbounce.utils.saveload.LevelLoader;
import com.birb_birb.blockbounce.utils.saveload.ProgressLoader;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

import java.awt.*;

import static com.almasb.fxgl.dsl.FXGLForKtKt.*;

public class StoryModeGame extends GameManager {

    private static final StoryModeGame INSTANCE = new StoryModeGame();
    private final LevelLoader levelLoader = new LevelLoader();
    private final ProgressLoader progressManager = ProgressLoader.getInstance();
    private static int selectedStartingLevel = 1;
    private int startingLevel = 1;
    private Text levelText;

    private StoryModeGame() {}

    public static void setStartingLevel(int levelNumber) {
        selectedStartingLevel = levelNumber;
    }

    public static void startGame() {
        if (GameMode.getCurrentGameMode() != GameMode.STORY) {
            System.err.println("Warning: StoryModeGame.start() called but current mode is " + GameMode.getCurrentGameMode());
        }
        INSTANCE.startingLevel = selectedStartingLevel;
        INSTANCE.initialize();
    }

    public static StoryModeGame getInstance() {
        return INSTANCE;
    }

    @Override
    protected void setupProperties() {
        super.setupProperties();
        // update starting level before setting up game
        startingLevel = selectedStartingLevel;
        getWorldProperties().setValue("level", startingLevel);
    }

    @Override
    protected void setupNewGame() {
        GameFactory.createBackground();
        GameFactory.createWalls();

        // Load level from JSON based on starting level
        LevelData levelData = levelLoader.loadLevel(startingLevel);
        if (levelData != null) {
            System.out.println("Loading level " + startingLevel);
            GameFactory.createBricksFromLevel(levelData);
        } else {
            // Fallback to default brick generation if JSON level not found
            System.out.println("Level " + startingLevel + " JSON not found, using default brick generation");
            GameFactory.createBricks();
        }

        GameFactory.createPaddle();
        GameFactory.createBall();
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
        levelText = new Text("Level: " + geti("level"));
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
            long destructibleBricks = getGameWorld()
                .getEntitiesByType(EntityType.BRICK)
                .stream()
                .filter(brick -> {
                    BrickComponent brickComp = brick.getComponent(BrickComponent.class);
                    return brickComp != null && brickComp.getBrickType() != BrickType.OBSIDIAN;
                })
                .count();

            if (destructibleBricks == 0 && !getb("gameOver")) {
                nextLevel();
            }
        }, javafx.util.Duration.seconds(0.5));
    }

    private void nextLevel() {
        inc("level", 1);
        int currentLevel = geti("level");

        // Unlock the next level in progress
        if (currentLevel > progressManager.getUnlockedLevels()) {
            progressManager.unlockLevel(currentLevel);
        }

        // Clear all game elements on screen
        getGameWorld().getEntitiesByType(EntityType.BALL).forEach(Entity::removeFromWorld);
        getGameWorld().getEntitiesByType(EntityType.PADDLE).forEach(Entity::removeFromWorld);
        getGameWorld().getEntitiesByType(EntityType.BRICK).forEach(Entity::removeFromWorld);

        getGameController().pauseEngine();

        new Thread(() -> {
            try {
                SoundManager.playCompleteSound();

                javafx.application.Platform.runLater(() -> {
                    displayMessage("LEVEL " + (currentLevel - 1) + " COMPLETED!",
                        (Color) GameConstants.FONT_COLOR, 2.5, null);
                });

                Thread.sleep(2500);

                javafx.application.Platform.runLater(() -> {
                    getGameController().resumeEngine();
                    ButtonManager.navigateToStoryMode();
                });

            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                System.err.println("Level completion sequence interrupted: " + e.getMessage());
            }
        }).start();
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
