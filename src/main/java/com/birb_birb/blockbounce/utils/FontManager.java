package com.birb_birb.blockbounce.utils;

import com.birb_birb.blockbounce.constants.GameConstants;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public final class FontManager {

    private static Font customFont;

    private FontManager() {}

    public static Font getCustomFont() {
        if (customFont == null) {
            loadCustomFont();
        }
        return customFont;
    }

    /**
     * Load the custom font with fallback.
     */
    private static void loadCustomFont() {
        try {
            Font loadedFont = Font.loadFont(
                FontManager.class.getResourceAsStream(GameConstants.FONT_PATH),
                GameConstants.DEFAULT_FONT_SIZE
            );
            if (loadedFont != null) {
                customFont = loadedFont;
            } else {
                System.out.println("Custom font failed to load, using fallback");
                customFont = Font.font(GameConstants.FALLBACK_FONT, FontWeight.BOLD, GameConstants.DEFAULT_FONT_SIZE);
            }
        } catch (Exception e) {
            System.out.println("Exception loading custom font: " + e.getMessage());
            customFont = Font.font(GameConstants.FALLBACK_FONT, FontWeight.BOLD, GameConstants.DEFAULT_FONT_SIZE);
        }
    }
}
