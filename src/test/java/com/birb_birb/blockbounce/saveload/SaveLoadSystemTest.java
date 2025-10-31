package com.birb_birb.blockbounce.saveload;

import com.birb_birb.blockbounce.utils.saveload.SaveData;
import com.birb_birb.blockbounce.utils.saveload.SaveGameManager;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for Save/Load system
 */
public class SaveLoadSystemTest {

    @BeforeEach
    public void setUp() {
        // Clean up test saves before each test
        for (int i = 1; i <= 3; i++) {
            SaveGameManager.deleteSave(i);
        }
    }

    @AfterEach
    public void tearDown() {
        // Clean up test saves after each test
        for (int i = 1; i <= 3; i++) {
            SaveGameManager.deleteSave(i);
        }
    }

    @Test
    public void testCreateGameSaveData() {
        SaveData data = new SaveData();
        data.setGameMode("STORY");
        data.setCurrentLevel(5);
        data.setScore(1000);
        data.setLives(3);

        assertEquals("STORY", data.getGameMode());
        assertEquals(5, data.getCurrentLevel());
        assertEquals(1000, data.getScore());
        assertEquals(3, data.getLives());
        assertNotNull(data.getSaveDate());
    }

    @Test
    public void testSaveAndLoadGame() {
        // Create test save data
        SaveData saveData = new SaveData();
        saveData.setGameMode("STORY");
        saveData.setCurrentLevel(3);
        saveData.setScore(500);
        saveData.setLives(2);
        saveData.setPaddleX(400);
        saveData.setPaddleY(500);
        saveData.setBallX(410);
        saveData.setBallY(450);
        saveData.setBallVelocityX(2.5);
        saveData.setBallVelocityY(-2.5);
        saveData.setBallLaunched(true);

        // Save to slot 1
        boolean saveSuccess = SaveGameManager.saveGame(1, saveData);
        assertTrue(saveSuccess, "Save should succeed");

        // Verify save exists
        assertTrue(SaveGameManager.hasSaveData(1), "Save data should exist in slot 1");

        // Load from slot 1
        SaveData loadedData = SaveGameManager.loadGame(1);
        assertNotNull(loadedData, "Loaded data should not be null");

        // Verify loaded data matches saved data
        assertEquals("STORY", loadedData.getGameMode());
        assertEquals(3, loadedData.getCurrentLevel());
        assertEquals(500, loadedData.getScore());
        assertEquals(2, loadedData.getLives());
        assertEquals(400, loadedData.getPaddleX(), 0.01);
        assertEquals(500, loadedData.getPaddleY(), 0.01);
        assertEquals(410, loadedData.getBallX(), 0.01);
        assertEquals(450, loadedData.getBallY(), 0.01);
        assertEquals(2.5, loadedData.getBallVelocityX(), 0.01);
        assertEquals(-2.5, loadedData.getBallVelocityY(), 0.01);
        assertTrue(loadedData.isBallLaunched());
    }

    @Test
    public void testMultipleSaveSlots() {
        // Create different save data for each slot
        for (int slot = 1; slot <= 3; slot++) {
            SaveData data = new SaveData();
            data.setGameMode("STORY");
            data.setCurrentLevel(slot);
            data.setScore(slot * 100);
            data.setLives(slot);

            boolean success = SaveGameManager.saveGame(slot, data);
            assertTrue(success, "Save to slot " + slot + " should succeed");
        }

        // Verify all slots have data
        for (int slot = 1; slot <= 3; slot++) {
            assertTrue(SaveGameManager.hasSaveData(slot),
                "Slot " + slot + " should have save data");
        }

        // Load and verify each slot
        for (int slot = 1; slot <= 3; slot++) {
            SaveData loaded = SaveGameManager.loadGame(slot);
            assertNotNull(loaded);
            assertEquals(slot, loaded.getCurrentLevel());
            assertEquals(slot * 100, loaded.getScore());
            assertEquals(slot, loaded.getLives());
        }
    }

    @Test
    public void testDeleteSave() {
        // Create and save data
        SaveData data = new SaveData();
        data.setGameMode("STORY");
        data.setCurrentLevel(1);

        SaveGameManager.saveGame(1, data);
        assertTrue(SaveGameManager.hasSaveData(1));

        // Delete save
        boolean deleteSuccess = SaveGameManager.deleteSave(1);
        assertTrue(deleteSuccess, "Delete should succeed");

        // Verify save is gone
        assertFalse(SaveGameManager.hasSaveData(1), "Save should not exist after deletion");

        // Try to load deleted save
        SaveData loaded = SaveGameManager.loadGame(1);
        assertNull(loaded, "Loading deleted save should return null");
    }

    @Test
    public void testSavePreview() {
        // Create and save data
        SaveData data = new SaveData();
        data.setGameMode("STORY");
        data.setCurrentLevel(7);
        data.setScore(2500);
        data.setLives(3);

        SaveGameManager.saveGame(1, data);

        // Get preview
        SaveGameManager.SavePreview preview = SaveGameManager.getSavePreview(1);
        assertNotNull(preview, "Preview should not be null");
        assertEquals(1, preview.getSlot());
        assertEquals(7, preview.getLevel());
        assertEquals(2500, preview.getScore());
        assertEquals(3, preview.getLives());
        assertNotNull(preview.getSaveDate());
    }

    @Test
    public void testSavePreviewEmpty() {
        // Get preview for empty slot
        SaveGameManager.SavePreview preview = SaveGameManager.getSavePreview(1);
        assertNull(preview, "Preview for empty slot should be null");
    }

    @Test
    public void testInvalidSlotNumber() {
        SaveData data = new SaveData();

        // Test invalid slot numbers
        assertFalse(SaveGameManager.saveGame(0, data), "Slot 0 should be invalid");
        assertFalse(SaveGameManager.saveGame(4, data), "Slot 4 should be invalid");
        assertFalse(SaveGameManager.saveGame(-1, data), "Negative slot should be invalid");

        assertNull(SaveGameManager.loadGame(0), "Load from slot 0 should return null");
        assertNull(SaveGameManager.loadGame(4), "Load from slot 4 should return null");

        assertFalse(SaveGameManager.hasSaveData(0), "Slot 0 should not have data");
        assertFalse(SaveGameManager.hasSaveData(4), "Slot 4 should not have data");
    }

    @Test
    public void testBlockDataSerialization() {
        SaveData data = new SaveData();

        // Add some block data
        SaveData.BlockData block1 = new SaveData.BlockData(100, 200, "RED", 1);
        SaveData.BlockData block2 = new SaveData.BlockData(150, 250, "BLUE", 2);

        data.getBlocks().add(block1);
        data.getBlocks().add(block2);

        // Save and load
        SaveGameManager.saveGame(1, data);
        SaveData loaded = SaveGameManager.loadGame(1);

        assertNotNull(loaded);
        assertEquals(2, loaded.getBlocks().size());

        SaveData.BlockData loadedBlock1 = loaded.getBlocks().get(0);
        assertEquals(100, loadedBlock1.getX(), 0.01);
        assertEquals(200, loadedBlock1.getY(), 0.01);
        assertEquals("RED", loadedBlock1.getColor());
        assertEquals(1, loadedBlock1.getHits());

        SaveData.BlockData loadedBlock2 = loaded.getBlocks().get(1);
        assertEquals(150, loadedBlock2.getX(), 0.01);
        assertEquals(250, loadedBlock2.getY(), 0.01);
        assertEquals("BLUE", loadedBlock2.getColor());
        assertEquals(2, loadedBlock2.getHits());
    }

    @Test
    public void testOverwriteExistingSave() {
        // Save initial data
        SaveData data1 = new SaveData();
        data1.setCurrentLevel(1);
        data1.setScore(100);
        SaveGameManager.saveGame(1, data1);

        // Overwrite with new data
        SaveData data2 = new SaveData();
        data2.setCurrentLevel(5);
        data2.setScore(500);
        SaveGameManager.saveGame(1, data2);

        // Load and verify it's the new data
        SaveData loaded = SaveGameManager.loadGame(1);
        assertNotNull(loaded);
        assertEquals(5, loaded.getCurrentLevel());
        assertEquals(500, loaded.getScore());
    }
}

