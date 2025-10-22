package com.birb_birb.blockbounce.core.gamemode.versus;

import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.physics.BoundingShape;
import com.almasb.fxgl.physics.HitBox;
import com.birb_birb.blockbounce.constants.EntityType;
import com.birb_birb.blockbounce.constants.GameConstants;
import com.birb_birb.blockbounce.entities.BallComponent;
import com.birb_birb.blockbounce.entities.BrickComponent;
import com.birb_birb.blockbounce.entities.PaddleComponent;
import com.birb_birb.blockbounce.utils.TextureUtils;
import javafx.geometry.Point2D;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.util.ArrayList;
import java.util.List;

import static com.almasb.fxgl.dsl.FXGL.entityBuilder;
import static com.almasb.fxgl.dsl.FXGLForKtKt.getAssetLoader;

/**
 * Represents a single playfield in Versus mode.
 * Encapsulates all entities and game logic for one player.
 * Follows OOP principles: high cohesion, low coupling.
 */
public class Playfield {
    // Configuration
    private static final int DEFAULT_LIVES = 5;
    private static final int BRICK_COLS = 5;
    private static final int BRICK_ROWS = 5;
    private static final double BRICK_OFFSET_Y = 80;
    private static final double BRICK_OFFSET_X = 20;
    private static final int POINTS_PER_BRICK = 10;
    private static final double PADDLE_OFFSET_FROM_BOTTOM = 60;

    // Playfield bounds
    private final int playerId;
    private final double x;
    private final double y;
    private final double width;
    private final double height;

    // Entities - private for encapsulation
    private Entity paddle;
    private Entity ball;
    private final List<Entity> bricks;
    private final List<Entity> walls;

    // Game state
    private int score;
    private int lives;
    private boolean gameOver;

    public Playfield(int playerId, double x, double y, double width, double height) {
        this.playerId = playerId;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.bricks = new ArrayList<>();
        this.walls = new ArrayList<>();
        this.score = 0;
        this.lives = DEFAULT_LIVES;
        this.gameOver = false;
    }

    // ==================== INITIALIZATION ====================

    /**
     * Initialize all entities for this playfield
     */
    public void initialize() {
        createWalls();
        createPaddle();
        createBall();
        createBricks();
    }

    private void createWalls() {
        // Left wall
        Entity leftWall = entityBuilder()
                .type(EntityType.WALL)
                .at(x, y)
                .viewWithBBox(new Rectangle(10, height, Color.TRANSPARENT))
                .collidable()
                .buildAndAttach();
        leftWall.setProperty("playerId", playerId);
        walls.add(leftWall);

        // Right wall
        Entity rightWall = entityBuilder()
                .type(EntityType.WALL)
                .at(x + width - 10, y)
                .viewWithBBox(new Rectangle(10, height, Color.TRANSPARENT))
                .collidable()
                .buildAndAttach();
        rightWall.setProperty("playerId", playerId);
        walls.add(rightWall);

        // Top wall
        Entity topWall = entityBuilder()
                .type(EntityType.WALL)
                .at(x, y)
                .viewWithBBox(new Rectangle(width, 10, Color.TRANSPARENT))
                .collidable()
                .buildAndAttach();
        topWall.setProperty("playerId", playerId);
        walls.add(topWall);
    }

    private void createPaddle() {
        double paddleX = getCenterX() - GameConstants.PADDLE_WIDTH / 2.0;
        double paddleY = y + height - PADDLE_OFFSET_FROM_BOTTOM;

        this.paddle = entityBuilder()
                .type(EntityType.PADDLE)
                .at(paddleX, paddleY)
                .view(TextureUtils.loadTexture(GameConstants.PADDLE_TEXTURE,
                        GameConstants.PADDLE_WIDTH,
                        GameConstants.PADDLE_HEIGHT))
                .bbox(new HitBox(BoundingShape.box(GameConstants.PADDLE_WIDTH,
                        GameConstants.PADDLE_HEIGHT)))
                .with(new PaddleComponent(playerId))
                .collidable()
                .buildAndAttach();
    }

    private void createBall() {
        double ballX = getCenterX() - GameConstants.BALL_SIZE / 2.0;
        double ballY = getCenterY();

        this.ball = entityBuilder()
                .type(EntityType.BALL)
                .at(ballX, ballY)
                .view(TextureUtils.loadTexture(GameConstants.BALL_TEXTURE,
                        GameConstants.BALL_SIZE,
                        GameConstants.BALL_SIZE))
                .bbox(new HitBox(BoundingShape.circle(GameConstants.BALL_SIZE / 2)))
                .with(new BallComponent(this))
                .collidable()
                .buildAndAttach();

        ball.setProperty("playerId", playerId);
    }

    private void createBricks() {
        double offsetY = y + BRICK_OFFSET_Y;
        double offsetX = x + BRICK_OFFSET_X;

        var baseTexture = getAssetLoader().loadTexture(GameConstants.BRICK_TEXTURE);

        for (int row = 0; row < BRICK_ROWS; row++) {
            for (int col = 0; col < BRICK_COLS; col++) {
                Entity brick = entityBuilder()
                        .type(EntityType.BRICK)
                        .at(offsetX + col * GameConstants.BRICK_SIZE,
                                offsetY + row * GameConstants.BRICK_SIZE)
                        .view(TextureUtils.loadTextureCopy(baseTexture,
                                GameConstants.BRICK_SIZE,
                                GameConstants.BRICK_SIZE))
                        .bbox(new HitBox(BoundingShape.box(GameConstants.BRICK_SIZE,
                                GameConstants.BRICK_SIZE)))
                        .with(new BrickComponent())
                        .collidable()
                        .buildAndAttach();

                brick.setProperty("playerId", playerId);
                bricks.add(brick);
            }
        }
    }

    // ==================== GAME LOGIC ====================

    /**
     * Check if ball is out of bounds
     */
    public boolean isBallOutOfBounds() {
        return ball != null && ball.getY() > getBottomBoundary();
    }

    /**
     * Reset ball to center position
     */
    public void resetBall() {
        // For versus mode we want the ball to re-attach to the player's paddle
        if (ball != null) {
            BallComponent ballComp = ball.getComponent(BallComponent.class);

            // If we have both paddle and component, attach to paddle so
            // BallComponent's onUpdate will position it on the paddle and
            // prevent physics processing until launch.
            if (ballComp != null) {
                ballComp.attachToPaddle();
            }

            // Also immediately position the ball on top of the paddle to avoid
            // any visual glitch before the next update/frame.
            if (paddle != null) {
                double ballX = paddle.getX() + (paddle.getWidth() - GameConstants.BALL_SIZE) / 2.0;
                double ballY = paddle.getY() - GameConstants.BALL_SIZE - 2.0;
                ball.setPosition(ballX, ballY);
            }
        }
    }

    /**
     * Destroy a brick and update score
     */
    public void destroyBrick(Entity brick) {
        if (bricks.remove(brick)) {
            brick.removeFromWorld();
            addScore(POINTS_PER_BRICK);
        }
    }

    /**
     * Respawn all bricks
     */
    public void respawnBricks() {
        clearBricks();
        createBricks();
    }

    private void clearBricks() {
        for (Entity brick : bricks) {
            brick.removeFromWorld();
        }
        bricks.clear();
    }

    /**
     * Player loses a life
     */
    public void loseLife() {
        lives--;
        if (lives <= 0) {
            gameOver = true;
        }
    }

    /**
     * set per-player lives from world properties).
     */
    public void setLives(int lives) {
        this.lives = lives;
    }

    // ==================== BOUNDARY HELPERS ====================

    public double getCenterX() {
        return x + width / 2.0;
    }

    public double getCenterY() {
        return y + height / 2.0;
    }

    public double getLeftBoundary() {
        return x + 10;
    }

    public double getRightBoundary(double objectWidth) {
        return x + width - objectWidth - 10;
    }

    public double getTopBoundary() {
        return y + 10;
    }

    public double getBottomBoundary() {
        return y + height;
    }

    // ==================== GETTERS (MINIMAL) ====================

    public int getPlayerId() {
        return playerId;
    }

    public Entity getPaddle() {
        return paddle;
    }

    public Entity getBall() {
        return ball;
    }

    public List<Entity> getBricks() {
        return new ArrayList<>(bricks); // Defensive copy
    }

    public int getScore() {
        return score;
    }

    public int getLives() {
        return lives;
    }

    public boolean isGameOver() {
        return gameOver;
    }

    public boolean hasBricks() {
        return !bricks.isEmpty();
    }

    // ==================== PACKAGE-PRIVATE (for component access) ====================

    void addScore(int points) {
        this.score += points;
    }
}
