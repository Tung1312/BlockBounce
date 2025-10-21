package com.birb_birb.blockbounce.core.gamemode.versus;

import com.birb_birb.blockbounce.constants.GameConstants;
import com.birb_birb.blockbounce.core.BlockBounceApp;
import com.birb_birb.blockbounce.core.GameFactory;
import com.birb_birb.blockbounce.core.GameManager;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.input.KeyCode;

import static com.almasb.fxgl.dsl.FXGLForKtKt.*;
import com.almasb.fxgl.entity.Entity;
import com.birb_birb.blockbounce.entities.PaddleComponent;

import java.util.Set;

public class VersusModeGame extends GameManager {

    public static final VersusModeGame INSTANCE = new VersusModeGame();

    private VersusModeGame() {}

    public static void startGame() {
        if (BlockBounceApp.getCurrentGameMode() != BlockBounceApp.GameMode.VERSUS) {
            System.err.println("Warning: VersusModeGame.start() called but current mode is " + BlockBounceApp.getCurrentGameMode());
        }
        INSTANCE.initialize();
    }

    @Override
    protected void setupProperties() {
        super.setupProperties();
        // Versus mode has more lives
        set("lives", 5);
    }

    @Override
    protected void setupNewGame() {
        // Versus mode: don't create default paddle, we'll create two custom ones in createFrame()
        GameFactory.createBackground();
        GameFactory.createWalls();
        GameFactory.createBricks();
        // Note: NOT calling GameFactory.createPaddle() - we create two paddles in createFrame()
        GameFactory.createBall();
    }

    public boolean isMovingLeft(int playerId) {
        BlockBounceApp app = (BlockBounceApp) getApp();
        Set<KeyCode> pressedKeys = app.getPressedKeys();
        if (playerId == 1) {
            return pressedKeys.contains(KeyCode.A);
        } else if (playerId == 2) {
            return pressedKeys.contains(KeyCode.LEFT);
        }
        return false;
    }

    public boolean isMovingRight(int playerId) {
        BlockBounceApp app = (BlockBounceApp) getApp();
        Set<KeyCode> pressedKeys = app.getPressedKeys();
        if (playerId == 1) {
            return pressedKeys.contains(KeyCode.D);
        } else if (playerId == 2) {
            return pressedKeys.contains(KeyCode.RIGHT);
        }
        return false;
    }

    private void movePaddle(int playerId, double dx) {
        for (Entity paddle : getGameWorld().getEntitiesByType(com.birb_birb.blockbounce.constants.EntityType.PADDLE)) {
            PaddleComponent pc = paddle.getComponent(PaddleComponent.class);
            if (pc.getPlayerId() == playerId) {
                paddle.setX(paddle.getX() + dx);
            }
        }
    }

    @Override
    protected void createFrame() {
        GameFactory.createVersusModeFrame();
        // Spawn two paddles for Versus mode - one on left, one on right
        double leftPaddleX = GameConstants.OFFSET_LEFT + GameConstants.PLAYABLE_WIDTH / 2.0 - GameConstants.PADDLE_WIDTH / 2.0;
        double rightPaddleX = GameConstants.OFFSET_LEFT +  GameConstants.PLAYABLE_WIDTH / 2.0 - GameConstants.PADDLE_WIDTH / 2.0;
        double paddleY = GameConstants.WINDOW_HEIGHT - GameConstants.OFFSET_BOTTOM - 60;
        GameFactory.createPaddle(leftPaddleX, paddleY, 1); // Player 1 - left half
        GameFactory.createPaddle(rightPaddleX, paddleY, 2); // Player 2 - right half
    }

    @Override
    protected void setupGameplayButtons() {
        // Center the buttons for versus mode
        VBox buttonPanel = com.birb_birb.blockbounce.ui.GameplayButtons.createButtonPanel();
        buttonPanel.setTranslateX((GameConstants.WINDOW_WIDTH / 2.0) - 26);
        buttonPanel.setTranslateY(10);
        getGameScene().addUINode(buttonPanel);
    }

    @Override
    protected void setupGameLogic() {
        // Versus mode: automatically respawn bricks when all are destroyed
        getGameTimer().runAtInterval(() -> {
            if (getGameWorld().getEntitiesByType(com.birb_birb.blockbounce.constants.EntityType.BRICK).isEmpty()
                && !getb("gameOver")) {
                respawnBricks();
            }
        }, javafx.util.Duration.seconds(0.5));
    }

    private void respawnBricks() {
        // Create new bricks for versus mode
        GameFactory.createBricks();

        // Show message
        displayMessage("FIGHT!", Color.ORANGE, 1.5);
    }
}
