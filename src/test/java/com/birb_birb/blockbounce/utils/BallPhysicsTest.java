package com.birb_birb.blockbounce.utils;

import com.birb_birb.blockbounce.constants.GameConstants;
import com.birb_birb.blockbounce.physics.BallPhysics;
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

}
