package com.birb_birb.blockbounce.core;

import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.app.GameSettings;
import com.almasb.fxgl.app.scene.FXGLMenu;
import com.almasb.fxgl.app.scene.SceneFactory;
import com.birb_birb.blockbounce.constants.EntityType;
import com.birb_birb.blockbounce.constants.GameConstants;
import com.birb_birb.blockbounce.core.gamemode.score.ScoreModeGame;
import com.birb_birb.blockbounce.core.gamemode.story.StoryModeGame;
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

    // Track for current game mode
    public enum GameMode {
        STORY, VERSUS, ENDLESS
    }
    private static GameMode currentGameMode = GameMode.STORY;

    // Track pressed keys manually
    private final Set<KeyCode> pressedKeys = new HashSet<>();
    private boolean inputInitialized = false;

    public static void setGameMode(GameMode mode) {
        currentGameMode = mode;
    }

    public static GameMode getCurrentGameMode() {
        return currentGameMode;
    }

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
        switch (currentGameMode) {
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
                // Consume the event to prevent FXGL from intercepting arrow keys (but NOT in Versus mode)
                if (currentGameMode != GameMode.VERSUS) {
                    if (e.getCode() == KeyCode.LEFT || e.getCode() == KeyCode.RIGHT ||
                        e.getCode() == KeyCode.UP || e.getCode() == KeyCode.DOWN) {
                        e.consume();
                    }
                }
            });

            getGameScene().getRoot().getScene().addEventFilter(javafx.scene.input.KeyEvent.KEY_RELEASED, e -> {
                pressedKeys.remove(e.getCode());
                // Consume the event to prevent FXGL from intercepting arrow keys (but NOT in Versus mode)
                if (currentGameMode != GameMode.VERSUS) {
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
        if (currentGameMode != GameMode.VERSUS) {
            if (pressedKeys.contains(KeyCode.LEFT) || pressedKeys.contains(KeyCode.A)) {
                getGameWorld().getEntitiesByType(EntityType.PADDLE).forEach(paddle ->
                    paddle.translateX(-4)
                );
            }

            if (pressedKeys.contains(KeyCode.RIGHT) || pressedKeys.contains(KeyCode.D)) {
                getGameWorld().getEntitiesByType(EntityType.PADDLE).forEach(paddle ->
                    paddle.translateX(4)
                );
            }
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
