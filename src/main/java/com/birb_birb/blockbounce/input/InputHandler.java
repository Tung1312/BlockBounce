package com.birb_birb.blockbounce.input;

import com.birb_birb.blockbounce.constants.EntityType;
import com.birb_birb.blockbounce.constants.GameMode;
import com.birb_birb.blockbounce.core.gamemode.score.ScoreModeGame;
import com.birb_birb.blockbounce.core.gamemode.versus.VersusModeGame;
import com.birb_birb.blockbounce.entities.BallComponent;
import com.birb_birb.blockbounce.utils.SoundManager;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

import java.util.HashSet;
import java.util.Set;

import static com.almasb.fxgl.dsl.FXGL.*;

public class InputHandler {

    private final Set<KeyCode> pressedKeys = new HashSet<>();
    private boolean initialized = false;

    public boolean isInitialized() {
        return initialized;
    }

    public void initialize(Scene scene) {
        if (initialized || scene == null) {
            return;
        }

        // Use addEventFilter to capture events BEFORE FXGL intercepts them
        scene.addEventFilter(KeyEvent.KEY_PRESSED, this::handleKeyPressed);
        scene.addEventFilter(KeyEvent.KEY_RELEASED, this::handleKeyReleased);

        initialized = true;
    }

    private void handleKeyPressed(KeyEvent e) {
        pressedKeys.add(e.getCode());

        // spacebar to launch ball
        if (e.getCode() == KeyCode.SPACE) {
            handleSpaceKey(e);
        }

        // Enter to launch ball for Player 2
        if (e.getCode() == KeyCode.ENTER) {
            handleEnterKey(e);
        }

        // S to Save
        if (e.getCode() == KeyCode.S) {
            handleSaveKey(e);
        }

        consumeArrowKeys(e);
    }

    private void handleKeyReleased(KeyEvent e) {
        pressedKeys.remove(e.getCode());
        consumeArrowKeys(e);
    }

    /**Handle spacebar press for launching balls*/
    private void handleSpaceKey(KeyEvent e) {
        // In Versus mode, Space launches Player 1's ball only
        if (GameMode.getCurrentGameMode() == GameMode.VERSUS) {
            var p1 = VersusModeGame.INSTANCE.getPlayer1Playfield();
            if (p1 != null && p1.getBall() != null) {
                try {
                    BallComponent bc = p1.getBall().getComponent(BallComponent.class);
                    if (bc != null && !bc.hasLaunched()) {
                        bc.launch();
                        SoundManager.playHitSound();
                    }
                } catch (IllegalArgumentException ignored) {
                    // Ball entity exists but doesn't have BallComponent attached
                }
            }
        } else {
            launchBall();
        }
        e.consume();
    }

    /**Handle Enter key press for launching Player 2's ball in Versus mode*/
    private void handleEnterKey(KeyEvent e) {
        if (GameMode.getCurrentGameMode() == GameMode.VERSUS) {
            var p2 = VersusModeGame.INSTANCE.getPlayer2Playfield();
            if (p2 != null && p2.getBall() != null) {
                try {
                    BallComponent bc = p2.getBall().getComponent(BallComponent.class);
                    if (bc != null && !bc.hasLaunched()) {
                        bc.launch();
                        SoundManager.playHitSound();
                    }
                } catch (IllegalArgumentException ignored) {
                    // Ball entity exists but doesn't have BallComponent attached
                }
            }
            e.consume();
        }
    }

    /**Handle S key press for saving game state*/
    private void handleSaveKey(KeyEvent e) {
        if (GameMode.getCurrentGameMode() == GameMode.ENDLESS) {
            // Do not allow saving after game over
            if (getb("gameOver")) {
                e.consume();
                return;
            }

            getGameController().pauseEngine();

            boolean success = ScoreModeGame.getInstance().saveGame(1);
            if (success) {
                new Thread(() -> {
                    try {
                        Thread.sleep(1500);
                    } catch (InterruptedException ex) {
                        Thread.currentThread().interrupt();
                    }

                    javafx.application.Platform.runLater(() -> {
                        getGameController().gotoMainMenu();
                    });
                }).start();
            } else {
                getGameController().resumeEngine();
            }
            e.consume();
        }
    }

    /**Consume arrow key events to prevent FXGL interception (except in Versus mode)*/
    private void consumeArrowKeys(KeyEvent e) {
        if (GameMode.getCurrentGameMode() != GameMode.VERSUS) {
            if (e.getCode() == KeyCode.LEFT || e.getCode() == KeyCode.RIGHT ||
                e.getCode() == KeyCode.UP || e.getCode() == KeyCode.DOWN) {
                e.consume();
            }
        }
    }

    /**Update paddle positions based on currently pressed keys*/
    public void updatePaddleMovement() {
        if (GameMode.getCurrentGameMode() != GameMode.VERSUS) {
            // Single player modes - move all paddles
            if (pressedKeys.contains(KeyCode.LEFT) || pressedKeys.contains(KeyCode.A)) {
                getGameWorld().getEntitiesByType(EntityType.PADDLE).forEach(paddle ->
                    paddle.translateX(-6)
                );
            }

            if (pressedKeys.contains(KeyCode.RIGHT) || pressedKeys.contains(KeyCode.D)) {
                getGameWorld().getEntitiesByType(EntityType.PADDLE).forEach(paddle ->
                    paddle.translateX(6)
                );
            }
        } else {
            // Versus mode - move paddles independently based on player ID
            getGameWorld().getEntitiesByType(EntityType.PADDLE).forEach(paddle -> {
                int paddleId = 1; // default
                try {
                    paddleId = paddle.getInt("playerId");
                } catch (Exception ex) {
                    // nothing :p
                }

                // Player 1 controls: A and D
                if (paddleId == 1) {
                    if (pressedKeys.contains(KeyCode.A)) {
                        paddle.translateX(-6);
                    }
                    if (pressedKeys.contains(KeyCode.D)) {
                        paddle.translateX(6);
                    }
                }
                // Player 2 controls: LEFT and RIGHT arrow keys
                else if (paddleId == 2) {
                    if (pressedKeys.contains(KeyCode.LEFT)) {
                        paddle.translateX(-6);
                    }
                    if (pressedKeys.contains(KeyCode.RIGHT)) {
                        paddle.translateX(6);
                    }
                }
            });
        }
    }

    /**Launch all balls in single-player modes*/
    private void launchBall() {
        getGameWorld().getEntitiesByType(EntityType.BALL).forEach(ball -> {
            BallComponent ballComponent = ball.getComponent(BallComponent.class);
            if (ballComponent != null && !ballComponent.hasLaunched()) {
                ballComponent.launch();
                SoundManager.playHitSound();
            }
        });
    }

    public void reset() {
        pressedKeys.clear();
        initialized = false;
    }
}
