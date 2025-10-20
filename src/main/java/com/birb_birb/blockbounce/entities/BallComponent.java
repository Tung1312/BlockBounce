package com.birb_birb.blockbounce.entities;

import com.almasb.fxgl.entity.component.Component;
import com.birb_birb.blockbounce.constants.EntityType;
import com.birb_birb.blockbounce.constants.GameConstants;
import javafx.geometry.Point2D;

import static com.almasb.fxgl.dsl.FXGL.*;


public class BallComponent extends Component {

    private Point2D velocity = new Point2D(3, -2.5);
    private boolean hasCollidedThisFrame = false;

    @Override
    public void onUpdate(double tpf) {
        hasCollidedThisFrame = false;
        entity.translate(velocity);

        // Bounce off walls (left and right)
        if (entity.getX() <= GameConstants.OFFSET_LEFT + 10 ||
            entity.getRightX() >= GameConstants.WINDOW_WIDTH - GameConstants.OFFSET_RIGHT - 10) {
            velocity = new Point2D(-velocity.getX(), velocity.getY());
        }

        // Bounce off top wall
        if (entity.getY() <= GameConstants.OFFSET_TOP + 10) {
            velocity = new Point2D(velocity.getX(), -velocity.getY());
        }

        // Check paddle collision
        getGameWorld().getEntitiesByType(EntityType.PADDLE).forEach(paddle -> {
            if (entity.isColliding(paddle)) {
                velocity = new Point2D(velocity.getX(), -Math.abs(velocity.getY()));
            }
        });

        // Brick collision - only destroy ONE brick per frame
        if (!hasCollidedThisFrame) {
            for (var brick : getGameWorld().getEntitiesByType(EntityType.BRICK)) {
                if (entity.isColliding(brick)) {
                    brick.removeFromWorld();
                    velocity = new Point2D(velocity.getX(), -velocity.getY());
                    hasCollidedThisFrame = true;
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
        velocity = new Point2D(3, -2.5);
        hasCollidedThisFrame = false;
    }
}

