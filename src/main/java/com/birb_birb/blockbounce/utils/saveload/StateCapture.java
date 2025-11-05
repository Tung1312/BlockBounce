package com.birb_birb.blockbounce.utils.saveload;

import com.almasb.fxgl.entity.Entity;
import com.birb_birb.blockbounce.constants.EntityType;
import com.birb_birb.blockbounce.core.GameFactory;
import com.birb_birb.blockbounce.entities.BallComponent;
import com.birb_birb.blockbounce.entities.BrickComponent;
import com.birb_birb.blockbounce.constants.BrickType;
import javafx.geometry.Point2D;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.List;

import static com.almasb.fxgl.dsl.FXGL.*;

/**Helper class to capture and restore game state*/
public class StateCapture {

    /**Capture current game state for Score Mode*/
    public static SaveData captureScoreModeState(double elapsedTime) {
        SaveData data = new SaveData();

        // Set game mode
        data.setGameMode("SCORE");

        // Capture game properties
        data.setScore(geti("score"));
        data.setLives(geti("lives"));
        data.setHighScore(geti("highScore"));
        data.setElapsedTime(elapsedTime);

        // Capture paddle state
        List<Entity> paddles = getGameWorld().getEntitiesByType(EntityType.PADDLE);
        if (!paddles.isEmpty()) {
            Entity paddle = paddles.getFirst();
            data.setPaddleX(paddle.getX());
            data.setPaddleY(paddle.getY());
        }

        // Capture ball state
        List<Entity> balls = getGameWorld().getEntitiesByType(EntityType.BALL);
        if (!balls.isEmpty()) {
            Entity ball = balls.getFirst();
            data.setBallX(ball.getX());
            data.setBallY(ball.getY());

            BallComponent ballComponent = ball.getComponent(BallComponent.class);
            if (ballComponent != null) {
                data.setBallLaunched(ballComponent.hasLaunched());
                Point2D velocity = ballComponent.getVelocity();
                if (velocity != null) {
                    data.setBallVelocityX(velocity.getX());
                    data.setBallVelocityY(velocity.getY());
                }
            }
        }

        // Capture blocks state
        List<BlockData> blocks = new ArrayList<>();
        List<Entity> bricks = getGameWorld().getEntitiesByType(EntityType.BRICK);
        for (Entity brick : bricks) {
            BlockData blockData = new BlockData();
            blockData.setX(brick.getX());
            blockData.setY(brick.getY());

            // Get brick component to extract type and durability
            BrickComponent brickComponent = brick.getComponent(BrickComponent.class);

            if (brickComponent != null) {
                // Save brick type and current durability
                blockData.setBrickType(brickComponent.getBrickType().name());
                blockData.setDurability(brickComponent.getDurability());
                blockData.setHits(brickComponent.getDurability()); // For backward compatibility
            } else {
                // Fallback for bricks without component
                blockData.setBrickType("WOOD");
                blockData.setDurability(1);
                blockData.setHits(1);
            }

            // Get color from view (optional, may not be used for textured bricks)
            if (!brick.getViewComponent().getChildren().isEmpty()) {
                javafx.scene.Node node = brick.getViewComponent().getChildren().getFirst();
                if (node instanceof javafx.scene.shape.Rectangle rect) {
                    Color color = (Color) rect.getFill();
                    blockData.setColor(colorToString(color));
                }
            }


            blocks.add(blockData);
        }
        data.setBlocks(blocks);

        return data;
    }

    /**Restore Score Mode game state from save data*/
    public static RestoreResult restoreScoreModeState(SaveData data) {
        if (data == null) {
            System.err.println("Cannot restore null save data");
            return null;
        }

        // Clear current entities
        getGameWorld().getEntitiesByType(EntityType.BALL).forEach(Entity::removeFromWorld);
        getGameWorld().getEntitiesByType(EntityType.PADDLE).forEach(Entity::removeFromWorld);
        getGameWorld().getEntitiesByType(EntityType.BRICK).forEach(Entity::removeFromWorld);

        // Restore game properties
        set("score", data.getScore());
        set("lives", data.getLives());
        set("highScore", data.getHighScore());
        set("gameOver", false);

        // Restore paddle
        Entity paddle = com.birb_birb.blockbounce.core.GameFactory.createPaddle();
        paddle.setPosition(data.getPaddleX(), data.getPaddleY());

        // Restore ball
        Entity ball = com.birb_birb.blockbounce.core.GameFactory.createBall();
        ball.setPosition(data.getBallX(), data.getBallY());

        BallComponent ballComponent = ball.getComponent(BallComponent.class);
        if (ballComponent != null && data.isBallLaunched()) {
            // Set velocity
            Point2D velocity = new Point2D(data.getBallVelocityX(), data.getBallVelocityY());
            ballComponent.setVelocity(velocity);
            ballComponent.setLaunched(true);
        }

        // Restore blocks
        for (BlockData blockData : data.getBlocks()) {
            Color color = stringToColor(blockData.getColor());

            // Determine brick type
            BrickType brickType;
            try {
                String typeStr = blockData.getBrickType();
                if (typeStr != null && !typeStr.isEmpty()) {
                    brickType = BrickType.valueOf(typeStr);
                } else {
                    brickType = BrickType.WOOD;
                }
            } catch (IllegalArgumentException e) {
                // If brick type is invalid, default to WOOD
                brickType = BrickType.WOOD;
            }

            // Create brick with correct type
            Entity brick = GameFactory.createBrick(
                blockData.getX(), blockData.getY(), color, brickType
            );

            // Restore durability if it was damaged
            BrickComponent brickComponent = brick.getComponent(BrickComponent.class);

            if (brickComponent != null && blockData.getDurability() > 0) {
                brickComponent.setDurability(blockData.getDurability());

                // If durability is 1 and brick type has 2 max durability, show cracked texture
                if (blockData.getDurability() == 1 &&
                    (brickType == BrickType.STONE ||
                     brickType == BrickType.NETHERBRICK ||
                     brickType == BrickType.ENDSTONE)) {
                    // Manually trigger texture update to cracked version
                    java.lang.reflect.Method updateMethod;
                    try {
                        updateMethod = BrickComponent.class
                            .getDeclaredMethod("updateTextureToCracked");
                        updateMethod.setAccessible(true);
                        updateMethod.invoke(brickComponent);
                    } catch (Exception e) {
                        System.err.println("Could not update cracked texture: " + e.getMessage());
                    }
                }
            }
        }

        // Return elapsed time and timer state
        return new RestoreResult(data.getElapsedTime(), data.isBallLaunched());
    }

    /**Result of restore operation for Score Mode*/
    public static class RestoreResult {
        private final double elapsedTime;
        private final boolean timerStarted;

        public RestoreResult(double elapsedTime, boolean timerStarted) {
            this.elapsedTime = elapsedTime;
            this.timerStarted = timerStarted;
        }

        public double getElapsedTime() {
            return elapsedTime;
        }

        public boolean isTimerStarted() {
            return timerStarted;
        }
    }

    /**Color to String for serialization*/
    private static String colorToString(Color color) {
        if (color.equals(Color.RED)) return "RED";
        if (color.equals(Color.BLUE)) return "BLUE";
        if (color.equals(Color.GREEN)) return "GREEN";
        if (color.equals(Color.YELLOW)) return "YELLOW";
        if (color.equals(Color.ORANGE)) return "ORANGE";
        if (color.equals(Color.PURPLE)) return "PURPLE";
        if (color.equals(Color.PINK)) return "PINK";
        if (color.equals(Color.CYAN)) return "CYAN";
        return "WHITE";
    }

    /**String to Color for deserialization*/
    private static Color stringToColor(String colorStr) {
        if (colorStr == null) {
            return Color.WHITE;
        }

        switch (colorStr) {
            case "RED": return Color.RED;
            case "BLUE": return Color.BLUE;
            case "GREEN": return Color.GREEN;
            case "YELLOW": return Color.YELLOW;
            case "ORANGE": return Color.ORANGE;
            case "PURPLE": return Color.PURPLE;
            case "PINK": return Color.PINK;
            case "CYAN": return Color.CYAN;
            default: return Color.WHITE;
        }
    }
}
