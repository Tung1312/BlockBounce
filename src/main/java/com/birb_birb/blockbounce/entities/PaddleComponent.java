package com.birb_birb.blockbounce.entities;

import com.almasb.fxgl.entity.component.Component;
import com.birb_birb.blockbounce.constants.GameConstants;
import com.birb_birb.blockbounce.core.BlockBounceApp;
import com.birb_birb.blockbounce.core.gamemode.versus.VersusModeGame;


public class PaddleComponent extends Component {
    private final int playerId;
    private static final double SPEED = 6;

    public PaddleComponent(int playerId) {
        this.playerId = playerId;
    }

    public int getPlayerId() {
        return playerId;
    }

    @Override
    public void onUpdate(double tpf) {
        double paddleWidth = entity.getBoundingBoxComponent().getWidth();
        double minX, maxX;
        double x = entity.getX();
        if (BlockBounceApp.getCurrentGameMode() == BlockBounceApp.GameMode.VERSUS) {
            // Versus mode: clamp to half screen with middle divider gap
            if (playerId == 1) {
                // Player 1: Left side (from left edge to middle divider)
                minX = GameConstants.OFFSET_LEFT;
                maxX = GameConstants.OFFSET_LEFT + GameConstants.VERSUS_PLAYABLE_WIDTH / 2.0 - paddleWidth;
            } else {
                // Player 2: Right side (from middle divider to right edge)
                minX = GameConstants.OFFSET_LEFT + GameConstants.VERSUS_PLAYABLE_WIDTH / 2.0 + GameConstants.OFFSET_MIDDLE;
                maxX = GameConstants.WINDOW_WIDTH - GameConstants.OFFSET_LEFT - paddleWidth;
            }
            if (VersusModeGame.INSTANCE.isMovingLeft(playerId)) x -= SPEED;
            if (VersusModeGame.INSTANCE.isMovingRight(playerId)) x += SPEED;
        } else {
            // Other modes: clamp to full play area
            minX = GameConstants.OFFSET_LEFT;
            maxX = GameConstants.WINDOW_WIDTH - GameConstants.OFFSET_RIGHT - paddleWidth;
        }
        double clampedX = Math.max(minX, Math.min(x, maxX));
        entity.setX(clampedX);
    }
}
