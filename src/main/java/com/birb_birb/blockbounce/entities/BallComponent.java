package com.birb_birb.blockbounce.entities;

import com.almasb.fxgl.entity.Entity;
import com.birb_birb.blockbounce.constants.BrickType;
import com.birb_birb.blockbounce.constants.EntityType;
import com.birb_birb.blockbounce.constants.GameConstants;
import com.birb_birb.blockbounce.constants.PowerUpType;
import com.birb_birb.blockbounce.core.GameFactory;
import com.birb_birb.blockbounce.core.gamemode.versus.Playfield;
import com.birb_birb.blockbounce.physics.BallPhysics;
import com.birb_birb.blockbounce.physics.CollisionHandler;
import com.birb_birb.blockbounce.physics.PhysicsComponent;
import com.birb_birb.blockbounce.utils.SoundManager;
import javafx.geometry.Point2D;

import java.util.List;

import static com.almasb.fxgl.dsl.FXGL.*;

public class BallComponent extends PhysicsComponent {

    private boolean hasCollidedThisFrame = false;
    private double collisionCooldown = 0;
    private boolean isAttachedToPaddle = true;
    private boolean hasLaunched = false;
    private double speedMultiplier = 1.0;
    private final Playfield playfield;

    public BallComponent() {
        this.playfield = null;
        this.velocity = new Point2D(2.5, -2.5);
    }

    public BallComponent(Playfield playfield) {
        this.playfield = playfield;
        this.velocity = new Point2D(2.5, -2.5);
    }

    @Override
    public void onUpdate(double tpf) {
        if (isFrozen) {
            return;
        }

        Entity paddle = getPaddle();

        // If ball is attached to paddle, follow paddle position
        if (isAttachedToPaddle) {
            if (paddle != null) {
                double paddleWidth = getPaddleWidth(paddle);

                // Center ball on top of paddle
                double ballX = paddle.getX() + (paddleWidth - entity.getWidth()) / 2;
                double ballY = paddle.getY() - entity.getHeight() - 2;
                entity.setPosition(ballX, ballY);
            }
            // skip applying physics when attached to paddle
            return;
        }

        if (collisionCooldown > 0) {
            collisionCooldown -= tpf;
        }

        hasCollidedThisFrame = false;
        entity.translate(velocity);

        // Wall collision
        handleWallCollision();

        // Paddle collision
        if (!hasCollidedThisFrame && collisionCooldown <= 0) {
            handlePaddleCollision(paddle);
        }

        // Brick collision
        if (!hasCollidedThisFrame && collisionCooldown <= 0) {
            handleBrickCollision();
        }

        // normalize velocity only if collision occurred this frame
        if (hasCollidedThisFrame) {
            velocity = BallPhysics.normalizeVelocity(velocity, GameConstants.BASE_SPEED * speedMultiplier);
        }

        // Out of bounds check
        if (isOutOfBounds()) {
            handleOutOfBounds();
        }
    }

    // COLLIDE HANDLERS

    private void handleWallCollision() {
        double leftBound, rightBound, topBound;

        if (playfield != null) {
            // when in Versus mode - use playfield bounds
            leftBound = playfield.getLeftBoundary();
            rightBound = playfield.getRightBoundary(entity.getWidth());
            topBound = playfield.getTopBoundary();
        } else {
            // when in Single-player mode - use global bounds
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

            hasCollidedThisFrame = true;
            SoundManager.playHitSound();
        }
    }

    private void handlePaddleCollision(Entity paddle) {
        if (paddle == null) return;

        double paddleWidth = getPaddleWidth(paddle);
        double paddleOffsetX = (GameConstants.PADDLE_WIDTH - paddleWidth) / 2.0;

        Point2D newVelocity = CollisionHandler.resolvePaddleCollisionWithOffset(
            entity,
            paddle,
            paddleWidth,
            paddleOffsetX,
            GameConstants.BASE_SPEED * speedMultiplier
        );

        // If collision occurred, update velocity and set collision flags
        if (newVelocity != null) {
            velocity = newVelocity;
            hasCollidedThisFrame = true;
            collisionCooldown = 0.05;
            SoundManager.playHitSound();
        }
    }

    private void handleBrickCollision() {
        List<Entity> bricks = getBricks();

        for (Entity brick : bricks) {
            if (!CollisionHandler.checkAABB(entity, brick)) {
                continue;
            }

            // full collision check
            if (entity.isColliding(brick)) {
                velocity = CollisionHandler.resolveBrickCollision(entity, brick, velocity);

                BrickComponent brickComponent = brick.getComponent(BrickComponent.class);
                if (brickComponent != null) {
                    brickComponent.takeDamage();

                    // only destroy bricks with
                    if (brickComponent.getDurability() == 0) {
                        destroyBrick(brick);
                    }
                }

                hasCollidedThisFrame = true;
                collisionCooldown = 0.05;
                SoundManager.playBreakSound();
                break;
            }
        }
    }

    private Entity getPaddle() {
        if (playfield != null) {
            return playfield.getPaddle();
        } else {
            var paddles = getGameWorld().getEntitiesByType(EntityType.PADDLE);
            return paddles.isEmpty() ? null : paddles.getFirst();
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
            // In versus mode, delegate destruction
            playfield.destroyBrick(brick);

            // Check if this is a lucky brick and spawn power-up
            BrickComponent brickComp = brick.getComponent(BrickComponent.class);
            if (brickComp != null && brickComp.getBrickType() == BrickType.LUCKY) {
                // Lucky brick always spawns a power-up
                int r = (int) (Math.random() * 5);
                PowerUpType type = r == 0 ? PowerUpType.DOUBLE_BALL
                    : r == 1 ? PowerUpType.SMALL_PADDLE
                    : r == 2 ? PowerUpType.FAST_BALL
                    : r == 3 ? PowerUpType.EXTRA_LIFE
                    : PowerUpType.DOUBLE_POINTS;

                // target the owner of this ball if available
                int target = 0;
                try { target = entity.getInt("playerId"); } catch (Exception ignored) {}

                com.birb_birb.blockbounce.core.GameFactory.createPowerUp(brick.getX(), brick.getY(), type, target);
            }

        } else {
            brick.removeFromWorld();

            // Check if double points is active
            int points = 10;
            try {
                if (getb("doublePoints")) {
                    points *= 2;
                }
            } catch (Exception ignored) {}

            inc("score", points);

            // Check if this is a lucky brick and spawn power-up
            BrickComponent brickComp = brick.getComponent(BrickComponent.class);
            if (brickComp != null && brickComp.getBrickType() == BrickType.LUCKY) {
                // Lucky brick always spawns a power-up
                int r = (int) (Math.random() * 5);
                PowerUpType type = r == 0 ? PowerUpType.DOUBLE_BALL
                    : r == 1 ? PowerUpType.SMALL_PADDLE
                    : r == 2 ? PowerUpType.FAST_BALL
                    : r == 3 ? PowerUpType.EXTRA_LIFE
                    : PowerUpType.DOUBLE_POINTS;

                GameFactory.createPowerUp(brick.getX(), brick.getY(), type, 1);
            }
        }
    }


    /**Attach the ball to the paddle*/
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
        double launchSpeed = GameConstants.BASE_SPEED;

        velocity = new Point2D(
            launchSpeed * Math.sin(Math.toRadians(launchAngle)),
            -launchSpeed * Math.cos(Math.toRadians(launchAngle))
        );
    }

    public boolean hasLaunched() {
        return hasLaunched;
    }

    /**Set the launched state*/
    public void setLaunched(boolean launched) {
        this.hasLaunched = launched;
        this.isAttachedToPaddle = !launched;
    }

    // SPEED CONTROL

    public double getSpeedMultiplier() {
        return speedMultiplier;
    }

    public void setSpeedMultiplier(double multiplier) {
        this.speedMultiplier = multiplier;
        // Immediately apply to current velocity if ball is moving
        if (velocity.magnitude() > 0.1) {
            double currentAngle = Math.atan2(velocity.getY(), velocity.getX());
            double newSpeed = GameConstants.BASE_SPEED * multiplier;
            velocity = new Point2D(
                Math.cos(currentAngle) * newSpeed,
                Math.sin(currentAngle) * newSpeed
            );
        }
    }

    // ==================== HELPER METHODS ====================

    // Helper to get paddle logical width (uses property if present)
    private double getPaddleWidth(Entity paddle) {
        try {
            return paddle.getDouble("paddleWidth");
        } catch (Exception ignored) {
            return paddle.getWidth();
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
        // In Versus mode, DON'T remove the ball - let Playfield handle everything
        if (playfield != null) {
            // Do nothing - Playfield will detect and handle it
            return;
        }

        // Single-player mode logic
        List<Entity> allBalls = getGameWorld().getEntitiesByType(EntityType.BALL);
        // If there's more than one ball, just remove this one silently
        if (allBalls.size() > 1) {
            entity.removeFromWorld();
            return;
        }
        inc("lives", -1);
        if (geti("lives") > 0) {
            SoundManager.playDeathSound();
        }
        if (geti("lives") <= 0) {
            set("gameOver", true);
        } else {
            resetGameState();
        }
    }

    /**
     * Reset paddle position, ball state, and remove power-up effects when a life is lost
     */
    private void resetGameState() {
        Entity paddle = getPaddle();

        if (paddle != null) {
            // Reset paddle width to normal (remove SMALL_PADDLE power-up effect)
            try {
                paddle.setProperty("paddleWidth", GameConstants.PADDLE_WIDTH);
            } catch (Exception ignored) {}

            // Reset paddle hitbox to normal
            try {
                paddle.getBoundingBoxComponent().clearHitBoxes();
                paddle.getBoundingBoxComponent().addHitBox(new com.almasb.fxgl.physics.HitBox(
                    com.almasb.fxgl.physics.BoundingShape.box(GameConstants.PADDLE_WIDTH, GameConstants.PADDLE_HEIGHT)
                ));
            } catch (Exception e) {
                System.err.println("Error restoring paddle hitbox on reset: " + e.getMessage());
            }

            // Reset paddle visual scale
            try {
                if (!paddle.getViewComponent().getChildren().isEmpty()) {
                    javafx.scene.Node view = paddle.getViewComponent().getChildren().getFirst();
                    if (view != null) {
                        view.setScaleX(1.0);
                    }
                }
            } catch (Exception ignored) {}
        }

        // Remove all extra balls (from DOUBLE_BALL power-up)
        List<Entity> allBalls = getGameWorld().getEntitiesByType(EntityType.BALL);
        for (Entity ball : allBalls) {
            if (ball != entity) {
                ball.removeFromWorld();
            }
        }

        // Remove all falling power-ups
        List<Entity> powerUps = getGameWorld().getEntitiesByType(EntityType.POWERUP);
        for (Entity powerUp : powerUps) {
            powerUp.removeFromWorld();
        }

        // Reset ball speed multiplier to normal (remove FAST_BALL power-up effect)
        this.speedMultiplier = 1.0;

        // Attach ball to paddle and position it
        attachToPaddle();

        if (paddle != null) {
            double ballX = paddle.getX() + (getPaddleWidth(paddle) - entity.getWidth()) / 2.0;
            double ballY = paddle.getY() - entity.getHeight() - 2.0;
            entity.setPosition(ballX, ballY);
        }
    }
}
