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

    /**Initialize sound manager and load sound assets*/
    public static void initialize() {
        if (initialized) {
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
        if (hoverSound != null) getAudioPlayer().playSound(hoverSound);
    }

    public static void playClickSound() {
        if (clickSound != null) getAudioPlayer().playSound(clickSound);
    }

    public static void playHitSound() {
        if (ballHitSound != null) getAudioPlayer().playSound(ballHitSound);
    }

    public static void playBreakSound() {
        if (breakSound != null) getAudioPlayer().playSound(breakSound);
    }

    public static void playDeathSound() {
        if (deathSound != null) getAudioPlayer().playSound(deathSound);
    }

    public static void playLooseSound() {
        if (looseSound != null) getAudioPlayer().playSound(looseSound);
    }

    public static void playCompleteSound() {
        if (completeSound != null) getAudioPlayer().playSound(completeSound);
    }

    public static void playOrbSound() {
        if (orbSound != null) getAudioPlayer().playSound(orbSound);
    }

    public static void playAnvilSound() {
        if (anvilSound != null) getAudioPlayer().playSound(anvilSound);
    }

    public static boolean isInitialized() {
        return initialized;
    }
}
