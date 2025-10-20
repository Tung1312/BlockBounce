package com.birb_birb.blockbounce.core;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.birb_birb.blockbounce.constants.EntityType;
import com.birb_birb.blockbounce.constants.GameConstants;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import static com.almasb.fxgl.dsl.FXGL.entityBuilder;
import static com.almasb.fxgl.dsl.FXGLForKtKt.*;

public final class GameInitializer {

    private GameInitializer() {}

    public static void initializeGame() {
        resetWorld();
        setupLevel();
    }
    private static void resetWorld() {
        // 1️⃣ Clear all existing entities and physics state
        getGameWorld().getEntitiesCopy().forEach(Entity::removeFromWorld);
        getPhysicsWorld().clear();

        // 2️⃣ Clear UI overlays, timers, variables if you use them
        getGameTimer().clear();
        getWorldProperties().clear();
    }

    private static void setupLevel() {
        // 3️⃣ Recreate your gameplay entities in correct order
        GameFactory.createBackground();
        GameFactory.createWalls();
        GameFactory.createBricks();
        GameFactory.createPaddle();
        GameFactory.createBall();
        GameFactory.createStoryModeFrame();
    }

    public void restartGame() {
        initializeGame();
        getGameScene().clearUINodes();
        getGameScene().getViewport().setBounds(0, 0,
                GameConstants.WINDOW_WIDTH, GameConstants.WINDOW_HEIGHT);
        getGameScene().getViewport().setZoom(1.0);
    }
}