package com.birb_birb.blockbounce.utils;

import com.almasb.fxgl.audio.Sound;
import com.birb_birb.blockbounce.constants.GameConstants;
import static com.almasb.fxgl.dsl.FXGL.getAssetLoader;
import static com.almasb.fxgl.dsl.FXGL.getAudioPlayer;

public class SoundManager {
    private static Sound hoverSound;
    private static Sound clickSound;
    private static Sound ballHitSound;
    private static Sound breakSound;
    private static Sound deathSound;
    private static Sound looseSound;
    private static Sound completeSound;
    private static Sound orbSound;
    private static Sound anvilSound;

    private static boolean initialized = false;

    /**
     * Initialize the sound manager and load sound assets
     */
    public static void initialize() {
        if (initialized) {
            System.out.println("SoundManager already initialized");
            return;
        }

        try {
            hoverSound = getAssetLoader().loadSound(GameConstants.SOUND_HOVER);
            clickSound = getAssetLoader().loadSound(GameConstants.SOUND_CLICK);
            ballHitSound = getAssetLoader().loadSound(GameConstants.SOUND_HIT);
            breakSound = getAssetLoader().loadSound(GameConstants.SOUND_BREAK);
            deathSound = getAssetLoader().loadSound(GameConstants.SOUND_DEATH);
            looseSound = getAssetLoader().loadSound(GameConstants.SOUND_LOOSE);
            completeSound = getAssetLoader().loadSound(GameConstants.SOUND_COMPLETE);
            orbSound = getAssetLoader().loadSound(GameConstants.SOUND_ORB);
            anvilSound = getAssetLoader().loadSound(GameConstants.SOUND_ANVIL);

            initialized = true;
        } catch (Exception e) {
            System.err.println("Failed to load sounds: " + e.getMessage());
        }
    }

    public static void playHoverSound() {
        try {
            getAudioPlayer().playSound(hoverSound);
        } catch (Exception e) {
            System.err.println("Failed to play sound: " + e.getMessage());
        }
    }

    public static void playClickSound() {
        try {
            getAudioPlayer().playSound(clickSound);
        } catch (Exception e) {
            System.err.println("Failed to play sound: " + e.getMessage());
        }
    }

    public static void playHitSound() {
        try {
            getAudioPlayer().playSound(ballHitSound);
        } catch (Exception e) {
            System.err.println("Failed to play sound: " + e.getMessage());
        }
    }

    public static void playBreakSound() {
        try {
            getAudioPlayer().playSound(breakSound);
        } catch (Exception e) {
            System.err.println("Failed to play sound: " + e.getMessage());
        }
    }

    public static void playDeathSound() {
        try {
            getAudioPlayer().playSound(deathSound);
        } catch (Exception e) {
            System.err.println("Failed to play sound: " + e.getMessage());
        }
    }

    public static void playLooseSound() {
        try {
            getAudioPlayer().playSound(looseSound);
        } catch (Exception e) {
            System.err.println("Failed to play sound: " + e.getMessage());
        }
    }

    public static void playCompleteSound() {
        try {
            getAudioPlayer().playSound(completeSound);
        } catch (Exception e) {
            System.err.println("Failed to play sound: " + e.getMessage());
        }
    }

    public static void playOrbSound() {
        try {
            getAudioPlayer().playSound(orbSound);
        } catch (Exception e) {
            System.err.println("Failed to play sound: " + e.getMessage());
        }
    }

    public static void playAnvilSound() {
        try {
            getAudioPlayer().playSound(anvilSound);
        } catch (Exception e) {
            System.err.println("Failed to play sound: " + e.getMessage());
        }
    }

    public static boolean isInitialized() {
        return initialized;
    }
}
