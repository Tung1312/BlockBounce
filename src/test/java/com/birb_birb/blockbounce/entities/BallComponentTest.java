package com.birb_birb.blockbounce.entities;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.BeforeEach;
import javafx.geometry.Point2D;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for BallComponent class
 * Tests ball behavior, velocity management, and state tracking
 */
@DisplayName("Ball Component Tests")
class BallComponentTest {

    @Test
    @DisplayName("Default constructor should create ball with initial velocity")
    void testDefaultConstructor() {
        BallComponent ball = new BallComponent();

        assertNotNull(ball);
        assertNotNull(ball.getVelocity());
    }

    @Test
    @DisplayName("Constructor with playfield should create ball with initial velocity")
    void testConstructorWithPlayfield() {
        BallComponent ball = new BallComponent(null);

        assertNotNull(ball);
        assertNotNull(ball.getVelocity());
    }

    @Test
    @DisplayName("Initial velocity should be Point2D(2.5, -2.5)")
    void testInitialVelocity() {
        BallComponent ball = new BallComponent();
        Point2D velocity = ball.getVelocity();

        assertEquals(2.5, velocity.getX(), 0.001);
        assertEquals(-2.5, velocity.getY(), 0.001);
    }

    @Test
    @DisplayName("hasLaunched should return false initially")
    void testInitialLaunchedState() {
        BallComponent ball = new BallComponent();

        assertFalse(ball.hasLaunched());
    }

    @Test
    @DisplayName("launch should set hasLaunched to true")
    void testLaunchSetsLaunchedState() {
        BallComponent ball = new BallComponent();

        ball.launch();

        assertTrue(ball.hasLaunched());
    }

    @Test
    @DisplayName("launch should set velocity with upward direction")
    void testLaunchSetsUpwardVelocity() {
        BallComponent ball = new BallComponent();

        ball.launch();
        Point2D velocity = ball.getVelocity();

        // Y velocity should be negative (upward)
        assertTrue(velocity.getY() < 0, "Y velocity should be negative (upward)");
    }

    @Test
    @DisplayName("setLaunched(true) should set hasLaunched to true")
    void testSetLaunchedTrue() {
        BallComponent ball = new BallComponent();

        ball.setLaunched(true);

        assertTrue(ball.hasLaunched());
    }

    @Test
    @DisplayName("setLaunched(false) should set hasLaunched to false")
    void testSetLaunchedFalse() {
        BallComponent ball = new BallComponent();
        ball.launch();

        ball.setLaunched(false);

        assertFalse(ball.hasLaunched());
    }

    @Test
    @DisplayName("attachToPaddle should reset launched state")
    void testAttachToPaddleResetsLaunchedState() {
        BallComponent ball = new BallComponent();
        ball.launch();

        ball.attachToPaddle();

        assertFalse(ball.hasLaunched());
    }

    @Test
    @DisplayName("attachToPaddle should set velocity to zero")
    void testAttachToPaddleSetsVelocityToZero() {
        BallComponent ball = new BallComponent();
        ball.launch();

        ball.attachToPaddle();
        Point2D velocity = ball.getVelocity();

        assertEquals(0, velocity.getX(), 0.001);
        assertEquals(0, velocity.getY(), 0.001);
    }

    @Test
    @DisplayName("getSpeedMultiplier should return 1.0 initially")
    void testInitialSpeedMultiplier() {
        BallComponent ball = new BallComponent();

        assertEquals(1.0, ball.getSpeedMultiplier(), 0.001);
    }

}
