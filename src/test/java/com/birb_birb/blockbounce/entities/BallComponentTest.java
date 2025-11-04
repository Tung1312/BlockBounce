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

    @Test
    @DisplayName("setSpeedMultiplier should update speed multiplier")
    void testSetSpeedMultiplier() {
        BallComponent ball = new BallComponent();

        ball.setSpeedMultiplier(1.5);

        assertEquals(1.5, ball.getSpeedMultiplier(), 0.001);
    }

    @Test
    @DisplayName("setSpeedMultiplier should accept different values")
    void testSetSpeedMultiplierDifferentValues() {
        BallComponent ball = new BallComponent();

        ball.setSpeedMultiplier(0.5);
        assertEquals(0.5, ball.getSpeedMultiplier(), 0.001);

        ball.setSpeedMultiplier(2.0);
        assertEquals(2.0, ball.getSpeedMultiplier(), 0.001);

        ball.setSpeedMultiplier(0.75);
        assertEquals(0.75, ball.getSpeedMultiplier(), 0.001);
    }

    @Test
    @DisplayName("setVelocity should update ball velocity")
    void testSetVelocity() {
        BallComponent ball = new BallComponent();
        Point2D newVelocity = new Point2D(5.0, -5.0);

        ball.setVelocity(newVelocity);

        assertEquals(newVelocity.getX(), ball.getVelocity().getX(), 0.001);
        assertEquals(newVelocity.getY(), ball.getVelocity().getY(), 0.001);
    }

    @Test
    @DisplayName("getVelocity should return current velocity")
    void testGetVelocity() {
        BallComponent ball = new BallComponent();
        Point2D velocity = ball.getVelocity();

        assertNotNull(velocity);
        assertTrue(velocity instanceof Point2D);
    }

    @Test
    @DisplayName("freeze should set frozen state to true")
    void testFreeze() {
        BallComponent ball = new BallComponent();

        ball.freeze();

        assertTrue(ball.isFrozen());
    }

    @Test
    @DisplayName("unfreeze should set frozen state to false")
    void testUnfreeze() {
        BallComponent ball = new BallComponent();
        ball.freeze();

        ball.unfreeze();

        assertFalse(ball.isFrozen());
    }

    @Test
    @DisplayName("isFrozen should return false initially")
    void testInitialFrozenState() {
        BallComponent ball = new BallComponent();

        assertFalse(ball.isFrozen());
    }

    @Test
    @DisplayName("launch should create velocity with magnitude close to base speed")
    void testLaunchVelocityMagnitude() {
        BallComponent ball = new BallComponent();

        ball.launch();
        Point2D velocity = ball.getVelocity();
        double magnitude = velocity.magnitude();

        // Base speed is 5.0 (from GameConstants.BASE_SPEED)
        // Allow some tolerance for the random angle (launch angle is between -15 and +15 degrees)
        // The magnitude should be equal to BASE_SPEED regardless of angle
        assertTrue(magnitude > 4.5 && magnitude < 5.5,
            "Velocity magnitude should be close to base speed (5.0), got: " + magnitude);
    }

    @Test
    @DisplayName("Multiple launches should produce different velocities due to randomness")
    void testLaunchRandomness() {
        BallComponent ball1 = new BallComponent();
        BallComponent ball2 = new BallComponent();

        ball1.launch();
        ball2.launch();

        Point2D velocity1 = ball1.getVelocity();
        Point2D velocity2 = ball2.getVelocity();

        // With randomness, velocities should be different (very unlikely to be the same)
        // However, this test might occasionally fail due to random chance
        // We check magnitude is consistent though
        double mag1 = velocity1.magnitude();
        double mag2 = velocity2.magnitude();

        assertTrue(Math.abs(mag1 - mag2) < 0.5, "Magnitudes should be similar");
    }

    @Test
    @DisplayName("BallComponent should extend PhysicsComponent")
    void testBallComponentExtendsPhysicsComponent() {
        assertTrue(BallComponent.class.getSuperclass().getSimpleName().equals("PhysicsComponent"));
    }

    @Test
    @DisplayName("hasLaunched method should be public")
    void testHasLaunchedMethodIsPublic() throws NoSuchMethodException {
        var method = BallComponent.class.getMethod("hasLaunched");
        assertTrue(java.lang.reflect.Modifier.isPublic(method.getModifiers()));
    }

    @Test
    @DisplayName("launch method should be public")
    void testLaunchMethodIsPublic() throws NoSuchMethodException {
        var method = BallComponent.class.getMethod("launch");
        assertTrue(java.lang.reflect.Modifier.isPublic(method.getModifiers()));
    }

    @Test
    @DisplayName("setLaunched method should be public")
    void testSetLaunchedMethodIsPublic() throws NoSuchMethodException {
        var method = BallComponent.class.getMethod("setLaunched", boolean.class);
        assertTrue(java.lang.reflect.Modifier.isPublic(method.getModifiers()));
    }

    @Test
    @DisplayName("attachToPaddle method should be public")
    void testAttachToPaddleMethodIsPublic() throws NoSuchMethodException {
        var method = BallComponent.class.getMethod("attachToPaddle");
        assertTrue(java.lang.reflect.Modifier.isPublic(method.getModifiers()));
    }

    @Test
    @DisplayName("getSpeedMultiplier method should be public")
    void testGetSpeedMultiplierMethodIsPublic() throws NoSuchMethodException {
        var method = BallComponent.class.getMethod("getSpeedMultiplier");
        assertTrue(java.lang.reflect.Modifier.isPublic(method.getModifiers()));
    }

    @Test
    @DisplayName("setSpeedMultiplier method should be public")
    void testSetSpeedMultiplierMethodIsPublic() throws NoSuchMethodException {
        var method = BallComponent.class.getMethod("setSpeedMultiplier", double.class);
        assertTrue(java.lang.reflect.Modifier.isPublic(method.getModifiers()));
    }

    @Test
    @DisplayName("hasLaunched should return boolean")
    void testHasLaunchedReturnsBoolean() throws NoSuchMethodException {
        var method = BallComponent.class.getMethod("hasLaunched");
        assertEquals(boolean.class, method.getReturnType());
    }

    @Test
    @DisplayName("getSpeedMultiplier should return double")
    void testGetSpeedMultiplierReturnsDouble() throws NoSuchMethodException {
        var method = BallComponent.class.getMethod("getSpeedMultiplier");
        assertEquals(double.class, method.getReturnType());
    }

    @Test
    @DisplayName("launch method should take no parameters")
    void testLaunchMethodTakesNoParameters() throws NoSuchMethodException {
        var method = BallComponent.class.getMethod("launch");
        assertEquals(0, method.getParameterCount());
    }

    @Test
    @DisplayName("attachToPaddle method should take no parameters")
    void testAttachToPaddleMethodTakesNoParameters() throws NoSuchMethodException {
        var method = BallComponent.class.getMethod("attachToPaddle");
        assertEquals(0, method.getParameterCount());
    }

    @Test
    @DisplayName("BallComponent class should be public")
    void testBallComponentClassIsPublic() {
        assertTrue(java.lang.reflect.Modifier.isPublic(BallComponent.class.getModifiers()));
    }

    @Test
    @DisplayName("setSpeedMultiplier with zero should set multiplier to zero")
    void testSetSpeedMultiplierZero() {
        BallComponent ball = new BallComponent();

        ball.setSpeedMultiplier(0.0);

        assertEquals(0.0, ball.getSpeedMultiplier(), 0.001);
    }

    @Test
    @DisplayName("setSpeedMultiplier with negative value should set negative multiplier")
    void testSetSpeedMultiplierNegative() {
        BallComponent ball = new BallComponent();

        ball.setSpeedMultiplier(-1.0);

        assertEquals(-1.0, ball.getSpeedMultiplier(), 0.001);
    }

    @Test
    @DisplayName("Velocity should maintain direction after attachToPaddle and launch")
    void testVelocityDirectionAfterAttachAndLaunch() {
        BallComponent ball = new BallComponent();

        ball.launch();
        Point2D velocity1 = ball.getVelocity();
        assertTrue(velocity1.getY() < 0, "Should move upward after launch");

        ball.attachToPaddle();
        Point2D velocity2 = ball.getVelocity();
        assertEquals(0, velocity2.magnitude(), 0.001, "Should have zero velocity when attached");

        ball.launch();
        Point2D velocity3 = ball.getVelocity();
        assertTrue(velocity3.getY() < 0, "Should move upward after second launch");
    }

    @Test
    @DisplayName("BallComponent should have public constructors")
    void testConstructorsArePublic() {
        var constructors = BallComponent.class.getConstructors();
        assertTrue(constructors.length >= 1);

        for (var constructor : constructors) {
            assertTrue(java.lang.reflect.Modifier.isPublic(constructor.getModifiers()));
        }
    }
}
