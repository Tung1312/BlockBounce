package com.birb_birb.blockbounce.constants;

public class GameConstants {
    public static final int WINDOW_WIDTH = 1280;
    public static final int WINDOW_HEIGHT = 720;
    public static final String GAME_TITLE = "BlockBounce";
    public static final String GAME_VERSION = "(beta 0.1)";

    public static final String MAIN_MENU_BACKGROUND = "/assets/textures/menus/main_menu_background.png";
    public static final String STORY_MODE_BACKGROUND = "/assets/textures/menus/story_mode_template.png";
    public static final String SCORE_MODE_BACKGROUND = "/assets/textures/menus/score_mode_template.png";
    public static final String VERSUS_MODE_BACKGROUND = "/assets/textures/menus/versus_mode_template.png";
    public static final String HOW_TO_PLAY = "/assets/textures/menus/how_to_play.png";

    public static final String BRICK_TEXTURE = "/Entities/blakblk.png";
    public static final String PADDLE_TEXTURE = "/Entities/padmed.png";
    public static final String BALL_TEXTURE = "/Entities/ball1.png";
    public static final String BACKGROUND_TEXTURE = "/Entities/space.png";


    public static final String FONT_PATH = "resources/assets/fonts/Daydream.ttf";
    public static final int DEFAULT_FONT_SIZE = 16;
    public static final String FALLBACK_FONT = null;

    public static final String CURSOR_PATH = "/assets/textures/ui/cursor.png";

    public static final double BUTTON_WIDTH = 340;
    public static final double BUTTON_HEIGHT = 50;
    public static final double BUTTON_SPACING = 70;

    public static final String SOUND_HOVER = "hover.wav";
    public static final String SOUND_CLICK = "click.wav";


    public static final double BRICK_WIDTH = WINDOW_WIDTH / 10.0;
    public static final double BRICK_HEIGHT = 50;
    public static final double BALL_SIZE = 24;
    public static final double PADDLE_WIDTH = 180;
    public static final double PADDLE_HEIGHT = 30;

    private GameConstants() {
    }
}
