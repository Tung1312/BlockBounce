package com.birb_birb.blockbounce.core;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.birb_birb.blockbounce.constants.EntityType;
import com.birb_birb.blockbounce.constants.GameConstants;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import static com.almasb.fxgl.dsl.FXGL.entityBuilder;

public final class GameInitializer {

    private GameInitializer() {}

    public static void initGameWorld() {
        FXGL.getGameScene().setBackgroundColor(Color.BLACK);
        GameFactory.createWalls();
        GameFactory.createBricks();
        GameFactory.createBall();
        GameFactory.createPaddle();
    }
}