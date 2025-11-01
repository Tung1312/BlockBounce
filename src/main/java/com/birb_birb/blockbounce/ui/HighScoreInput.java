package com.birb_birb.blockbounce.ui;

import com.almasb.fxgl.scene.SubScene;
import com.birb_birb.blockbounce.constants.GameConstants;
import com.birb_birb.blockbounce.utils.FontManager;
import com.birb_birb.blockbounce.utils.MenuManager;
import com.birb_birb.blockbounce.utils.SoundManager;
import javafx.geometry.Pos;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

import java.util.function.Consumer;

public class HighScoreInput extends SubScene {

    private final TextField nameInput;
    private final Consumer<String> onSubmit;
    private final Runnable onCancel;

    public HighScoreInput(int score, int rank, Consumer<String> onSubmit, Runnable onCancel) {
        this.onSubmit = onSubmit;
        this.onCancel = onCancel;

        // Background dimming
        Rectangle overlay = MenuManager.createDimmingOverlay(0);

        // Create dialog box
        Rectangle dialogBox = new Rectangle(GameConstants.DIALOG_WIDTH, GameConstants.DIALOG_HEIGHT);
        dialogBox.setFill(Color.rgb(40, 30, 20));
        dialogBox.setStroke(Color.rgb(200, 150, 100));
        dialogBox.setStrokeWidth(3);
        dialogBox.setArcWidth(20);
        dialogBox.setArcHeight(20);

        // Load custom font
        Font custom = FontManager.getSecondFont();
        Font title = Font.font(custom.getFamily(), 24);
        Font small = Font.font(custom.getFamily(), 14);

        // Create title text
        Text titleText = new Text("YOUR SCORE: " + score);
        titleText.setFont(custom);
        titleText.setFill(Color.GOLD);

        // Create rank text
        Text rankText = new Text("Rank #" + rank);
        rankText.setFont(title);
        rankText.setFill(Color.LIGHTGREEN);

        // Create instruction text
        Text instructionText = new Text("Enter your name:");
        instructionText.setFont(small);
        instructionText.setFill(Color.LIGHTGRAY);

        // Create text input field
        nameInput = new TextField();
        nameInput.setPromptText("Player Name");
        nameInput.setMaxWidth(300);
        nameInput.setFont(Font.font(custom.getFamily(), 20));
        nameInput.setStyle(
            "-fx-background-color: rgba(60, 50, 40, 0.9); " +
            "-fx-text-fill: white; " +
            "-fx-prompt-text-fill: gray; " +
            "-fx-border-color: #C89664; " +
            "-fx-border-width: 2; " +
            "-fx-border-radius: 5; " +
            "-fx-background-radius: 5; " +
            "-fx-padding: 8;"
        );

        // Limit name length
        nameInput.textProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal.length() > 20) {
                nameInput.setText(oldVal);
            }
        });

        // Create submit instruction
        Text submitText = new Text("[Press ENTER to submit]");
        submitText.setFont(Font.font(custom.getFamily(), 10));
        submitText.setFill(Color.LIGHTBLUE);

        // Layout
        VBox contentBox = new VBox(12);
        contentBox.setAlignment(Pos.CENTER);
        contentBox.getChildren().addAll(
            titleText, rankText, instructionText, nameInput, submitText
        );

        StackPane dialogPane = new StackPane();
        dialogPane.getChildren().addAll(dialogBox, contentBox);

        StackPane root = new StackPane();
        root.getChildren().addAll(overlay, dialogPane);

        getContentRoot().getChildren().add(root);

        // Handle input
        nameInput.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.ENTER) {
                submitName();
            } else if (e.getCode() == KeyCode.ESCAPE) {
                cancel();
            }
        });

        // Autofocus the text field
        nameInput.requestFocus();
    }

    private void submitName() {
        String name = nameInput.getText().trim();

        if (name.isEmpty()) {
            name = "Player";
        }

        SoundManager.playAnvilSound();

        if (onSubmit != null) {
            onSubmit.accept(name);
        }
    }

    private void cancel() {
        SoundManager.playClickSound();

        if (onCancel != null) {
            onCancel.run();
        }
    }
}
