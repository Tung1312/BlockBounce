package com.birb_birb.blockbounce.constants;

public enum GameMode {
    STORY,
    VERSUS,
    ENDLESS;

    private static GameMode currentGameMode = STORY;
    private static boolean shouldLoadSave = false;

    public static void setCurrentGameMode(GameMode mode) {
        currentGameMode = mode;
    }

    public static GameMode getCurrentGameMode() {
        return currentGameMode;
    }

    public static void setShouldLoadSave(boolean load) {
        shouldLoadSave = load;
    }

    public static boolean shouldLoadSave() {
        return shouldLoadSave;
    }
}