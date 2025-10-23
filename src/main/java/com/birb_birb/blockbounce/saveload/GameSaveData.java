package com.birb_birb.blockbounce.saveload;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Data structure for saving game state in Story Mode
 */
public class GameSaveData implements Serializable {
    private static final long serialVersionUID = 1L;

    // Game metadata
    private LocalDateTime saveDate;
    private String gameMode; // "STORY", "SCORE", "VERSUS"

    // Story Mode data
    private int currentLevel;
    private int score;
    private int lives;

    // Score Mode data
    private int highScore;
    private double elapsedTime;

    // Paddle state
    private double paddleX;
    private double paddleY;

    // Ball state
    private double ballX;
    private double ballY;
    private double ballVelocityX;
    private double ballVelocityY;
    private boolean ballLaunched;

    // Blocks state
    private List<BlockData> blocks;

    public GameSaveData() {
        this.saveDate = LocalDateTime.now();
        this.blocks = new ArrayList<>();
    }

    // Getters and Setters
    public LocalDateTime getSaveDate() {
        return saveDate;
    }

    public void setSaveDate(LocalDateTime saveDate) {
        this.saveDate = saveDate;
    }

    public String getGameMode() {
        return gameMode;
    }

    public void setGameMode(String gameMode) {
        this.gameMode = gameMode;
    }

    public int getCurrentLevel() {
        return currentLevel;
    }

    public void setCurrentLevel(int currentLevel) {
        this.currentLevel = currentLevel;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getLives() {
        return lives;
    }

    public void setLives(int lives) {
        this.lives = lives;
    }

    public int getHighScore() {
        return highScore;
    }

    public void setHighScore(int highScore) {
        this.highScore = highScore;
    }

    public double getElapsedTime() {
        return elapsedTime;
    }

    public void setElapsedTime(double elapsedTime) {
        this.elapsedTime = elapsedTime;
    }

    public double getPaddleX() {
        return paddleX;
    }

    public void setPaddleX(double paddleX) {
        this.paddleX = paddleX;
    }

    public double getPaddleY() {
        return paddleY;
    }

    public void setPaddleY(double paddleY) {
        this.paddleY = paddleY;
    }

    public double getBallX() {
        return ballX;
    }

    public void setBallX(double ballX) {
        this.ballX = ballX;
    }

    public double getBallY() {
        return ballY;
    }

    public void setBallY(double ballY) {
        this.ballY = ballY;
    }

    public double getBallVelocityX() {
        return ballVelocityX;
    }

    public void setBallVelocityX(double ballVelocityX) {
        this.ballVelocityX = ballVelocityX;
    }

    public double getBallVelocityY() {
        return ballVelocityY;
    }

    public void setBallVelocityY(double ballVelocityY) {
        this.ballVelocityY = ballVelocityY;
    }

    public boolean isBallLaunched() {
        return ballLaunched;
    }

    public void setBallLaunched(boolean ballLaunched) {
        this.ballLaunched = ballLaunched;
    }

    public List<BlockData> getBlocks() {
        return blocks;
    }

    public void setBlocks(List<BlockData> blocks) {
        this.blocks = blocks;
    }

    /**
     * Data structure for individual block state
     */
    public static class BlockData implements Serializable {
        private static final long serialVersionUID = 1L;

        private double x;
        private double y;
        private String color; // Color as string (e.g., "RED", "BLUE", "GREEN")
        private int hits; // Number of hits remaining

        public BlockData() {}

        public BlockData(double x, double y, String color, int hits) {
            this.x = x;
            this.y = y;
            this.color = color;
            this.hits = hits;
        }

        public double getX() {
            return x;
        }

        public void setX(double x) {
            this.x = x;
        }

        public double getY() {
            return y;
        }

        public void setY(double y) {
            this.y = y;
        }

        public String getColor() {
            return color;
        }

        public void setColor(String color) {
            this.color = color;
        }

        public int getHits() {
            return hits;
        }

        public void setHits(int hits) {
            this.hits = hits;
        }
    }
}
