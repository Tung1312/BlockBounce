package com.birb_birb.blockbounce.core.gamemode.score;

import com.birb_birb.blockbounce.constants.GameMode;
import com.birb_birb.blockbounce.constants.GameConstants;
import com.birb_birb.blockbounce.constants.EntityType;
import com.birb_birb.blockbounce.core.GameFactory;
import com.birb_birb.blockbounce.core.GameManager;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

import static com.almasb.fxgl.dsl.FXGLForKtKt.*;

public class ScoreModeGame extends GameManager {

    private static final ScoreModeGame INSTANCE = new ScoreModeGame();
    private Text highScoreText;

    private ScoreModeGame() {}

    public static void startGame() {
        if (GameMode.getCurrentGameMode() != GameMode.ENDLESS) {
            System.err.println("Warning: ScoreModeGame.start() called but current mode is " + GameMode.getCurrentGameMode());
        }
        INSTANCE.initialize();
    }

    @Override
    protected void setupProperties() {
        super.setupProperties();
        getWorldProperties().setValue("highScore", 0);
    }

    @Override
    protected void createFrame() {
        GameFactory.createScoreModeFrame();
    }

    @Override
    protected void setupUI() {
        super.setupUI();
        createHighScoreDisplay();
    }

    private void createHighScoreDisplay() {
        highScoreText = new Text("High: 0");
        highScoreText.setFont(gameFont);
        highScoreText.setFill(Color.YELLOW);
        highScoreText.setTranslateX(GameConstants.WINDOW_WIDTH / 2.0 - 60);
        highScoreText.setTranslateY(30);
        getGameScene().addUINode(highScoreText);

        // Bind high score text to property
        getWorldProperties().addListener("highScore", (prev, now) -> {
            highScoreText.setText("High: " + now);
        });
    }

    @Override
    protected void onScoreChanged(int oldScore, int newScore) {
        // Update high score if current score exceeds it
        if (newScore > geti("highScore")) {
            set("highScore", newScore);
        }
    }

    @Override
    protected void setupGameLogic() {
        // Automatically spawn more bricks when all are destroyed (endless mode)
        getGameTimer().runAtInterval(() -> {
            if (getGameWorld().getEntitiesByType(EntityType.BRICK).isEmpty()
                && !getb("gameOver")) {
                spawnMoreBricks();
                // Show continue message (no completion callback)
                displayMessage("CONTINUE!", Color.LIGHTGREEN, 1.5, null);
            }
        }, javafx.util.Duration.seconds(0.5));
    }

    private void spawnMoreBricks() {
        // Create new bricks for endless mode
        GameFactory.createBricks();

        // Show continue message
        displayMessage("CONTINUE!", Color.LIGHTGREEN, 1.5, null);
    }
}
