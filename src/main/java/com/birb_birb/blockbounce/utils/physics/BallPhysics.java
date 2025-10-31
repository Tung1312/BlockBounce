package com.birb_birb.blockbounce.utils.physics;

import javafx.geometry.Point2D;

/**
 * Utility class for ball physics calculations.
 * Contains methods for normalizing angles and calculating velocities.
 */
public class BallPhysics {

    // Limit angle to avoid the ball falling too steeply (75 degrees)
    private static final double MAX_VERTICAL_ANGLE = 75.0;

    // Minimum angle to avoid the ball traveling too flat (15 degrees)
    private static final double MIN_VERTICAL_ANGLE = 15.0;

    // Maximum allowed speed multiplier
    private static final double MAX_SPEED_MULTIPLIER = 1.2;

    private BallPhysics() {
        // Prevent instantiation
    }

    /**
     * Calculate angle from current velocity.
     */
    public static double calculateAngle(double velocityX, double velocityY) {
        return Math.toDegrees(Math.atan2(velocityY, velocityX));
    }

    /**
     * Normalize angle to avoid the ball falling too steeply or traveling too flat.
     */
    public static double normalizeAngle(double angle) {
        double normalizedAngle = angle;

        // Handle angles in the range -180 to 180
        while (normalizedAngle > 180) normalizedAngle -= 360;
        while (normalizedAngle < -180) normalizedAngle += 360;

        double absAngle = Math.abs(normalizedAngle);

        // Handle 0° (completely horizontal)
        if (absAngle < 0.01) {
            return MIN_VERTICAL_ANGLE; // Default to a small positive angle
        }

        // If angle is too steep (> 75° and < 105°), clamp to 75°
        if (absAngle > MAX_VERTICAL_ANGLE && absAngle < 180 - MAX_VERTICAL_ANGLE) {
            normalizedAngle = Math.signum(normalizedAngle) * MAX_VERTICAL_ANGLE;
        }

        // If angle is too flat (< 15° or > 165°), push to the minimum angle
        if (absAngle < MIN_VERTICAL_ANGLE && absAngle > 0.01) {
            normalizedAngle = Math.signum(normalizedAngle) * MIN_VERTICAL_ANGLE;
        }

        // Handle angles near 180° (traveling horizontally backwards)
        if (absAngle > 180 - MIN_VERTICAL_ANGLE) {
            normalizedAngle = Math.signum(normalizedAngle) * (180 - MIN_VERTICAL_ANGLE);
        }

        return normalizedAngle;
    }

    /**
     * Calculate velocity from angle and speed.
     */
    public static Point2D calculateVelocity(double angle, double speed) {
        double radians = Math.toRadians(angle);
        return new Point2D(
            Math.cos(radians) * speed,
            Math.sin(radians) * speed
        );
    }

    /**
     * Normalize velocity to ensure a reasonable angle and limit maximum speed.
     */
    public static Point2D normalizeVelocity(Point2D velocity, double baseSpeed) {
        // Calculate current angle
        double angle = calculateAngle(velocity.getX(), velocity.getY());

        // Normalize angle
        double normalizedAngle = normalizeAngle(angle);

        // Calculate current speed
        double currentSpeed = velocity.magnitude();

        // Limit maximum speed
        double targetSpeed = Math.min(currentSpeed, baseSpeed * MAX_SPEED_MULTIPLIER);

        // Create a new velocity with the normalized angle
        return calculateVelocity(normalizedAngle, targetSpeed);
    }

    /**
     * Calculate bounce velocity from paddle based on collision position.
     */
    public static Point2D calculatePaddleBounce(double ballCenterX, double paddleCenterX,
                                                double paddleWidth, double baseSpeed) {
        // Compute offset from paddle center (-1 to 1)
        double hitOffset = (ballCenterX - paddleCenterX) / (paddleWidth / 2);

        // Clamp hitOffset to [-0.75, 0.75]
        hitOffset = Math.max(-0.75, Math.min(0.75, hitOffset));

        // Calculate bounce angle (±60 degrees corresponding to ±π/3)
        double bounceAngle = hitOffset * Math.PI / 3;

        // Create velocity: X follows the offset direction, Y always points upward
        double newVelX = baseSpeed * Math.sin(bounceAngle);
        double newVelY = -baseSpeed * Math.cos(bounceAngle);

        // Ensure Y velocity points upward and is strong enough (minimum -2.0)
        newVelY = Math.min(newVelY, -2.0);

        return new Point2D(newVelX, newVelY);
    }

    /**
     * Check whether an angle is valid.
     */
    public static boolean isAngleValid(double angle) {
        double absAngle = Math.abs(angle);

        // Angle is valid if it lies within [15°, 75°] or [105°, 165°]
        return (absAngle >= MIN_VERTICAL_ANGLE && absAngle <= MAX_VERTICAL_ANGLE) ||
                (absAngle >= 180 - MAX_VERTICAL_ANGLE && absAngle <= 180 - MIN_VERTICAL_ANGLE);
    }

    /**
     * Get maximum vertical angle limit
     */
    public static double getMaxVerticalAngle() {
        return MAX_VERTICAL_ANGLE;
    }

    /**
     * Get minimum vertical angle limit
     */
    public static double getMinVerticalAngle() {
        return MIN_VERTICAL_ANGLE;
    }
}
