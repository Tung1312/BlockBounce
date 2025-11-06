package com.birb_birb.blockbounce.input;

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


    public void pressKey(KeyCode keyCode) {
        if (keyCode != null) {
            pressedKeys.add(keyCode);
        }
    }


    public void releaseKey(KeyCode keyCode) {
        if (keyCode != null) {
            pressedKeys.remove(keyCode);
        }
    }

    /**
     * Check if a specific key is currently pressed
     */
    public boolean isKeyPressed(KeyCode keyCode) {
        return keyCode != null && pressedKeys.contains(keyCode);
    }


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


    public boolean shouldMoveLeft() {
        return isAnyKeyPressed(KeyCode.LEFT, KeyCode.A);
    }


    public boolean shouldMoveRight() {
        return isAnyKeyPressed(KeyCode.RIGHT, KeyCode.D);
    }


    public boolean shouldMoveUp() {
        return isAnyKeyPressed(KeyCode.UP, KeyCode.W);
    }


    public boolean shouldMoveDown() {
        return isAnyKeyPressed(KeyCode.DOWN, KeyCode.S);
    }


    public int getPressedKeyCount() {
        return pressedKeys.size();
    }


    public void clearAll() {
        pressedKeys.clear();
    }


    public Set<KeyCode> getPressedKeys() {
        return new HashSet<>(pressedKeys);
    }


    public static boolean isArrowKey(KeyCode keyCode) {
        return keyCode == KeyCode.LEFT ||
               keyCode == KeyCode.RIGHT ||
               keyCode == KeyCode.UP ||
               keyCode == KeyCode.DOWN;
    }


    public static boolean isWASDKey(KeyCode keyCode) {
        return keyCode == KeyCode.W ||
               keyCode == KeyCode.A ||
               keyCode == KeyCode.S ||
               keyCode == KeyCode.D;
    }


    public static boolean isMovementKey(KeyCode keyCode) {
        return isArrowKey(keyCode) || isWASDKey(keyCode);
    }
}