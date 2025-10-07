package com.birb_birb.blockbounce.utils;

import com.almasb.fxgl.audio.Sound;
import com.birb_birb.blockbounce.constants.GameConstants;
import static com.almasb.fxgl.dsl.FXGL.getAssetLoader;
import static com.almasb.fxgl.dsl.FXGL.getAudioPlayer;

public class SoundManager {
    private static Sound hoverSound;
    private static Sound clickSound;

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
            initialized = true;
        } catch (Exception e) {
            System.err.println("Failed to load sounds: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static void playHoverSound() {
        try {
            getAudioPlayer().playSound(hoverSound);
        } catch (Exception e) {
            System.err.println("Failed to play hover sound: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static void playClickSound() {
        try {
            getAudioPlayer().playSound(clickSound);
        } catch (Exception e) {
            System.err.println("Failed to play click sound: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static boolean isInitialized() {
        return initialized;
    }
}
