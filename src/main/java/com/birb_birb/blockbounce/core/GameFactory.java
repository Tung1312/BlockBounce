package com.birb_birb.blockbounce.core;

import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.physics.BoundingShape;
import com.almasb.fxgl.physics.HitBox;
import com.almasb.fxgl.texture.Texture;
import com.birb_birb.blockbounce.constants.BrickType;
import com.birb_birb.blockbounce.constants.EntityType;
import com.birb_birb.blockbounce.constants.GameConstants;
import com.birb_birb.blockbounce.constants.PowerUpType;
import com.birb_birb.blockbounce.entities.*;
import com.birb_birb.blockbounce.utils.TextureManager;
import com.birb_birb.blockbounce.utils.saveload.BlockData;
import com.birb_birb.blockbounce.utils.saveload.LevelData;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.util.List;

import static com.almasb.fxgl.dsl.FXGL.*;
import static com.birb_birb.blockbounce.constants.GameConstants.*;

public final class GameFactory {

    private GameFactory() {}

    /**
     * Get the texture path for a given brick type
     */
    private static String getTexturePathForBrickType(BrickType type) {
        switch (type) {
            case WOOD:
                return GameConstants.WOOD_TEXTURE;
            case STONE:
                return GameConstants.STONE_TEXTURE;
            case NETHERACK:
                return GameConstants.NETHERACK_TEXTURE;
            case NETHERBRICK:
                return GameConstants.NETHERBRICK_TEXTURE;
            case ENDSTONE:
                return GameConstants.ENDSTONE_TEXTURE;
            case OBSIDIAN:
                return GameConstants.OBSIDIAN_TEXTURE;
            case LUCKY:
                return GameConstants.LUCKY_BLOCK_TEXTURE;
            default:
                return GameConstants.WOOD_TEXTURE;
        }
    }

    public static Entity createBackground() {
        Texture backgroundTexture = TextureManager.loadTexture(
                GameConstants.BACKGROUND_TEXTURE,
                GameConstants.WINDOW_WIDTH,
                GameConstants.WINDOW_HEIGHT
        );

        return entityBuilder()
                .type(EntityType.WALL)
                .at(0, 0)
                .view(backgroundTexture)
                .zIndex(-100)
                .buildAndAttach();
    }

    public static Entity createBall() {
        Texture ballTexture = TextureManager.loadTexture(
                GameConstants.BALL_TEXTURE,
                GameConstants.BALL_SIZE,
                GameConstants.BALL_SIZE
        );

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
        Texture paddleTexture = TextureManager.loadTexture(
                GameConstants.PADDLE_TEXTURE,
                GameConstants.PADDLE_WIDTH,
                GameConstants.PADDLE_HEIGHT
        );

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
        int cols = 10;   // number of bricks per row
        int rows = 6;    // number of brick rows
        double offsetY = GameConstants.OFFSET_TOP + 80;
        double offsetX = GameConstants.OFFSET_LEFT + 60;

        for (int y = 1; y <= rows; y++) {
            for (int x = 1; x <= cols; x++) {
                BrickType type = BrickType.WOOD;

                switch (y) {
                    case 2:
                        type = BrickType.STONE;
                        break;
                    case 3:
                        type = BrickType.NETHERACK;
                        break;
                    case 4:
                        type = BrickType.NETHERBRICK;
                        break;
                    case 5:
                        type = BrickType.ENDSTONE;
                        break;
                    case 6:
                        type = BrickType.OBSIDIAN;
                        break;
                }

                String texturePath = getTexturePathForBrickType(type);
                Texture baseTexture = getAssetLoader().loadTexture(texturePath);
                Texture texture = TextureManager.loadTextureCopy(baseTexture, BRICK_SIZE, BRICK_SIZE);

                entityBuilder()
                        .type(EntityType.BRICK)
                        .at(offsetX + x * BRICK_SIZE, offsetY + y * BRICK_SIZE)
                        .view(texture)
                        .bbox(new HitBox(BoundingShape.box(BRICK_SIZE, BRICK_SIZE)))
                        .with(new BrickComponent(type))
                        .collidable()
                        .buildAndAttach();
            }
        }
    }

    /**
     * Create a single brick at specified position with color and type
     * Used for restoring saved game state
     */
    public static Entity createBrick(double x, double y, Color color, BrickType type) {
        String texturePath = getTexturePathForBrickType(type);

        Texture baseTexture = getAssetLoader().loadTexture(texturePath);
        Texture texture = TextureManager.loadTextureCopy(baseTexture, BRICK_SIZE, BRICK_SIZE);

        return entityBuilder()
                .type(EntityType.BRICK)
                .at(x, y)
                .view(texture)
                .bbox(new HitBox(BoundingShape.box(BRICK_SIZE, BRICK_SIZE)))
                .with(new BrickComponent(type))
                .collidable()
                .buildAndAttach();
    }

    /**Create bricks from a LevelData JSON configuration*/
    public static void createBricksFromLevel(LevelData levelData) {
        if (levelData == null) {
            System.err.println("LevelData is null, falling back to default brick creation");
            createBricks();
            return;
        }

        List<BlockData> bricks = levelData.getBricks();
        for (BlockData blockData : bricks) {
            BrickType type = parseBrickType(blockData.getBrickType());
            String texturePath = getTexturePathForBrickType(type);

            Texture baseTexture = getAssetLoader().loadTexture(texturePath);
            Texture texture = TextureManager.loadTextureCopy(baseTexture, BRICK_SIZE, BRICK_SIZE);

            entityBuilder()
                    .type(EntityType.BRICK)
                    .at(blockData.getX(), blockData.getY())
                    .view(texture)
                    .bbox(new HitBox(BoundingShape.box(BRICK_SIZE, BRICK_SIZE)))
                    .with(new BrickComponent(type))
                    .collidable()
                    .buildAndAttach();
        }
    }

    /**
     * Create bricks from LevelData - alias for createBricksFromLevel
     * Used for infinite mode random level generation
     */
    public static void createBricksFromData(LevelData levelData) {
        createBricksFromLevel(levelData);
    }

    /**Parse brick type string to BrickType enum*/
    private static BrickType parseBrickType(String typeString) {
        if (typeString == null) {
            return BrickType.WOOD;
        }

        return switch (typeString.toUpperCase()) {
            case "STONE" -> BrickType.STONE;
            case "NETHERACK" -> BrickType.NETHERACK;
            case "NETHERBRICK" -> BrickType.NETHERBRICK;
            case "ENDSTONE" -> BrickType.ENDSTONE;
            case "OBSIDIAN" -> BrickType.OBSIDIAN;
            case "LUCKY" -> BrickType.LUCKY;
            default -> BrickType.WOOD;
        };
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
        Texture frameTexture = TextureManager.loadTexture(
                STORY_MODE_FRAME,
                WINDOW_WIDTH,
                WINDOW_HEIGHT
        );

        return entityBuilder()
                .type(EntityType.WALL)
                .at(0, 0)
                .view(frameTexture)
                .zIndex(100)
                .buildAndAttach();
    }

    public static Entity createScoreModeFrame() {
        Texture frameTexture = TextureManager.loadTexture(
                SCORE_MODE_FRAME,
                WINDOW_WIDTH,
                WINDOW_HEIGHT
        );

        return entityBuilder()
                .type(EntityType.WALL)
                .at(0, 0)
                .view(frameTexture)
                .zIndex(100)
                .buildAndAttach();
    }

    public static Entity createVersusModeFrame() {
        Texture frameTexture = TextureManager.loadTexture(
                VERSUS_MODE_FRAME,
                WINDOW_WIDTH,
                WINDOW_HEIGHT
        );

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
    public static Entity createPowerUp(double x, double y, PowerUpType type, int playerId) {
        // Select texture based on power-up type
        String texturePath;
        switch (type) {
            case DOUBLE_BALL:
                texturePath = POWERUP_MULTIPLY_TEXTURE;
                break;
            case SMALL_PADDLE:
                texturePath = POWERUP_SHRINK_TEXTURE;
                break;
            case FAST_BALL:
                texturePath = POWERUP_SPEED_TEXTURE;
                break;
            case EXTRA_LIFE:
                texturePath = POWERUP_LIFE_TEXTURE;
                break;
            case DOUBLE_POINTS:
                texturePath = POWERUP_DOUBLE_TEXTURE;
                break;
            default:
                texturePath = BALL_TEXTURE;
                break;
        }

        Texture tex = TextureManager.loadTexture(texturePath, 32, 32);

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