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

        Entity e = entityBuilder()
                .type(EntityType.BALL)
                .at(GameConstants.OFFSET_LEFT + GameConstants.PLAYABLE_WIDTH / 2.0 - BALL_SIZE / 2,
                        GameConstants.OFFSET_TOP + GameConstants.PLAYABLE_HEIGHT / 2.0)
                .view(ballTexture)
                .bbox(new HitBox(BoundingShape.circle(BALL_SIZE / 2)))
                .with(new BallComponent())
                .collidable()
                .buildAndAttach();

        // default paddle-less single-player ball
        try { e.setProperty("playerId", 1); } catch (Exception ignored) {}
        return e;
    }

    public static Entity createPaddle() {
        return createPaddle(
            GameConstants.OFFSET_LEFT + GameConstants.PLAYABLE_WIDTH / 2.0 - PADDLE_WIDTH / 2.0,
            WINDOW_HEIGHT - GameConstants.OFFSET_BOTTOM - 60,
            1
        );
    }

    public static Entity createPaddle(double x, double y, int playerId) {
        Texture paddleTexture = getAssetLoader().loadTexture(GameConstants.PADDLE_TEXTURE);
        paddleTexture.setFitWidth(GameConstants.PADDLE_WIDTH);
        paddleTexture.setFitHeight(GameConstants.PADDLE_HEIGHT);
        paddleTexture.setPreserveRatio(false);
        paddleTexture.setSmooth(false);

        Entity e = entityBuilder()
                .type(EntityType.PADDLE)
                .at(x, y)
                .view(paddleTexture)
                .bbox(new HitBox(BoundingShape.box(GameConstants.PADDLE_WIDTH, GameConstants.PADDLE_HEIGHT)))
                .with(new PaddleComponent(playerId))
                .collidable()
                .buildAndAttach();

        // expose player id and current paddle width for power-ups
        try { e.setProperty("playerId", playerId); } catch (Exception ignored) {}
        try { e.setProperty("paddleWidth", GameConstants.PADDLE_WIDTH); } catch (Exception ignored) {}
        return e;
    }


    public static void createBricks() {
        Texture baseTexture = getAssetLoader().loadTexture(GameConstants.BRICK_TEXTURE);

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

    /**
     * Create a single brick at specified position with color
     * Used for restoring saved game state
     */
    public static Entity createBrick(double x, double y, Color color) {
        Texture baseTexture = getAssetLoader().loadTexture(GameConstants.BRICK_TEXTURE);
        Texture texture = baseTexture.copy();
        texture.setPreserveRatio(false);
        texture.setSmooth(false);
        texture.setFitWidth(BRICK_SIZE);
        texture.setFitHeight(BRICK_SIZE);

        return entityBuilder()
                .type(EntityType.BRICK)
                .at(x, y)
                .view(texture)
                .bbox(new HitBox(BoundingShape.box(BRICK_SIZE, BRICK_SIZE)))
                .with(new BrickComponent())
                .collidable()
                .buildAndAttach();
    }

    public static void createWalls() {
        entityBuilder()
                .type(EntityType.WALL)
                .at(OFFSET_LEFT, OFFSET_TOP)
                .viewWithBBox(new Rectangle(10, PLAYABLE_HEIGHT, Color.TRANSPARENT))
                .collidable()
                .buildAndAttach();

        entityBuilder()
                .type(EntityType.WALL)
                .at(WINDOW_WIDTH - OFFSET_RIGHT - 10, OFFSET_TOP)
                .viewWithBBox(new Rectangle(10, PLAYABLE_HEIGHT, Color.TRANSPARENT))
                .collidable()
                .buildAndAttach();

        entityBuilder()
                .type(EntityType.WALL)
                .at(0, 0)
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

    public static Entity createScoreModeFrame() {
        Texture frameTexture = getAssetLoader().loadTexture(SCORE_MODE_FRAME);
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

    public static Entity createVersusModeFrame() {
        Texture frameTexture = getAssetLoader().loadTexture(VERSUS_MODE_FRAME);
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

    /**
     * Spawn a power-up entity at given position.
     * playerId = 0 for neutral, >0 to target a specific player (useful in versus)
     */
    public static Entity createPowerUp(double x, double y, PowerUpComponent.PowerUpType type, int playerId) {
        // Select texture based on power-up type
        String texturePath;
        switch (type) {
            case DOUBLE_BALL:
                texturePath = GameConstants.POWERUP_MULTIPLY_TEXTURE;
                break;
            case SMALL_PADDLE:
                texturePath = GameConstants.POWERUP_SHRINK_TEXTURE;
                break;
            case FAST_BALL:
                texturePath = GameConstants.POWERUP_SPEED_TEXTURE;
                break;
            default:
                texturePath = GameConstants.BALL_TEXTURE;
                break;
        }

        Texture tex = getAssetLoader().loadTexture(texturePath);
        tex.setFitWidth(20);
        tex.setFitHeight(20);
        tex.setPreserveRatio(false);
        tex.setSmooth(false);

        Entity e = entityBuilder()
                .type(EntityType.POWERUP)
                .at(x, y)
                .view(tex)
                .bbox(new HitBox(BoundingShape.circle(10)))
                .with(new PowerUpComponent(type))
                .collidable()
                .buildAndAttach();

        try { e.setProperty("playerId", playerId); } catch (Exception ignored) {}
        return e;
    }
}