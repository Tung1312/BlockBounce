package com.birb_birb.blockbounce.api;

import javafx.geometry.Point2D;

/**
 * Represents an object that has velocity and can be moved.
 * Keep this minimal so existing code can adopt it easily.
 */
public interface Movable {
    Point2D getVelocity();
    void setVelocity(Point2D velocity);

    double getSpeedMultiplier();
    void setSpeedMultiplier(double multiplier);
}

