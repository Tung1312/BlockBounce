package com.birb_birb.blockbounce.entities;

import com.almasb.fxgl.entity.component.Component;
import com.birb_birb.blockbounce.constants.GameConstants;


public class PaddleComponent extends Component {

    @Override
    public void onUpdate(double tpf) {
        // Clamp paddle position to stay within game boundaries
        double paddleWidth = entity.getBoundingBoxComponent().getWidth();
        double minX = GameConstants.OFFSET_LEFT;
        double maxX = GameConstants.WINDOW_WIDTH - GameConstants.OFFSET_RIGHT - paddleWidth;

        if (entity.getX() < minX) {
            entity.setX(minX);
        } else if (entity.getX() > maxX) {
            entity.setX(maxX);
        }
    }
}
