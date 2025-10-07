package com.birb_birb.blockbounce.ui.menus;

import com.almasb.fxgl.app.scene.MenuType;
import com.birb_birb.blockbounce.constants.GameConstants;
import javafx.scene.control.Button;

import static com.almasb.fxgl.dsl.FXGL.*;

/**
 * Main menu of the game with options for different game modes.
 * Extends BaseMenu to inherit common functionality.
 */
public class MainMenu extends BaseMenu {

    public MainMenu() {
        super(MenuType.MAIN_MENU);
    }

    @Override
    protected String getBackgroundImagePath() {
        return GameConstants.MAIN_MENU_BACKGROUND;
    }

    @Override
    protected void setupContent() {
        // Calculate button positions
        double buttonX = getButtonX();
        double startY = getStartY();

        // Create menu buttons
        Button storyModeButton = createMenuButton("Story Mode");
        Button scoreModeButton = createMenuButton("Score Mode");
        Button versusModeButton = createMenuButton("Versus Mode");

        // Position buttons
        storyModeButton.setLayoutX(buttonX);
        storyModeButton.setLayoutY(startY);

        scoreModeButton.setLayoutX(buttonX);
        scoreModeButton.setLayoutY(startY + GameConstants.BUTTON_SPACING);

        versusModeButton.setLayoutX(buttonX);
        versusModeButton.setLayoutY(startY + (GameConstants.BUTTON_SPACING * 2));

        // Set button actions
        storyModeButton.setOnAction(e -> navigateToStoryMode());
        scoreModeButton.setOnAction(e -> navigateToScoreMode());
        versusModeButton.setOnAction(e -> navigateToVersusMode());

        // Add buttons to the root pane
        root.getChildren().addAll(storyModeButton, scoreModeButton, versusModeButton);
    }

    private void navigateToStoryMode() {
        // Use scene service to properly navigate without creating conflicting subscenes
        getSceneService().pushSubScene(new StoryModeMenu());
    }

    private void navigateToScoreMode() {
        // Use scene service to properly navigate without creating conflicting subscenes
        getSceneService().pushSubScene(new ScoreModeMenu());
    }

    private void navigateToVersusMode() {
        // Use scene service to properly navigate without creating conflicting subscenes
        getSceneService().pushSubScene(new VersusModeMenu());
    }
}
