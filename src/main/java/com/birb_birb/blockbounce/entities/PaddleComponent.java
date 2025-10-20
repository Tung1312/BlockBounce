package com.birb_birb.blockbounce.entities;

import com.almasb.fxgl.entity.component.Component;
import com.birb_birb.blockbounce.constants.GameConstants;


public class PaddleComponent extends Component {

    @Override
    public void onUpdate(double tpf) {
        // Only clamp paddle position to stay within bounds
        double paddleWidth = entity.getBoundingBoxComponent().getWidth();
        double minX = GameConstants.OFFSET_LEFT;
        double maxX = GameConstants.WINDOW_WIDTH - GameConstants.OFFSET_RIGHT - paddleWidth;
        double clampedX = Math.max(minX, Math.min(entity.getX(), maxX));
        entity.setX(clampedX);
    }
}
