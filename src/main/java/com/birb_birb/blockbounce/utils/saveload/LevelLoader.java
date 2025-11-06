package com.birb_birb.blockbounce.utils.saveload;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

/**Utility class for loading level data from JSON files*/
public class LevelLoader {
    private final Gson gson;

    public LevelLoader() {
        this.gson = new GsonBuilder().setPrettyPrinting().create();
    }


    public LevelData loadLevel(int levelNumber) {
        String resourcePath = "/levels/level_" + levelNumber + ".json";

        try (InputStream inputStream = getClass().getResourceAsStream(resourcePath)) {
            if (inputStream == null) {
                System.err.println("Level file not found: " + resourcePath);
                return null;
            }

            try (InputStreamReader reader = new InputStreamReader(inputStream, StandardCharsets.UTF_8)) {
                return gson.fromJson(reader, LevelData.class);
            }

        } catch (Exception e) {
            System.err.println("Failed to load level " + levelNumber + ": " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }
}

