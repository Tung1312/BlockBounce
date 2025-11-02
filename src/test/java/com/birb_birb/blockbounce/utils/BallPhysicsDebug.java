package com.birb_birb.blockbounce.utils;

import com.birb_birb.blockbounce.physics.BallPhysics;
import javafx.geometry.Point2D;

public class BallPhysicsDebug {
    public static void main(String[] args) {
        Point2D velocity = new Point2D(10, 0.5); // Angle ~2.86° (too flat)
        System.out.println("Original velocity: " + velocity);
        System.out.println("Original angle: " + BallPhysics.calculateAngle(velocity.getX(), velocity.getY()));

        Point2D normalized = BallPhysics.normalizeVelocity(velocity, 5.0);
        System.out.println("Normalized velocity: " + normalized);

        double angle = BallPhysics.calculateAngle(normalized.getX(), normalized.getY());
        System.out.println("Normalized angle: " + angle);
        System.out.println("Abs angle: " + Math.abs(angle));
        System.out.println("Is >= 15? " + (Math.abs(angle) >= 15.0));
    }
}
