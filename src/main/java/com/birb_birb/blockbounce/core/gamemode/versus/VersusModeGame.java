package com.birb_birb.blockbounce.core.gamemode.versus;

import com.birb_birb.blockbounce.constants.GameConstants;
import com.birb_birb.blockbounce.constants.GameMode;
import com.birb_birb.blockbounce.core.BlockBounceApp;
import com.birb_birb.blockbounce.core.GameFactory;
import com.birb_birb.blockbounce.core.GameManager;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.input.KeyCode;

import static com.almasb.fxgl.dsl.FXGLForKtKt.*;

import java.util.Set;

public class VersusModeGame extends GameManager {

    public static final VersusModeGame INSTANCE = new VersusModeGame();

    // Two separate playfields - one for each player
    private Playfield player1Playfield;
    private Playfield player2Playfield;

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
        set("player1Lives", 5);
        set("player1GameOver", false);

        // Player 2 properties
        set("player2Score", 0);
        set("player2Lives", 5);
        set("player2GameOver", false);
    }

    @Override
    protected void setupNewGame() {
        // Create background and frame
        GameFactory.createBackground();

        // Create two separate playfields
        player1Playfield = PlayfieldFactory.createLeftPlayfield();
        player2Playfield = PlayfieldFactory.createRightPlayfield();
    }

    public boolean isMovingLeft(int playerId) {
        BlockBounceApp app = (BlockBounceApp) getApp();
        Set<KeyCode> pressedKeys = app.getPressedKeys();
        return playerId == 1 ? pressedKeys.contains(KeyCode.A) : pressedKeys.contains(KeyCode.LEFT);
    }

    public boolean isMovingRight(int playerId) {
        BlockBounceApp app = (BlockBounceApp) getApp();
        Set<KeyCode> pressedKeys = app.getPressedKeys();
        return playerId == 1 ? pressedKeys.contains(KeyCode.D) : pressedKeys.contains(KeyCode.RIGHT);
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
            playfield.loseLife();
            set(propertyPrefix + "Lives", playfield.getLives());

            if (playfield.isGameOver()) {
                set(propertyPrefix + "GameOver", true);
                displayMessage(playerName + " LOSES!", Color.RED, 2.0);
                checkGameEnd();
            } else {
                playfield.resetBall();
            }
        }

        if (!playfield.hasBricks()) {
            playfield.respawnBricks();
            displayMessage(playerName + ": NEW WAVE!", Color.CYAN, 1.5);
        }
    }

    private void checkGameEnd() {
        if (player1Playfield.isGameOver() && player2Playfield.isGameOver()) {
            // Both players lost - compare scores
            int score1 = player1Playfield.getScore();
            int score2 = player2Playfield.getScore();

            if (score1 > score2) {
                displayMessage("PLAYER 1 WINS!", Color.GOLD, 3.0);
            } else if (score2 > score1) {
                displayMessage("PLAYER 2 WINS!", Color.GOLD, 3.0);
            } else {
                displayMessage("TIE GAME!", Color.ORANGE, 3.0);
            }
            set("gameOver", true);
        } else if (player1Playfield.isGameOver()) {
            displayMessage("PLAYER 2 WINS!", Color.GOLD, 3.0);
            set("gameOver", true);
        } else if (player2Playfield.isGameOver()) {
            displayMessage("PLAYER 1 WINS!", Color.GOLD, 3.0);
            set("gameOver", true);
        }
    }

    public Playfield getPlayer1Playfield() {
        return player1Playfield;
    }

    public Playfield getPlayer2Playfield() {
        return player2Playfield;
    }
}
