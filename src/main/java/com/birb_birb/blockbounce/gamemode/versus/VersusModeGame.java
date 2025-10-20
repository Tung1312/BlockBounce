package com.birb_birb.blockbounce.gamemode.versus;

import com.almasb.fxgl.entity.Entity;
import com.birb_birb.blockbounce.core.BlockBounceApp;
import com.birb_birb.blockbounce.core.GameFactory;

import static com.almasb.fxgl.dsl.FXGLForKtKt.*;
import static com.almasb.fxgl.dsl.FXGLForKtKt.getWorldProperties;

public class VersusModeGame {

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

        // Set up new game
        GameFactory.createBackground();
        GameFactory.createWalls();
        GameFactory.createBricks();
        GameFactory.createPaddle();
        GameFactory.createBall();
        GameFactory.createVersusModeFrame();

        // TODO: Add endless-specific initialization here
        // - Set up endless brick generation
        // - Initialize high score tracking
        // - Set up endless UI elements
    }
}

