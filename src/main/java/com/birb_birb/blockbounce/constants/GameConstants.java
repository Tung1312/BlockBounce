package com.birb_birb.blockbounce.constants;

import javafx.scene.paint.Paint;

import static javafx.scene.paint.Color.rgb;

public class GameConstants {
    public static final int WINDOW_WIDTH = 1280;
    public static final int WINDOW_HEIGHT = 720;
    public static final int DIALOG_WIDTH = 400;
    public static final int DIALOG_HEIGHT = 220;
    public static final String GAME_TITLE = "BlockBounce";
    public static final String GAME_VERSION = "(beta 0.3)";

    public static final int OFFSET_TOP = 20;
    public static final int OFFSET_BOTTOM = 20;
    public static final int OFFSET_MIDDLE = 90;
    public static final double OFFSET_LEFT = 20;
    public static final double OFFSET_RIGHT = 80;
    public static final double PLAYABLE_WIDTH = WINDOW_WIDTH - OFFSET_LEFT - OFFSET_RIGHT;
    public static final double PLAYABLE_HEIGHT = WINDOW_HEIGHT - OFFSET_TOP - OFFSET_BOTTOM;
    public static final double VERSUS_PLAYABLE_WIDTH =  WINDOW_WIDTH - (OFFSET_LEFT * 2) - OFFSET_MIDDLE;

    public static final String MAIN_MENU_BACKGROUND = "/assets/textures/menus/main_menu_background.png";
    public static final String SCORE_MODE_BACKGROUND = "/assets/textures/menus/score_mode_background.png";
    public static final String VERSUS_MODE_BACKGROUND = "/assets/textures/menus/versus_mode_template.png";
    // public static final String HOW_TO_PLAY = "/assets/textures/menus/how_to_play.png";

    public static final String STORY_MODE_BACKGROUND = "/assets/textures/menus/story_mode_background.png";
    public static final String STORY_MODE_LEVELS = "/assets/textures/menus/levels/unlocked_level_8.png";
    public static final String SELECT_1 = "/assets/textures/menus/levels/select_level_1.png";
    public static final String SELECT_2 = "/assets/textures/menus/levels/select_level_2.png";
    public static final String SELECT_3 = "/assets/textures/menus/levels/select_level_3.png";
    public static final String SELECT_4 = "/assets/textures/menus/levels/select_level_4.png";
    public static final String SELECT_5 = "/assets/textures/menus/levels/select_level_5.png";
    public static final String SELECT_6 = "/assets/textures/menus/levels/select_level_6.png";
    public static final String SELECT_7 = "/assets/textures/menus/levels/select_level_7.png";
    public static final String SELECT_8 = "/assets/textures/menus/levels/select_level_8.png";

    public static final String WOOD_TEXTURE = "/entities/blocks/wood.png";
    public static final String STONE_TEXTURE = "/entities/blocks/stone.png";
    public static final String STONE_CRACKED_TEXTURE = "/entities/blocks/stone_cracked.png";
    public static final String NETHERACK_TEXTURE = "/entities/blocks/netherack.png";
    public static final String NETHERBRICK_TEXTURE = "/entities/blocks/netherbrick.png";
    public static final String NETHERBRICK_CRACKED_TEXTURE = "/entities/blocks/netherbrick_cracked.png";
    public static final String ENDSTONE_TEXTURE = "/entities/blocks/endstone.png";
    public static final String ENDSTONE_CRACKED_TEXTURE = "/entities/blocks/endstone_cracked.png";
    public static final String OBSIDIAN_TEXTURE = "/entities/blocks/obsidian.png";
    public static final String PADDLE_TEXTURE = "/entities/paddle.png";
    public static final String BALL_TEXTURE = "/entities/ball.png";
    public static final String BACKGROUND_TEXTURE = "/entities/background.png";
    public static final String POWERUP_MULTIPLY_TEXTURE = "/entities/powerup/multiply.png";
    public static final String POWERUP_SHRINK_TEXTURE = "/entities/powerup/shrink.png";
    public static final String POWERUP_SPEED_TEXTURE = "/entities/powerup/speed.png";
    public static final String POWERUP_DOUBLE_TEXTURE = "/entities/powerup/double.png";
    public static final String POWERUP_LIFE_TEXTURE = "/entities/powerup/life.png";

    public static final String MAIN_FONT_PATH = "/assets/fonts/Daydream.ttf";
    public static final String SECONDARY_FONT_PATH = "/assets/fonts/MinecraftTen.ttf";
    public static final Paint FONT_COLOR = rgb(62, 32, 31);
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
    public static final String SOUND_HIT = "hit.wav";
    public static final String SOUND_BREAK = "break.wav";
    public static final String SOUND_DEATH = "death.wav";
    public static final String SOUND_LOOSE = "loose.wav";
    public static final String SOUND_COMPLETE = "complete.wav";
    public static final String SOUND_ORB = "orb.wav";
    public static final String SOUND_ANVIL = "anvil.wav";

    public static final double BRICK_SIZE = 48;
    public static final double BALL_SIZE = 24;
    public static final double PADDLE_WIDTH = 172;
    public static final double PADDLE_HEIGHT = 32;

    public static final double BASE_SPEED = 5.0;
    public static final double POWER_UP_FALL_SPEED = 2.0;

    public static final double MAX_VERTICAL_ANGLE = 75.0;
    public static final double MIN_VERTICAL_ANGLE = 15.0;
    public static final double MAX_SPEED_MULTIPLIER = 1.2;
    public static final double MAX_HIT_OFFSET = 0.75;
    public static final double BOUNCE_ANGLE_RANGE = Math.PI / 3;
    public static final double MIN_UPWARD_VELOCITY = -2.0;
    public static final double COLLISION_EPSILON = 0.01;

    private GameConstants() {
    }
}
