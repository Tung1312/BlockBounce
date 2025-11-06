package com.birb_birb.blockbounce.utils;

import com.almasb.fxgl.texture.Texture;

import static com.almasb.fxgl.dsl.FXGLForKtKt.getAssetLoader;

/**
 * Utility class for texture loading operations.
 */
public class TextureManager {

    private TextureManager() {}


    public static Texture loadTexture(String path, double width, double height) {
        Texture texture = getAssetLoader().loadTexture(path);
        texture.setFitWidth(width);
        texture.setFitHeight(height);
        texture.setPreserveRatio(false);
        texture.setSmooth(false);
        return texture;
    }


    public static Texture loadTextureCopy(Texture baseTexture, double width, double height) {
        Texture texture = baseTexture.copy();
        texture.setFitWidth(width);
        texture.setFitHeight(height);
        texture.setPreserveRatio(false);
        texture.setSmooth(false);
        return texture;
    }
}

