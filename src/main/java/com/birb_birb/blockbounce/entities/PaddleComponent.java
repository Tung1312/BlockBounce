package com.birb_birb.blockbounce.entities;

import com.almasb.fxgl.entity.component.Component;
import com.almasb.fxgl.input.Input;
import com.almasb.fxgl.input.UserAction;
import javafx.scene.input.KeyCode;

import static com.almasb.fxgl.dsl.FXGL.getInput;


public class PaddleComponent extends Component {

    private double speed = 8;
    private boolean left, right;

    @Override
    public void onAdded() {
        Input input = getInput();

        input.addAction(new UserAction("Move Left") {
            @Override
            protected void onAction() {
                left = true;
            }
            @Override
            protected void onActionEnd() {
                left = false;
            }
        }, KeyCode.LEFT);

        input.addAction(new UserAction("Move Right") {
            @Override
            protected void onAction() {
                right = true;
            }
            @Override
            protected void onActionEnd() {
                right = false;
            }
        }, KeyCode.RIGHT);
    }

    @Override
    public void onUpdate(double tpf) {
        if (left) {
            entity.translateX(-speed);
        } else if (right) {
            entity.translateX(speed);
        }
    }
}
