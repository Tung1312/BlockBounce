package com.birb_birb.blockbounce.examples;

import com.birb_birb.blockbounce.constants.GameMode;
import com.birb_birb.blockbounce.core.gamemode.story.StoryModeGame;
import com.birb_birb.blockbounce.saveload.SaveGameManager;

/**
 * Example code demonstrating how to use the Save/Load system
 * This is for reference only - integrate into your actual UI/Menu system
 */
public class SaveLoadExample {

    /**
     * Example: Main menu with Continue option
     */
    public static void mainMenuExample() {
        System.out.println("=== MAIN MENU ===");
        System.out.println("1. New Game");

        // Check if any save exists
        boolean hasSave = false;
        for (int slot = 1; slot <= 3; slot++) {
            if (SaveGameManager.hasSaveData(slot)) {
                hasSave = true;
                break;
            }
        }

        if (hasSave) {
            System.out.println("2. Continue");
        }

        System.out.println("3. Load Game");
        System.out.println("4. Exit");
    }

    /**
     * Example: Start new game
     */
    public static void startNewGame() {
        GameMode.setCurrentGameMode(GameMode.STORY);
        StoryModeGame.startGame();
        System.out.println("New game started!");
    }

    /**
     * Example: Continue from most recent save
     */
    public static void continueGame() {
        // Find most recent save
        int mostRecentSlot = findMostRecentSave();

        if (mostRecentSlot > 0) {
            GameMode.setCurrentGameMode(GameMode.STORY);
            StoryModeGame.startFromSave(mostRecentSlot);
            System.out.println("Continued from slot " + mostRecentSlot);
        } else {
            System.out.println("No save found!");
        }
    }

    /**
     * Example: Show load game menu with all save slots
     */
    public static void showLoadGameMenu() {
        System.out.println("\n=== LOAD GAME ===");

        for (int slot = 1; slot <= 3; slot++) {
            System.out.print("Slot " + slot + ": ");

            if (SaveGameManager.hasSaveData(slot)) {
                SaveGameManager.SavePreview preview = SaveGameManager.getSavePreview(slot);
                if (preview != null) {
                    System.out.println(preview.toString());
                } else {
                    System.out.println("Error loading preview");
                }
            } else {
                System.out.println("Empty");
            }
        }
    }

    /**
     * Example: Load from specific slot
     */
    public static void loadFromSlot(int slot) {
        if (SaveGameManager.hasSaveData(slot)) {
            GameMode.setCurrentGameMode(GameMode.STORY);
            StoryModeGame.startFromSave(slot);
            System.out.println("Loaded from slot " + slot);
        } else {
            System.out.println("Slot " + slot + " is empty!");
        }
    }

    /**
     * Example: Save game menu
     */
    public static void showSaveGameMenu() {
        System.out.println("\n=== SAVE GAME ===");

        for (int slot = 1; slot <= 3; slot++) {
            System.out.print("Slot " + slot + ": ");

            if (SaveGameManager.hasSaveData(slot)) {
                SaveGameManager.SavePreview preview = SaveGameManager.getSavePreview(slot);
                if (preview != null) {
                    System.out.println(preview.toString() + " [WILL OVERWRITE]");
                }
            } else {
                System.out.println("Empty");
            }
        }
    }

    /**
     * Example: Save to specific slot with confirmation
     */
    public static void saveToSlot(int slot) {
        // Check if slot already has data
        if (SaveGameManager.hasSaveData(slot)) {
            System.out.println("Slot " + slot + " already has data. Overwrite? (y/n)");
            // In real implementation, wait for user input
            // For now, we'll just proceed
        }

        // Save the game
        StoryModeGame instance = StoryModeGame.getInstance();
        boolean success = instance.saveGame(slot);

        if (success) {
            System.out.println("Game saved to slot " + slot);
        } else {
            System.out.println("Failed to save game");
        }
    }

    /**
     * Example: Delete save slot with confirmation
     */
    public static void deleteSlot(int slot) {
        if (!SaveGameManager.hasSaveData(slot)) {
            System.out.println("Slot " + slot + " is empty!");
            return;
        }

        System.out.println("Are you sure you want to delete slot " + slot + "? (y/n)");
        // In real implementation, wait for user input

        boolean success = SaveGameManager.deleteSave(slot);
        if (success) {
            System.out.println("Slot " + slot + " deleted");
        } else {
            System.out.println("Failed to delete slot " + slot);
        }
    }

    /**
     * Helper: Find most recent save slot
     */
    private static int findMostRecentSave() {
        SaveGameManager.SavePreview mostRecent = null;
        int mostRecentSlot = 0;

        for (int slot = 1; slot <= 3; slot++) {
            if (SaveGameManager.hasSaveData(slot)) {
                SaveGameManager.SavePreview preview = SaveGameManager.getSavePreview(slot);
                if (preview != null) {
                    if (mostRecent == null) {
                        mostRecent = preview;
                        mostRecentSlot = slot;
                    }
                    // In a real implementation, compare dates to find most recent
                    // For now, just use the first one found
                }
            }
        }

        return mostRecentSlot;
    }

    /**
     * Example: Quick save (F5)
     */
    public static void quickSave() {
        // Quick save always uses slot 1
        StoryModeGame instance = StoryModeGame.getInstance();
        instance.saveGame(1);
        System.out.println("Quick saved!");
    }

    /**
     * Example: Quick load (F9)
     */
    public static void quickLoad() {
        // Quick load always uses slot 1
        if (SaveGameManager.hasSaveData(1)) {
            StoryModeGame instance = StoryModeGame.getInstance();
            instance.loadGame(1);
            System.out.println("Quick loaded!");
        } else {
            System.out.println("No quick save found!");
        }
    }

    /**
     * Example: Pause menu with save option
     */
    public static void pauseMenuExample() {
        System.out.println("\n=== PAUSE MENU ===");
        System.out.println("1. Resume");
        System.out.println("2. Save Game");
        System.out.println("3. Load Game");
        System.out.println("4. Save & Quit");
        System.out.println("5. Quit without Saving");
    }

    /**
     * Example: Save and quit
     */
    public static void saveAndQuit() {
        StoryModeGame instance = StoryModeGame.getInstance();
        int currentSlot = instance.getCurrentSaveSlot();

        boolean success = instance.saveGame(currentSlot);
        if (success) {
            System.out.println("Game saved. Returning to main menu...");
            // Return to main menu
        } else {
            System.out.println("Failed to save game!");
        }
    }

    /**
     * Example usage in FXGL game
     */
    public static void fxglIntegrationExample() {
        // This would be in your BlockBounceApp or menu system

        // On game start - check for saves and show continue option
        /*
        @Override
        protected void initGame() {
            if (hasContinueOption) {
                continueGame();
            } else {
                startNewGame();
            }
        }
        */

        // Add keyboard shortcuts
        /*
        @Override
        protected void initInput() {
            // F5 - Quick Save
            onKeyDown(KeyCode.F5, () -> {
                StoryModeGame.getInstance().saveGame(1);
                showNotification("Game Saved!");
            });

            // F9 - Quick Load
            onKeyDown(KeyCode.F9, () -> {
                if (SaveGameManager.hasSaveData(1)) {
                    StoryModeGame.getInstance().loadGame(1);
                    showNotification("Game Loaded!");
                } else {
                    showNotification("No save found!");
                }
            });
        }
        */
    }
}

