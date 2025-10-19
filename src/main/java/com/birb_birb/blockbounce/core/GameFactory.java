package com.birb_birb.blockbounce.core;

import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.physics.BoundingShape;
import com.almasb.fxgl.physics.HitBox;
import com.almasb.fxgl.texture.Texture;
import com.birb_birb.blockbounce.constants.EntityType;
import com.birb_birb.blockbounce.constants.GameConstants;
import com.birb_birb.blockbounce.entities.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;

import static com.almasb.fxgl.dsl.FXGL.*;
import static com.almasb.fxgl.dsl.FXGLForKtKt.getAssetLoader;
import static com.birb_birb.blockbounce.constants.GameConstants.*;

public final class GameFactory {

    private GameFactory() {}

    public static Entity createBackground() {
        Texture backgroundTexture = getAssetLoader().loadTexture(GameConstants.BACKGROUND_TEXTURE);
        backgroundTexture.setFitWidth(GameConstants.WINDOW_WIDTH);
        backgroundTexture.setFitHeight(GameConstants.WINDOW_HEIGHT);
        backgroundTexture.setPreserveRatio(false);
        backgroundTexture.setSmooth(false);

        return entityBuilder()
                .type(EntityType.WALL)
                .at(0, 0)
                .view(backgroundTexture)
                .zIndex(-100)
                .buildAndAttach();
    }

    public static Entity createBall() {
        Texture ballTexture = getAssetLoader().loadTexture(GameConstants.BALL_TEXTURE);
        ballTexture.setFitWidth(GameConstants.BALL_SIZE);
        ballTexture.setFitHeight(GameConstants.BALL_SIZE);
        ballTexture.setPreserveRatio(false);
        ballTexture.setSmooth(false);

        return entityBuilder()
                .type(EntityType.BALL)
                .at(GameConstants.WINDOW_WIDTH / 2.0 - GameConstants.BALL_SIZE / 2,
                        GameConstants.WINDOW_HEIGHT / 2.0)
                .view(ballTexture)
                .bbox(new HitBox(BoundingShape.circle(GameConstants.BALL_SIZE / 2)))
                .with(new BallComponent())
                .collidable()
                .buildAndAttach();
    }

    public static Entity createPaddle() {
        Texture paddleTexture = getAssetLoader().loadTexture(GameConstants.PADDLE_TEXTURE);
        paddleTexture.setFitWidth(GameConstants.PADDLE_WIDTH);
        paddleTexture.setFitHeight(GameConstants.PADDLE_HEIGHT);
        paddleTexture.setPreserveRatio(false);
        paddleTexture.setSmooth(false);

        return entityBuilder()
                .type(EntityType.PADDLE)
                .at(GameConstants.WINDOW_WIDTH / 2.0 - GameConstants.PADDLE_WIDTH / 2.0,
                        GameConstants.WINDOW_HEIGHT - 100)
                .view(paddleTexture)
                .bbox(new HitBox(BoundingShape.box(GameConstants.PADDLE_WIDTH, GameConstants.PADDLE_HEIGHT)))
                .with(new PaddleComponent())
                .collidable()
                .buildAndAttach();
    }


    public static void createBricks() {
        Texture baseTexture = getAssetLoader().loadTexture(GameConstants.BRICK_TEXTURE);

        int cols = 10;   // number of bricks per row
        int rows = 5;    // number of brick rows
        double offsetY = 80; // top margin

        for (int y = 0; y < rows; y++) {
            for (int x = 0; x < cols; x++) {
                Texture texture = baseTexture.copy();
                texture.setPreserveRatio(false);
                texture.setSmooth(false);
                texture.setFitWidth(BRICK_WIDTH);
                texture.setFitHeight(BRICK_HEIGHT);

                entityBuilder()
                        .type(EntityType.BRICK)
                        .at(x * BRICK_WIDTH, offsetY + y * BRICK_HEIGHT)
                        .view(texture)
                        .bbox(new HitBox(BoundingShape.box(BRICK_WIDTH, BRICK_HEIGHT)))
                        .with(new BrickComponent()) // simple brick component
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
