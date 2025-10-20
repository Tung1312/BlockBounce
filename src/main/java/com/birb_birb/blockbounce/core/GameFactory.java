package com.birb_birb.blockbounce.core;

import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.physics.BoundingShape;
import com.almasb.fxgl.physics.HitBox;
import com.almasb.fxgl.texture.Texture;
import com.birb_birb.blockbounce.constants.EntityType;
import com.birb_birb.blockbounce.constants.GameConstants;
import com.birb_birb.blockbounce.entities.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import static com.almasb.fxgl.dsl.FXGL.*;
import static com.almasb.fxgl.dsl.FXGLForKtKt.getAssetLoader;
import static com.birb_birb.blockbounce.constants.GameConstants.*;

public final class GameFactory {

    private GameFactory() {}

    public static Entity createBackground() {
        Texture backgroundTexture = getAssetLoader().loadTexture(BACKGROUND_TEXTURE);
        backgroundTexture.setFitWidth(WINDOW_WIDTH);
        backgroundTexture.setFitHeight(WINDOW_HEIGHT);
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
        Texture ballTexture = getAssetLoader().loadTexture(BALL_TEXTURE);
        ballTexture.setFitWidth(BALL_SIZE);
        ballTexture.setFitHeight(BALL_SIZE);
        ballTexture.setPreserveRatio(false);
        ballTexture.setSmooth(false);

        return entityBuilder()
                .type(EntityType.BALL)
                .at(GameConstants.OFFSET_LEFT + GameConstants.PLAYABLE_WIDTH / 2.0 - BALL_SIZE / 2,
                        GameConstants.OFFSET_TOP + GameConstants.PLAYABLE_HEIGHT / 2.0)
                .view(ballTexture)
                .bbox(new HitBox(BoundingShape.circle(BALL_SIZE / 2)))
                .with(new BallComponent())
                .collidable()
                .buildAndAttach();
    }

    public static Entity createPaddle() {
        Texture paddleTexture = getAssetLoader().loadTexture(PADDLE_TEXTURE);
        paddleTexture.setFitWidth(PADDLE_WIDTH);
        paddleTexture.setFitHeight(PADDLE_HEIGHT);
        paddleTexture.setPreserveRatio(false);
        paddleTexture.setSmooth(false);

        return entityBuilder()
                .type(EntityType.PADDLE)
                .at(GameConstants.OFFSET_LEFT + GameConstants.PLAYABLE_WIDTH / 2.0 - PADDLE_WIDTH / 2.0,
                        WINDOW_HEIGHT - GameConstants.OFFSET_BOTTOM - 60)
                .view(paddleTexture)
                .bbox(new HitBox(BoundingShape.box(PADDLE_WIDTH, PADDLE_HEIGHT)))
                .with(new PaddleComponent())
                .collidable()
                .buildAndAttach();
    }


    public static void createBricks() {
        Texture baseTexture = getAssetLoader().loadTexture(BRICK_TEXTURE);

        int cols = 15;   // number of bricks per row
        int rows = 5;    // number of brick rows
        double offsetY = GameConstants.OFFSET_TOP + 80;
        double offsetX = GameConstants.OFFSET_LEFT + 60;

        for (int y = 0; y < rows; y++) {
            for (int x = 0; x < cols; x++) {
                Texture texture = baseTexture.copy();
                texture.setPreserveRatio(false);
                texture.setSmooth(false);
                texture.setFitWidth(BRICK_SIZE);
                texture.setFitHeight(BRICK_SIZE);

                entityBuilder()
                        .type(EntityType.BRICK)
                        .at(offsetX + x * BRICK_SIZE, offsetY + y * BRICK_SIZE)
                        .view(texture)
                        .bbox(new HitBox(BoundingShape.box(BRICK_SIZE, BRICK_SIZE)))
                        .with(new BrickComponent())
                        .collidable()
                        .buildAndAttach();
            }
        }
    }

    public static void createWalls() {
        // Left wall
        entityBuilder()
                .type(EntityType.WALL)
                .at(OFFSET_LEFT, OFFSET_TOP)
                .viewWithBBox(new Rectangle(10, PLAYABLE_HEIGHT, Color.TRANSPARENT))
                .collidable()
                .buildAndAttach();

        // Right wall
        entityBuilder()
                .type(EntityType.WALL)
                .at(WINDOW_WIDTH - OFFSET_RIGHT - 10, OFFSET_TOP)
                .viewWithBBox(new Rectangle(10, PLAYABLE_HEIGHT, Color.TRANSPARENT))
                .collidable()
                .buildAndAttach();

        // Top wall
        entityBuilder()
                .type(EntityType.WALL)
                .at(OFFSET_LEFT, OFFSET_TOP)
                .viewWithBBox(new Rectangle(PLAYABLE_WIDTH, 10, Color.TRANSPARENT))
                .collidable()
                .buildAndAttach();
    }

    public static Entity createStoryModeFrame() {
        Texture frameTexture = getAssetLoader().loadTexture(STORY_MODE_FRAME);
        frameTexture.setFitWidth(WINDOW_WIDTH);
        frameTexture.setFitHeight(WINDOW_HEIGHT);
        frameTexture.setPreserveRatio(false);
        frameTexture.setSmooth(false);

        return entityBuilder()
                .type(EntityType.WALL)
                .at(0, 0)
                .view(frameTexture)
                .zIndex(100)
                .buildAndAttach();
    }
}
