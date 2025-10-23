package com.birb_birb.blockbounce.entities;

import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.component.Component;
import com.birb_birb.blockbounce.constants.EntityType;
import com.birb_birb.blockbounce.constants.GameConstants;
import com.birb_birb.blockbounce.core.gamemode.versus.Playfield;
import com.birb_birb.blockbounce.utils.BallPhysics;
import com.birb_birb.blockbounce.utils.SoundManager;
import javafx.geometry.Point2D;

import java.util.List;

import static com.almasb.fxgl.dsl.FXGL.*;

/**
 * Unified Ball component that works for both single-player and versus modes.
 * Uses Strategy Pattern to handle different game modes.
 */
public class BallComponent extends Component {

    private static final double BASE_SPEED = 3.0;
    private Point2D velocity = new Point2D(2.5, -2.5);
    private boolean hasCollidedThisFrame = false;
    private double collisionCooldown = 0;

    // Ball launch mechanism
    private boolean isAttachedToPaddle = true;
    private boolean hasLaunched = false;

    // Freeze mechanism for save/load
    private boolean isFrozen = false;

    // Strategy: null for single-player, non-null for versus mode
    private final Playfield playfield;

    // Constructor for single-player modes
    public BallComponent() {
        this.playfield = null;
    }

    // Constructor for versus mode
    public BallComponent(Playfield playfield) {
        this.playfield = playfield;
    }

    @Override
    public void onUpdate(double tpf) {
        // If frozen, don't process any movement
        if (isFrozen) {
            return;
        }

        // If ball is attached to paddle, follow paddle position
        if (isAttachedToPaddle) {
            Entity paddle = getPaddle();
            if (paddle != null) {
                // Center ball on top of paddle
                double ballX = paddle.getX() + (paddle.getWidth() - entity.getWidth()) / 2;
                double ballY = paddle.getY() - entity.getHeight() - 2;
                entity.setPosition(ballX, ballY);
            }
            return; // Don't process physics while attached
        }

        if (collisionCooldown > 0) {
            collisionCooldown -= tpf;
        }

        hasCollidedThisFrame = false;
        entity.translate(velocity);

        // Wall collision - strategy based
        handleWallCollision();

        // Paddle collision
        if (!hasCollidedThisFrame && collisionCooldown <= 0) {
            handlePaddleCollision();
        }

        // Brick collision
        if (!hasCollidedThisFrame && collisionCooldown <= 0) {
            handleBrickCollision();
        }

        // Out of bounds check
        if (isOutOfBounds()) {
            handleOutOfBounds();
        }
    }

    // ==================== COLLISION HANDLERS ====================

    private void handleWallCollision() {
        double leftBound, rightBound, topBound;

        if (playfield != null) {
            // Versus mode - use playfield boundaries
            leftBound = playfield.getLeftBoundary();
            rightBound = playfield.getRightBoundary(entity.getWidth());
            topBound = playfield.getTopBoundary();
        } else {
            // Single-player mode - use global boundaries
            leftBound = GameConstants.OFFSET_LEFT + 10;
            rightBound = GameConstants.WINDOW_WIDTH - GameConstants.OFFSET_RIGHT - 10;
            topBound = GameConstants.OFFSET_TOP + 10;
        }

        if (entity.getX() <= leftBound || entity.getRightX() >= rightBound || entity.getY() <= topBound) {
            if (entity.getX() <= leftBound || entity.getRightX() >= rightBound) {
                velocity = new Point2D(-velocity.getX(), velocity.getY());
                entity.setX(Math.max(leftBound, Math.min(entity.getX(), rightBound - entity.getWidth())));
            }

            if (entity.getY() <= topBound) {
                velocity = new Point2D(velocity.getX(), -velocity.getY());
                entity.setY(topBound);
            }

            // Normalize velocity using BallPhysics to ensure valid angle
            velocity = BallPhysics.normalizeVelocity(velocity, BASE_SPEED);

            SoundManager.playHitSound();
        }
    }

    private void handlePaddleCollision() {
        Entity paddle = getPaddle();
        if (paddle == null || !entity.isColliding(paddle)) return;

        // Calculate collision geometry
        double ballCenterX = entity.getX() + entity.getWidth() / 2;
        double ballCenterY = entity.getY() + entity.getHeight() / 2;
        double paddleCenterX = paddle.getX() + paddle.getWidth() / 2;
        double paddleCenterY = paddle.getY() + paddle.getHeight() / 2;

        double deltaX = ballCenterX - paddleCenterX;
        double deltaY = ballCenterY - paddleCenterY;

        // Calculate which side was hit
        double ratioX = Math.abs(deltaX) / (paddle.getWidth() / 2);
        double ratioY = Math.abs(deltaY) / (paddle.getHeight() / 2);

        if (ratioY > ratioX) {
            // Hit from top or bottom
            double ballBottom = entity.getY() + entity.getHeight();
            double paddleTop = paddle.getY();

            if (ballBottom > paddleTop && velocity.getY() > 0 && deltaY < 0) {
                // Hit from TOP - normal bounce
                entity.setY(paddleTop - entity.getHeight() - 1);

                // Calculate bounce velocity using BallPhysics
                velocity = BallPhysics.calculatePaddleBounce(
                    ballCenterX,
                    paddleCenterX,
                    paddle.getWidth(),
                    BASE_SPEED
                );

                hasCollidedThisFrame = true;
                collisionCooldown = 0.05;
                SoundManager.playHitSound();
            }
        } else {
            // Hit from LEFT or RIGHT side - bounce horizontally only
            if (deltaX > 0) {
                // Hit from right side - push ball to the right
                entity.setX(paddle.getX() + paddle.getWidth() + 1);
            } else {
                // Hit from left side - push ball to the left
                entity.setX(paddle.getX() - entity.getWidth() - 1);
            }

            // Reverse horizontal velocity, keep vertical velocity
            velocity = new Point2D(-velocity.getX(), velocity.getY());

            // Normalize velocity to ensure valid angle
            velocity = BallPhysics.normalizeVelocity(velocity, BASE_SPEED);

            hasCollidedThisFrame = true;
            collisionCooldown = 0.05;
            SoundManager.playHitSound();
        }
    }

    private void handleBrickCollision() {
        List<Entity> bricks = getBricks();

        for (Entity brick : bricks) {
            if (entity.isColliding(brick)) {
                // Calculate collision side
                double ballCenterX = entity.getX() + entity.getWidth() / 2;
                double ballCenterY = entity.getY() + entity.getHeight() / 2;
                double brickCenterX = brick.getX() + brick.getWidth() / 2;
                double brickCenterY = brick.getY() + brick.getHeight() / 2;

                double deltaX = ballCenterX - brickCenterX;
                double deltaY = ballCenterY - brickCenterY;

                double ratioX = Math.abs(deltaX) / (brick.getWidth() / 2);
                double ratioY = Math.abs(deltaY) / (brick.getHeight() / 2);

                if (ratioX > ratioY) {
                    // Hit from left or right -> Push ball out horizontally
                    velocity = new Point2D(-velocity.getX(), velocity.getY());
                    if (deltaX > 0) {
                        entity.setX(brick.getX() + brick.getWidth() + 1);
                    } else {
                        entity.setX(brick.getX() - entity.getWidth() - 1);
                    }
                } else {
                    // Hit from top or bottom -> Push ball out vertically
                    velocity = new Point2D(velocity.getX(), -velocity.getY());
                    if (deltaY > 0) {
                        entity.setY(brick.getY() + brick.getHeight() + 1);
                    } else {
                        entity.setY(brick.getY() - entity.getHeight() - 1);
                    }
                }

                // Normalize velocity using BallPhysics to ensure valid angle and speed
                velocity = BallPhysics.normalizeVelocity(velocity, BASE_SPEED);

                // Destroy brick using appropriate strategy
                destroyBrick(brick);

                hasCollidedThisFrame = true;
                collisionCooldown = 0.05;
                SoundManager.playBreakSound();
                break;
            }
        }
    }

    // ==================== STRATEGY METHODS ====================

    private Entity getPaddle() {
        if (playfield != null) {
            return playfield.getPaddle();
        } else {
            var paddles = getGameWorld().getEntitiesByType(EntityType.PADDLE);
            return paddles.isEmpty() ? null : paddles.get(0);
        }
    }

    private List<Entity> getBricks() {
        if (playfield != null) {
            return playfield.getBricks();
        } else {
            return getGameWorld().getEntitiesByType(EntityType.BRICK);
        }
    }

    private void destroyBrick(Entity brick) {
        if (playfield != null) {
            playfield.destroyBrick(brick);
        } else {
            brick.removeFromWorld();
            inc("score", 10);
        }
    }

    private boolean isOutOfBounds() {
        if (playfield != null) {
            return playfield.isBallOutOfBounds();
        } else {
            return entity.getY() > GameConstants.WINDOW_HEIGHT - GameConstants.OFFSET_BOTTOM;
        }
    }

    private void handleOutOfBounds() {
        if (playfield != null) {
            // Versus mode - playfield handles this in VersusModeGame
            return;
        } else {
            // Single-player mode: attach the existing ball to the paddle instead
            Entity paddle = getPaddle();

            if (paddle != null) {
                // Stop movement and mark as attached so onUpdate will position it
                attachToPaddle();

                // Immediately position it on top of the paddle to avoid visual jump
                double ballX = paddle.getX() + (paddle.getWidth() - entity.getWidth()) / 2.0;
                double ballY = paddle.getY() - entity.getHeight() - 2.0;
                entity.setPosition(ballX, ballY);
            } else {
                // Fallback - if no paddle is found, reset to center as before
                resetBall();
            }

            // Decrement lives and check for game over
            inc("lives", -1);

            if (geti("lives") <= 0) {
                set("gameOver", true);
            }
        }
    }

    private void resetBall() {
        // Reset position to center
        entity.setPosition(
            GameConstants.OFFSET_LEFT + GameConstants.PLAYABLE_WIDTH / 2.0,
            GameConstants.OFFSET_TOP + GameConstants.PLAYABLE_HEIGHT / 2.0
        );

        // Reset velocity
        velocity = new Point2D(2.5, -2.5);

        // Reset collision states
        hasCollidedThisFrame = false;
        collisionCooldown = 0;

        // Attach ball back to paddle
        isAttachedToPaddle = true;
        hasLaunched = false;
    }

    // ==================== PUBLIC API ====================

    public Point2D getVelocity() {
        return velocity;
    }

    public void setVelocity(Point2D velocity) {
        this.velocity = velocity;
    }

    /**
     * Attach the ball to the paddle (used when a life is lost)
     */
    public void attachToPaddle() {
        this.isAttachedToPaddle = true;
        this.hasLaunched = false;
        this.velocity = new Point2D(0, 0);
        this.hasCollidedThisFrame = false;
        this.collisionCooldown = 0;
    }

    public void launch() {
        isAttachedToPaddle = false;
        hasLaunched = true;

        // Launch ball straight up with slight randomness for variety
        // X velocity is very small or zero for straight launch
        double launchAngle = Math.random() * 30 - 15; // Random angle between -15 and +15 degrees
        double launchSpeed = BASE_SPEED;

        velocity = new Point2D(
            launchSpeed * Math.sin(Math.toRadians(launchAngle)),
            -launchSpeed * Math.cos(Math.toRadians(launchAngle))
        );
    }

    public boolean hasLaunched() {
        return hasLaunched;
    }

    /**
     * Set the launched state (used when loading saved game)
     */
    public void setLaunched(boolean launched) {
        this.hasLaunched = launched;
        this.isAttachedToPaddle = !launched;
    }

    // ==================== FREEZE CONTROL ====================

    public void freeze() {
        isFrozen = true;
    }

    public void unfreeze() {
        isFrozen = false;
    }

    public boolean isFrozen() {
        return isFrozen;
    }
}
