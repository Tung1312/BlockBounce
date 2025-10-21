package com.birb_birb.blockbounce.core.gamemode.versus;

import com.birb_birb.blockbounce.constants.GameConstants;

/**
 * Factory for creating playfields in Versus mode.
 * Simplified - only handles playfield creation, not entity management.
 */
public class PlayfieldFactory {

    private PlayfieldFactory() {
        // Private constructor to prevent instantiation
    }

    /**
     * Creates the left playfield for Player 1
     */
    public static Playfield createLeftPlayfield() {
        double x = GameConstants.OFFSET_LEFT;
        double y = GameConstants.OFFSET_TOP;
        double width = GameConstants.VERSUS_PLAYABLE_WIDTH / 2.0;
        double height = GameConstants.PLAYABLE_HEIGHT;

        Playfield playfield = new Playfield(1, x, y, width, height);
        playfield.initialize();
        return playfield;
    }

    /**
     * Creates the right playfield for Player 2
     */
    public static Playfield createRightPlayfield() {
        double x = GameConstants.OFFSET_LEFT + GameConstants.VERSUS_PLAYABLE_WIDTH / 2.0 + GameConstants.OFFSET_MIDDLE;
        double y = GameConstants.OFFSET_TOP;
        double width = GameConstants.VERSUS_PLAYABLE_WIDTH / 2.0;
        double height = GameConstants.PLAYABLE_HEIGHT;

        Playfield playfield = new Playfield(2, x, y, width, height);
        playfield.initialize();
        return playfield;
    }
}
