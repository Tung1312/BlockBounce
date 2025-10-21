package com.birb_birb.blockbounce.entities;

import com.almasb.fxgl.entity.component.Component;
import com.birb_birb.blockbounce.constants.EntityType;
import com.birb_birb.blockbounce.constants.GameConstants;
import com.birb_birb.blockbounce.utils.SoundManager;
import javafx.geometry.Point2D;

import static com.almasb.fxgl.dsl.FXGL.*;


public class BallComponent extends Component {

    private static final double BASE_SPEED = 3.0; // Tốc độ cơ bản không đổi
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
            SoundManager.playBounce(); // Thêm âm thanh
        }

        // Bounce off top wall
        if (entity.getY() <= GameConstants.OFFSET_TOP + 10) {
            velocity = new Point2D(velocity.getX(), -velocity.getY());
            entity.setY(GameConstants.OFFSET_TOP + 10);
            SoundManager.playBounce(); // Thêm âm thanh
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

                        // Calculate angle based on where ball hits paddle
                        double paddleCenter = paddle.getX() + paddle.getWidth() / 2;
                        double ballCenter = entity.getX() + entity.getWidth() / 2;
                        double hitOffset = (ballCenter - paddleCenter) / (paddle.getWidth() / 2);

                        // Clamp hit offset để tránh góc quá dốc
                        hitOffset = Math.max(-0.75, Math.min(0.75, hitOffset));

                        // Tính góc bounce (từ -60 đến 60 độ)
                        double bounceAngle = hitOffset * Math.PI / 3; // ±60 degrees

                        // Tạo velocity mới với tốc độ cố định BASE_SPEED
                        double newVelX = BASE_SPEED * Math.sin(bounceAngle);
                        double newVelY = -BASE_SPEED * Math.cos(bounceAngle);

                        // Đảm bảo velocity Y luôn hướng lên và không quá nhỏ
                        newVelY = Math.min(newVelY, -2.0);

                        velocity = new Point2D(newVelX, newVelY);
                        hasCollidedThisFrame = true;
                        collisionCooldown = 0.05; // 50ms cooldown
                        SoundManager.playPaddleHit(); // Thêm âm thanh
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

                    // Normalize velocity để giữ tốc độ ổn định sau va chạm
                    double currentSpeed = velocity.magnitude();
                    if (currentSpeed > BASE_SPEED * 1.2) {
                        velocity = velocity.normalize().multiply(BASE_SPEED);
                    }

                    brick.removeFromWorld();
                    hasCollidedThisFrame = true;
                    collisionCooldown = 0.05; // 50ms cooldown to prevent multiple hits

                    // Thêm âm thanh và cập nhật điểm
                    SoundManager.playBrickBreak();
                    inc("score", 10); // Tăng 10 điểm mỗi block

                    break; // Stop after first collision
                }
            }
        }

        // Reset if ball falls off screen
        if (entity.getY() > GameConstants.WINDOW_HEIGHT - GameConstants.OFFSET_BOTTOM) {
            resetBall();
            inc("lives", -1); // Trừ 1 mạng

            // Kiểm tra game over
            if (geti("lives") <= 0) {
                // Game over logic sẽ được xử lý ở game mode
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

    public Point2D getVelocity() {
        return velocity;
    }

    public void setVelocity(Point2D velocity) {
        this.velocity = velocity;
    }
}
