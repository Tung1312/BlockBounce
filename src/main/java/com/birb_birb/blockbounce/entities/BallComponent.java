package com.birb_birb.blockbounce.entities;

import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.component.Component;
import com.birb_birb.blockbounce.constants.EntityType;
import com.birb_birb.blockbounce.constants.GameConstants;
import com.birb_birb.blockbounce.core.gamemode.versus.Playfield;
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

            SoundManager.playBounce();
        }
    }

    private void handlePaddleCollision() {
        Entity paddle = getPaddle();
        if (paddle == null || !entity.isColliding(paddle)) return;

        double ballBottom = entity.getY() + entity.getHeight();
        double paddleTop = paddle.getY();

        if (ballBottom > paddleTop && velocity.getY() > 0) {
            entity.setY(paddleTop - entity.getHeight() - 1);

            // Calculate bounce angle based on hit position
            double paddleCenter = paddle.getX() + paddle.getWidth() / 2;
            double ballCenter = entity.getX() + entity.getWidth() / 2;
            double hitOffset = (ballCenter - paddleCenter) / (paddle.getWidth() / 2);

            hitOffset = Math.max(-0.75, Math.min(0.75, hitOffset));
            double bounceAngle = hitOffset * Math.PI / 3;

            double newVelX = BASE_SPEED * Math.sin(bounceAngle);
            double newVelY = -BASE_SPEED * Math.cos(bounceAngle);
            newVelY = Math.min(newVelY, -2.0);

            velocity = new Point2D(newVelX, newVelY);
            hasCollidedThisFrame = true;
            collisionCooldown = 0.05;
            SoundManager.playPaddleHit();
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
                    velocity = new Point2D(-velocity.getX(), velocity.getY());
                    if (deltaX > 0) {
                        entity.setX(brick.getX() + brick.getWidth() + 1);
                    } else {
                        entity.setX(brick.getX() - entity.getWidth() - 1);
                    }
                } else {
                    velocity = new Point2D(velocity.getX(), -velocity.getY());
                    if (deltaY > 0) {
                        entity.setY(brick.getY() + brick.getHeight() + 1);
                    } else {
                        entity.setY(brick.getY() - entity.getHeight() - 1);
                    }
                }

                double currentSpeed = velocity.magnitude();
                if (currentSpeed > BASE_SPEED * 1.2) {
                    velocity = velocity.normalize().multiply(BASE_SPEED);
                }

                // Destroy brick using appropriate strategy
                destroyBrick(brick);

                hasCollidedThisFrame = true;
                collisionCooldown = 0.05;
                SoundManager.playBrickBreak();
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
            // Single-player mode
            resetBall();
            inc("lives", -1);

            if (geti("lives") <= 0) {
                set("gameOver", true);
            }
        }
    }

    private void resetBall() {
        entity.setPosition(
            GameConstants.OFFSET_LEFT + GameConstants.PLAYABLE_WIDTH / 2.0,
            GameConstants.OFFSET_TOP + GameConstants.PLAYABLE_HEIGHT / 2.0
        );
        velocity = new Point2D(2.5, -2.5);
        hasCollidedThisFrame = false;
        collisionCooldown = 0;
    }

    // ==================== PUBLIC API ====================

    public Point2D getVelocity() {
        return velocity;
    }

    public void setVelocity(Point2D velocity) {
        this.velocity = velocity;
    }
}

