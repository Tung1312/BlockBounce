package com.birb_birb.blockbounce.physics;

import com.almasb.fxgl.entity.Entity;
import javafx.geometry.Point2D;

public class CollisionHandler {

    private CollisionHandler() {}

    /**check for collision*/
    public static boolean checkAABB(Entity a, Entity b) {
        return a.getRightX() >= b.getX() &&
               a.getX() <= b.getRightX() &&
               a.getBottomY() >= b.getY() &&
               a.getY() <= b.getBottomY();
    }

    /**Calculate hit side of entity B by entity A using overlap depth*/
    public static String detectCollisionSide(Entity a, Entity b) {
        double overlapLeft = a.getRightX() - b.getX();
        double overlapRight = b.getRightX() - a.getX();
        double overlapTop = a.getBottomY() - b.getY();
        double overlapBottom = b.getBottomY() - a.getY();

        double minOverlap = Math.min(
            Math.min(overlapLeft, overlapRight),
            Math.min(overlapTop, overlapBottom)
        );

        if (minOverlap == overlapLeft) return "left";
        if (minOverlap == overlapRight) return "right";
        if (minOverlap == overlapTop) return "top";
        return "bottom";
    }

    /** check if entity is moving towards another entity */
    public static boolean isMovingTowards(Point2D velocity, Entity from, Entity to) {
        double dx = to.getCenter().getX() - from.getCenter().getX();
        double dy = to.getCenter().getY() - from.getCenter().getY();

        return velocity.dotProduct(new Point2D(dx, dy)) > 0;
    }

    /** check if collision is valid (overlapping and moving towards) */
    public static boolean isValidCollision(Entity entity, Point2D velocity, Entity target) {
        if (!checkAABB(entity, target)) {
            return false;
        }
        return isMovingTowards(velocity, entity, target);
    }

    /**Resolver for brick collision (includes position adjustment)*/
    public static Point2D resolveBrickCollision(Entity ball, Entity brick, Point2D velocity) {
        // Calculate collision geometry
        double ballCenterX = ball.getX() + ball.getWidth() / 2;
        double ballCenterY = ball.getY() + ball.getHeight() / 2;
        double brickCenterX = brick.getX() + brick.getWidth() / 2;
        double brickCenterY = brick.getY() + brick.getHeight() / 2;

        double deltaX = ballCenterX - brickCenterX;
        double deltaY = ballCenterY - brickCenterY;

        double ratioX = Math.abs(deltaX) / (brick.getWidth() / 2);
        double ratioY = Math.abs(deltaY) / (brick.getHeight() / 2);

        Point2D newVelocity;

        if (ratioX > ratioY) {
            // Hit from left or right -> Push ball horizontally
            newVelocity = new Point2D(-velocity.getX(), velocity.getY());
            if (deltaX > 0) {
                ball.setX(brick.getX() + brick.getWidth() + 1);
            } else {
                ball.setX(brick.getX() - ball.getWidth() - 1);
            }
        } else {
            // Hit from top or bottom -> Push ball vertically
            newVelocity = new Point2D(velocity.getX(), -velocity.getY());
            if (deltaY > 0) {
                ball.setY(brick.getY() + brick.getHeight() + 1);
            } else {
                ball.setY(brick.getY() - ball.getHeight() - 1);
            }
        }

        return newVelocity;
    }

    /**Resolver for paddle collision with position adjustment*/
    public static Point2D resolvePaddleCollisionWithOffset(Entity ball, Entity paddle, double paddleWidth, double paddleOffsetX, double baseSpeed) {
        double paddleHeight = paddle.getHeight();

        // Calculate bounds
        double ballLeft = ball.getX();
        double ballRight = ball.getX() + ball.getWidth();
        double ballTop = ball.getY();
        double ballBottom = ball.getY() + ball.getHeight();

        double paddleLeft = paddle.getX() + paddleOffsetX;
        double paddleRight = paddle.getX() + paddleOffsetX + paddleWidth;
        double paddleTop = paddle.getY();
        double paddleBottom = paddle.getY() + paddleHeight;

        // Check overlap
        boolean overlapping = ballRight >= paddleLeft && ballLeft <= paddleRight &&
                             ballBottom >= paddleTop && ballTop <= paddleBottom;

        if (!overlapping) {
            return null;
        }

        // Calculate collision geometry
        double ballCenterX = (ballLeft + ballRight) / 2.0;
        double ballCenterY = (ballTop + ballBottom) / 2.0;
        double paddleCenterX = paddle.getX() + paddleOffsetX + paddleWidth / 2.0;
        double paddleCenterY = paddle.getY() + paddleHeight / 2.0;

        double deltaX = ballCenterX - paddleCenterX;
        double deltaY = ballCenterY - paddleCenterY;

        // Calculate which side was hit
        double ratioX = Math.abs(deltaX) / (paddleWidth / 2);
        double ratioY = Math.abs(deltaY) / (paddleHeight / 2);

        if (ratioY > ratioX) {
            // Hit from top - return paddle bounce velocity
            ball.setY(paddleTop - ball.getHeight() - 1);

            return BallPhysics.calculatePaddleBounce(
                ballCenterX,
                paddleCenterX,
                paddleWidth,
                baseSpeed
            );
        } else {
            // Hit from side - reflect horizontally
            if (deltaX > 0) {
                ball.setX(paddleRight + 1);
            } else {
                ball.setX(paddleLeft - ball.getWidth() - 1);
            }

            return new Point2D(-ball.getX(), ball.getY());
        }
    }
}

