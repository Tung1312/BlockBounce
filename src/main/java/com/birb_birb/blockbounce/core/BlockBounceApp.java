package com.birb_birb.blockbounce.core;

import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.app.GameSettings;
import com.almasb.fxgl.app.scene.FXGLMenu;
import com.almasb.fxgl.app.scene.SceneFactory;
import com.almasb.fxgl.entity.Entity;
import com.birb_birb.blockbounce.constants.EntityType;
import com.birb_birb.blockbounce.constants.GameConstants;
import com.birb_birb.blockbounce.gamemode.score.ScoreModeGame;
import com.birb_birb.blockbounce.gamemode.story.StoryModeGame;
import com.birb_birb.blockbounce.ui.menus.MainMenu;
import com.birb_birb.blockbounce.utils.CursorManager;
import com.birb_birb.blockbounce.utils.SoundManager;
import com.birb_birb.blockbounce.gamemode.versus.VersusModeGame;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import org.jetbrains.annotations.NotNull;

import static com.almasb.fxgl.dsl.FXGL.*;

public class BlockBounceApp extends GameApplication {

    // Game mode tracking
    public enum GameMode {
        STORY, VERSUS, ENDLESS
    }

    private static GameMode currentGameMode = GameMode.STORY;
    private boolean leftPressed = false;
    private boolean rightPressed = false;
    private Entity paddle;

    public static void setGameMode(GameMode mode) {
        currentGameMode = mode;
    }

    public static GameMode getCurrentGameMode() {
        return currentGameMode;
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
        settings.setGameMenuEnabled(false);  // Disable game menu to free up arrow keys

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
        // Use A/D keys with onKey
        onKey(KeyCode.A, () -> {
            getGameWorld().getEntitiesByType(EntityType.PADDLE)
                    .forEach(paddle -> paddle.translateX(-8));
        });

        onKey(KeyCode.D, () -> {
            getGameWorld().getEntitiesByType(EntityType.PADDLE)
                    .forEach(paddle -> paddle.translateX(8));
        });
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
                StoryModeGame.initialize();
                break;
            case VERSUS:
                VersusModeGame.initialize();
                break;
            case ENDLESS:
                ScoreModeGame.initialize();
                break;
        }

        getGameScene().setBackgroundColor(Color.BLACK);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
