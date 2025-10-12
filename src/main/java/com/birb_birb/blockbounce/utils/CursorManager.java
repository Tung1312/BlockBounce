package com.birb_birb.blockbounce.utils;

import com.birb_birb.blockbounce.constants.GameConstants;
import javafx.application.Platform;
import javafx.scene.Cursor;
import javafx.scene.ImageCursor;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;

/**
 * Manages the game's custom mouse cursor.
 *
 * Creates a small crosshair-style cursor programmatically without relying on FX-thread-only APIs.
 */
public final class CursorManager {

    private static Cursor CUSTOM_CURSOR;

    private CursorManager() {
    }

    /**
     * Falls back to programmatic crosshair if PNG loading fails.
     */
    public static synchronized Cursor getCustomCursor() {
        if (CUSTOM_CURSOR == null) {
            CUSTOM_CURSOR = loadPngCursor();
            if (CUSTOM_CURSOR == null) {
                CUSTOM_CURSOR = buildCrosshairCursor();
            }
        }
        return CUSTOM_CURSOR;
    }

    public static void apply(Node node) {
        if (node == null) return;
        Runnable r = () -> node.setCursor(getCustomCursor());
        if (Platform.isFxApplicationThread()) {
            r.run();
        } else {
            Platform.runLater(r);
        }
    }

    /**
     * A simple crosshair cursor image and wrap it as an ImageCursor.
     */
    private static Cursor buildCrosshairCursor() {
        int size = 24;
        int cx = size / 2;
        int cy = size / 2;

        WritableImage img = new WritableImage(size, size);
        PixelWriter pw = img.getPixelWriter();

        int transparent = 0x00000000;
        int white = 0xFFFFFFFF;
        int blackA = 0xBF000000;

        for (int y = 0; y < size; y++) {
            for (int x = 0; x < size; x++) {
                pw.setArgb(x, y, transparent);
            }
        }

        // Draw a 3px-thick vertical and horizontal line with white core and dark outline.
        for (int y = 1; y < size - 1; y++) {
            // vertical outline
            if (cx - 1 >= 0) pw.setArgb(cx - 1, y, blackA);
            if (cx + 1 < size) pw.setArgb(cx + 1, y, blackA);
            // vertical core
            pw.setArgb(cx, y, white);
        }

        for (int x = 1; x < size - 1; x++) {
            // horizontal outline
            if (cy - 1 >= 0) pw.setArgb(x, cy - 1, blackA);
            if (cy + 1 < size) pw.setArgb(x, cy + 1, blackA);
            // horizontal core
            pw.setArgb(x, cy, white);
        }

        // Center dot highlight
        pw.setArgb(cx, cy, white);

        // Hotspot at the center for precise targeting.
        return new ImageCursor(img, cx, cy);
    }

    /**
     * Loads the custom cursor from the PNG file.
     */
    private static Cursor loadPngCursor() {
        try {
            // Load the cursor image from resources using GameConstants
            Image cursorImage = new Image(CursorManager.class.getResourceAsStream(GameConstants.CURSOR_PATH));

            if (cursorImage.isError()) {
                System.err.println("Failed to load cursor image from: " + GameConstants.CURSOR_PATH);
                return null;
            }

            // Create ImageCursor with hotspot at top-left corner
            return new ImageCursor(cursorImage, 0, 0);

        } catch (Exception e) {
            System.err.println("Error loading cursor PNG: " + e.getMessage());
            return null;
        }
    }
}
