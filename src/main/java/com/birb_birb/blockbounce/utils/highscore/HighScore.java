package com.birb_birb.blockbounce.utils.highscore;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Represents a high score entry with player name, score, and timestamp
 */
public class HighScore implements Serializable, Comparable<HighScore> {
    private static final long serialVersionUID = 1L;

    private final String playerName;
    private final int score;
    private final String timestamp;

    public HighScore(String playerName, int score) {
        this.playerName = playerName;
        this.score = score;
        this.timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }

    public String getPlayerName() {
        return playerName;
    }

    public int getScore() {
        return score;
    }

    @Override
    public int compareTo(HighScore other) {
        // Sort in descending order (highest score first)
        return Integer.compare(other.score, this.score);
    }

    @Override
    public String toString() {
        return String.format("%s - %d points - %s", playerName, score, timestamp);
    }
}

