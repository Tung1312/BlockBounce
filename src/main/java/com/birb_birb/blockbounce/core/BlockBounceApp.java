package com.birb_birb.blockbounce.core;

import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.app.GameSettings;
import com.almasb.fxgl.app.scene.FXGLMenu;
import com.almasb.fxgl.app.scene.SceneFactory;
import com.birb_birb.blockbounce.constants.GameConstants;
import com.birb_birb.blockbounce.ui.menus.MainMenu;
import com.birb_birb.blockbounce.utils.CursorManager;
import com.birb_birb.blockbounce.utils.SoundManager;
import javafx.scene.paint.Color;
import org.jetbrains.annotations.NotNull;

import static com.almasb.fxgl.dsl.FXGL.*;

public class BlockBounceApp extends GameApplication {

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
    protected void onPreInit() {
        SoundManager.initialize();
    }

    @Override
    protected void initGame() {
        CursorManager.apply(getGameScene().getRoot());
        GameInitializer.initGameWorld();
        getGameScene().setBackgroundColor(Color.BLACK);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
