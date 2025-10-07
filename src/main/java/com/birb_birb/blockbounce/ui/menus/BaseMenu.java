package com.birb_birb.blockbounce.ui.menus;

import com.almasb.fxgl.app.scene.FXGLMenu;
import com.almasb.fxgl.app.scene.MenuType;
import com.birb_birb.blockbounce.constants.GameConstants;
import com.birb_birb.blockbounce.utils.FontManager;
import com.birb_birb.blockbounce.utils.SoundManager;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;

import java.util.Objects;

/**
 * Base class for all game menus.
 */
public abstract class BaseMenu extends FXGLMenu {

    protected Pane root;

    public BaseMenu(MenuType menuType) {
        super(menuType);

        if (!SoundManager.isInitialized()) {
            SoundManager.initialize();
        }

        // Initialize root pane
        root = new Pane();
        root.setFocusTraversable(true);
        root.requestFocus();

        // Set up the menu
        setupBackground();
        setupContent();

        getContentRoot().getChildren().add(root);
    }

    protected abstract void setupContent();

    protected abstract String getBackgroundImagePath();

    /**
     * Set up the background image for menu.
     */
    private void setupBackground() {
        try {
            String imagePath = getBackgroundImagePath();
            if (imagePath != null) {
                Image backgroundImage = new Image(Objects.requireNonNull(getClass().getResourceAsStream(imagePath)));
                ImageView backgroundView = new ImageView(backgroundImage);
                backgroundView.setFitWidth(getAppWidth());
                backgroundView.setFitHeight(getAppHeight());
                backgroundView.setPreserveRatio(false);
                root.getChildren().add(backgroundView);
            }
        } catch (Exception e) {
            // Fallback
            root.setStyle("-fx-background-color: #2c3e50;");
            System.out.println("Failed to load background image: " + e.getMessage());
        }
    }

    /**
     * Create a styled menu button
     */
    protected Button createMenuButton(String text) {
        Button button = new Button(text);
        button.setPrefWidth(GameConstants.BUTTON_WIDTH);
        button.setPrefHeight(GameConstants.BUTTON_HEIGHT);

        final javafx.scene.text.Font customFont = FontManager.getCustomFont();
        button.setFont(customFont);
        button.getStyleClass().clear();

        String fontFamily = customFont != null ? customFont.getFamily() : "Arial";

        // Declare custom button styles:
        String baseStyle = "-fx-font-family: '" + fontFamily + "';" +
                "-fx-font-size: " + (customFont != null ? customFont.getSize() : 16) + "px;" +
                "-fx-background-color: rgba(54,222,227,0.55);" +
                "-fx-background-radius: 6;" +
                "-fx-border-radius: 6;" +
                "-fx-text-fill: rgb(62, 32, 31);" +
                "-fx-font-weight: bold;" +
                "-fx-alignment: center;" +
                "-fx-text-alignment: center;";
        String hoverStyle = "-fx-font-family: '" + fontFamily + "';" +
                "-fx-font-size: " + (customFont != null ? customFont.getSize() : 16) + "px;" +
                "-fx-background-color: rgba(54,222,227,0.55);" +
                "-fx-background-radius: 6;" +
                "-fx-border-radius: 6;" +
                "-fx-text-fill: white;" +
                "-fx-font-weight: bold;" +
                "-fx-alignment: center;" +
                "-fx-text-alignment: center;" +
                "-fx-scale-x: 1.05;" +  // Scale when
                "-fx-scale-y: 1.05;";   // hovered
        String pressStyle = "-fx-font-family: '" + fontFamily + "';" +
                "-fx-font-size: " + (customFont != null ? customFont.getSize() : 16) + "px;" +
                "-fx-background-color: rgba(54,222,227,0.55);" +
                "-fx-background-radius: 6;" +
                "-fx-border-radius: 6;" +
                "-fx-text-fill: white;" +
                "-fx-font-weight: bold;" +
                "-fx-alignment: center;" +
                "-fx-text-alignment: center;";

        button.setStyle(baseStyle);

        button.setOnMouseEntered(e -> {
            if (!button.isPressed()) {
                SoundManager.playHoverSound();
                button.setStyle(hoverStyle);
                button.setFont(customFont);
            }
        });

        button.setOnMouseExited(e -> {
            if (!button.isPressed()) {
                button.setStyle(baseStyle);
                button.setFont(customFont);
            }
        });

        button.setOnMousePressed(e -> {
            SoundManager.playClickSound();
            button.setStyle(pressStyle);
            button.setFont(customFont);
        });

        button.setOnMouseReleased(e -> {
            if (button.isHover()) {
                button.setStyle(hoverStyle);
            } else {
                button.setStyle(baseStyle);
            }
            button.setFont(customFont);
        });

        return button;
    }

    /**
     * Button positioning
     */
    protected double getButtonX() {
        return getAppWidth() / 2.0 - (GameConstants.BUTTON_WIDTH / 2.0);
    }

    protected double getStartY() {
        return getAppHeight() * 0.445;
    }
}

