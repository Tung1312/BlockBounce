package com.birb_birb.blockbounce.constants;

public class GameConstants {
    public static final int WINDOW_WIDTH = 1280;
    public static final int WINDOW_HEIGHT = 720;
    public static final String GAME_TITLE = "BlockBounce";
    public static final String GAME_VERSION = "(beta 0.2)";

    // Playable area offsets
    public static final double OFFSET_TOP = 20;
    public static final double OFFSET_BOTTOM = 20;
    public static final double OFFSET_LEFT = 20;
    public static final double OFFSET_RIGHT = 80;

    public static final double PLAYABLE_WIDTH = WINDOW_WIDTH - OFFSET_LEFT - OFFSET_RIGHT;
    public static final double PLAYABLE_HEIGHT = WINDOW_HEIGHT - OFFSET_TOP - OFFSET_BOTTOM;

    public static final String MAIN_MENU_BACKGROUND = "/assets/textures/menus/main_menu_background.png";
    public static final String STORY_MODE_BACKGROUND = "/assets/textures/menus/story_mode_template.png";
    public static final String SCORE_MODE_BACKGROUND = "/assets/textures/menus/score_mode_template.png";
    public static final String VERSUS_MODE_BACKGROUND = "/assets/textures/menus/versus_mode_template.png";
    public static final String HOW_TO_PLAY = "/assets/textures/menus/how_to_play.png";

    public static final String BRICK_TEXTURE = "/entities/brick.png";
    public static final String PADDLE_TEXTURE = "/entities/paddle.png";
    public static final String BALL_TEXTURE = "/entities/ball.png";
    public static final String BACKGROUND_TEXTURE = "/entities/background.png";

    public static final String FONT_PATH = "/assets/fonts/Daydream.ttf";
    public static final int DEFAULT_FONT_SIZE = 18;
    public static final String FALLBACK_FONT = null;

    public static final String CURSOR_PATH = "/assets/textures/ui/cursor.png";

    public static final String STORY_MODE_FRAME = "ui/story_mode_frame.png";
    public static final String SCORE_MODE_FRAME = "ui/score_mode_frame.png";
    public static final String VERSUS_MODE_FRAME = "ui/versus_mode_frame.png";

    public static final double BUTTON_WIDTH = 340;
    public static final double BUTTON_HEIGHT = 50;
    public static final double BUTTON_SPACING = 70;

    public static final String SOUND_HOVER = "hover.wav";
    public static final String SOUND_CLICK = "click.wav";

    public static final double BRICK_SIZE = 48;
    public static final double BALL_SIZE = 24;
    public static final double PADDLE_WIDTH = 172;
    public static final double PADDLE_HEIGHT = 32;

    private GameConstants() {
    }
}
