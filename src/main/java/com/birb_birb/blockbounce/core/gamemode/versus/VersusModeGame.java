package com.birb_birb.blockbounce.core.gamemode.versus;

import com.almasb.fxgl.dsl.FXGLForKtKt;
import com.almasb.fxgl.entity.Entity;
import com.birb_birb.blockbounce.constants.EntityType;
import com.birb_birb.blockbounce.constants.GameConstants;
import com.birb_birb.blockbounce.constants.GameMode;
import com.birb_birb.blockbounce.core.GameFactory;
import com.birb_birb.blockbounce.core.GameManager;
import com.birb_birb.blockbounce.utils.MenuManager;
import com.birb_birb.blockbounce.utils.saveload.RandomLevelLoader;
import com.birb_birb.blockbounce.utils.saveload.LevelData;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

import static com.almasb.fxgl.dsl.FXGLForKtKt.*;

public class VersusModeGame extends GameManager {

    public static final VersusModeGame INSTANCE = new VersusModeGame();

    // Two separate playfields - one for each player
    private Playfield player1Playfield;
    private Playfield player2Playfield;

    // Guard to ensure end-of-match actions are scheduled only once
    private boolean endScheduled = false;

    // Random level loader for synchronized levels
    private RandomLevelLoader randomLevelLoader;

    private VersusModeGame() {
        randomLevelLoader = new RandomLevelLoader();
    }

    public static void startGame() {
        if (GameMode.getCurrentGameMode() != GameMode.VERSUS) {
            System.err.println("Warning: VersusModeGame.start() called but current mode is " + GameMode.getCurrentGameMode());
        }
        INSTANCE.initialize();
    }

    @Override
    protected void setupProperties() {
        super.setupProperties();

        // Player 1 properties
        set("player1Score", 0);
        set("player1Lives", 3);
        set("player1GameOver", false);

        // Player 2 properties
        set("player2Score", 0);
        set("player2Lives", 3);
        set("player2GameOver", false);
    }

    @Override
    protected void setupNewGame() {
        // Create background and frame
        GameFactory.createBackground();

        // Create two separate playfields
        player1Playfield = PlayfieldFactory.createLeftPlayfield();
        player2Playfield = PlayfieldFactory.createRightPlayfield();

        // Initialize playfield lives from world properties so per-player
        try {
            player1Playfield.setLives(geti("player1Lives"));
        } catch (Exception ignored) {
            // If property missing, keep Playfield's default
        }

        try {
            player2Playfield.setLives(geti("player2Lives"));
        } catch (Exception ignored) {
            // If property missing, keep Playfield's default
        }

        // Reset match-end guard
        endScheduled = false;
    }

    @Override
    protected void createFrame() {
        GameFactory.createVersusModeFrame();
    }

    @Override
    protected void setupGameplayButtons() {
        // Center the buttons for versus mode
        VBox buttonPanel = com.birb_birb.blockbounce.ui.GameplayButtons.createButtonPanel(GameMode.VERSUS);
        buttonPanel.setTranslateX((GameConstants.WINDOW_WIDTH / 2.0) - 26);
        buttonPanel.setTranslateY(10);
        getGameScene().addUINode(buttonPanel);
    }

    @Override
    protected void setupUI() {
        // Create frame first
        createFrame();

        // Player 1 UI (left)
        javafx.scene.text.Text p1ScoreText = new javafx.scene.text.Text("0");
        p1ScoreText.setFont(gameFont);
        p1ScoreText.setFill(GameConstants.FONT_COLOR);
        p1ScoreText.setTranslateX(GameConstants.OFFSET_LEFT + 57);
        p1ScoreText.setTranslateY(56);
        getGameScene().addUINode(p1ScoreText);

        javafx.scene.text.Text p1LivesText = new javafx.scene.text.Text("0");
        p1LivesText.setFont(gameFont);
        p1LivesText.setFill(GameConstants.FONT_COLOR);
        p1LivesText.setTranslateX(GameConstants.OFFSET_LEFT + 220);
        p1LivesText.setTranslateY(56);
        getGameScene().addUINode(p1LivesText);

        // Player 2 UI (right)
        javafx.scene.text.Text p2ScoreText = new javafx.scene.text.Text("0");
        p2ScoreText.setFont(gameFont);
        p2ScoreText.setFill(GameConstants.FONT_COLOR);
        p2ScoreText.setTranslateX(GameConstants.WINDOW_WIDTH - GameConstants.OFFSET_RIGHT - 450);
        p2ScoreText.setTranslateY(56);
        getGameScene().addUINode(p2ScoreText);

        javafx.scene.text.Text p2LivesText = new javafx.scene.text.Text("0");
        p2LivesText.setFont(gameFont);
        p2LivesText.setFill(GameConstants.FONT_COLOR);
        p2LivesText.setTranslateX(GameConstants.WINDOW_WIDTH - GameConstants.OFFSET_RIGHT - 287);
        p2LivesText.setTranslateY(56);
        getGameScene().addUINode(p2LivesText);

        // Bind to world properties so the UI updates automatically
        getWorldProperties().addListener("player1Score", (prev, now) -> p1ScoreText.setText(String.valueOf(now)));
        getWorldProperties().addListener("player1Lives", (prev, now) -> p1LivesText.setText(String.valueOf(now)));
        getWorldProperties().addListener("player2Score", (prev, now) -> p2ScoreText.setText(String.valueOf(now)));
        getWorldProperties().addListener("player2Lives", (prev, now) -> p2LivesText.setText(String.valueOf(now)));

        // Initialize with current values
        p1ScoreText.setText(String.valueOf(FXGLForKtKt.geti("player1Score")));
        p1LivesText.setText(String.valueOf(FXGLForKtKt.geti("player1Lives")));
        p2ScoreText.setText(String.valueOf(FXGLForKtKt.geti("player2Score")));
        p2LivesText.setText(String.valueOf(FXGLForKtKt.geti("player2Lives")));
    }

    @Override
    protected void setupGameLogic() {
        // Check for ball out of bounds and brick respawn for both players
        getGameTimer().runAtInterval(() -> {
            // Handle ball out of bounds for both players
            handlePlayerBallOutOfBounds(player1Playfield, "player1", "PLAYER 1");
            handlePlayerBallOutOfBounds(player2Playfield, "player2", "PLAYER 2");

            // Handle independent brick respawn for each player
            // Player 1 gets new level when they clear their bricks
            if (!player1Playfield.hasBricks() && !player1Playfield.isGameOver()) {
                LevelData randomLevel = randomLevelLoader.loadRandomLevel();
                if (randomLevel != null) {
                    player1Playfield.respawnBricksFromLevel(randomLevel);
                } else {
                    player1Playfield.respawnBricks();
                }
                // Reset ball to paddle for new level
                player1Playfield.resetBall();
                displayMessage("PLAYER 1: NEW WAVE!", Color.CYAN, 1.5, null);
            }

            // Player 2 gets new level when they clear their bricks
            if (!player2Playfield.hasBricks() && !player2Playfield.isGameOver()) {
                LevelData randomLevel = randomLevelLoader.loadRandomLevel();
                if (randomLevel != null) {
                    player2Playfield.respawnBricksFromLevel(randomLevel);
                } else {
                    player2Playfield.respawnBricks();
                }
                // Reset ball to paddle for new level
                player2Playfield.resetBall();
                displayMessage("PLAYER 2: NEW WAVE!", Color.CYAN, 1.5, null);
            }

            // Update score properties
            set("player1Score", player1Playfield.getScore());
            set("player2Score", player2Playfield.getScore());
        }, javafx.util.Duration.seconds(0.1));
    }

    /**
     * Handle ball out of bounds for a single player
     */
    private void handlePlayerBallOutOfBounds(Playfield playfield, String propertyPrefix, String playerName) {
        if (playfield.isGameOver()) return;

        if (playfield.isBallOutOfBounds()) {
            // Ball went out of bounds - lose life and respawn
            playfield.loseLife();
            set(propertyPrefix + "Lives", playfield.getLives());

            if (playfield.isGameOver()) {
                set(propertyPrefix + "GameOver", true);
                // Show the player's loss message, then check for match end after
                // the message (and the configured 3s inter-message gap) finishes.
                displayMessage(playerName + " LOSES!", Color.RED, 2.0, this::checkGameEnd);
            } else {
                playfield.resetBall();
            }
        }
    }

    private void checkGameEnd() {
        // Only end the versus match when both players have exhausted their lives.
        // At that point, compare scores and declare the winner (or tie).
        if (player1Playfield.isGameOver() && player2Playfield.isGameOver()) {
            if (endScheduled) return;
            endScheduled = true;

            int score1 = player1Playfield.getScore();
            int score2 = player2Playfield.getScore();

            // Determine winner
            String resultMsg;
            Color msgColor;
            int winnerPlayerId = 0;

            if (score1 > score2) {
                resultMsg = "PLAYER 1 WINS!";
                msgColor = Color.GOLD;
                winnerPlayerId = 1;
            } else if (score2 > score1) {
                resultMsg = "PLAYER 2 WINS!";
                msgColor = Color.GOLD;
                winnerPlayerId = 2;
            } else {
                resultMsg = "TIE GAME!";
                msgColor = Color.ORANGE;
                winnerPlayerId = 0; // Tie
            }

            handleGameOver(resultMsg, msgColor, score1, score2, winnerPlayerId);
        }
    }

    /**
     * Handle game over with multi-threading pattern
     * Shows countdown, saves game (optional), then returns to menu
     */
    private void handleGameOver(String resultMsg, Color msgColor, int score1, int score2, int winnerPlayerId) {
        getGameController().pauseEngine();

        // Add dimming overlay
        getGameScene().addUINode(MenuManager.createDimmingOverlay());

        // Display winner message
        javafx.scene.text.Text winnerText = new javafx.scene.text.Text(resultMsg);
        winnerText.setFont(gameFont);
        winnerText.setFill(msgColor);
        winnerText.setTranslateX((GameConstants.WINDOW_WIDTH / 2.0) - 200);
        winnerText.setTranslateY((GameConstants.WINDOW_HEIGHT / 2.0) - 100);
        getGameScene().addUINode(winnerText);

        // Display scores
        javafx.scene.text.Text scoresText = new javafx.scene.text.Text(
            String.format("Player 1: %d  |  Player 2: %d", score1, score2)
        );
        scoresText.setFont(gameFont);
        scoresText.setFill(Color.WHITE);
        scoresText.setTranslateX((GameConstants.WINDOW_WIDTH / 2.0) - 200);
        scoresText.setTranslateY((GameConstants.WINDOW_HEIGHT / 2.0) - 40);
        getGameScene().addUINode(scoresText);

        // Countdown text
        javafx.scene.text.Text countdownText = new javafx.scene.text.Text("");
        countdownText.setFont(gameFont);
        countdownText.setFill(Color.CYAN);
        countdownText.setTranslateX((GameConstants.WINDOW_WIDTH / 2.0) - 150);
        countdownText.setTranslateY((GameConstants.WINDOW_HEIGHT / 2.0) + 50);
        getGameScene().addUINode(countdownText);

        // Create worker thread for countdown and cleanup
        new Thread(() -> {
            try {
                // Show countdown: 3... 2... 1...
                for (int i = 3; i > 0; i--) {
                    final String count = String.valueOf(i);
                    javafx.application.Platform.runLater(() -> {
                        countdownText.setText("Returning to menu in " + count + "...");
                    });
                    Thread.sleep(1000);
                }

                // Optional: Save game stats (you can implement this later)
                // saveGameStats(score1, score2, winnerPlayerId);

            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }

            // Return to JavaFX thread for UI operations
            javafx.application.Platform.runLater(() -> {
                try {
                    set("gameOver", true);
                    getGameController().resumeEngine();
                    getGameController().gotoMainMenu();
                } catch (Exception ignored) {
                    // Fallback: try to go to menu anyway
                    try {
                        getGameController().gotoMainMenu();
                    } catch (Exception e) {
                        System.err.println("Failed to return to menu: " + e.getMessage());
                    }
                }
            });
        }).start(); // Start the worker thread
    }

    /**
     * Optional: Save game statistics
     * Can be implemented to save match history, player stats, etc.
     */
    private void saveGameStats(int score1, int score2, int winnerPlayerId) {
        // TODO: Implement game stats saving
        // Example: Save to file, database, or high scores
        System.out.println("Saving game stats: P1=" + score1 + ", P2=" + score2 + ", Winner=" + winnerPlayerId);
    }

    public Playfield getPlayer1Playfield() {
        return player1Playfield;
    }

    public Playfield getPlayer2Playfield() {
        return player2Playfield;
    }
}
