package com.birb_birb.blockbounce.core;

import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.app.GameSettings;
import com.almasb.fxgl.app.scene.FXGLMenu;
import com.almasb.fxgl.app.scene.SceneFactory;
import com.birb_birb.blockbounce.constants.EntityType;
import com.birb_birb.blockbounce.constants.GameConstants;
import com.birb_birb.blockbounce.constants.GameMode;
import com.birb_birb.blockbounce.core.gamemode.score.ScoreModeGame;
import com.birb_birb.blockbounce.core.gamemode.story.StoryModeGame;
import com.birb_birb.blockbounce.entities.BallComponent;
import com.birb_birb.blockbounce.ui.menus.MainMenu;
import com.birb_birb.blockbounce.utils.CursorManager;
import com.birb_birb.blockbounce.utils.SoundManager;
import com.birb_birb.blockbounce.core.gamemode.versus.VersusModeGame;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.Set;

import static com.almasb.fxgl.dsl.FXGL.*;

public class BlockBounceApp extends GameApplication {

    // Track pressed keys manually
    private final Set<KeyCode> pressedKeys = new HashSet<>();
    private boolean inputInitialized = false;

    public Set<KeyCode> getPressedKeys() {
        return pressedKeys;
    }

    @Override
    protected void initSettings(GameSettings settings) {
        settings.setWidth(GameConstants.WINDOW_WIDTH);
        settings.setHeight(GameConstants.WINDOW_HEIGHT);
        settings.setTitle(GameConstants.GAME_TITLE);
        settings.setVersion(GameConstants.GAME_VERSION);
        settings.setFullScreenAllowed(true);
        settings.setFullScreenFromStart(false);
        settings.setMainMenuEnabled(true);
        settings.setGameMenuEnabled(true);
        settings.getCSSList().add("fxgl.css");

        settings.setSceneFactory(new SceneFactory() {
            @NotNull
            @Override
            public FXGLMenu newMainMenu() {
                return new MainMenu();
            }
        });
    }

    @Override
    protected void initInput() {
        // Input bindings get cleared when switching scenes
        // Handle input directly using JavaFX scene events
    }

    @Override
    protected void onPreInit() {
        SoundManager.initialize();
    }

    @Override
    protected void initGame() {
        CursorManager.apply(getGameScene().getRoot());

        // Initialize game based on selected mode
        switch (GameMode.getCurrentGameMode()) {
            case STORY:
                StoryModeGame.startGame();
                break;
            case VERSUS:
                VersusModeGame.startGame();
                break;
            case ENDLESS:
                ScoreModeGame.startGame();
                break;
        }

        getGameScene().setBackgroundColor(Color.BLACK);
    }

    @Override
    protected void onUpdate(double tpf) {
        // Setup input listeners once scene is ready
        if (!inputInitialized && getGameScene().getRoot().getScene() != null) {
            // Use addEventFilter instead of setOnKeyPressed to capture events BEFORE FXGL intercepts them
            getGameScene().getRoot().getScene().addEventFilter(javafx.scene.input.KeyEvent.KEY_PRESSED, e -> {
                pressedKeys.add(e.getCode());

                // Handle spacebar to launch ball
                if (e.getCode() == KeyCode.SPACE) {
                    // In Versus mode, Space launches Player 1's ball only.
                    if (GameMode.getCurrentGameMode() == GameMode.VERSUS) {
                        var p1 = VersusModeGame.INSTANCE.getPlayer1Playfield();
                        if (p1 != null && p1.getBall() != null) {
                            BallComponent bc = p1.getBall().getComponent(BallComponent.class);
                            if (bc != null && !bc.hasLaunched()) {
                                bc.launch();
                                SoundManager.playHitSound();
                            }
                        }
                    } else {
                        launchBall();
                    }
                    e.consume();
                }

                // Handle Enter key to launch Player 2 in Versus mode
                if (e.getCode() == KeyCode.ENTER) {
                    if (GameMode.getCurrentGameMode() == GameMode.VERSUS) {
                        var p2 = VersusModeGame.INSTANCE.getPlayer2Playfield();
                        if (p2 != null && p2.getBall() != null) {
                            BallComponent bc = p2.getBall().getComponent(BallComponent.class);
                            if (bc != null && !bc.hasLaunched()) {
                                bc.launch();
                                SoundManager.playHitSound();
                            }
                        }
                        e.consume();
                    }
                }

                // Handle S key for Save in Story Mode
                if (e.getCode() == KeyCode.S) {
                    if (GameMode.getCurrentGameMode() == GameMode.STORY) {
                        // Do not allow saving after game over
                        if (getb("gameOver")) {
                            e.consume();
                            return;
                        }
                         // Freeze all movement immediately
                         freezeAllEntities();

                         boolean success = StoryModeGame.getInstance().saveGame(1);
                         if (success) {
                             // Add delay before returning to menu for smoother transition
                             getGameTimer().runOnceAfter(() -> {
                                 getGameController().gotoMainMenu();
                             }, javafx.util.Duration.millis(500));
                         } else {
                             // If save failed, unfreeze
                             unfreezeAllEntities();
                         }
                         e.consume();
                    } else if (GameMode.getCurrentGameMode() == GameMode.ENDLESS) {
                        // Do not allow saving after game over (e.g., while entering High Score name)
                        if (getb("gameOver")) {
                            e.consume();
                            return;
                        }
                         // Freeze all movement immediately
                         freezeAllEntities();

                         boolean success = ScoreModeGame.getInstance().saveGame(1);
                         if (success) {
                             // Add delay before returning to menu for smoother transition
                             getGameTimer().runOnceAfter(() -> {
                                 getGameController().gotoMainMenu();
                             }, javafx.util.Duration.millis(500));
                         } else {
                             // If save failed, unfreeze
                             unfreezeAllEntities();
                         }
                         e.consume();
                    }
                }

                // Handle L key for Load in Story Mode (changed from D to L to avoid conflict)
                if (e.getCode() == KeyCode.L) {
                    if (GameMode.getCurrentGameMode() == GameMode.STORY) {
                        StoryModeGame.getInstance().loadGame(1);
                        e.consume();
                    } else if (GameMode.getCurrentGameMode() == GameMode.ENDLESS) {
                        ScoreModeGame.getInstance().loadGame(1);
                        e.consume();
                    }
                }

                // Consume the event to prevent FXGL from intercepting arrow keys (but NOT in Versus mode)
                if (GameMode.getCurrentGameMode() != GameMode.VERSUS) {
                    if (e.getCode() == KeyCode.LEFT || e.getCode() == KeyCode.RIGHT ||
                        e.getCode() == KeyCode.UP || e.getCode() == KeyCode.DOWN) {
                        e.consume();
                    }
                }
            });

            getGameScene().getRoot().getScene().addEventFilter(javafx.scene.input.KeyEvent.KEY_RELEASED, e -> {
                pressedKeys.remove(e.getCode());
                // Consume the event to prevent FXGL from intercepting arrow keys (but NOT in Versus mode)
                if (GameMode.getCurrentGameMode() != GameMode.VERSUS) {
                    if (e.getCode() == KeyCode.LEFT || e.getCode() == KeyCode.RIGHT ||
                        e.getCode() == KeyCode.UP || e.getCode() == KeyCode.DOWN) {
                        e.consume();
                    }
                }
            });

            inputInitialized = true;
        }

        // Handle paddle movement based on manually tracked keys - reduced speed to 4 for smoother control
        // Skip this in Versus mode - paddles are controlled independently there
        if (GameMode.getCurrentGameMode() != GameMode.VERSUS) {
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
        }
    }

    private void launchBall() {
        getGameWorld().getEntitiesByType(EntityType.BALL).forEach(ball -> {
            BallComponent ballComponent = ball.getComponent(BallComponent.class);
            if (ballComponent != null && !ballComponent.hasLaunched()) {
                ballComponent.launch();
                SoundManager.playHitSound();
            }
        });
    }

    /**
     * Freeze all entity movement (for saving)
     */
    private void freezeAllEntities() {
        getGameWorld().getEntitiesByType(EntityType.BALL).forEach(ball -> {
            BallComponent ballComponent = ball.getComponent(BallComponent.class);
            if (ballComponent != null) {
                ballComponent.freeze();
            }
        });
    }

    /**
     * Unfreeze all entity movement (after failed save)
     */
    private void unfreezeAllEntities() {
        getGameWorld().getEntitiesByType(EntityType.BALL).forEach(ball -> {
            BallComponent ballComponent = ball.getComponent(BallComponent.class);
            if (ballComponent != null) {
                ballComponent.unfreeze();
            }
        });
    }

    public static void main(String[] args) {
        launch(args);
    }
}
