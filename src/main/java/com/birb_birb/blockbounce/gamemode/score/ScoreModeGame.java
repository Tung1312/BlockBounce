package com.birb_birb.blockbounce.gamemode.score;

import com.almasb.fxgl.entity.Entity;
import com.birb_birb.blockbounce.constants.GameConstants;
import com.birb_birb.blockbounce.core.BlockBounceApp;
import com.birb_birb.blockbounce.core.GameFactory;
import com.birb_birb.blockbounce.ui.GameplayButtons;
import javafx.scene.layout.VBox;

import static com.almasb.fxgl.dsl.FXGLForKtKt.*;
import static com.almasb.fxgl.dsl.FXGLForKtKt.getWorldProperties;

public class ScoreModeGame {

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

        // TODO: Add endless-specific initialization here
        // - Set up endless brick generation
        // - Initialize high score tracking
        // - Set up endless UI elements
    }
}
