package com.birb_birb.blockbounce.ui.menus;

import com.almasb.fxgl.app.scene.MenuType;
import com.birb_birb.blockbounce.constants.GameConstants;
import com.birb_birb.blockbounce.constants.GameMode;
import com.birb_birb.blockbounce.utils.ButtonManager;
import com.birb_birb.blockbounce.utils.FontManager;
import com.birb_birb.blockbounce.utils.saveload.SaveGameManager;
import com.birb_birb.blockbounce.utils.MenuManager;
import com.birb_birb.blockbounce.utils.SoundManager;
import com.birb_birb.blockbounce.utils.highscore.HighScore;
import com.birb_birb.blockbounce.utils.highscore.HighScoreManager;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.OverrunStyle;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

import java.util.List;
import java.util.stream.IntStream;

/**
 * Score Mode submenu for selecting New Game or Load Game
 */
public class ScoreModeMenu extends MenuManager {

    public ScoreModeMenu() {
        super(MenuType.MAIN_MENU);
    }

    @Override
    protected String getBackgroundImagePath() {
        return GameConstants.SCORE_MODE_BACKGROUND;
    }

    @Override
    protected void setupContent() {

        // Calculate button positions
        double buttonX = getAppWidth() / 2.0 + 148;
        double startY = getAppHeight() * 0.284;

        // New Game button
        Button newGameButton = createMenuButton("New Game");
        newGameButton.setLayoutX(buttonX);
        newGameButton.setLayoutY(startY);
        newGameButton.setOnAction(e -> {
            SoundManager.playClickSound();
            GameMode.setCurrentGameMode(GameMode.ENDLESS);
            fireNewGame();
        });

        // Load Game button
        Button loadGameButton = createMenuButton("Load Game");
        loadGameButton.setLayoutX(buttonX);
        loadGameButton.setLayoutY(startY + 293);

        // Check if any save exists
        boolean hasSave = false;
        for (int slot = 1; slot <= 3; slot++) {
            if (SaveGameManager.hasScoreSaveData(slot)) {
                hasSave = true;
                break;
            }
        }

        if (!hasSave) {
            loadGameButton.setDisable(true);
            loadGameButton.setOpacity(0.5);
        } else {
            loadGameButton.setOnAction(e -> {
                SoundManager.playClickSound();
                GameMode.setCurrentGameMode(GameMode.ENDLESS);
                GameMode.setShouldLoadSave(true);
                fireNewGame();
            });
        }

        VBox buttonPanel = createButtonPanel();
        root.getChildren().add(buttonPanel);

        // Highscores list
        VBox highscoresBox = buildHighscoresBox();
        highscoresBox.setLayoutX(92);
        highscoresBox.setLayoutY(170);
        root.getChildren().add(highscoresBox);

        root.getChildren().addAll(newGameButton, loadGameButton);
    }

    private VBox buildHighscoresBox() {
        // Load, sort (desc) and clamp to top 5
        List<HighScore> scores = HighScoreManager.loadHighScores();
        scores.sort(null); // HighScore implements Comparable descending by score
        int limit = Math.min(5, scores.size());

        // Prepare fonts
        Font base = FontManager.getSecondFont();
        String family = base != null ? base.getFamily() : "Arial";
        Font headerFont = Font.font(family, 28);
        Font rowFont = Font.font(family, 26);

        VBox box = new VBox(12);
        box.setAlignment(Pos.TOP_LEFT);
        box.setPadding(new Insets(4, 0, 0, 0));

        // Grid with 3 columns: Rank | Name | Score
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);

        ColumnConstraints c0 = new ColumnConstraints();
        c0.setPrefWidth(30); // rank
        ColumnConstraints c1 = new ColumnConstraints();
        c1.setMinWidth(250);
        c1.setPrefWidth(250); // name column fixed width
        c1.setMaxWidth(250);
        c1.setHgrow(Priority.NEVER);
        ColumnConstraints c2 = new ColumnConstraints();
        c2.setPrefWidth(100); // score
        c2.setHalignment(HPos.LEFT); // align score to left
        grid.getColumnConstraints().addAll(c0, c1, c2);

        // Header row
        Text hRank = new Text("#");
        hRank.setFont(headerFont);
        hRank.setFill(Color.rgb(120, 80, 60));
        Text hName = new Text("NAME");
        hName.setFont(headerFont);
        hName.setFill(Color.rgb(120, 80, 60));
        Text hScore = new Text("SCORE");
        hScore.setFont(headerFont);
        hScore.setFill(Color.rgb(120, 80, 60));
        grid.add(hRank, 0, 0);
        grid.add(hName, 1, 0);
        grid.add(hScore, 2, 0);
        GridPane.setValignment(hRank, VPos.BASELINE);
        GridPane.setValignment(hName, VPos.BASELINE);
        GridPane.setValignment(hScore, VPos.BASELINE);

        // Rows
        IntStream.range(0, limit).forEach(i -> {
            HighScore hs = scores.get(i);
            String name = hs.getPlayerName();

            Text rRank = new Text((i + 1) + ".");
            rRank.setFont(rowFont);
            rRank.setFill(GameConstants.FONT_COLOR);

            // Name as Label with ellipsis when exceeding 250px column width
            Label rName = new Label(name);
            rName.setFont(rowFont);
            rName.setStyle("-fx-text-fill: rgb(62, 32, 31);");
            rName.setTextOverrun(OverrunStyle.ELLIPSIS);
            rName.setAlignment(Pos.CENTER_LEFT);
            rName.setMinWidth(250);
            rName.setPrefWidth(250);
            rName.setMaxWidth(250);
            rName.setPadding(Insets.EMPTY);

            Text rScore = new Text(String.format("%,d", hs.getScore()));
            rScore.setFont(rowFont);
            rScore.setFill(GameConstants.FONT_COLOR);

            int row = i + 1;
            grid.add(rRank, 0, row);
            grid.add(rName, 1, row);
            grid.add(rScore, 2, row);
            GridPane.setValignment(rRank, VPos.BASELINE);
            GridPane.setValignment(rName, VPos.BASELINE);
            GridPane.setValignment(rScore, VPos.BASELINE);
        });

        box.getChildren().add(grid);
        return box;
    }

    public static VBox createButtonPanel() {
        Button settingButton = ButtonManager.createButton();
        settingButton.setOnAction(e -> ButtonManager.openSettings());
        Button homeButton = ButtonManager.createButton();
        homeButton.setOnAction(e -> ButtonManager.exitToMainMenu());

        Region spacer = new Region();
        spacer.setPrefHeight(601);
        spacer.setMinHeight(601);
        spacer.setMaxHeight(601);

        VBox buttonPanel = new VBox();
        buttonPanel.setAlignment(Pos.TOP_RIGHT);
        buttonPanel.setPadding(new Insets(6.5, 0, 0, GameConstants.WINDOW_WIDTH - 60)); // top, right, bottom, left
        buttonPanel.getChildren().addAll(settingButton, spacer, homeButton);

        return buttonPanel;
    }
}
