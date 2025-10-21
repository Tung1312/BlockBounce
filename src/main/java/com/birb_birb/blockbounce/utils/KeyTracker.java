package com.birb_birb.blockbounce.utils;

import javafx.scene.input.KeyCode;
import java.util.HashSet;
import java.util.Set;

/**
 * Utility class for tracking pressed keys.
 * Provides methods to manage key states and check movement directions.
 */
public class KeyTracker {

    private final Set<KeyCode> pressedKeys;

    public KeyTracker() {
        this.pressedKeys = new HashSet<>();
    }

    /**
     * Register a key as pressed
     * @param keyCode the key code to register
     */
    public void pressKey(KeyCode keyCode) {
        if (keyCode != null) {
            pressedKeys.add(keyCode);
        }
    }

    /**
     * Register a key as released
     * @param keyCode the key code to release
     */
    public void releaseKey(KeyCode keyCode) {
        if (keyCode != null) {
            pressedKeys.remove(keyCode);
        }
    }

    /**
     * Check if a specific key is currently pressed
     * @param keyCode the key code to check
     * @return true if the key is pressed
     */
    public boolean isKeyPressed(KeyCode keyCode) {
        return keyCode != null && pressedKeys.contains(keyCode);
    }

    /**
     * Check if any of the provided keys are pressed
     * @param keyCodes variable number of key codes to check
     * @return true if at least one key is pressed
     */
    public boolean isAnyKeyPressed(KeyCode... keyCodes) {
        if (keyCodes == null || keyCodes.length == 0) {
            return false;
        }

        for (KeyCode keyCode : keyCodes) {
            if (isKeyPressed(keyCode)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Check if all provided keys are pressed simultaneously
     * @param keyCodes variable number of key codes to check
     * @return true if all keys are pressed
     */
    public boolean areAllKeysPressed(KeyCode... keyCodes) {
        if (keyCodes == null || keyCodes.length == 0) {
            return false;
        }

        for (KeyCode keyCode : keyCodes) {
            if (!isKeyPressed(keyCode)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Check if left movement keys are pressed (LEFT or A)
     * @return true if left movement is requested
     */
    public boolean shouldMoveLeft() {
        return isAnyKeyPressed(KeyCode.LEFT, KeyCode.A);
    }

    /**
     * Check if right movement keys are pressed (RIGHT or D)
     * @return true if right movement is requested
     */
    public boolean shouldMoveRight() {
        return isAnyKeyPressed(KeyCode.RIGHT, KeyCode.D);
    }

    /**
     * Check if up movement keys are pressed (UP or W)
     * @return true if up movement is requested
     */
    public boolean shouldMoveUp() {
        return isAnyKeyPressed(KeyCode.UP, KeyCode.W);
    }

    /**
     * Check if down movement keys are pressed (DOWN or S)
     * @return true if down movement is requested
     */
    public boolean shouldMoveDown() {
        return isAnyKeyPressed(KeyCode.DOWN, KeyCode.S);
    }

    /**
     * Get the number of currently pressed keys
     * @return the count of pressed keys
     */
    public int getPressedKeyCount() {
        return pressedKeys.size();
    }

    /**
     * Clear all pressed keys
     */
    public void clearAll() {
        pressedKeys.clear();
    }

    /**
     * Get a copy of all currently pressed keys
     * @return a new set containing all pressed keys
     */
    public Set<KeyCode> getPressedKeys() {
        return new HashSet<>(pressedKeys);
    }

    /**
     * Check if arrow keys should be consumed (to prevent FXGL interception)
     * @param keyCode the key code to check
     * @return true if the key is an arrow key
     */
    public static boolean isArrowKey(KeyCode keyCode) {
        return keyCode == KeyCode.LEFT ||
               keyCode == KeyCode.RIGHT ||
               keyCode == KeyCode.UP ||
               keyCode == KeyCode.DOWN;
    }

    /**
     * Check if the key is a WASD movement key
     * @param keyCode the key code to check
     * @return true if the key is W, A, S, or D
     */
    public static boolean isWASDKey(KeyCode keyCode) {
        return keyCode == KeyCode.W ||
               keyCode == KeyCode.A ||
               keyCode == KeyCode.S ||
               keyCode == KeyCode.D;
    }

    /**
     * Check if the key is any movement key (arrow or WASD)
     * @param keyCode the key code to check
     * @return true if the key is a movement key
     */
    public static boolean isMovementKey(KeyCode keyCode) {
        return isArrowKey(keyCode) || isWASDKey(keyCode);
    }
}