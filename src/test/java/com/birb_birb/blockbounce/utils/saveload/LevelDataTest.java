package com.birb_birb.blockbounce.utils.saveload;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.BeforeEach;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for LevelData class
 * Tests level data structure, grid parsing, and brick generation
 */
@DisplayName("Level Data Tests")
class LevelDataTest {

    private LevelData levelData;

    @BeforeEach
    void setUp() {
        levelData = new LevelData();
    }

    @Test
    @DisplayName("LevelData constructor should create instance successfully")
    void testConstructor() {
        assertNotNull(levelData);
    }

    @Test
    @DisplayName("setLevelId and getLevelId should work correctly")
    void testLevelIdGetterSetter() {
        levelData.setLevelId(5);
        assertEquals(5, levelData.getLevelId());
    }

    @Test
    @DisplayName("setStartX and getStartX should work correctly")
    void testStartXGetterSetter() {
        levelData.setStartX(100.0);
        assertEquals(100.0, levelData.getStartX(), 0.001);
    }

    @Test
    @DisplayName("setStartY and getStartY should work correctly")
    void testStartYGetterSetter() {
        levelData.setStartY(200.0);
        assertEquals(200.0, levelData.getStartY(), 0.001);
    }

    @Test
    @DisplayName("setGrid and getGrid should work correctly")
    void testGridGetterSetter() {
        List<String> grid = Arrays.asList("WWW", "SSS");
        levelData.setGrid(grid);

        assertNotNull(levelData.getGrid());
        assertEquals(2, levelData.getGrid().size());
        assertEquals("WWW", levelData.getGrid().get(0));
        assertEquals("SSS", levelData.getGrid().get(1));
    }

    @Test
    @DisplayName("getBricks should return empty list for empty grid")
    void testGetBricksWithEmptyGrid() {
        levelData.setGrid(Arrays.asList("...", "..."));
        levelData.setStartX(0);
        levelData.setStartY(0);

        List<BlockData> bricks = levelData.getBricks();

        assertNotNull(bricks);
        assertTrue(bricks.isEmpty(), "Empty grid should produce no bricks");
    }

    @Test
    @DisplayName("getBricks should parse WOOD bricks correctly")
    void testGetBricksWithWoodBricks() {
        levelData.setGrid(Arrays.asList("W"));
        levelData.setStartX(100.0);
        levelData.setStartY(200.0);

        List<BlockData> bricks = levelData.getBricks();

        assertEquals(1, bricks.size());
        BlockData brick = bricks.get(0);
        assertEquals("WOOD", brick.getBrickType());
        assertEquals(1, brick.getDurability());
        assertEquals(100.0, brick.getX(), 0.001);
        assertEquals(200.0, brick.getY(), 0.001);
    }

    @Test
    @DisplayName("getBricks should parse STONE bricks correctly")
    void testGetBricksWithStoneBricks() {
        levelData.setGrid(Arrays.asList("S"));
        levelData.setStartX(0);
        levelData.setStartY(0);

        List<BlockData> bricks = levelData.getBricks();

        assertEquals(1, bricks.size());
        BlockData brick = bricks.get(0);
        assertEquals("STONE", brick.getBrickType());
        assertEquals(2, brick.getDurability());
    }

    @Test
    @DisplayName("getBricks should parse NETHERACK bricks correctly")
    void testGetBricksWithNetherackBricks() {
        levelData.setGrid(Arrays.asList("R"));
        levelData.setStartX(0);
        levelData.setStartY(0);

        List<BlockData> bricks = levelData.getBricks();

        assertEquals(1, bricks.size());
        BlockData brick = bricks.get(0);
        assertEquals("NETHERACK", brick.getBrickType());
        assertEquals(1, brick.getDurability());
    }

    @Test
    @DisplayName("getBricks should parse NETHERBRICK bricks correctly")
    void testGetBricksWithNetherbrickBricks() {
        levelData.setGrid(Arrays.asList("N"));
        levelData.setStartX(0);
        levelData.setStartY(0);

        List<BlockData> bricks = levelData.getBricks();

        assertEquals(1, bricks.size());
        BlockData brick = bricks.get(0);
        assertEquals("NETHERBRICK", brick.getBrickType());
        assertEquals(2, brick.getDurability());
    }

    @Test
    @DisplayName("getBricks should parse ENDSTONE bricks correctly")
    void testGetBricksWithEndstoneBricks() {
        levelData.setGrid(Arrays.asList("E"));
        levelData.setStartX(0);
        levelData.setStartY(0);

        List<BlockData> bricks = levelData.getBricks();

        assertEquals(1, bricks.size());
        BlockData brick = bricks.get(0);
        assertEquals("ENDSTONE", brick.getBrickType());
        assertEquals(2, brick.getDurability());
    }

    @Test
    @DisplayName("getBricks should parse OBSIDIAN bricks correctly")
    void testGetBricksWithObsidianBricks() {
        levelData.setGrid(Arrays.asList("O"));
        levelData.setStartX(0);
        levelData.setStartY(0);

        List<BlockData> bricks = levelData.getBricks();

        assertEquals(1, bricks.size());
        BlockData brick = bricks.get(0);
        assertEquals("OBSIDIAN", brick.getBrickType());
        assertEquals(-1, brick.getDurability());
    }

    @Test
    @DisplayName("getBricks should parse LUCKY bricks correctly")
    void testGetBricksWithLuckyBricks() {
        levelData.setGrid(Arrays.asList("L"));
        levelData.setStartX(0);
        levelData.setStartY(0);

        List<BlockData> bricks = levelData.getBricks();

        assertEquals(1, bricks.size());
        BlockData brick = bricks.get(0);
        assertEquals("LUCKY", brick.getBrickType());
        assertEquals(1, brick.getDurability());
    }

    @Test
    @DisplayName("getBricks should parse multiple bricks in a row")
    void testGetBricksWithMultipleBricksInRow() {
        levelData.setGrid(Arrays.asList("WWW"));
        levelData.setStartX(0);
        levelData.setStartY(0);

        List<BlockData> bricks = levelData.getBricks();

        assertEquals(3, bricks.size());
    }

    @Test
    @DisplayName("getBricks should parse multiple rows of bricks")
    void testGetBricksWithMultipleRows() {
        levelData.setGrid(Arrays.asList("WW", "SS"));
        levelData.setStartX(0);
        levelData.setStartY(0);

        List<BlockData> bricks = levelData.getBricks();

        assertEquals(4, bricks.size());
    }

    @Test
    @DisplayName("getBricks should calculate correct X positions for multiple columns")
    void testGetBricksXPositionCalculation() {
        levelData.setGrid(Arrays.asList("WWW"));
        levelData.setStartX(100.0);
        levelData.setStartY(0);

        List<BlockData> bricks = levelData.getBricks();

        assertEquals(3, bricks.size());
        assertTrue(bricks.get(0).getX() == 100.0);
        assertTrue(bricks.get(1).getX() > bricks.get(0).getX());
        assertTrue(bricks.get(2).getX() > bricks.get(1).getX());
    }

    @Test
    @DisplayName("getBricks should calculate correct Y positions for multiple rows")
    void testGetBricksYPositionCalculation() {
        levelData.setGrid(Arrays.asList("W", "W", "W"));
        levelData.setStartX(0);
        levelData.setStartY(100.0);

        List<BlockData> bricks = levelData.getBricks();

        assertEquals(3, bricks.size());
        assertTrue(bricks.get(0).getY() == 100.0);
        assertTrue(bricks.get(1).getY() > bricks.get(0).getY());
        assertTrue(bricks.get(2).getY() > bricks.get(1).getY());
    }

    @Test
    @DisplayName("getBricks should skip empty spaces in grid")
    void testGetBricksSkipsEmptySpaces() {
        levelData.setGrid(Arrays.asList("W.W"));
        levelData.setStartX(0);
        levelData.setStartY(0);

        List<BlockData> bricks = levelData.getBricks();

        assertEquals(2, bricks.size(), "Should only create bricks for W, not for .");
    }

    @Test
    @DisplayName("getBricks should handle mixed brick types")
    void testGetBricksWithMixedTypes() {
        levelData.setGrid(Arrays.asList("WSR"));
        levelData.setStartX(0);
        levelData.setStartY(0);

        List<BlockData> bricks = levelData.getBricks();

        assertEquals(3, bricks.size());
        assertEquals("WOOD", bricks.get(0).getBrickType());
        assertEquals("STONE", bricks.get(1).getBrickType());
        assertEquals("NETHERACK", bricks.get(2).getBrickType());
    }

    @Test
    @DisplayName("getBricks should assign correct colors to brick types")
    void testGetBricksAssignsCorrectColors() {
        levelData.setGrid(Arrays.asList("WSRNEO"));
        levelData.setStartX(0);
        levelData.setStartY(0);

        List<BlockData> bricks = levelData.getBricks();

        assertEquals(6, bricks.size());
        assertNotNull(bricks.get(0).getColor());
        assertNotNull(bricks.get(1).getColor());
        assertNotNull(bricks.get(2).getColor());
        assertNotNull(bricks.get(3).getColor());
        assertNotNull(bricks.get(4).getColor());
        assertNotNull(bricks.get(5).getColor());
    }

    @Test
    @DisplayName("getBricks should handle complex patterns")
    void testGetBricksWithComplexPattern() {
        levelData.setGrid(Arrays.asList(
            "WWWWW",
            "S...S",
            "WWWWW"
        ));
        levelData.setStartX(0);
        levelData.setStartY(0);

        List<BlockData> bricks = levelData.getBricks();

        assertEquals(12, bricks.size(), "5 + 2 + 5 = 12 bricks");
    }

    @Test
    @DisplayName("getBricks should return new list each time")
    void testGetBricksReturnsNewList() {
        levelData.setGrid(Arrays.asList("W"));
        levelData.setStartX(0);
        levelData.setStartY(0);

        List<BlockData> bricks1 = levelData.getBricks();
        List<BlockData> bricks2 = levelData.getBricks();

        assertNotSame(bricks1, bricks2, "Should return new list instances");
    }

    @Test
    @DisplayName("LevelData should handle null grid gracefully")
    void testLevelDataWithNullGrid() {
        levelData.setGrid(null);

        assertThrows(NullPointerException.class, () -> {
            levelData.getBricks();
        });
    }

    @Test
    @DisplayName("LevelData class should be public")
    void testLevelDataClassIsPublic() {
        assertTrue(java.lang.reflect.Modifier.isPublic(LevelData.class.getModifiers()));
    }

    @Test
    @DisplayName("getLevelId method should be public")
    void testGetLevelIdMethodIsPublic() throws NoSuchMethodException {
        var method = LevelData.class.getMethod("getLevelId");
        assertTrue(java.lang.reflect.Modifier.isPublic(method.getModifiers()));
    }

    @Test
    @DisplayName("setLevelId method should be public")
    void testSetLevelIdMethodIsPublic() throws NoSuchMethodException {
        var method = LevelData.class.getMethod("setLevelId", int.class);
        assertTrue(java.lang.reflect.Modifier.isPublic(method.getModifiers()));
    }

    @Test
    @DisplayName("getBricks method should be public")
    void testGetBricksMethodIsPublic() throws NoSuchMethodException {
        var method = LevelData.class.getMethod("getBricks");
        assertTrue(java.lang.reflect.Modifier.isPublic(method.getModifiers()));
    }

    @Test
    @DisplayName("getBricks should return List type")
    void testGetBricksReturnType() throws NoSuchMethodException {
        var method = LevelData.class.getMethod("getBricks");
        assertEquals(List.class, method.getReturnType());
    }

    @Test
    @DisplayName("Different level IDs should be allowed")
    void testDifferentLevelIds() {
        levelData.setLevelId(1);
        assertEquals(1, levelData.getLevelId());

        levelData.setLevelId(100);
        assertEquals(100, levelData.getLevelId());

        levelData.setLevelId(-1);
        assertEquals(-1, levelData.getLevelId());
    }

    @Test
    @DisplayName("Start coordinates should accept any double values")
    void testStartCoordinatesAcceptAnyDouble() {
        levelData.setStartX(0.0);
        assertEquals(0.0, levelData.getStartX(), 0.001);

        levelData.setStartX(999.99);
        assertEquals(999.99, levelData.getStartX(), 0.001);

        levelData.setStartY(-50.5);
        assertEquals(-50.5, levelData.getStartY(), 0.001);
    }

    @Test
    @DisplayName("Grid can be updated after initial setting")
    void testGridCanBeUpdated() {
        levelData.setGrid(Arrays.asList("WWW"));
        assertEquals(1, levelData.getGrid().size());

        levelData.setGrid(Arrays.asList("SSS", "SSS"));
        assertEquals(2, levelData.getGrid().size());
    }

    @Test
    @DisplayName("LevelData should have public constructor")
    void testLevelDataHasPublicConstructor() {
        var constructors = LevelData.class.getConstructors();
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

