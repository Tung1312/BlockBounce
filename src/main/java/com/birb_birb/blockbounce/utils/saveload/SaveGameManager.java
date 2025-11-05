package com.birb_birb.blockbounce.utils.saveload;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.format.DateTimeFormatter;

/**
 * Manager for saving and loading game data
 */
public class SaveGameManager {

    private static final String SAVE_DIR = "saves";
    private static final String SAVE_FILE_PREFIX = "story_save_";
    private static final String SCORE_SAVE_FILE_PREFIX = "score_save_";
    private static final String SAVE_FILE_EXTENSION = ".dat";
    private static final int MAX_SAVE_SLOTS = 3;

    static {
        // Create saves directory if it doesn't exist
        try {
            Path savePath = Paths.get(SAVE_DIR);
            if (!Files.exists(savePath)) {
                Files.createDirectories(savePath);
            }
        } catch (IOException e) {
            System.err.println("Failed to create saves directory: " + e.getMessage());
        }
    }

    /**
     * Save game data to a specific slot
     * @param slot Save slot number (1-3)
     * @param data Game save data
     * @return true if save was successful
     */
    public static boolean saveGame(int slot, SaveData data) {
        if (slot < 1 || slot > MAX_SAVE_SLOTS) {
            System.err.println("Invalid save slot: " + slot);
            return false;
        }

        String fileName = SAVE_FILE_PREFIX + slot + SAVE_FILE_EXTENSION;
        Path savePath = Paths.get(SAVE_DIR, fileName);

        try (ObjectOutputStream oos = new ObjectOutputStream(
                new FileOutputStream(savePath.toFile()))) {
            oos.writeObject(data);
            System.out.println("Game saved successfully to slot " + slot);
            return true;
        } catch (IOException e) {
            System.err.println("Failed to save game: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Load game data from a specific slot
     * @param slot Save slot number (1-3)
     * @return SaveData or null if load failed
     */
    public static SaveData loadGame(int slot) {
        if (slot < 1 || slot > MAX_SAVE_SLOTS) {
            System.err.println("Invalid save slot: " + slot);
            return null;
        }

        String fileName = SAVE_FILE_PREFIX + slot + SAVE_FILE_EXTENSION;
        Path savePath = Paths.get(SAVE_DIR, fileName);

        if (!Files.exists(savePath)) {
            System.out.println("No save data found in slot " + slot);
            return null;
        }

        try (ObjectInputStream ois = new ObjectInputStream(
                new FileInputStream(savePath.toFile()))) {
            SaveData data = (SaveData) ois.readObject();
            System.out.println("Game loaded successfully from slot " + slot);
            return data;
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Failed to load game: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Save Score Mode game data to a specific slot
     * @param slot Save slot number (1-3)
     * @param data Game save data
     * @return true if save was successful
     */
    public static boolean saveScoreGame(int slot, SaveData data) {
        if (slot < 1 || slot > MAX_SAVE_SLOTS) {
            System.err.println("Invalid save slot: " + slot);
            return false;
        }

        String fileName = SCORE_SAVE_FILE_PREFIX + slot + SAVE_FILE_EXTENSION;
        Path savePath = Paths.get(SAVE_DIR, fileName);

        try (ObjectOutputStream oos = new ObjectOutputStream(
                new FileOutputStream(savePath.toFile()))) {
            oos.writeObject(data);
            System.out.println("Score Mode game saved successfully to slot " + slot);
            return true;
        } catch (IOException e) {
            System.err.println("Failed to save Score Mode game: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Load Score Mode game data from a specific slot
     * @param slot Save slot number (1-3)
     * @return SaveData or null if load failed
     */
    public static SaveData loadScoreGame(int slot) {
        if (slot < 1 || slot > MAX_SAVE_SLOTS) {
            System.err.println("Invalid save slot: " + slot);
            return null;
        }

        String fileName = SCORE_SAVE_FILE_PREFIX + slot + SAVE_FILE_EXTENSION;
        Path savePath = Paths.get(SAVE_DIR, fileName);

        if (!Files.exists(savePath)) {
            System.out.println("No Score Mode save data found in slot " + slot);
            return null;
        }

        try (ObjectInputStream ois = new ObjectInputStream(
                new FileInputStream(savePath.toFile()))) {
            SaveData data = (SaveData) ois.readObject();
            System.out.println("Score Mode game loaded successfully from slot " + slot);
            return data;
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Failed to load Score Mode game: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Check if a save slot has data
     * @param slot Save slot number (1-3)
     * @return true if save exists
     */
    public static boolean hasSaveData(int slot) {
        if (slot < 1 || slot > MAX_SAVE_SLOTS) {
            return false;
        }

        String fileName = SAVE_FILE_PREFIX + slot + SAVE_FILE_EXTENSION;
        Path savePath = Paths.get(SAVE_DIR, fileName);
        return Files.exists(savePath);
    }

    /**
     * Check if a Score Mode save slot has data
     * @param slot Save slot number (1-3)
     * @return true if save exists
     */
    public static boolean hasScoreSaveData(int slot) {
        if (slot < 1 || slot > MAX_SAVE_SLOTS) {
            return false;
        }

        String fileName = SCORE_SAVE_FILE_PREFIX + slot + SAVE_FILE_EXTENSION;
        Path savePath = Paths.get(SAVE_DIR, fileName);
        return Files.exists(savePath);
    }

    /**
     * Delete save data from a slot
     * @param slot Save slot number (1-3)
     * @return true if deletion was successful
     */
    public static boolean deleteSave(int slot) {
        if (slot < 1 || slot > MAX_SAVE_SLOTS) {
            System.err.println("Invalid save slot: " + slot);
            return false;
        }

        String fileName = SAVE_FILE_PREFIX + slot + SAVE_FILE_EXTENSION;
        Path savePath = Paths.get(SAVE_DIR, fileName);

        try {
            if (Files.exists(savePath)) {
                Files.delete(savePath);
                System.out.println("Save deleted from slot " + slot);
                return true;
            }
            return false;
        } catch (IOException e) {
            System.err.println("Failed to delete save: " + e.getMessage());
            return false;
        }
    }



    /**
     * Get preview information for a save slot
     * @param slot Save slot number (1-3)
     * @return SavePreview or null if no save exists
     */
    public static SavePreview getSavePreview(int slot) {
        SaveData data = loadGame(slot);
        if (data == null) {
            return null;
        }

        return new SavePreview(
            slot,
            data.getCurrentLevel(),
            data.getScore(),
            data.getLives(),
            data.getSaveDate()
        );
    }

    /**
     * Preview information for a save slot
     */
    public static class SavePreview {
        private final int slot;
        private final int level;
        private final int score;
        private final int lives;
        private final String saveDate;

        public SavePreview(int slot, int level, int score, int lives, java.time.LocalDateTime date) {
            this.slot = slot;
            this.level = level;
            this.score = score;
            this.lives = lives;
            this.saveDate = date.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"));
        }

        public int getSlot() {
            return slot;
        }

        public int getLevel() {
            return level;
        }

        public int getScore() {
            return score;
        }

        public int getLives() {
            return lives;
        }

        public String getSaveDate() {
            return saveDate;
        }

        @Override
        public String toString() {
            return String.format("Slot %d - Level %d | Score: %d | Lives: %d | %s",
                slot, level, score, lives, saveDate);
        }
    }
}
