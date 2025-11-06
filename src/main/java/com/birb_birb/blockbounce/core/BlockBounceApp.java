package com.birb_birb.blockbounce.core;

import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.app.GameSettings;
import com.almasb.fxgl.app.scene.FXGLMenu;
import com.almasb.fxgl.app.scene.SceneFactory;
import com.birb_birb.blockbounce.constants.GameConstants;
import com.birb_birb.blockbounce.constants.GameMode;
import com.birb_birb.blockbounce.core.gamemode.score.ScoreModeGame;
import com.birb_birb.blockbounce.core.gamemode.story.StoryModeGame;
import com.birb_birb.blockbounce.input.InputHandler;
import com.birb_birb.blockbounce.ui.menus.MainMenu;
import com.birb_birb.blockbounce.utils.CursorManager;
import com.birb_birb.blockbounce.utils.SoundManager;
import com.birb_birb.blockbounce.core.gamemode.versus.VersusModeGame;
import javafx.scene.paint.Color;
import org.jetbrains.annotations.NotNull;

import static com.almasb.fxgl.dsl.FXGL.*;

public class BlockBounceApp extends GameApplication {

    private final InputHandler inputHandler = new InputHandler();

    @Override
    protected void initSettings(GameSettings settings) {
        settings.setWidth(GameConstants.WINDOW_WIDTH);
        settings.setHeight(GameConstants.WINDOW_HEIGHT);
        settings.setTitle(GameConstants.GAME_TITLE);
        settings.setVersion(GameConstants.GAME_VERSION);
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
    protected void onPreInit() {
        SoundManager.initialize();
    }

    @Override
    protected void initGame() {
        CursorManager.apply(getGameScene().getRoot());

        getGameController().resumeEngine();

        inputHandler.reset();

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
        if (!inputHandler.isInitialized() && getGameScene().getRoot().getScene() != null) {
            inputHandler.initialize(getGameScene().getRoot().getScene());
        }

        inputHandler.updatePaddleMovement();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
