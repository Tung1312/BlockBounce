package com.birb_birb.blockbounce.utils.saveload;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.BeforeEach;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for SaveData class
 * Tests score and lives data storage and retrieval
 */
@DisplayName("Save Data Tests - Score and Lives Display")
class SaveDataTest {

    private SaveData saveData;

    @BeforeEach
    void setUp() {
        saveData = new SaveData();
    }

    @Test
    @DisplayName("Default constructor should create instance successfully")
    void testDefaultConstructor() {
        assertNotNull(saveData);
    }

    @Test
    @DisplayName("Default constructor should initialize save date")
    void testDefaultConstructorInitializesSaveDate() {
        assertNotNull(saveData.getSaveDate());
    }

    @Test
    @DisplayName("Default constructor should initialize blocks list")
    void testDefaultConstructorInitializesBlocksList() {
        assertNotNull(saveData.getBlocks());
        assertTrue(saveData.getBlocks().isEmpty());
    }

    // Score Tests
    @Test
    @DisplayName("setScore and getScore should work correctly")
    void testScoreGetterSetter() {
        saveData.setScore(1000);
        assertEquals(1000, saveData.getScore());
    }

    @Test
    @DisplayName("Score should accept zero value")
    void testScoreAcceptsZero() {
        saveData.setScore(0);
        assertEquals(0, saveData.getScore());
    }

    @Test
    @DisplayName("Score should accept large values")
    void testScoreAcceptsLargeValues() {
        saveData.setScore(999999);
        assertEquals(999999, saveData.getScore());
    }

    @Test
    @DisplayName("Score should accept negative values")
    void testScoreAcceptsNegative() {
        saveData.setScore(-100);
        assertEquals(-100, saveData.getScore());
    }

    @Test
    @DisplayName("Score can be updated multiple times")
    void testScoreCanBeUpdated() {
        saveData.setScore(100);
        assertEquals(100, saveData.getScore());

        saveData.setScore(250);
        assertEquals(250, saveData.getScore());

        saveData.setScore(500);
        assertEquals(500, saveData.getScore());
    }

    // Lives Tests
    @Test
    @DisplayName("setLives and getLives should work correctly")
    void testLivesGetterSetter() {
        saveData.setLives(3);
        assertEquals(3, saveData.getLives());
    }

    @Test
    @DisplayName("Lives should accept zero value")
    void testLivesAcceptsZero() {
        saveData.setLives(0);
        assertEquals(0, saveData.getLives());
    }

    @Test
    @DisplayName("Lives should accept different values")
    void testLivesAcceptsDifferentValues() {
        saveData.setLives(1);
        assertEquals(1, saveData.getLives());

        saveData.setLives(5);
        assertEquals(5, saveData.getLives());

        saveData.setLives(10);
        assertEquals(10, saveData.getLives());
    }

    @Test
    @DisplayName("Lives can be decreased")
    void testLivesCanBeDecreased() {
        saveData.setLives(3);
        assertEquals(3, saveData.getLives());

        saveData.setLives(2);
        assertEquals(2, saveData.getLives());

        saveData.setLives(1);
        assertEquals(1, saveData.getLives());
    }

    @Test
    @DisplayName("Lives can be increased")
    void testLivesCanBeIncreased() {
        saveData.setLives(3);
        assertEquals(3, saveData.getLives());

        saveData.setLives(4);
        assertEquals(4, saveData.getLives());

        saveData.setLives(5);
        assertEquals(5, saveData.getLives());
    }

    // High Score Tests
    @Test
    @DisplayName("setHighScore and getHighScore should work correctly")
    void testHighScoreGetterSetter() {
        saveData.setHighScore(5000);
        assertEquals(5000, saveData.getHighScore());
    }

    @Test
    @DisplayName("High score should accept zero value")
    void testHighScoreAcceptsZero() {
        saveData.setHighScore(0);
        assertEquals(0, saveData.getHighScore());
    }

    @Test
    @DisplayName("High score should accept very large values")
    void testHighScoreAcceptsVeryLargeValues() {
        saveData.setHighScore(9999999);
        assertEquals(9999999, saveData.getHighScore());
    }

    // Level Tests
    @Test
    @DisplayName("setCurrentLevel and getCurrentLevel should work correctly")
    void testCurrentLevelGetterSetter() {
        saveData.setCurrentLevel(5);
        assertEquals(5, saveData.getCurrentLevel());
    }

    @Test
    @DisplayName("Current level should accept different values")
    void testCurrentLevelAcceptsDifferentValues() {
        saveData.setCurrentLevel(1);
        assertEquals(1, saveData.getCurrentLevel());

        saveData.setCurrentLevel(8);
        assertEquals(8, saveData.getCurrentLevel());
    }

    // Game Mode Tests
    @Test
    @DisplayName("setGameMode and getGameMode should work correctly")
    void testGameModeGetterSetter() {
        saveData.setGameMode("STORY");
        assertEquals("STORY", saveData.getGameMode());
    }

    @Test
    @DisplayName("Game mode should support all game modes")
    void testGameModeSupportAllModes() {
        saveData.setGameMode("STORY");
        assertEquals("STORY", saveData.getGameMode());

        saveData.setGameMode("SCORE");
        assertEquals("SCORE", saveData.getGameMode());

        saveData.setGameMode("VERSUS");
        assertEquals("VERSUS", saveData.getGameMode());
    }

    // Elapsed Time Tests
    @Test
    @DisplayName("setElapsedTime and getElapsedTime should work correctly")
    void testElapsedTimeGetterSetter() {
        saveData.setElapsedTime(120.5);
        assertEquals(120.5, saveData.getElapsedTime(), 0.001);
    }

    @Test
    @DisplayName("Elapsed time should accept zero")
    void testElapsedTimeAcceptsZero() {
        saveData.setElapsedTime(0.0);
        assertEquals(0.0, saveData.getElapsedTime(), 0.001);
    }

    @Test
    @DisplayName("Elapsed time should accept decimal values")
    void testElapsedTimeAcceptsDecimal() {
        saveData.setElapsedTime(45.75);
        assertEquals(45.75, saveData.getElapsedTime(), 0.001);
    }

    // Save Date Tests
    @Test
    @DisplayName("setSaveDate and getSaveDate should work correctly")
    void testSaveDateGetterSetter() {
        LocalDateTime date = LocalDateTime.now();
        saveData.setSaveDate(date);
        assertEquals(date, saveData.getSaveDate());
    }

    @Test
    @DisplayName("Save date should be preserved across operations")
    void testSaveDatePreserved() {
        LocalDateTime originalDate = saveData.getSaveDate();
        saveData.setScore(100);
        saveData.setLives(3);
        assertEquals(originalDate, saveData.getSaveDate());
    }

    // Paddle State Tests
    @Test
    @DisplayName("setPaddleX and getPaddleX should work correctly")
    void testPaddleXGetterSetter() {
        saveData.setPaddleX(500.0);
        assertEquals(500.0, saveData.getPaddleX(), 0.001);
    }

    @Test
    @DisplayName("setPaddleY and getPaddleY should work correctly")
    void testPaddleYGetterSetter() {
        saveData.setPaddleY(700.0);
        assertEquals(700.0, saveData.getPaddleY(), 0.001);
    }

    // Ball State Tests
    @Test
    @DisplayName("setBallX and getBallX should work correctly")
    void testBallXGetterSetter() {
        saveData.setBallX(300.0);
        assertEquals(300.0, saveData.getBallX(), 0.001);
    }

    @Test
    @DisplayName("setBallY and getBallY should work correctly")
    void testBallYGetterSetter() {
        saveData.setBallY(400.0);
        assertEquals(400.0, saveData.getBallY(), 0.001);
    }

    @Test
    @DisplayName("setBallVelocityX and getBallVelocityX should work correctly")
    void testBallVelocityXGetterSetter() {
        saveData.setBallVelocityX(5.0);
        assertEquals(5.0, saveData.getBallVelocityX(), 0.001);
    }

    @Test
    @DisplayName("setBallVelocityY and getBallVelocityY should work correctly")
    void testBallVelocityYGetterSetter() {
        saveData.setBallVelocityY(-5.0);
        assertEquals(-5.0, saveData.getBallVelocityY(), 0.001);
    }

    @Test
    @DisplayName("setBallLaunched and isBallLaunched should work correctly")
    void testBallLaunchedGetterSetter() {
        saveData.setBallLaunched(true);
        assertTrue(saveData.isBallLaunched());

        saveData.setBallLaunched(false);
        assertFalse(saveData.isBallLaunched());
    }

    // Blocks Tests
    @Test
    @DisplayName("setBlocks and getBlocks should work correctly")
    void testBlocksGetterSetter() {
        List<BlockData> blocks = new ArrayList<>();
        blocks.add(new BlockData(100, 100, "RED", 1));
        blocks.add(new BlockData(200, 100, "BLUE", 2));

        saveData.setBlocks(blocks);

        assertEquals(2, saveData.getBlocks().size());
    }

    @Test
    @DisplayName("Blocks list can be empty")
    void testBlocksCanBeEmpty() {
        saveData.setBlocks(new ArrayList<>());
        assertTrue(saveData.getBlocks().isEmpty());
    }

    // Combined State Tests
    @Test
    @DisplayName("Multiple properties can be set independently")
    void testMultiplePropertiesIndependent() {
        saveData.setScore(1500);
        saveData.setLives(5);
        saveData.setCurrentLevel(3);
        saveData.setGameMode("STORY");
        saveData.setHighScore(2000);

        assertEquals(1500, saveData.getScore());
        assertEquals(5, saveData.getLives());
        assertEquals(3, saveData.getCurrentLevel());
        assertEquals("STORY", saveData.getGameMode());
        assertEquals(2000, saveData.getHighScore());
    }

    @Test
    @DisplayName("Score and lives should be independent")
    void testScoreAndLivesIndependent() {
        saveData.setScore(1000);
        saveData.setLives(3);

        saveData.setScore(2000);
        assertEquals(3, saveData.getLives(), "Lives should not change when score changes");

        saveData.setLives(2);
        assertEquals(2000, saveData.getScore(), "Score should not change when lives change");
    }

    @Test
    @DisplayName("SaveData should implement Serializable")
    void testSaveDataImplementsSerializable() {
        assertTrue(java.io.Serializable.class.isAssignableFrom(SaveData.class));
    }

    // Method Visibility Tests
    @Test
    @DisplayName("SaveData class should be public")
    void testSaveDataClassIsPublic() {
        assertTrue(java.lang.reflect.Modifier.isPublic(SaveData.class.getModifiers()));
    }

    @Test
    @DisplayName("getScore method should be public")
    void testGetScoreMethodIsPublic() throws NoSuchMethodException {
        var method = SaveData.class.getMethod("getScore");
        assertTrue(java.lang.reflect.Modifier.isPublic(method.getModifiers()));
    }

    @Test
    @DisplayName("setScore method should be public")
    void testSetScoreMethodIsPublic() throws NoSuchMethodException {
        var method = SaveData.class.getMethod("setScore", int.class);
        assertTrue(java.lang.reflect.Modifier.isPublic(method.getModifiers()));
    }

    @Test
    @DisplayName("getLives method should be public")
    void testGetLivesMethodIsPublic() throws NoSuchMethodException {
        var method = SaveData.class.getMethod("getLives");
        assertTrue(java.lang.reflect.Modifier.isPublic(method.getModifiers()));
    }

    @Test
    @DisplayName("setLives method should be public")
    void testSetLivesMethodIsPublic() throws NoSuchMethodException {
        var method = SaveData.class.getMethod("setLives", int.class);
        assertTrue(java.lang.reflect.Modifier.isPublic(method.getModifiers()));
    }

    @Test
    @DisplayName("getScore should return int type")
    void testGetScoreReturnType() throws NoSuchMethodException {
        var method = SaveData.class.getMethod("getScore");
        assertEquals(int.class, method.getReturnType());
    }

    @Test
    @DisplayName("getLives should return int type")
    void testGetLivesReturnType() throws NoSuchMethodException {
        var method = SaveData.class.getMethod("getLives");
        assertEquals(int.class, method.getReturnType());
    }

    @Test
    @DisplayName("SaveData should have public no-arg constructor")
    void testSaveDataHasPublicConstructor() {
        var constructors = SaveData.class.getConstructors();

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

    // Edge Cases
    @Test
    @DisplayName("Score and lives combination should work for game over scenario")
    void testGameOverScenario() {
        saveData.setScore(1500);
        saveData.setLives(0);

        assertEquals(1500, saveData.getScore());
        assertEquals(0, saveData.getLives());
    }

    @Test
    @DisplayName("High score should be independent of current score")
    void testHighScoreIndependentOfScore() {
        saveData.setScore(1000);
        saveData.setHighScore(5000);

        assertEquals(1000, saveData.getScore());
        assertEquals(5000, saveData.getHighScore());

        saveData.setScore(2000);
        assertEquals(5000, saveData.getHighScore(), "High score should not change when current score changes");
    }

    @Test
    @DisplayName("Complete game state can be captured")
    void testCompleteGameStateCapture() {
        // Set up a complete game state
        saveData.setScore(2500);
        saveData.setLives(2);
        saveData.setCurrentLevel(4);
        saveData.setGameMode("STORY");
        saveData.setHighScore(5000);
        saveData.setElapsedTime(180.5);
        saveData.setPaddleX(500.0);
        saveData.setPaddleY(700.0);
        saveData.setBallX(300.0);
        saveData.setBallY(400.0);
        saveData.setBallVelocityX(5.0);
        saveData.setBallVelocityY(-5.0);
        saveData.setBallLaunched(true);

        // Verify all values are preserved
        assertEquals(2500, saveData.getScore());
        assertEquals(2, saveData.getLives());
        assertEquals(4, saveData.getCurrentLevel());
        assertEquals("STORY", saveData.getGameMode());
        assertEquals(5000, saveData.getHighScore());
        assertEquals(180.5, saveData.getElapsedTime(), 0.001);
        assertEquals(500.0, saveData.getPaddleX(), 0.001);
        assertEquals(700.0, saveData.getPaddleY(), 0.001);
        assertEquals(300.0, saveData.getBallX(), 0.001);
        assertEquals(400.0, saveData.getBallY(), 0.001);
        assertEquals(5.0, saveData.getBallVelocityX(), 0.001);
        assertEquals(-5.0, saveData.getBallVelocityY(), 0.001);
        assertTrue(saveData.isBallLaunched());
    }
}

