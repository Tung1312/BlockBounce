package com.birb_birb.blockbounce.entities;

import com.almasb.fxgl.entity.component.Component;
import com.birb_birb.blockbounce.constants.EntityType;
import com.birb_birb.blockbounce.constants.GameConstants;
import javafx.geometry.Point2D;

import static com.almasb.fxgl.dsl.FXGL.*;


public class BallComponent extends Component {

    private Point2D velocity = new Point2D(4, -4);

    @Override
    public void onUpdate(double tpf) {
        entity.translate(velocity);

        // Bounce off walls
        if (entity.getX() <= 10 || entity.getRightX() >= GameConstants.WINDOW_WIDTH - 10) {
            velocity = new Point2D(-velocity.getX(), velocity.getY());
        }

        if (entity.getY() <= 10) {
            velocity = new Point2D(velocity.getX(), -velocity.getY());
        }

        // Check paddle collision
        getGameWorld().getEntitiesByType(EntityType.PADDLE).forEach(paddle -> {
            if (entity.isColliding(paddle)) {
                velocity = new Point2D(velocity.getX(), -Math.abs(velocity.getY()));
            }
        });

        // Brick collision
        getGameWorld().getEntitiesByType(EntityType.BRICK).forEach(brick -> {
            if (entity.isColliding(brick)) {
                brick.removeFromWorld();
                velocity = new Point2D(velocity.getX(), -velocity.getY());
            }
        });

        // Reset if ball falls off screen
        if (entity.getY() > GameConstants.WINDOW_HEIGHT) {
            resetBall();
        }
    }

    private void resetBall() {
        entity.setPosition(GameConstants.WINDOW_WIDTH / 2.0, GameConstants.WINDOW_HEIGHT / 2.0);
        velocity = new Point2D(4, -4);
    }
}
