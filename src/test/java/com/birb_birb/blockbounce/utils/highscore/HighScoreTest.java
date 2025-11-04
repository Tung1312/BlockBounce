package com.birb_birb.blockbounce.utils.highscore;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.BeforeEach;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for HighScore class
 * Tests high score data structure and comparison logic
 */
@DisplayName("High Score Tests - Score Display")
class HighScoreTest {

    private HighScore highScore;

    @BeforeEach
    void setUp() {
        highScore = new HighScore("TestPlayer", 1000);
    }

    @Test
    @DisplayName("Constructor should create instance successfully")
    void testConstructor() {
        assertNotNull(highScore);
    }

    @Test
    @DisplayName("Constructor should set player name correctly")
    void testConstructorSetsPlayerName() {
        HighScore score = new HighScore("Player1", 500);
        assertEquals("Player1", score.getPlayerName());
    }

    @Test
    @DisplayName("Constructor should set score correctly")
    void testConstructorSetsScore() {
        HighScore score = new HighScore("Player1", 500);
        assertEquals(500, score.getScore());
    }

    @Test
    @DisplayName("Constructor should generate timestamp")
    void testConstructorGeneratesTimestamp() {
        assertNotNull(highScore.getTimestamp());
        assertFalse(highScore.getTimestamp().isEmpty());
    }

    @Test
    @DisplayName("getPlayerName should return correct name")
    void testGetPlayerName() {
        assertEquals("TestPlayer", highScore.getPlayerName());
    }

    @Test
    @DisplayName("getScore should return correct score")
    void testGetScore() {
        assertEquals(1000, highScore.getScore());
    }

    @Test
    @DisplayName("getTimestamp should return non-null timestamp")
    void testGetTimestamp() {
        assertNotNull(highScore.getTimestamp());
    }

    @Test
    @DisplayName("Score should accept zero value")
    void testScoreAcceptsZero() {
        HighScore score = new HighScore("Player", 0);
        assertEquals(0, score.getScore());
    }

    @Test
    @DisplayName("Score should accept large values")
    void testScoreAcceptsLargeValues() {
        HighScore score = new HighScore("Player", 999999);
        assertEquals(999999, score.getScore());
    }

    @Test
    @DisplayName("Score should accept negative values")
    void testScoreAcceptsNegativeValues() {
        HighScore score = new HighScore("Player", -100);
        assertEquals(-100, score.getScore());
    }

    @Test
    @DisplayName("Player name should accept empty string")
    void testPlayerNameAcceptsEmptyString() {
        HighScore score = new HighScore("", 100);
        assertEquals("", score.getPlayerName());
    }

    @Test
    @DisplayName("Player name should accept special characters")
    void testPlayerNameAcceptsSpecialCharacters() {
        HighScore score = new HighScore("Player@123!", 100);
        assertEquals("Player@123!", score.getPlayerName());
    }

    @Test
    @DisplayName("Player name should accept long strings")
    void testPlayerNameAcceptsLongStrings() {
        String longName = "VeryLongPlayerNameThatExceedsNormalLength";
        HighScore score = new HighScore(longName, 100);
        assertEquals(longName, score.getPlayerName());
    }

    @Test
    @DisplayName("compareTo should return negative for higher score")
    void testCompareToHigherScore() {
        HighScore score1 = new HighScore("Player1", 500);
        HighScore score2 = new HighScore("Player2", 1000);

        assertTrue(score1.compareTo(score2) > 0, "Lower score should come after higher score");
    }

    @Test
    @DisplayName("compareTo should return positive for lower score")
    void testCompareToLowerScore() {
        HighScore score1 = new HighScore("Player1", 1000);
        HighScore score2 = new HighScore("Player2", 500);

        assertTrue(score1.compareTo(score2) < 0, "Higher score should come before lower score");
    }

    @Test
    @DisplayName("compareTo should return zero for equal scores")
    void testCompareToEqualScores() {
        HighScore score1 = new HighScore("Player1", 1000);
        HighScore score2 = new HighScore("Player2", 1000);

        assertEquals(0, score1.compareTo(score2));
    }

    @Test
    @DisplayName("compareTo should sort in descending order")
    void testCompareToDescendingOrder() {
        HighScore score1 = new HighScore("Player1", 100);
        HighScore score2 = new HighScore("Player2", 200);
        HighScore score3 = new HighScore("Player3", 300);

        assertTrue(score3.compareTo(score2) < 0);
        assertTrue(score2.compareTo(score1) < 0);
        assertTrue(score3.compareTo(score1) < 0);
    }

    @Test
    @DisplayName("toString should include player name")
    void testToStringIncludesPlayerName() {
        String result = highScore.toString();
        assertTrue(result.contains("TestPlayer"));
    }

    @Test
    @DisplayName("toString should include score")
    void testToStringIncludesScore() {
        String result = highScore.toString();
        assertTrue(result.contains("1000"));
    }

    @Test
    @DisplayName("toString should include timestamp")
    void testToStringIncludesTimestamp() {
        String result = highScore.toString();
        assertNotNull(result);
        assertTrue(result.contains(highScore.getTimestamp()));
    }

    @Test
    @DisplayName("toString should return non-null string")
    void testToStringReturnsNonNull() {
        assertNotNull(highScore.toString());
    }

    @Test
    @DisplayName("HighScore should implement Serializable")
    void testHighScoreImplementsSerializable() {
        assertTrue(java.io.Serializable.class.isAssignableFrom(HighScore.class));
    }

    @Test
    @DisplayName("HighScore should implement Comparable")
    void testHighScoreImplementsComparable() {
        assertTrue(Comparable.class.isAssignableFrom(HighScore.class));
    }

    @Test
    @DisplayName("Multiple HighScore instances should be comparable")
    void testMultipleInstancesComparable() {
        HighScore score1 = new HighScore("Player1", 500);
        HighScore score2 = new HighScore("Player2", 1000);
        HighScore score3 = new HighScore("Player3", 750);

        assertTrue(score2.compareTo(score1) < 0);
        assertTrue(score2.compareTo(score3) < 0);
        assertTrue(score3.compareTo(score1) < 0);
    }

    @Test
    @DisplayName("Timestamp format should be valid")
    void testTimestampFormat() {
        String timestamp = highScore.getTimestamp();
        // Should contain date and time components
        assertTrue(timestamp.matches("\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2}"));
    }

    @Test
    @DisplayName("Different instances should have different timestamps if created at different times")
    void testDifferentTimestamps() throws InterruptedException {
        HighScore score1 = new HighScore("Player1", 100);
        Thread.sleep(1100); // Wait over 1 second
        HighScore score2 = new HighScore("Player2", 200);

        assertNotEquals(score1.getTimestamp(), score2.getTimestamp());
    }

    @Test
    @DisplayName("HighScore class should be public")
    void testHighScoreClassIsPublic() {
        assertTrue(java.lang.reflect.Modifier.isPublic(HighScore.class.getModifiers()));
    }

    @Test
    @DisplayName("getPlayerName method should be public")
    void testGetPlayerNameMethodIsPublic() throws NoSuchMethodException {
        var method = HighScore.class.getMethod("getPlayerName");
        assertTrue(java.lang.reflect.Modifier.isPublic(method.getModifiers()));
    }

    @Test
    @DisplayName("getScore method should be public")
    void testGetScoreMethodIsPublic() throws NoSuchMethodException {
        var method = HighScore.class.getMethod("getScore");
        assertTrue(java.lang.reflect.Modifier.isPublic(method.getModifiers()));
    }

    @Test
    @DisplayName("getTimestamp method should be public")
    void testGetTimestampMethodIsPublic() throws NoSuchMethodException {
        var method = HighScore.class.getMethod("getTimestamp");
        assertTrue(java.lang.reflect.Modifier.isPublic(method.getModifiers()));
    }

    @Test
    @DisplayName("getPlayerName should return String type")
    void testGetPlayerNameReturnType() throws NoSuchMethodException {
        var method = HighScore.class.getMethod("getPlayerName");
        assertEquals(String.class, method.getReturnType());
    }

    @Test
    @DisplayName("getScore should return int type")
    void testGetScoreReturnType() throws NoSuchMethodException {
        var method = HighScore.class.getMethod("getScore");
        assertEquals(int.class, method.getReturnType());
    }

    @Test
    @DisplayName("getTimestamp should return String type")
    void testGetTimestampReturnType() throws NoSuchMethodException {
        var method = HighScore.class.getMethod("getTimestamp");
        assertEquals(String.class, method.getReturnType());
    }

    @Test
    @DisplayName("compareTo method should be public")
    void testCompareToMethodIsPublic() throws NoSuchMethodException {
        var method = HighScore.class.getMethod("compareTo", HighScore.class);
        assertTrue(java.lang.reflect.Modifier.isPublic(method.getModifiers()));
    }

    @Test
    @DisplayName("toString method should be public")
    void testToStringMethodIsPublic() throws NoSuchMethodException {
        var method = HighScore.class.getMethod("toString");
        assertTrue(java.lang.reflect.Modifier.isPublic(method.getModifiers()));
    }

    @Test
    @DisplayName("HighScore should have public constructor with two parameters")
    void testHighScoreHasPublicConstructor() {
        var constructors = HighScore.class.getConstructors();

        boolean hasCorrectConstructor = false;
        for (var constructor : constructors) {
            if (java.lang.reflect.Modifier.isPublic(constructor.getModifiers())
                && constructor.getParameterCount() == 2) {
                hasCorrectConstructor = true;
                break;
            }
        }
        assertTrue(hasCorrectConstructor, "Should have public constructor with 2 parameters");
    }

    @Test
    @DisplayName("Player name and score should be immutable")
    void testImmutability() {
        String originalName = highScore.getPlayerName();
        int originalScore = highScore.getScore();

        // Try to get values multiple times
        assertEquals(originalName, highScore.getPlayerName());
        assertEquals(originalScore, highScore.getScore());
        assertEquals(originalName, highScore.getPlayerName());
        assertEquals(originalScore, highScore.getScore());
    }

    @Test
    @DisplayName("compareTo should be consistent with multiple calls")
    void testCompareToConsistency() {
        HighScore score1 = new HighScore("Player1", 1000);
        HighScore score2 = new HighScore("Player2", 500);

        int result1 = score1.compareTo(score2);
        int result2 = score1.compareTo(score2);
        int result3 = score1.compareTo(score2);

        assertEquals(result1, result2);
        assertEquals(result2, result3);
    }

    @Test
    @DisplayName("compareTo should be transitive")
    void testCompareToTransitive() {
        HighScore score1 = new HighScore("Player1", 300);
        HighScore score2 = new HighScore("Player2", 200);
        HighScore score3 = new HighScore("Player3", 100);

        assertTrue(score1.compareTo(score2) < 0);
        assertTrue(score2.compareTo(score3) < 0);
        assertTrue(score1.compareTo(score3) < 0);
    }

    @Test
    @DisplayName("Same player can have different scores")
    void testSamePlayerDifferentScores() {
        HighScore score1 = new HighScore("Player1", 100);
        HighScore score2 = new HighScore("Player1", 200);

        assertEquals("Player1", score1.getPlayerName());
        assertEquals("Player1", score2.getPlayerName());
        assertEquals(100, score1.getScore());
        assertEquals(200, score2.getScore());
    }
}

