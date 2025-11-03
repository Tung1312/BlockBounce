package com.birb_birb.blockbounce.utils.saveload;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Data structure for saving game state in Story Mode
 */
public class SaveData implements Serializable {
    @Serial
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

    public SaveData() {
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
}
