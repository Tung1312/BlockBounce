package com.birb_birb.blockbounce.utils.saveload;

import com.almasb.fxgl.entity.Entity;
import com.birb_birb.blockbounce.constants.EntityType;
import com.birb_birb.blockbounce.entities.BallComponent;
import com.birb_birb.blockbounce.utils.saveload.SaveData.BlockData;
import javafx.geometry.Point2D;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.List;

import static com.almasb.fxgl.dsl.FXGL.*;

/**
 * Helper class to capture and restore game state
 */
public class StateCapture {

    /**
     * Capture current game state for Story Mode
     */
    public static SaveData captureStoryModeState() {
        SaveData data = new SaveData();

        // Set game mode
        data.setGameMode("STORY");

        // Capture game properties
        data.setCurrentLevel(geti("level"));
        data.setScore(geti("score"));
        data.setLives(geti("lives"));

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

            // Get color from view
            if (!brick.getViewComponent().getChildren().isEmpty()) {
                javafx.scene.Node node = brick.getViewComponent().getChildren().getFirst();
                if (node instanceof javafx.scene.shape.Rectangle rect) {
                    Color color = (Color) rect.getFill();
                    blockData.setColor(colorToString(color));
                }
            }

            // Default hits to 1 (blocks don't have hits property in current implementation)
            blockData.setHits(1);

            blocks.add(blockData);
        }
        data.setBlocks(blocks);

        return data;
    }

    /**
     * Capture current game state for Score Mode
     */
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

            // Get color from view
            if (!brick.getViewComponent().getChildren().isEmpty()) {
                javafx.scene.Node node = brick.getViewComponent().getChildren().getFirst();
                if (node instanceof javafx.scene.shape.Rectangle rect) {
                    Color color = (Color) rect.getFill();
                    blockData.setColor(colorToString(color));
                }
            }

            // Default hits to 1
            blockData.setHits(1);

            blocks.add(blockData);
        }
        data.setBlocks(blocks);

        return data;
    }

    /**
     * Restore game state from save data
     */
    public static void restoreStoryModeState(SaveData data) {
        if (data == null) {
            System.err.println("Cannot restore null save data");
            return;
        }

        // Clear current entities
        getGameWorld().getEntitiesByType(EntityType.BALL).forEach(Entity::removeFromWorld);
        getGameWorld().getEntitiesByType(EntityType.PADDLE).forEach(Entity::removeFromWorld);
        getGameWorld().getEntitiesByType(EntityType.BRICK).forEach(Entity::removeFromWorld);

        // Restore game properties
        set("level", data.getCurrentLevel());
        set("score", data.getScore());
        set("lives", data.getLives());
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
            ball.setProperty("velocity", velocity);
            ballComponent.setLaunched(true);
        }

        // Restore blocks
        for (BlockData blockData : data.getBlocks()) {
            Color color = stringToColor(blockData.getColor());
            Entity brick = com.birb_birb.blockbounce.core.GameFactory.createBrick(
                blockData.getX(), blockData.getY(), color
            );

            if (blockData.getHits() > 0) {
                brick.setProperty("hits", blockData.getHits());
            }
        }
    }

    /**
     * Restore Score Mode game state from save data
     */
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
            Entity brick = com.birb_birb.blockbounce.core.GameFactory.createBrick(
                blockData.getX(), blockData.getY(), color
            );

            if (blockData.getHits() > 0) {
                brick.setProperty("hits", blockData.getHits());
            }
        }

        // Return elapsed time and timer state
        return new RestoreResult(data.getElapsedTime(), data.isBallLaunched());
    }

    /**
     * Result of restore operation for Score Mode
     */
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

    /**
     * Convert Color to String for serialization
     */
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

    /**
     * Convert String to Color for deserialization
     */
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
