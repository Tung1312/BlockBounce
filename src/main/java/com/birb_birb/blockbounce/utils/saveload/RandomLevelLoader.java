package com.birb_birb.blockbounce.utils.saveload;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Random;

/**
 * Utility class for loading random levels from the storage directory
 */
public class RandomLevelLoader {
    private final Gson gson;
    private final Random random;
    private static final int MIN_LEVEL = 9;
    private static final int MAX_LEVEL = 23;

    public RandomLevelLoader() {
        this.gson = new GsonBuilder().setPrettyPrinting().create();
        this.random = new Random();
    }

    /**
     * Load a random level from storage directory (levels 9-23)
     * @return LevelData object or null if loading failed
     */
    public LevelData loadRandomLevel() {
        int randomLevel = MIN_LEVEL + random.nextInt(MAX_LEVEL - MIN_LEVEL + 1);
        return loadLevelFromStorage(randomLevel);
    }

    /**
     * Load a specific level from storage directory
     * @param levelNumber The level number (9-23)
     * @return LevelData object or null if loading failed
     */
    public LevelData loadLevelFromStorage(int levelNumber) {
        String resourcePath = "/levels/storage/level_" + levelNumber + ".json";

        try (InputStream inputStream = getClass().getResourceAsStream(resourcePath)) {
            if (inputStream == null) {
                System.err.println("Level file not found in storage: " + resourcePath);
                return null;
            }

            try (InputStreamReader reader = new InputStreamReader(inputStream, StandardCharsets.UTF_8)) {
                LevelData data = gson.fromJson(reader, LevelData.class);
                System.out.println("Loaded random level: " + levelNumber);
                return data;
            }

        } catch (Exception e) {
            System.err.println("Failed to load level from storage " + levelNumber + ": " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }
}

