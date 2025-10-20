package com.birb_birb.blockbounce.entities;

import com.almasb.fxgl.entity.component.Component;
import com.birb_birb.blockbounce.constants.EntityType;
import com.birb_birb.blockbounce.constants.GameConstants;
import javafx.geometry.Point2D;

import static com.almasb.fxgl.dsl.FXGL.*;


public class BallComponent extends Component {

    private Point2D velocity = new Point2D(2.5, -2.5);
    private boolean hasCollidedThisFrame = false;
    private double collisionCooldown = 0;

    @Override
    public void onUpdate(double tpf) {
        // Decrease cooldown
        if (collisionCooldown > 0) {
            collisionCooldown -= tpf;
        }

        hasCollidedThisFrame = false;
        entity.translate(velocity);

        // Bounce off walls (left and right)
        if (entity.getX() <= GameConstants.OFFSET_LEFT + 10 ||
            entity.getRightX() >= GameConstants.WINDOW_WIDTH - GameConstants.OFFSET_RIGHT - 10) {
            velocity = new Point2D(-velocity.getX(), velocity.getY());
            entity.setX(Math.max(GameConstants.OFFSET_LEFT + 10,
                        Math.min(entity.getX(), GameConstants.WINDOW_WIDTH - GameConstants.OFFSET_RIGHT - 10 - entity.getWidth())));
        }

        // Bounce off top wall
        if (entity.getY() <= GameConstants.OFFSET_TOP + 10) {
            velocity = new Point2D(velocity.getX(), -velocity.getY());
            entity.setY(GameConstants.OFFSET_TOP + 10);
        }

        // Check paddle collision - only once per frame to prevent multiple bounces
        if (!hasCollidedThisFrame && collisionCooldown <= 0) {
            getGameWorld().getEntitiesByType(EntityType.PADDLE).forEach(paddle -> {
                if (entity.isColliding(paddle) && !hasCollidedThisFrame) {
                    // Adjust ball position to prevent getting stuck in paddle
                    double ballBottom = entity.getY() + entity.getHeight();
                    double paddleTop = paddle.getY();

                    if (ballBottom > paddleTop && velocity.getY() > 0) {
                        // Push ball above paddle
                        entity.setY(paddleTop - entity.getHeight() - 1);

                        // Bounce with slight angle based on where ball hits paddle
                        double paddleCenter = paddle.getX() + paddle.getWidth() / 2;
                        double ballCenter = entity.getX() + entity.getWidth() / 2;
                        double hitOffset = (ballCenter - paddleCenter) / (paddle.getWidth() / 2);

                        // Add angle to the bounce (max ±1.5 horizontal velocity)
                        double newVelX = velocity.getX() + hitOffset * 1.2;
                        // Clamp horizontal velocity to prevent ball from going too fast horizontally
                        newVelX = Math.max(-4, Math.min(4, newVelX));

                        velocity = new Point2D(newVelX, -Math.abs(velocity.getY()));
                        hasCollidedThisFrame = true;
                        collisionCooldown = 0.05; // 50ms cooldown
                    }
                }
            });
        }

        // Brick collision - only destroy ONE brick per frame with proper position adjustment
        if (!hasCollidedThisFrame && collisionCooldown <= 0) {
            for (var brick : getGameWorld().getEntitiesByType(EntityType.BRICK)) {
                if (entity.isColliding(brick)) {
                    // Calculate collision side to determine bounce direction
                    double ballCenterX = entity.getX() + entity.getWidth() / 2;
                    double ballCenterY = entity.getY() + entity.getHeight() / 2;
                    double brickCenterX = brick.getX() + brick.getWidth() / 2;
                    double brickCenterY = brick.getY() + brick.getHeight() / 2;

                    double deltaX = ballCenterX - brickCenterX;
                    double deltaY = ballCenterY - brickCenterY;

                    // Determine which side was hit based on the ratio of deltas
                    double ratioX = Math.abs(deltaX) / (brick.getWidth() / 2);
                    double ratioY = Math.abs(deltaY) / (brick.getHeight() / 2);

                    if (ratioX > ratioY) {
                        // Hit from left or right
                        velocity = new Point2D(-velocity.getX(), velocity.getY());
                        // Push ball out horizontally
                        if (deltaX > 0) {
                            entity.setX(brick.getX() + brick.getWidth() + 1);
                        } else {
                            entity.setX(brick.getX() - entity.getWidth() - 1);
                        }
                    } else {
                        // Hit from top or bottom
                        velocity = new Point2D(velocity.getX(), -velocity.getY());
                        // Push ball out vertically
                        if (deltaY > 0) {
                            entity.setY(brick.getY() + brick.getHeight() + 1);
                        } else {
                            entity.setY(brick.getY() - entity.getHeight() - 1);
                        }
                    }

                    brick.removeFromWorld();
                    hasCollidedThisFrame = true;
                    collisionCooldown = 0.05; // 50ms cooldown to prevent multiple hits
                    break; // Stop after first collision
                }
            }
        }

        // Reset if ball falls off screen
        if (entity.getY() > GameConstants.WINDOW_HEIGHT - GameConstants.OFFSET_BOTTOM) {
            resetBall();
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
}

