package com.birb_birb.blockbounce.core.gamemode.versus;

import com.almasb.fxgl.dsl.FXGLForKtKt;
import com.almasb.fxgl.entity.Entity;
import com.birb_birb.blockbounce.constants.EntityType;
import com.birb_birb.blockbounce.constants.GameConstants;
import com.birb_birb.blockbounce.constants.GameMode;
import com.birb_birb.blockbounce.core.GameFactory;
import com.birb_birb.blockbounce.core.GameManager;
import com.birb_birb.blockbounce.utils.MenuManager;
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

    private VersusModeGame() {}

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
        set("player1Lives", 1);
        set("player1GameOver", false);

        // Player 2 properties
        set("player2Score", 0);
        set("player2Lives", 1);
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
            updatePlayer(player1Playfield, "player1", "PLAYER 1");
            updatePlayer(player2Playfield, "player2", "PLAYER 2");

            // Update score properties
            set("player1Score", player1Playfield.getScore());
            set("player2Score", player2Playfield.getScore());
        }, javafx.util.Duration.seconds(0.1));
    }

    /**
     * Update logic for a single player - eliminates code duplication
     */
    private void updatePlayer(Playfield playfield, String propertyPrefix, String playerName) {
        if (playfield.isGameOver()) return;

        if (playfield.isBallOutOfBounds()) {
            // Count how many balls are still alive for this player
            int playerId = playfield.getPlayerId();
            java.util.List<Entity> allBalls = getGameWorld().getEntitiesByType(EntityType.BALL);
            int aliveBalls = 0;

            for (Entity b : allBalls) {
                // Check if ball belongs to this player
                try {
                    int ballPlayerId = b.getInt("playerId");
                    if (ballPlayerId == playerId) {
                        // Check if ball is not out of bounds yet
                        if (b.getY() <= playfield.getBottomBoundary()) {
                            aliveBalls++;
                        }
                    }
                } catch (Exception ignored) {}
            }

            // If this is NOT the last ball, just remove it and continue
            if (aliveBalls > 1) {
                // Find and remove the out-of-bounds ball
                for (Entity b : allBalls) {
                    try {
                        int ballPlayerId = b.getInt("playerId");
                        if (ballPlayerId == playerId && b.getY() > playfield.getBottomBoundary()) {
                            b.removeFromWorld();
                            break;
                        }
                    } catch (Exception ignored) {}
                }
                return; // Don't lose life or reset
            }

            // This is the last ball - lose life and respawn
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

        if (!playfield.hasBricks()) {
            playfield.respawnBricks();
            displayMessage(playerName + ": NEW WAVE!", Color.CYAN, 1.5, null);
        }
    }

    private void checkGameEnd() {
        // Only end the versus match when both players have exhausted their lives.
        // At that point, compare scores and declare the winner (or tie).
        if (player1Playfield.isGameOver() && player2Playfield.isGameOver()) {
            if (endScheduled) return;

            // Add dimming overlay
            getGameScene().addUINode(MenuManager.createDimmingOverlay());

            int score1 = player1Playfield.getScore();
            int score2 = player2Playfield.getScore();

            String resultMsg;
            Color msgColor;

            if (score1 > score2) {
                resultMsg = "PLAYER 1 WINS!";
                msgColor = Color.GOLD;
            } else if (score2 > score1) {
                resultMsg = "PLAYER 2 WINS!";
                msgColor = Color.GOLD;
            } else {
                resultMsg = "TIE GAME!";
                msgColor = Color.ORANGE;
            }

            endScheduled = true;

            getGameController().pauseEngine();

            displayMessage(resultMsg, msgColor, 3.0, null);

            //wait then return to menu
            new Thread(() -> {
                try {
                    Thread.sleep(6000);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }

                javafx.application.Platform.runLater(() -> {
                    try {
                        set("gameOver", true);
                        getGameController().resumeEngine();
                        getGameController().gotoMainMenu();
                    } catch (Exception ignored) {
                    }
                });
            }).start();
        }
    }

    public Playfield getPlayer1Playfield() {
        return player1Playfield;
    }

    public Playfield getPlayer2Playfield() {
        return player2Playfield;
    }
}
