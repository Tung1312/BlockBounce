package com.birb_birb.blockbounce.constants;

public enum GameMode {
    STORY,
    VERSUS,
    ENDLESS;

    private static GameMode currentGameMode = STORY;

    public static void setCurrentGameMode(GameMode mode) {
        currentGameMode = mode;
    }

    public static GameMode getCurrentGameMode() {
        return currentGameMode;
    }
}