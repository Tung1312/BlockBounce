package com.birb_birb.blockbounce.ui.menus;

import com.almasb.fxgl.app.scene.MenuType;
import com.birb_birb.blockbounce.constants.GameConstants;
import com.birb_birb.blockbounce.core.BlockBounceApp;
import com.birb_birb.blockbounce.utils.MenuManager;
import javafx.scene.control.Button;
import javafx.scene.input.KeyCode;

import static com.almasb.fxgl.dsl.FXGL.*;

/**
 * Score Mode menu.
 */
public class ScoreModeMenu extends MenuManager {

    public ScoreModeMenu() {
        super(MenuType.GAME_MENU);
    }

    @Override
    protected String getBackgroundImagePath() {
        return GameConstants.SCORE_MODE_BACKGROUND;
    }

    @Override
    protected void setupContent() {
        Button newGameButton = createMenuButton("New Game");
        newGameButton.setLayoutX(getButtonX());
        newGameButton.setLayoutY(getStartY());
        newGameButton.setOnAction(e -> {
            BlockBounceApp.setGameMode(BlockBounceApp.GameMode.ENDLESS);
            getGameController().startNewGame();
        });
        root.getChildren().add(newGameButton);

        setupKeyHandling();
    }

    private void setupKeyHandling() {
        getInput().addAction(new com.almasb.fxgl.input.UserAction("ESC_BACK") {
            @Override
            protected void onActionBegin() {
                getSceneService().popSubScene();
            }
        }, KeyCode.ESCAPE);
    }
}
