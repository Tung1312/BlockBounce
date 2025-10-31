package com.birb_birb.blockbounce.ui.menus;

import com.almasb.fxgl.app.scene.FXGLMenu;
import com.almasb.fxgl.app.scene.MenuType;
import com.birb_birb.blockbounce.constants.GameConstants;
import com.birb_birb.blockbounce.utils.SoundManager;
import com.birb_birb.blockbounce.utils.highscore.HighScore;
import com.birb_birb.blockbounce.utils.highscore.HighScoreManager;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

import java.util.List;

import static com.almasb.fxgl.dsl.FXGL.getGameController;

/**
 * Displays the high score leaderboard
 */
public class HighScoreMenu extends FXGLMenu {

    private Font titleFont;
    private Font contentFont;

    public HighScoreMenu() {
        super(MenuType.MAIN_MENU);

        // Load fonts
        try {
            titleFont = Font.loadFont(
                getClass().getResourceAsStream("/assets/fonts/Daydream.ttf"), 48
            );
            contentFont = Font.loadFont(
                getClass().getResourceAsStream("/assets/fonts/Daydream.ttf"), 20
            );
        } catch (Exception e) {
            titleFont = Font.font("Arial", 48);
            contentFont = Font.font("Arial", 20);
        }

        createMenu();
    }

    private void createMenu() {
        // Background
        Rectangle background = new Rectangle(GameConstants.WINDOW_WIDTH, GameConstants.WINDOW_HEIGHT);
        background.setFill(Color.rgb(20, 15, 10));

        // Decorative background panel
        Rectangle panel = new Rectangle(700, 650);
        panel.setFill(Color.rgb(40, 30, 20, 0.9));
        panel.setStroke(Color.rgb(200, 150, 100));
        panel.setStrokeWidth(4);
        panel.setArcWidth(20);
        panel.setArcHeight(20);

        // Title
        Text title = new Text("HIGH SCORES");
        title.setFont(titleFont);
        title.setFill(Color.GOLD);

        // Load and display high scores
        List<HighScore> highScores = HighScoreManager.loadHighScores();

        VBox scoresBox = new VBox(10);
        scoresBox.setAlignment(Pos.CENTER);

        if (highScores.isEmpty()) {
            Text noScores = new Text("No high scores yet!");
            noScores.setFont(contentFont);
            noScores.setFill(Color.LIGHTGRAY);
            scoresBox.getChildren().add(noScores);
        } else {
            // Header
            Text header = new Text(String.format("%-5s %-20s %-10s", "RANK", "NAME", "SCORE"));
            header.setFont(Font.font(contentFont.getFamily(), 18));
            header.setFill(Color.LIGHTBLUE);
            scoresBox.getChildren().add(header);

            // Add separator
            Rectangle separator = new Rectangle(600, 2);
            separator.setFill(Color.rgb(200, 150, 100));
            scoresBox.getChildren().add(separator);

            // Display each score
            int limit = Math.min(5, highScores.size());
            for (int i = 0; i < limit; i++) {
                HighScore hs = highScores.get(i);

                // Create rank text with medal emoji for top 3
                String rankStr;
                Color rankColor;
                if (i == 0) {
                    rankStr = "🥇 1st";
                    rankColor = Color.GOLD;
                } else if (i == 1) {
                    rankStr = "🥈 2nd";
                    rankColor = Color.SILVER;
                } else if (i == 2) {
                    rankStr = "🥉 3rd";
                    rankColor = Color.rgb(205, 127, 50); // Bronze
                } else {
                    rankStr = String.format("%d.", i + 1);
                    rankColor = Color.WHITE;
                }

                // Truncate name if too long
                String displayName = hs.getPlayerName();
                if (displayName.length() > 15) {
                    displayName = displayName.substring(0, 12) + "...";
                }

                Text scoreEntry = new Text(String.format("%-8s %-20s %,d",
                    rankStr, displayName, hs.getScore()));
                scoreEntry.setFont(contentFont);
                scoreEntry.setFill(rankColor);

                scoresBox.getChildren().add(scoreEntry);
            }
        }

        // Back button
        Button backButton = createStyledButton("BACK TO MENU");
        backButton.setOnAction(e -> {
            SoundManager.playClickSound();
            getGameController().gotoMainMenu();
        });

        // Layout
        VBox layout = new VBox(30);
        layout.setAlignment(Pos.CENTER);
        layout.getChildren().addAll(title, scoresBox, backButton);

        StackPane root = new StackPane();
        root.getChildren().addAll(background, panel, layout);

        getContentRoot().getChildren().add(root);
    }

    private Button createStyledButton(String text) {
        Button button = new Button(text);
        button.setFont(Font.font(contentFont.getFamily(), 24));
        button.setStyle(
            "-fx-background-color: rgba(80, 60, 40, 0.9); " +
            "-fx-text-fill: white; " +
            "-fx-border-color: #C89664; " +
            "-fx-border-width: 2; " +
            "-fx-border-radius: 10; " +
            "-fx-background-radius: 10; " +
            "-fx-padding: 15 30 15 30; " +
            "-fx-cursor: hand;"
        );

        button.setOnMouseEntered(e -> {
            button.setStyle(
                "-fx-background-color: rgba(120, 90, 60, 0.9); " +
                "-fx-text-fill: gold; " +
                "-fx-border-color: gold; " +
                "-fx-border-width: 2; " +
                "-fx-border-radius: 10; " +
                "-fx-background-radius: 10; " +
                "-fx-padding: 15 30 15 30; " +
                "-fx-cursor: hand;"
            );
            SoundManager.playHoverSound();
        });

        button.setOnMouseExited(e -> {
            button.setStyle(
                "-fx-background-color: rgba(80, 60, 40, 0.9); " +
                "-fx-text-fill: white; " +
                "-fx-border-color: #C89664; " +
                "-fx-border-width: 2; " +
                "-fx-border-radius: 10; " +
                "-fx-background-radius: 10; " +
                "-fx-padding: 15 30 15 30; " +
                "-fx-cursor: hand;"
            );
        });

        return button;
    }
}
