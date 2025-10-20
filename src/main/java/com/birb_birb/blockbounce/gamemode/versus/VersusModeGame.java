package com.birb_birb.blockbounce.gamemode.versus;

import com.birb_birb.blockbounce.core.BlockBounceApp;
import com.birb_birb.blockbounce.core.GameInitializer;

public class VersusModeGame {

    public static void initialize() {

        if (BlockBounceApp.getCurrentGameMode() != BlockBounceApp.GameMode.VERSUS) {
            System.err.println("Warning: VersusModeGame.initialize() called but current mode is "
                    + BlockBounceApp.getCurrentGameMode());
        }
        // Initialize the base game
        GameInitializer.initializeGame();

        // TODO: Add endless-specific initialization here
        // - Set up endless brick generation
        // - Initialize high score tracking
        // - Set up endless UI elements
    }
}

