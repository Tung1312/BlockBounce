package com.birb_birb.blockbounce.entities;

import com.almasb.fxgl.entity.component.Component;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.physics.*;
import com.birb_birb.blockbounce.constants.EntityType;
import com.birb_birb.blockbounce.constants.GameConstants;
import com.birb_birb.blockbounce.core.GameFactory;
import com.birb_birb.blockbounce.core.gamemode.versus.Playfield;
import com.birb_birb.blockbounce.utils.TextureUtils;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.util.Duration;

import java.util.List;

import static com.almasb.fxgl.dsl.FXGL.entityBuilder;
import static com.almasb.fxgl.dsl.FXGLForKtKt.getGameTimer;
import static com.almasb.fxgl.dsl.FXGLForKtKt.getGameWorld;

public class PowerUpComponent extends Component {

    public enum PowerUpType {
        DOUBLE_BALL,
        SMALL_PADDLE,
        FAST_BALL
    }

    private final PowerUpType type;

    public PowerUpComponent(PowerUpType type) {
        this.type = type;
    }

    @Override
    public void onUpdate(double tpf) {
        // fall straight down
        entity.translateY(GameConstants.POWER_UP_FALL_SPEED);

        // check out of bounds
        if (entity.getY() > GameConstants.WINDOW_HEIGHT - GameConstants.OFFSET_BOTTOM) {
            entity.removeFromWorld();
            return;
        }

        // check collision with paddles
        List<Entity> paddles = getGameWorld().getEntitiesByType(EntityType.PADDLE);
        if (paddles.isEmpty()) return;

        // If this powerup was spawned for a specific player, prefer that paddle
        int targetPlayer = 0;
        try {
            targetPlayer = entity.getInt("playerId");
        } catch (Exception ignored) {}

        for (Entity paddle : paddles) {
            int paddleId = 1;
            try {
                paddleId = paddle.getInt("playerId");
            } catch (Exception ignored) {}

            if (targetPlayer != 0 && targetPlayer != paddleId) continue;

            if (entity.isColliding(paddle)) {
                applyEffect(paddle, paddleId);
                entity.removeFromWorld();
                break;
            }
        }
    }

    private void applyEffect(Entity paddle, int paddleId) {
        switch (type) {
            case DOUBLE_BALL:
                applyDoubleBall(paddleId);
                break;
            case SMALL_PADDLE:
                applySmallPaddle(paddle);
                break;
            case FAST_BALL:
                applyFastBall(paddleId);
                break;
        }
    }

    private void applyDoubleBall(int paddleId) {
        // Double ALL existing balls for the affected player
        List<Entity> balls = getGameWorld().getEntitiesByType(EntityType.BALL);
        List<Entity> ballsToDuplicate = new java.util.ArrayList<>();

        // Collect all balls belonging to this player
        for (Entity b : balls) {
            try {
                int bid = b.getInt("playerId");
                if (bid == paddleId) {
                    ballsToDuplicate.add(b);
                }
            } catch (Exception ignored) {
                // No playerId means single-player - add if paddleId is 1
                if (paddleId == 1) {
                    ballsToDuplicate.add(b);
                }
            }
        }

        if (ballsToDuplicate.isEmpty()) {
            // No balls to duplicate
            return;
        }

        // Duplicate each ball
        for (Entity reference : ballsToDuplicate) {
            BallComponent refComp = reference.getComponent(BallComponent.class);
            if (refComp == null) continue;

            Point2D vel = refComp.getVelocity();

            // Position new ball offset from reference (avoid immediate collision)
            double bx = reference.getX() + 15;
            double by = reference.getY() - 15;

            // Create ball - use the reference ball's playfield if in versus mode
            Entity newBall;
            Playfield playfield = null;
            try {
                // Get playfield from reference ball component via reflection
                java.lang.reflect.Field field = BallComponent.class.getDeclaredField("playfield");
                field.setAccessible(true);
                playfield = (Playfield) field.get(refComp);
            } catch (Exception ignored) {}

            // Build the new ball with proper component
            if (playfield != null) {
                // Versus mode - create with playfield reference
                newBall = entityBuilder()
                    .type(EntityType.BALL)
                    .at(bx, by)
                    .view(TextureUtils.loadTexture(
                        GameConstants.BALL_TEXTURE,
                        GameConstants.BALL_SIZE,
                        GameConstants.BALL_SIZE))
                    .bbox(new HitBox(
                        BoundingShape.circle(GameConstants.BALL_SIZE / 2)))
                    .with(new BallComponent(playfield))
                    .collidable()
                    .buildAndAttach();
            } else {
                // Single-player mode - use factory method
                newBall = GameFactory.createBall();
                newBall.setPosition(bx, by);
            }

            // Set player ID
            try {
                newBall.setProperty("playerId", paddleId);
            } catch (Exception ignored) {}

            BallComponent newComp = newBall.getComponent(BallComponent.class);
            if (newComp != null) {
                // Add slight variation to velocity so balls don't move identically
                double angle = Math.random() * 20 - 10; // -10 to +10 degrees variation
                double angleRad = Math.toRadians(angle);
                double cos = Math.cos(angleRad);
                double sin = Math.sin(angleRad);
                Point2D variedVel = new Point2D(
                    vel.getX() * cos - vel.getY() * sin,
                    vel.getX() * sin + vel.getY() * cos
                );
                newComp.setVelocity(variedVel);
                newComp.unfreeze();
                newComp.setLaunched(true);
            }
        }
    }

    private void applySmallPaddle(Entity paddle) {
        // Visually shrink paddle and reduce movement bounds by setting property
        double originalWidth = GameConstants.PADDLE_WIDTH;
        double newWidth = originalWidth * 0.6;

        // store original width so we can restore later
        try { paddle.setProperty("origPaddleWidth", originalWidth); } catch (Exception ignored) {}
        try { paddle.setProperty("paddleWidth", newWidth); } catch (Exception ignored) {}

        // scale the visual node if possible: get first child from view component
        try {
            if (!paddle.getViewComponent().getChildren().isEmpty()) {
                Node view = paddle.getViewComponent().getChildren().getFirst();
                if (view != null) {
                    double scale = newWidth / originalWidth;
                    view.setScaleX(scale);
                }
            }
        } catch (Exception ignored) {}

        // schedule restore after 10 seconds
        getGameTimer().runOnceAfter(() -> {
            try { paddle.setProperty("paddleWidth", originalWidth); } catch (Exception ignored) {}
            try {
                if (!paddle.getViewComponent().getChildren().isEmpty()) {
                    Node v = paddle.getViewComponent().getChildren().getFirst();
                    if (v != null) v.setScaleX(1.0);
                }
            } catch (Exception ignored) {}
        }, Duration.seconds(10));
    }

    private void applyFastBall(int paddleId) {
        // Increase speed of all balls belonging to the player
        double multiplier = 1.6;
        List<Entity> balls = getGameWorld().getEntitiesByType(EntityType.BALL);

        for (Entity b : balls) {
            try {
                int bid = b.getInt("playerId");
                if (bid != paddleId) continue;
            } catch (Exception ignored) {
                if (paddleId != 1) continue;
            }

            BallComponent bc = b.getComponent(BallComponent.class);
            if (bc != null) {
                // Only apply if not already fasted (avoid stacking)
                if (bc.getSpeedMultiplier() == 1.0) {
                    bc.setSpeedMultiplier(multiplier);

                    // Store original multiplier for reverting
                    try { b.setProperty("speedBoostActive", true); } catch (Exception ignored) {}
                }
            }
        }

        // Revert after 8 seconds
        getGameTimer().runOnceAfter(() -> {
            for (Entity b : getGameWorld().getEntitiesByType(EntityType.BALL)) {
                try {
                    int bid = b.getInt("playerId");
                    if (bid != paddleId) continue;
                } catch (Exception ignored) {
                    if (paddleId != 1) continue;
                }

                BallComponent bc = b.getComponent(BallComponent.class);
                if (bc != null) {
                    try {
                        boolean wasBoost = b.getBoolean("speedBoostActive");
                        if (wasBoost) {
                            bc.setSpeedMultiplier(1.0);
                            b.setProperty("speedBoostActive", false);
                        }
                    } catch (Exception ignored) {}
                }
            }
        }, Duration.seconds(8));
    }
}

