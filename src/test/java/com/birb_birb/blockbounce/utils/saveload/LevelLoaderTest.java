package com.birb_birb.blockbounce.utils.saveload;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.BeforeEach;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for LevelLoader class
 * Tests level loading from JSON files
 */
@DisplayName("Level Loader Tests")
class LevelLoaderTest {

    private LevelLoader levelLoader;

    @BeforeEach
    void setUp() {
        levelLoader = new LevelLoader();
    }

    @Test
    @DisplayName("LevelLoader constructor should create instance successfully")
    void testConstructor() {
        assertNotNull(levelLoader);
    }

    @Test
    @DisplayName("loadLevel should return LevelData for level 1")
    void testLoadLevel1() {
        LevelData levelData = levelLoader.loadLevel(1);

        assertNotNull(levelData, "Level 1 should load successfully");
    }

    @Test
    @DisplayName("loadLevel should return LevelData for level 2")
    void testLoadLevel2() {
        LevelData levelData = levelLoader.loadLevel(2);

        assertNotNull(levelData, "Level 2 should load successfully");
    }

    @Test
    @DisplayName("loadLevel should return LevelData for level 3")
    void testLoadLevel3() {
        LevelData levelData = levelLoader.loadLevel(3);

        assertNotNull(levelData, "Level 3 should load successfully");
    }

    @Test
    @DisplayName("loadLevel should return null for non-existent level")
    void testLoadNonExistentLevel() {
        LevelData levelData = levelLoader.loadLevel(999);

        assertNull(levelData, "Non-existent level should return null");
    }

    @Test
    @DisplayName("loadLevel should return null for negative level number")
    void testLoadNegativeLevelNumber() {
        LevelData levelData = levelLoader.loadLevel(-1);

        assertNull(levelData, "Negative level number should return null");
    }

    @Test
    @DisplayName("loadLevel should return null for zero level number")
    void testLoadZeroLevelNumber() {
        LevelData levelData = levelLoader.loadLevel(0);

        assertNull(levelData, "Zero level number should return null");
    }

    @Test
    @DisplayName("Loaded level should have correct level ID")
    void testLoadedLevelHasCorrectId() {
        LevelData levelData = levelLoader.loadLevel(1);

        assertNotNull(levelData);
        assertEquals(1, levelData.getLevelId());
    }

    @Test
    @DisplayName("Loaded level should have grid data")
    void testLoadedLevelHasGrid() {
        LevelData levelData = levelLoader.loadLevel(1);

        assertNotNull(levelData);
        assertNotNull(levelData.getGrid());
        assertFalse(levelData.getGrid().isEmpty(), "Grid should not be empty");
    }

    @Test
    @DisplayName("Loaded level should have valid start coordinates")
    void testLoadedLevelHasStartCoordinates() {
        LevelData levelData = levelLoader.loadLevel(1);

        assertNotNull(levelData);
        assertTrue(levelData.getStartX() >= 0, "Start X should be non-negative");
        assertTrue(levelData.getStartY() >= 0, "Start Y should be non-negative");
    }

    @Test
    @DisplayName("Multiple loadLevel calls should return independent objects")
    void testMultipleLoadCallsReturnIndependentObjects() {
        LevelData level1First = levelLoader.loadLevel(1);
        LevelData level1Second = levelLoader.loadLevel(1);

        assertNotNull(level1First);
        assertNotNull(level1Second);
        assertNotSame(level1First, level1Second, "Should return different instances");
    }

    @Test
    @DisplayName("Different levels should have different level IDs")
    void testDifferentLevelsHaveDifferentIds() {
        LevelData level1 = levelLoader.loadLevel(1);
        LevelData level2 = levelLoader.loadLevel(2);

        assertNotNull(level1);
        assertNotNull(level2);
        assertNotEquals(level1.getLevelId(), level2.getLevelId());
    }

    @Test
    @DisplayName("LevelLoader should be reusable for multiple loads")
    void testLevelLoaderReusability() {
        LevelData level1 = levelLoader.loadLevel(1);
        LevelData level2 = levelLoader.loadLevel(2);
        LevelData level3 = levelLoader.loadLevel(3);

        assertNotNull(level1);
        assertNotNull(level2);
        assertNotNull(level3);
    }

    @Test
    @DisplayName("loadLevel should handle invalid level gracefully")
    void testLoadInvalidLevelGracefully() {
        assertDoesNotThrow(() -> {
            LevelData levelData = levelLoader.loadLevel(9999);
            assertNull(levelData);
        });
    }

    @Test
    @DisplayName("Loaded level grid should contain valid brick codes")
    void testLoadedLevelGridContainsValidCodes() {
        LevelData levelData = levelLoader.loadLevel(1);

        assertNotNull(levelData);
        assertNotNull(levelData.getGrid());

        for (String row : levelData.getGrid()) {
            for (char c : row.toCharArray()) {
                assertTrue(
                    c == 'W' || c == 'S' || c == 'R' || c == 'N' ||
                    c == 'E' || c == 'O' || c == 'L' || c == '.' || c == ' ',
                    "Invalid brick code found: " + c
                );
            }
        }
    }

    @Test
    @DisplayName("LevelLoader class should be public")
    void testLevelLoaderClassIsPublic() {
        assertTrue(java.lang.reflect.Modifier.isPublic(LevelLoader.class.getModifiers()));
    }

    @Test
    @DisplayName("loadLevel method should be public")
    void testLoadLevelMethodIsPublic() throws NoSuchMethodException {
        var method = LevelLoader.class.getMethod("loadLevel", int.class);
        assertTrue(java.lang.reflect.Modifier.isPublic(method.getModifiers()));
    }

    @Test
    @DisplayName("loadLevel should return LevelData type")
    void testLoadLevelReturnType() throws NoSuchMethodException {
        var method = LevelLoader.class.getMethod("loadLevel", int.class);
        assertEquals(LevelData.class, method.getReturnType());
    }

    @Test
    @DisplayName("LevelLoader should have public constructor")
    void testLevelLoaderHasPublicConstructor() {
        var constructors = LevelLoader.class.getConstructors();
        assertTrue(constructors.length >= 1);

        boolean hasPublicConstructor = false;
        for (var constructor : constructors) {
            if (java.lang.reflect.Modifier.isPublic(constructor.getModifiers())
                && constructor.getParameterCount() == 0) {
                hasPublicConstructor = true;
                break;
            }
        }
        assertTrue(hasPublicConstructor, "Should have public no-arg constructor");
    }
}

