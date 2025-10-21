package com.birb_birb.blockbounce.core.gamemode.story;

import com.almasb.fxgl.entity.Entity;
import com.birb_birb.blockbounce.constants.GameConstants;
import com.birb_birb.blockbounce.constants.GameMode;
import com.birb_birb.blockbounce.core.GameFactory;
import com.birb_birb.blockbounce.core.GameManager;
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
        levelText.setFill(Color.WHITE);
        levelText.setTranslateX(GameConstants.WINDOW_WIDTH / 2.0 - 50);
        levelText.setTranslateY(30);
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

        // Show level up message
        displayMessage("LEVEL " + geti("level") + "!", Color.LIGHTBLUE, 2.0);

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
