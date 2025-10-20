package com.birb_birb.blockbounce.entities;

import com.almasb.fxgl.entity.component.Component;
import com.almasb.fxgl.input.Input;
import com.almasb.fxgl.input.UserAction;
import com.birb_birb.blockbounce.constants.GameConstants;
import javafx.scene.Node;

import javafx.scene.input.KeyCode;

import static com.almasb.fxgl.dsl.FXGL.getInput;


public class PaddleComponent extends Component {

    private double speed = 8;
    private boolean left, right;

    public void moveLeft(boolean active) { left = active; }
    public void moveRight(boolean active) { right = active; }

    @Override
    public void onUpdate(double tpf) {
        if (left) entity.translateX(-speed);
        if (right) entity.translateX(speed);

        double paddleWidth = entity.getBoundingBoxComponent().getWidth();
        double minX = GameConstants.OFFSET_LEFT;
        double maxX = GameConstants.WINDOW_WIDTH - GameConstants.OFFSET_RIGHT - paddleWidth;
        double clampedX = Math.max(minX, Math.min(entity.getX(), maxX));
        entity.setX(clampedX);
    }
}
