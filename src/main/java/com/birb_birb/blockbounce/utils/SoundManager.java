package com.birb_birb.blockbounce.utils;

import com.almasb.fxgl.audio.Sound;
import com.birb_birb.blockbounce.constants.GameConstants;
import static com.almasb.fxgl.dsl.FXGL.getAssetLoader;
import static com.almasb.fxgl.dsl.FXGL.getAudioPlayer;

public class SoundManager {
    private static Sound hoverSound;
    private static Sound clickSound;
    private static Sound bounceSound;
    private static Sound paddleHitSound;
    private static Sound brickBreakSound;

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

            // Sử dụng lại các âm thanh có sẵn cho gameplay
            // Nếu không có file riêng, dùng click sound cho các hiệu ứng
            bounceSound = clickSound;
            paddleHitSound = clickSound;
            brickBreakSound = clickSound;

            initialized = true;
        } catch (Exception e) {
            System.err.println("Failed to load sounds: " + e.getMessage());
        }
    }

    public static void playHoverSound() {
        try {
            getAudioPlayer().playSound(hoverSound);
        } catch (Exception e) {
            System.err.println("Failed to play hover sound: " + e.getMessage());
        }
    }

    public static void playClickSound() {
        try {
            getAudioPlayer().playSound(clickSound);
        } catch (Exception e) {
            System.err.println("Failed to play click sound: " + e.getMessage());
        }
    }

    public static void playBounce() {
        try {
            if (bounceSound != null) {
                getAudioPlayer().playSound(bounceSound);
            }
        } catch (Exception e) {
            // Không in lỗi để tránh spam console
        }
    }

    public static void playPaddleHit() {
        try {
            if (paddleHitSound != null) {
                getAudioPlayer().playSound(paddleHitSound);
            }
        } catch (Exception e) {
            // Không in lỗi để tránh spam console
        }
    }

    public static void playBrickBreak() {
        try {
            if (brickBreakSound != null) {
                getAudioPlayer().playSound(brickBreakSound);
            }
        } catch (Exception e) {
            // Không in lỗi để tránh spam console
        }
    }

    public static boolean isInitialized() {
        return initialized;
    }
}
