package com.birb_birb.blockbounce.utils;

import com.almasb.fxgl.texture.Texture;

import static com.almasb.fxgl.dsl.FXGLForKtKt.getAssetLoader;

/**
 * Utility class for texture loading operations.
 */
public class TextureUtils {

    private TextureUtils() {}

    /**
     * Load and configure a texture with standard settings
     * @param path Path to the texture
     * @param width Desired width
     * @param height Desired height
     * @return Configured texture
     */
    public static Texture loadTexture(String path, double width, double height) {
        Texture texture = getAssetLoader().loadTexture(path);
        texture.setFitWidth(width);
        texture.setFitHeight(height);
        texture.setPreserveRatio(false);
        texture.setSmooth(false);
        return texture;
    }

    /**
     * Load texture copy with standard settings (for bricks that need multiple instances)
     * @param baseTexture Base texture to copy
     * @param width Desired width
     * @param height Desired height
     * @return Configured texture copy
     */
    public static Texture loadTextureCopy(Texture baseTexture, double width, double height) {
        Texture texture = baseTexture.copy();
        texture.setFitWidth(width);
        texture.setFitHeight(height);
        texture.setPreserveRatio(false);
        texture.setSmooth(false);
        return texture;
    }
}

