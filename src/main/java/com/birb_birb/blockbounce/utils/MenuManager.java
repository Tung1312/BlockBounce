package com.birb_birb.blockbounce.utils;

import com.almasb.fxgl.app.scene.FXGLMenu;
import com.almasb.fxgl.app.scene.MenuType;
import com.birb_birb.blockbounce.constants.GameConstants;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;

import java.util.Objects;

/**
 * Base class for all game menus.
 */
public abstract class MenuManager extends FXGLMenu {

    protected Pane root;

    public MenuManager(MenuType menuType) {
        super(menuType);

        if (!SoundManager.isInitialized()) {
            SoundManager.initialize();
        }

        // Initialize root pane
        root = new Pane();
        root.setFocusTraversable(true);
        root.requestFocus();
        // Apply custom cursor to menu roots
        CursorManager.apply(root);
        CursorManager.apply(getContentRoot());

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

        final Font customFont = FontManager.getMainFont();
        button.setFont(customFont);
        button.getStyleClass().clear();
        button.getStyleClass().add("game-menu-button");

        String fontFamily = customFont != null ? customFont.getFamily() : "Arial";
        double fontSize = customFont != null ? customFont.getSize() : 16;
        button.setStyle("-fx-font-family: '" + fontFamily + "'; -fx-font-size: " + fontSize + "px;");

        button.setOnMouseEntered(e -> {
            if (!button.isPressed()) {
                SoundManager.playHoverSound();
            }
        });

        button.setOnMousePressed(e -> {
            SoundManager.playClickSound();
        });

        return button;
    }

    public static Rectangle createDimmingOverlay() {
        return createDimmingOverlay(0.7);
    }

    public static Rectangle createDimmingOverlay(double opacity) {
        Rectangle overlay = new Rectangle(GameConstants.WINDOW_WIDTH, GameConstants.WINDOW_HEIGHT);
        overlay.setFill(Color.rgb(0, 0, 0, opacity));
        return overlay;
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
