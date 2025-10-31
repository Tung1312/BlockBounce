package com.birb_birb.blockbounce.utils.highscore;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Manages high scores - saves to and loads from file
 */
public class HighScoreManager {
    private static final String HIGHSCORE_DIR = "saves";
    private static final String HIGHSCORE_FILE = "highscores.dat";
    private static final int MAX_HIGHSCORES = 10;

    /**
     * Add a new high score entry
     */
    public static boolean addHighScore(String playerName, int score) {
        List<HighScore> highScores = loadHighScores();

        // Add new score
        highScores.add(new HighScore(playerName, score));

        // Sort and keep only top scores
        Collections.sort(highScores);
        if (highScores.size() > MAX_HIGHSCORES) {
            highScores = highScores.subList(0, MAX_HIGHSCORES);
        }

        return saveHighScores(highScores);
    }

    /**
     * Check if a score qualifies as a high score
     */
    public static boolean isHighScore(int score) {
        List<HighScore> highScores = loadHighScores();

        // If we have less than max entries, any score qualifies
        if (highScores.size() < MAX_HIGHSCORES) {
            return true;
        }

        // Check if score is higher than the lowest high score
        return score > highScores.get(highScores.size() - 1).getScore();
    }

    /**
     * Load all high scores from file
     */
    public static List<HighScore> loadHighScores() {
        Path filePath = Paths.get(HIGHSCORE_DIR, HIGHSCORE_FILE);

        if (!Files.exists(filePath)) {
            return new ArrayList<>();
        }

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filePath.toFile()))) {
            @SuppressWarnings("unchecked")
            List<HighScore> highScores = (List<HighScore>) ois.readObject();
            return highScores;
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Error loading high scores: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    /**
     * Save high scores to file
     */
    private static boolean saveHighScores(List<HighScore> highScores) {
        try {
            // Ensure directory exists
            Path dirPath = Paths.get(HIGHSCORE_DIR);
            if (!Files.exists(dirPath)) {
                Files.createDirectories(dirPath);
            }

            Path filePath = Paths.get(HIGHSCORE_DIR, HIGHSCORE_FILE);

            try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filePath.toFile()))) {
                oos.writeObject(highScores);
                return true;
            }
        } catch (IOException e) {
            System.err.println("Error saving high scores: " + e.getMessage());
            return false;
        }
    }

    /**
     * Get the rank of a score (1-based, 1 is highest)
     * Returns -1 if score doesn't qualify
     */
    public static int getScoreRank(int score) {
        List<HighScore> highScores = loadHighScores();

        // Add temporary score to see where it ranks
        List<HighScore> tempList = new ArrayList<>(highScores);
        tempList.add(new HighScore("TEMP", score));
        Collections.sort(tempList);

        for (int i = 0; i < tempList.size(); i++) {
            if (tempList.get(i).getPlayerName().equals("TEMP")) {
                int rank = i + 1;
                // Only count if it's within MAX_HIGHSCORES
                return rank <= MAX_HIGHSCORES ? rank : -1;
            }
        }

        return -1;
    }
}
