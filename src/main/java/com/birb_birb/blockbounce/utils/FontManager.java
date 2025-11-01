package com.birb_birb.blockbounce.utils;

import com.birb_birb.blockbounce.constants.GameConstants;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public final class FontManager {

    private static Font mainFont;
    private static Font secondFont;

    private FontManager() {}

    // Main font = Daydream
    public static Font getMainFont() {
        if (mainFont == null) {
            loadMainFont();
        }
        return mainFont;
    }

    private static void loadMainFont() {
        try {
            Font loadedFont = Font.loadFont(
                FontManager.class.getResourceAsStream(GameConstants.MAIN_FONT_PATH),
                GameConstants.DEFAULT_FONT_SIZE
            );
            if (loadedFont != null) {
                mainFont = loadedFont;
            } else {
                System.out.println("Custom font failed to load, using fallback");
                mainFont = Font.font(
                        GameConstants.FALLBACK_FONT,
                        FontWeight.BOLD,
                        GameConstants.DEFAULT_FONT_SIZE);
            }
        } catch (Exception e) {
            System.out.println("Exception loading custom font: " + e.getMessage());
            mainFont = Font.font(
                    GameConstants.FALLBACK_FONT,
                    FontWeight.BOLD,
                    GameConstants.DEFAULT_FONT_SIZE);
        }
    }

    // Second font = MinecraftTen
    public static Font getSecondFont() {
        if (secondFont == null) {
            loadSecondFont();
        }
        return secondFont;
    }

    private static void loadSecondFont() {
        try {
            Font loadedFont = Font.loadFont(FontManager.class.getResourceAsStream(GameConstants.SECONDARY_FONT_PATH), 32);
            if (loadedFont != null) {
                secondFont = loadedFont;
            } else {
                System.out.println("Custom font failed to load, using fallback");
                secondFont = Font.font(
                        GameConstants.FALLBACK_FONT,
                        FontWeight.BOLD,
                        GameConstants.DEFAULT_FONT_SIZE);
            }
        } catch (Exception e) {
            System.out.println("Exception loading custom font: " + e.getMessage());
            secondFont = Font.font(
                    GameConstants.FALLBACK_FONT,
                    FontWeight.BOLD,
                    GameConstants.DEFAULT_FONT_SIZE);
        }
    }
}
