package com.birb_birb.blockbounce.physics;

import com.almasb.fxgl.entity.component.Component;
import javafx.geometry.Point2D;

public abstract class PhysicsComponent extends Component {
    protected Point2D velocity = Point2D.ZERO;
    protected boolean isFrozen = false;

    public Point2D getVelocity() {
        return velocity;
    }

    public void setVelocity(Point2D velocity) {
        this.velocity = velocity;
    }

    public double getSpeed() {
        return velocity.magnitude();
    }

    public boolean isFrozen() {
        return isFrozen;
    }

    public void setFrozen(boolean frozen) {
        this.isFrozen = frozen;
    }

    public void freeze() {
        isFrozen = true;
    }

    public void unfreeze() {
        isFrozen = false;
    }

    protected void applyVelocity() {
        if (!isFrozen) {
            entity.translate(velocity);
        }
    }
}
