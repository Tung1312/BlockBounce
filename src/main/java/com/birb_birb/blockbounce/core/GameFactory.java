package com.birb_birb.blockbounce.core;

import com.almasb.fxgl.entity.Entity;
import com.birb_birb.blockbounce.constants.EntityType;
import com.birb_birb.blockbounce.constants.GameConstants;
import com.birb_birb.blockbounce.entities.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;

import static com.almasb.fxgl.dsl.FXGL.entityBuilder;

public final class GameFactory {

    private GameFactory() {}

    public static Entity createBall() {
        return entityBuilder()
                .type(EntityType.BALL)
                .at(GameConstants.WINDOW_WIDTH / 2.0, GameConstants.WINDOW_HEIGHT / 2.0)
                .viewWithBBox(new Circle(10, Color.WHITE))
                .with(new BallComponent())
                .collidable()
                .buildAndAttach();
    }

    public static Entity createPaddle() {
        return entityBuilder()
                .type(EntityType.PADDLE)
                .at(GameConstants.WINDOW_WIDTH / 2.0 - 60, GameConstants.WINDOW_HEIGHT - 80)
                .viewWithBBox(new Rectangle(120, 20, Color.DARKCYAN))
                .with(new PaddleComponent())
                .collidable()
                .buildAndAttach();
    }

    public static void createBricks() {
        int cols = 10, rows = 5;
        double brickWidth = 100, brickHeight = 30, offsetX = 80, offsetY = 80;

        for (int y = 0; y < rows; y++) {
            for (int x = 0; x < cols; x++) {
                entityBuilder()
                        .type(EntityType.BRICK)
                        .at(offsetX + x * (brickWidth + 5), offsetY + y * (brickHeight + 5))
                        .viewWithBBox(new Rectangle(brickWidth, brickHeight, Color.web("#FF6F61")))
                        .with(new BrickComponent())
                        .collidable()
                        .buildAndAttach();
            }
        }
    }

    public static void createWalls() {
        entityBuilder()
                .type(EntityType.WALL)
                .at(0, 0)
                .viewWithBBox(new Rectangle(10, GameConstants.WINDOW_HEIGHT, Color.TRANSPARENT))
                .collidable()
                .buildAndAttach();

        entityBuilder()
                .type(EntityType.WALL)
                .at(GameConstants.WINDOW_WIDTH - 10, 0)
                .viewWithBBox(new Rectangle(10, GameConstants.WINDOW_HEIGHT, Color.TRANSPARENT))
                .collidable()
                .buildAndAttach();

        entityBuilder()
                .type(EntityType.WALL)
                .at(0, 0)
                .viewWithBBox(new Rectangle(GameConstants.WINDOW_WIDTH, 10, Color.TRANSPARENT))
                .collidable()
                .buildAndAttach();
    }
}
