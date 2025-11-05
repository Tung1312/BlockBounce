package com.birb_birb.blockbounce.utils.saveload;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**Manages level progress (locked/unlocked)*/
public class ProgressLoader {
    private static final String SAVE_DIR = "saves";
    private static final String PROGRESS_FILE = "progress.json";
    private static final String DEFAULT_RESOURCE = "/progress_default.json";

    private static ProgressLoader instance;
    private final Gson gson;
    private LevelProgress progress;

    private ProgressLoader() {
        this.gson = new GsonBuilder().setPrettyPrinting().create();
        loadProgress();
    }

    public static ProgressLoader getInstance() {
        if (instance == null) {
            instance = new ProgressLoader();
        }
        return instance;
    }

    private static class LevelProgress {
        int unlocked = 1;
    }

    /**Load progress from saves folder, or copy from default if not exists*/
    private void loadProgress() {
        Path savePath = Paths.get(SAVE_DIR, PROGRESS_FILE);

        // 1. Check if save file exists
        if (!Files.exists(savePath)) {
            try {
                Files.createDirectories(Paths.get(SAVE_DIR)); // Create saves directory if it doesn't exist
            } catch (IOException e) {
                System.err.println("Failed to create saves directory: " + e.getMessage());
            }

            copyDefaultProgress(savePath); // Copy default progress from resources
        }

        // 2. Load progress from save file
        try (Reader reader = new FileReader(savePath.toFile(), StandardCharsets.UTF_8)) {
            progress = gson.fromJson(reader, LevelProgress.class);
            if (progress == null) {
                progress = new LevelProgress();
                progress.unlocked = 1;
            }
        } catch (IOException e) {
            System.err.println("Failed to load progress, using default: " + e.getMessage());
            progress = new LevelProgress();
            progress.unlocked = 1;
        }
    }

    /**Copy default progress file from resources to saves folder*/
    private void copyDefaultProgress(Path savePath) {
        try (InputStream inputStream = getClass().getResourceAsStream(DEFAULT_RESOURCE)) {
            if (inputStream == null) {
                System.err.println("Default progress file not found in resources");
                // Create a default one
                progress = new LevelProgress();
                progress.unlocked = 1;
                saveProgress();
                return;
            }
            // Copy to saves folder
            Files.copy(inputStream, savePath);
            System.out.println("Created default level progress file");
        } catch (IOException e) {
            System.err.println("Failed to copy default progress: " + e.getMessage());
            // Create a default one manually
            progress = new LevelProgress();
            progress.unlocked = 1;
            saveProgress();
        }
    }

    /**Save current progress to file*/
    public void saveProgress() {
        Path savePath = Paths.get(SAVE_DIR, PROGRESS_FILE);

        try (Writer writer = new FileWriter(savePath.toFile(), StandardCharsets.UTF_8)) {
            gson.toJson(progress, writer);
            System.out.println("Saved level progress: " + progress.unlocked + " levels unlocked");
        } catch (IOException e) {
            System.err.println("Failed to save progress: " + e.getMessage());
        }
    }

    public int getUnlockedLevels() {
        return progress.unlocked;
    }

    /**Reload progress from file*/
    public void reloadProgress() {
        Path savePath = Paths.get(SAVE_DIR, PROGRESS_FILE);
        if (!Files.exists(savePath)) {
            System.out.println("Progress file not found, using current progress");
            return;
        }

        try (Reader reader = new FileReader(savePath.toFile(), StandardCharsets.UTF_8)) {
            LevelProgress reloadedProgress = gson.fromJson(reader, LevelProgress.class);
            if (reloadedProgress != null) {
                progress = reloadedProgress;
                System.out.println("Reloaded level progress: " + progress.unlocked + " levels unlocked");
            }
        } catch (IOException e) {
            System.err.println("Failed to reload progress: " + e.getMessage());
        }
    }

    public boolean isLevelUnlocked(int levelNumber) {
        return levelNumber <= progress.unlocked;
    }

    public void unlockLevel(int levelNumber) {
        if (levelNumber > progress.unlocked) {
            progress.unlocked = levelNumber;
            saveProgress();
            System.out.println("Unlocked level " + levelNumber);
        }
    }

}
