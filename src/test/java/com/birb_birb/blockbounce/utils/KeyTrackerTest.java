package com.birb_birb.blockbounce.utils;

import javafx.scene.input.KeyCode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for KeyTracker class
 */
@DisplayName("Key Tracker Tests")
class KeyTrackerTest {

    private KeyTracker keyTracker;

    @BeforeEach
    void setUp() {
        keyTracker = new KeyTracker();
    }

    // ========== Basic Key Press/Release Tests ==========

    @Test
    @DisplayName("Press single key - key should be registered")
    void testPressSingleKey() {
        keyTracker.pressKey(KeyCode.A);
        assertTrue(keyTracker.isKeyPressed(KeyCode.A));
    }

    @Test
    @DisplayName("Release key - key should not be registered")
    void testReleaseKey() {
        keyTracker.pressKey(KeyCode.A);
        keyTracker.releaseKey(KeyCode.A);
        assertFalse(keyTracker.isKeyPressed(KeyCode.A));
    }

    @Test
    @DisplayName("Press multiple keys - all should be registered")
    void testPressMultipleKeys() {
        keyTracker.pressKey(KeyCode.A);
        keyTracker.pressKey(KeyCode.D);
        keyTracker.pressKey(KeyCode.W);

        assertTrue(keyTracker.isKeyPressed(KeyCode.A));
        assertTrue(keyTracker.isKeyPressed(KeyCode.D));
        assertTrue(keyTracker.isKeyPressed(KeyCode.W));
    }

    @Test
    @DisplayName("Press same key twice - should remain pressed once")
    void testPressSameKeyTwice() {
        keyTracker.pressKey(KeyCode.A);
        keyTracker.pressKey(KeyCode.A);

        assertEquals(1, keyTracker.getPressedKeyCount());
        assertTrue(keyTracker.isKeyPressed(KeyCode.A));
    }

    @Test
    @DisplayName("Press null key - should not cause error")
    void testPressNullKey() {
        assertDoesNotThrow(() -> keyTracker.pressKey(null));
        assertEquals(0, keyTracker.getPressedKeyCount());
    }

    @Test
    @DisplayName("Release null key - should not cause error")
    void testReleaseNullKey() {
        assertDoesNotThrow(() -> keyTracker.releaseKey(null));
    }

    @Test
    @DisplayName("Release unpressed key - should not cause error")
    void testReleaseUnpressedKey() {
        assertDoesNotThrow(() -> keyTracker.releaseKey(KeyCode.A));
        assertFalse(keyTracker.isKeyPressed(KeyCode.A));
    }

    // ========== Key Count Tests ==========

    @Test
    @DisplayName("Get pressed key count - should return correct count")
    void testGetPressedKeyCount() {
        assertEquals(0, keyTracker.getPressedKeyCount());

        keyTracker.pressKey(KeyCode.A);
        assertEquals(1, keyTracker.getPressedKeyCount());

        keyTracker.pressKey(KeyCode.D);
        assertEquals(2, keyTracker.getPressedKeyCount());

        keyTracker.releaseKey(KeyCode.A);
        assertEquals(1, keyTracker.getPressedKeyCount());
    }

    @Test
    @DisplayName("Clear all keys - count should be zero")
    void testClearAll() {
        keyTracker.pressKey(KeyCode.A);
        keyTracker.pressKey(KeyCode.D);
        keyTracker.pressKey(KeyCode.W);

        assertEquals(3, keyTracker.getPressedKeyCount());

        keyTracker.clearAll();
        assertEquals(0, keyTracker.getPressedKeyCount());
        assertFalse(keyTracker.isKeyPressed(KeyCode.A));
    }

    // ========== Any Key Pressed Tests ==========

    @Test
    @DisplayName("Check any key pressed - should return true if any key matches")
    void testIsAnyKeyPressed() {
        keyTracker.pressKey(KeyCode.A);

        assertTrue(keyTracker.isAnyKeyPressed(KeyCode.A, KeyCode.LEFT));
        assertTrue(keyTracker.isAnyKeyPressed(KeyCode.LEFT, KeyCode.A));
        assertFalse(keyTracker.isAnyKeyPressed(KeyCode.D, KeyCode.RIGHT));
    }

    @Test
    @DisplayName("Check any key pressed with null array - should return false")
    void testIsAnyKeyPressedWithNull() {
        assertFalse(keyTracker.isAnyKeyPressed((KeyCode[]) null));
    }

    @Test
    @DisplayName("Check any key pressed with empty array - should return false")
    void testIsAnyKeyPressedWithEmpty() {
        assertFalse(keyTracker.isAnyKeyPressed());
    }

    // ========== All Keys Pressed Tests ==========

    @Test
    @DisplayName("Check all keys pressed - should return true only if all match")
    void testAreAllKeysPressed() {
        keyTracker.pressKey(KeyCode.A);
        keyTracker.pressKey(KeyCode.D);

        assertTrue(keyTracker.areAllKeysPressed(KeyCode.A, KeyCode.D));
        assertFalse(keyTracker.areAllKeysPressed(KeyCode.A, KeyCode.D, KeyCode.W));
        assertFalse(keyTracker.areAllKeysPressed(KeyCode.W));
    }

    @Test
    @DisplayName("Check all keys pressed with null array - should return false")
    void testAreAllKeysPressedWithNull() {
        assertFalse(keyTracker.areAllKeysPressed((KeyCode[]) null));
    }

    @Test
    @DisplayName("Check all keys pressed with empty array - should return false")
    void testAreAllKeysPressedWithEmpty() {
        assertFalse(keyTracker.areAllKeysPressed());
    }

    // ========== Movement Direction Tests ==========

    @Test
    @DisplayName("Should move left - LEFT key")
    void testShouldMoveLeftWithArrow() {
        keyTracker.pressKey(KeyCode.LEFT);
        assertTrue(keyTracker.shouldMoveLeft());
        assertFalse(keyTracker.shouldMoveRight());
    }

    @Test
    @DisplayName("Should move left - A key")
    void testShouldMoveLeftWithA() {
        keyTracker.pressKey(KeyCode.A);
        assertTrue(keyTracker.shouldMoveLeft());
    }

    @Test
    @DisplayName("Should move right - RIGHT key")
    void testShouldMoveRightWithArrow() {
        keyTracker.pressKey(KeyCode.RIGHT);
        assertTrue(keyTracker.shouldMoveRight());
        assertFalse(keyTracker.shouldMoveLeft());
    }

    @Test
    @DisplayName("Should move right - D key")
    void testShouldMoveRightWithD() {
        keyTracker.pressKey(KeyCode.D);
        assertTrue(keyTracker.shouldMoveRight());
    }

    @Test
    @DisplayName("Should move up - UP key")
    void testShouldMoveUpWithArrow() {
        keyTracker.pressKey(KeyCode.UP);
        assertTrue(keyTracker.shouldMoveUp());
    }

    @Test
    @DisplayName("Should move up - W key")
    void testShouldMoveUpWithW() {
        keyTracker.pressKey(KeyCode.W);
        assertTrue(keyTracker.shouldMoveUp());
    }

    @Test
    @DisplayName("Should move down - DOWN key")
    void testShouldMoveDownWithArrow() {
        keyTracker.pressKey(KeyCode.DOWN);
        assertTrue(keyTracker.shouldMoveDown());
    }

    @Test
    @DisplayName("Should move down - S key")
    void testShouldMoveDownWithS() {
        keyTracker.pressKey(KeyCode.S);
        assertTrue(keyTracker.shouldMoveDown());
    }

    @Test
    @DisplayName("No movement - no keys pressed")
    void testNoMovement() {
        assertFalse(keyTracker.shouldMoveLeft());
        assertFalse(keyTracker.shouldMoveRight());
        assertFalse(keyTracker.shouldMoveUp());
        assertFalse(keyTracker.shouldMoveDown());
    }

    @Test
    @DisplayName("Both left and right pressed - both should return true")
    void testBothLeftAndRightPressed() {
        keyTracker.pressKey(KeyCode.LEFT);
        keyTracker.pressKey(KeyCode.RIGHT);

        assertTrue(keyTracker.shouldMoveLeft());
        assertTrue(keyTracker.shouldMoveRight());
    }

    // ========== Get Pressed Keys Tests ==========

    @Test
    @DisplayName("Get pressed keys - should return copy of set")
    void testGetPressedKeys() {
        keyTracker.pressKey(KeyCode.A);
        keyTracker.pressKey(KeyCode.D);

        Set<KeyCode> pressedKeys = keyTracker.getPressedKeys();

        assertEquals(2, pressedKeys.size());
        assertTrue(pressedKeys.contains(KeyCode.A));
        assertTrue(pressedKeys.contains(KeyCode.D));

        // Modify returned set should not affect tracker
        pressedKeys.add(KeyCode.W);
        assertEquals(2, keyTracker.getPressedKeyCount());
    }

    @Test
    @DisplayName("Get pressed keys - empty tracker returns empty set")
    void testGetPressedKeysEmpty() {
        Set<KeyCode> pressedKeys = keyTracker.getPressedKeys();
        assertNotNull(pressedKeys);
        assertTrue(pressedKeys.isEmpty());
    }

    // ========== Static Helper Method Tests ==========

    @Test
    @DisplayName("Is arrow key - should identify arrow keys")
    void testIsArrowKey() {
        assertTrue(KeyTracker.isArrowKey(KeyCode.LEFT));
        assertTrue(KeyTracker.isArrowKey(KeyCode.RIGHT));
        assertTrue(KeyTracker.isArrowKey(KeyCode.UP));
        assertTrue(KeyTracker.isArrowKey(KeyCode.DOWN));

        assertFalse(KeyTracker.isArrowKey(KeyCode.A));
        assertFalse(KeyTracker.isArrowKey(KeyCode.SPACE));
        assertFalse(KeyTracker.isArrowKey(null));
    }

    @Test
    @DisplayName("Is WASD key - should identify WASD keys")
    void testIsWASDKey() {
        assertTrue(KeyTracker.isWASDKey(KeyCode.W));
        assertTrue(KeyTracker.isWASDKey(KeyCode.A));
        assertTrue(KeyTracker.isWASDKey(KeyCode.S));
        assertTrue(KeyTracker.isWASDKey(KeyCode.D));

        assertFalse(KeyTracker.isWASDKey(KeyCode.LEFT));
        assertFalse(KeyTracker.isWASDKey(KeyCode.SPACE));
        assertFalse(KeyTracker.isWASDKey(null));
    }

    @Test
    @DisplayName("Is movement key - should identify all movement keys")
    void testIsMovementKey() {
        // Arrow keys
        assertTrue(KeyTracker.isMovementKey(KeyCode.LEFT));
        assertTrue(KeyTracker.isMovementKey(KeyCode.RIGHT));
        assertTrue(KeyTracker.isMovementKey(KeyCode.UP));
        assertTrue(KeyTracker.isMovementKey(KeyCode.DOWN));

        // WASD keys
        assertTrue(KeyTracker.isMovementKey(KeyCode.W));
        assertTrue(KeyTracker.isMovementKey(KeyCode.A));
        assertTrue(KeyTracker.isMovementKey(KeyCode.S));
        assertTrue(KeyTracker.isMovementKey(KeyCode.D));

        // Non-movement keys
        assertFalse(KeyTracker.isMovementKey(KeyCode.SPACE));
        assertFalse(KeyTracker.isMovementKey(KeyCode.ENTER));
        assertFalse(KeyTracker.isMovementKey(null));
    }

    // ========== Complex Scenario Tests ==========

    @Test
    @DisplayName("Complex scenario - multiple press and release operations")
    void testComplexScenario() {
        // Press multiple keys
        keyTracker.pressKey(KeyCode.A);
        keyTracker.pressKey(KeyCode.W);
        keyTracker.pressKey(KeyCode.SPACE);
        assertEquals(3, keyTracker.getPressedKeyCount());

        // Check movement
        assertTrue(keyTracker.shouldMoveLeft());
        assertTrue(keyTracker.shouldMoveUp());

        // Release one key
        keyTracker.releaseKey(KeyCode.W);
        assertEquals(2, keyTracker.getPressedKeyCount());
        assertFalse(keyTracker.shouldMoveUp());

        // Press another key
        keyTracker.pressKey(KeyCode.RIGHT);
        assertTrue(keyTracker.shouldMoveRight());
        assertEquals(3, keyTracker.getPressedKeyCount());

        // Clear all
        keyTracker.clearAll();
        assertEquals(0, keyTracker.getPressedKeyCount());
        assertFalse(keyTracker.shouldMoveLeft());
        assertFalse(keyTracker.shouldMoveRight());
    }

    @Test
    @DisplayName("Simultaneous arrow and WASD - both should work")
    void testSimultaneousArrowAndWASD() {
        keyTracker.pressKey(KeyCode.LEFT);
        keyTracker.pressKey(KeyCode.A);

        assertTrue(keyTracker.shouldMoveLeft());
        assertEquals(2, keyTracker.getPressedKeyCount());

        // Release one, should still move left
        keyTracker.releaseKey(KeyCode.LEFT);
        assertTrue(keyTracker.shouldMoveLeft());
    }
}

