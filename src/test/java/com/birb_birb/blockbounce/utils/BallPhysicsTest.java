package com.birb_birb.blockbounce.utils;

import javafx.geometry.Point2D;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for BallPhysics class
 */
@DisplayName("Ball Physics Tests")
class BallPhysicsTest {

    private static final double DELTA = 0.1; // Precision for floating point comparison

    @Test
    @DisplayName("Calculate angle from velocity - 45 degrees")
    void testCalculateAngle45Degrees() {
        double angle = BallPhysics.calculateAngle(1, 1);
        assertEquals(45.0, angle, DELTA);
    }

    @Test
    @DisplayName("Calculate angle from velocity - 0 degrees (horizontal right)")
    void testCalculateAngle0Degrees() {
        double angle = BallPhysics.calculateAngle(1, 0);
        assertEquals(0.0, angle, DELTA);
    }

    @Test
    @DisplayName("Calculate angle from velocity - 90 degrees (downward)")
    void testCalculateAngle90Degrees() {
        double angle = BallPhysics.calculateAngle(0, 1);
        assertEquals(90.0, angle, DELTA);
    }

    @Test
    @DisplayName("Calculate angle from velocity - -90 degrees (upward)")
    void testCalculateAngleNegative90Degrees() {
        double angle = BallPhysics.calculateAngle(0, -1);
        assertEquals(-90.0, angle, DELTA);
    }

    @Test
    @DisplayName("Normalize angle - too steep (80°) should reduce to 75°")
    void testNormalizeAngleTooSteep() {
        double angle = BallPhysics.normalizeAngle(80);
        assertEquals(75.0, angle, DELTA);
    }

    @Test
    @DisplayName("Normalize angle - too flat (5°) should increase to 15°")
    void testNormalizeAngleTooFlat() {
        double angle = BallPhysics.normalizeAngle(5);
        assertEquals(15.0, angle, DELTA);
    }

    @Test
    @DisplayName("Normalize angle - normal angle (45°) remains unchanged")
    void testNormalizeAngleNormal() {
        double angle = BallPhysics.normalizeAngle(45);
        assertEquals(45.0, angle, DELTA);
    }

    @Test
    @DisplayName("Normalize angle - negative too steep (-80°) should reduce to -75°")
    void testNormalizeAngleNegativeTooSteep() {
        double angle = BallPhysics.normalizeAngle(-80);
        assertEquals(-75.0, angle, DELTA);
    }

    @Test
    @DisplayName("Normalize angle - negative too flat (-5°) should increase to -15°")
    void testNormalizeAngleNegativeTooFlat() {
        double angle = BallPhysics.normalizeAngle(-5);
        assertEquals(-15.0, angle, DELTA);
    }

    @ParameterizedTest
    @CsvSource({
        "0, 15",      // 0° -> 15°
        "10, 15",     // 10° -> 15°
        "45, 45",     // 45° unchanged
        "75, 75",     // 75° unchanged
        "80, 75",     // 80° -> 75°
        "90, 75",     // 90° -> 75°
        "-5, -15",    // -5° -> -15°
        "-45, -45",   // -45° unchanged
        "-80, -75"    // -80° -> -75°
    })
    @DisplayName("Normalize angle - multiple test cases")
    void testNormalizeAngleMultipleCases(double input, double expected) {
        double result = BallPhysics.normalizeAngle(input);
        assertEquals(expected, result, DELTA);
    }

    @Test
    @DisplayName("Calculate velocity from angle 0° and speed 5")
    void testCalculateVelocityHorizontal() {
        Point2D velocity = BallPhysics.calculateVelocity(0, 5);
        assertEquals(5.0, velocity.getX(), DELTA);
        assertEquals(0.0, velocity.getY(), DELTA);
    }

    @Test
    @DisplayName("Calculate velocity from angle 90° and speed 5")
    void testCalculateVelocityVertical() {
        Point2D velocity = BallPhysics.calculateVelocity(90, 5);
        assertEquals(0.0, velocity.getX(), DELTA);
        assertEquals(5.0, velocity.getY(), DELTA);
    }

    @Test
    @DisplayName("Calculate velocity from angle 45° and speed 5")
    void testCalculateVelocity45Degrees() {
        Point2D velocity = BallPhysics.calculateVelocity(45, 5);
        assertEquals(3.535, velocity.getX(), DELTA);
        assertEquals(3.535, velocity.getY(), DELTA);
    }

    @Test
    @DisplayName("Normalize velocity - too fast speed should be reduced")
    void testNormalizeVelocityTooFast() {
        Point2D velocity = new Point2D(10, 10); // Speed ~14.14
        Point2D normalized = BallPhysics.normalizeVelocity(velocity, 3.0);

        // Speed after normalization should not exceed 3.0 * 1.2 = 3.6
        double speed = normalized.magnitude();
        assertTrue(speed <= 3.6);
    }

    @Test
    @DisplayName("Normalize velocity - angle adjusted if invalid")
    void testNormalizeVelocityInvalidAngle() {
        Point2D velocity = new Point2D(10, 0.5); // Angle ~2.86° (too flat)
        Point2D normalized = BallPhysics.normalizeVelocity(velocity, 5.0);

        // New angle must be >= 15° (with tolerance for rounding errors)
        double angle = BallPhysics.calculateAngle(normalized.getX(), normalized.getY());
        assertTrue(Math.abs(angle) >= 14.9, "Angle was: " + angle);
    }

    @Test
    @DisplayName("Calculate paddle bounce - center hit")
    void testPaddleBounceCenterHit() {
        Point2D velocity = BallPhysics.calculatePaddleBounce(400, 400, 80, 3.0);

        // Center hit -> velocity X = 0, Y upward
        assertEquals(0.0, velocity.getX(), DELTA);
        assertTrue(velocity.getY() < 0); // Upward
        assertEquals(-3.0, velocity.getY(), DELTA);
    }

    @Test
    @DisplayName("Calculate paddle bounce - left side hit")
    void testPaddleBounceLeftSide() {
        Point2D velocity = BallPhysics.calculatePaddleBounce(370, 400, 80, 3.0);

        // Left side hit -> velocity X negative (bounce left)
        assertTrue(velocity.getX() < 0);
        assertTrue(velocity.getY() < 0); // Upward
    }

    @Test
    @DisplayName("Calculate paddle bounce - right side hit")
    void testPaddleBounceRightSide() {
        Point2D velocity = BallPhysics.calculatePaddleBounce(430, 400, 80, 3.0);

        // Right side hit -> velocity X positive (bounce right)
        assertTrue(velocity.getX() > 0);
        assertTrue(velocity.getY() < 0); // Upward
    }

    @Test
    @DisplayName("Calculate paddle bounce - ensure minimum Y velocity -2.0")
    void testPaddleBounceMinimumYVelocity() {
        // Extreme edge hit
        Point2D velocity = BallPhysics.calculatePaddleBounce(440, 400, 80, 3.0);

        // Y velocity must be <= -2.0 to prevent slow bounces
        assertTrue(velocity.getY() <= -2.0);
    }

    @Test
    @DisplayName("Check valid angle - 45° is valid")
    void testIsAngleValid45Degrees() {
        assertTrue(BallPhysics.isAngleValid(45));
    }

    @Test
    @DisplayName("Check valid angle - 5° is invalid")
    void testIsAngleInvalid5Degrees() {
        assertFalse(BallPhysics.isAngleValid(5));
    }

    @Test
    @DisplayName("Check valid angle - 85° is invalid")
    void testIsAngleInvalid85Degrees() {
        assertFalse(BallPhysics.isAngleValid(85));
    }

    @Test
    @DisplayName("Get maximum vertical angle limit")
    void testGetMaxVerticalAngle() {
        assertEquals(75.0, BallPhysics.getMaxVerticalAngle(), DELTA);
    }

    @Test
    @DisplayName("Get minimum vertical angle limit")
    void testGetMinVerticalAngle() {
        assertEquals(15.0, BallPhysics.getMinVerticalAngle(), DELTA);
    }

    @Test
    @DisplayName("Velocity magnitude preserved after angle normalization")
    void testVelocityMagnitudePreserved() {
        double speed = 5.0;
        Point2D velocity = BallPhysics.calculateVelocity(30, speed);

        assertEquals(speed, velocity.magnitude(), DELTA);
    }

    @Test
    @DisplayName("Handle 180° angle (backward horizontal)")
    void testNormalizeAngle180Degrees() {
        double angle = BallPhysics.normalizeAngle(180);
        // 180° should be adjusted to 165° (180 - 15)
        assertEquals(165.0, angle, DELTA);
    }

    @Test
    @DisplayName("Handle -180° angle (backward horizontal)")
    void testNormalizeAngleNegative180Degrees() {
        double angle = BallPhysics.normalizeAngle(-180);
        assertEquals(-165.0, angle, DELTA);
    }
}
