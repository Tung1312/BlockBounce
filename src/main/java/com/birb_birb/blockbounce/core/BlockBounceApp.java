package com.birb_birb.blockbounce.core;

import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.app.GameSettings;
import com.almasb.fxgl.app.scene.FXGLMenu;
import com.almasb.fxgl.app.scene.SceneFactory;
import com.birb_birb.blockbounce.constants.EntityType;
import com.birb_birb.blockbounce.constants.GameConstants;
import com.birb_birb.blockbounce.gamemode.endless.EndlessModeGame;
import com.birb_birb.blockbounce.entities.PaddleComponent;
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
        getInput().addEventHandler(javafx.scene.input.KeyEvent.KEY_PRESSED, e -> {
            if (e.getCode() == KeyCode.LEFT) {
                getGameWorld().getEntitiesByType(EntityType.PADDLE)
                    .forEach(p -> p.getComponent(PaddleComponent.class).moveLeft(true));
            }
            if (e.getCode() == KeyCode.RIGHT) {
                getGameWorld().getEntitiesByType(EntityType.PADDLE)
                    .forEach(p -> p.getComponent(PaddleComponent.class).moveRight(true));
            }
        });
        getInput().addEventHandler(javafx.scene.input.KeyEvent.KEY_RELEASED, e -> {
            if (e.getCode() == KeyCode.LEFT) {
                getGameWorld().getEntitiesByType(EntityType.PADDLE)
                    .forEach(p -> p.getComponent(PaddleComponent.class).moveLeft(false));
            }
            if (e.getCode() == KeyCode.RIGHT) {
                getGameWorld().getEntitiesByType(EntityType.PADDLE)
                    .forEach(p -> p.getComponent(PaddleComponent.class).moveRight(false));
            }
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
                EndlessModeGame.initialize();
                break;
        }

        getGameScene().setBackgroundColor(Color.BLACK);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
