package com.birb_birb.blockbounce.core.gamemode.versus;

import com.birb_birb.blockbounce.constants.GameConstants;
import com.birb_birb.blockbounce.core.BlockBounceApp;
import com.birb_birb.blockbounce.core.GameFactory;
import com.birb_birb.blockbounce.core.GameManager;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

import static com.almasb.fxgl.dsl.FXGLForKtKt.*;

public class VersusModeGame extends GameManager {

    private static final VersusModeGame INSTANCE = new VersusModeGame();

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
    protected void createFrame() {
        GameFactory.createVersusModeFrame();
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
