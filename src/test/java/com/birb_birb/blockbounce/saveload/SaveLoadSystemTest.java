package com.birb_birb.blockbounce.saveload;

import com.birb_birb.blockbounce.utils.saveload.SaveData;
import com.birb_birb.blockbounce.utils.saveload.BlockData;
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

}

