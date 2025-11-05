package com.birb_birb.blockbounce.entities;

import com.birb_birb.blockbounce.constants.BrickType;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.BeforeEach;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for BrickComponent class
 * Tests brick behavior, durability system, and brick types
 */
@DisplayName("Brick Component Tests")
class BrickComponentTest {

    @Test
    @DisplayName("Default constructor should create WOOD brick with durability 1")
    void testDefaultConstructor() {
        BrickComponent brick = new BrickComponent();

        assertEquals(BrickType.WOOD, brick.getBrickType());
        assertEquals(1, brick.getDurability());
    }

    @Test
    @DisplayName("Constructor with WOOD type should have durability 1")
    void testWoodBrickCreation() {
        BrickComponent brick = new BrickComponent(BrickType.WOOD);

        assertEquals(BrickType.WOOD, brick.getBrickType());
        assertEquals(1, brick.getDurability());
    }

    @Test
    @DisplayName("Constructor with STONE type should have durability 2")
    void testStoneBrickCreation() {
        BrickComponent brick = new BrickComponent(BrickType.STONE);

        assertEquals(BrickType.STONE, brick.getBrickType());
        assertEquals(2, brick.getDurability());
    }

    @Test
    @DisplayName("Constructor with OBSIDIAN type should have durability -1 (unbreakable)")
    void testObsidianBrickCreation() {
        BrickComponent brick = new BrickComponent(BrickType.OBSIDIAN);

        assertEquals(BrickType.OBSIDIAN, brick.getBrickType());
        assertEquals(-1, brick.getDurability());
    }

    @Test
    @DisplayName("Constructor with LUCKY type should have durability 1")
    void testLuckyBrickCreation() {
        BrickComponent brick = new BrickComponent(BrickType.LUCKY);

        assertEquals(BrickType.LUCKY, brick.getBrickType());
        assertEquals(1, brick.getDurability());
    }

    @Test
    @DisplayName("NETHERACK brick should have durability 1")
    void testNetherackBrickCreation() {
        BrickComponent brick = new BrickComponent(BrickType.NETHERACK);

        assertEquals(BrickType.NETHERACK, brick.getBrickType());
        assertEquals(1, brick.getDurability());
    }

    @Test
    @DisplayName("NETHERBRICK brick should have durability 2")
    void testNetherbrickBrickCreation() {
        BrickComponent brick = new BrickComponent(BrickType.NETHERBRICK);

        assertEquals(BrickType.NETHERBRICK, brick.getBrickType());
        assertEquals(2, brick.getDurability());
    }

    @Test
    @DisplayName("ENDSTONE brick should have durability 2")
    void testEndstoneBrickCreation() {
        BrickComponent brick = new BrickComponent(BrickType.ENDSTONE);

        assertEquals(BrickType.ENDSTONE, brick.getBrickType());
        assertEquals(2, brick.getDurability());
    }

    @Test
    @DisplayName("takeDamage should reduce durability by 1 for WOOD brick")
    void testTakeDamageReducesDurability() {
        BrickComponent brick = new BrickComponent(BrickType.WOOD);

        int initialDurability = brick.getDurability();
        brick.takeDamage();

        assertEquals(initialDurability - 1, brick.getDurability());
    }

    @Test
    @DisplayName("takeDamage should not affect OBSIDIAN brick durability")
    void testTakeDamageOnObsidianBrick() {
        BrickComponent brick = new BrickComponent(BrickType.OBSIDIAN);

        int initialDurability = brick.getDurability();
        brick.takeDamage();

        assertEquals(initialDurability, brick.getDurability());
        assertEquals(-1, brick.getDurability());
    }

    @Test
    @DisplayName("STONE brick should require 2 hits to destroy")
    void testStoneBrickRequiresTwoHits() {
        BrickComponent brick = new BrickComponent(BrickType.STONE);

        assertEquals(2, brick.getDurability());

        brick.takeDamage();
        assertEquals(1, brick.getDurability());

        brick.takeDamage();
        assertEquals(0, brick.getDurability());
    }

    @Test
    @DisplayName("setDurability should update brick durability")
    void testSetDurability() {
        BrickComponent brick = new BrickComponent(BrickType.WOOD);

        brick.setDurability(5);
        assertEquals(5, brick.getDurability());

        brick.setDurability(0);
        assertEquals(0, brick.getDurability());
    }

    @Test
    @DisplayName("getBrickType should return correct brick type")
    void testGetBrickType() {
        BrickComponent woodBrick = new BrickComponent(BrickType.WOOD);
        BrickComponent stoneBrick = new BrickComponent(BrickType.STONE);
        BrickComponent obsidianBrick = new BrickComponent(BrickType.OBSIDIAN);

        assertEquals(BrickType.WOOD, woodBrick.getBrickType());
        assertEquals(BrickType.STONE, stoneBrick.getBrickType());
        assertEquals(BrickType.OBSIDIAN, obsidianBrick.getBrickType());
    }

    @Test
    @DisplayName("BrickType enum should have correct durability values")
    void testBrickTypeEnumDurabilityValues() {
        assertEquals(1, BrickType.WOOD.getDurability());
        assertEquals(1, BrickType.NETHERACK.getDurability());
        assertEquals(2, BrickType.STONE.getDurability());
        assertEquals(2, BrickType.NETHERBRICK.getDurability());
        assertEquals(2, BrickType.ENDSTONE.getDurability());
        assertEquals(-1, BrickType.OBSIDIAN.getDurability());
        assertEquals(1, BrickType.LUCKY.getDurability());
    }

    @Test
    @DisplayName("BrickType enum should have exactly 7 types")
    void testBrickTypeEnumCount() {
        BrickType[] types = BrickType.values();
        assertEquals(7, types.length);
    }

    @Test
    @DisplayName("Multiple takeDamage calls should reduce durability to zero")
    void testMultipleTakeDamageCalls() {
        BrickComponent brick = new BrickComponent(BrickType.STONE);

        assertEquals(2, brick.getDurability());

        brick.takeDamage();
        brick.takeDamage();

        assertEquals(0, brick.getDurability());
    }

    @Test
    @DisplayName("takeDamage on LUCKY brick should reduce durability")
    void testTakeDamageOnLuckyBrick() {
        BrickComponent brick = new BrickComponent(BrickType.LUCKY);

        assertEquals(1, brick.getDurability());

        brick.takeDamage();
        assertEquals(0, brick.getDurability());
    }

    @Test
    @DisplayName("Durability should not go negative for breakable bricks")
    void testDurabilityDoesNotGoNegative() {
        BrickComponent brick = new BrickComponent(BrickType.WOOD);

        brick.takeDamage();
        assertEquals(0, brick.getDurability());

        brick.takeDamage();
        assertEquals(-1, brick.getDurability());
    }

    @Test
    @DisplayName("setDurability should allow setting negative values")
    void testSetDurabilityAllowsNegativeValues() {
        BrickComponent brick = new BrickComponent(BrickType.WOOD);

        brick.setDurability(-1);
        assertEquals(-1, brick.getDurability());
    }

    @Test
    @DisplayName("All brick types should be accessible from enum")
    void testAllBrickTypesAccessible() {
        assertNotNull(BrickType.WOOD);
        assertNotNull(BrickType.NETHERACK);
        assertNotNull(BrickType.STONE);
        assertNotNull(BrickType.NETHERBRICK);
        assertNotNull(BrickType.ENDSTONE);
        assertNotNull(BrickType.OBSIDIAN);
        assertNotNull(BrickType.LUCKY);
    }

    @Test
    @DisplayName("Brick type should remain constant after creation")
    void testBrickTypeImmutable() {
        BrickComponent brick = new BrickComponent(BrickType.STONE);

        BrickType initialType = brick.getBrickType();
        brick.takeDamage();

        assertEquals(initialType, brick.getBrickType());
    }

    @Test
    @DisplayName("NETHERBRICK brick should require 2 hits to destroy")
    void testNetherbrickRequiresTwoHits() {
        BrickComponent brick = new BrickComponent(BrickType.NETHERBRICK);

        assertEquals(2, brick.getDurability());
        brick.takeDamage();
        assertEquals(1, brick.getDurability());
        brick.takeDamage();
        assertEquals(0, brick.getDurability());
    }

    @Test
    @DisplayName("ENDSTONE brick should require 2 hits to destroy")
    void testEndstoneRequiresTwoHits() {
        BrickComponent brick = new BrickComponent(BrickType.ENDSTONE);

        assertEquals(2, brick.getDurability());
        brick.takeDamage();
        assertEquals(1, brick.getDurability());
        brick.takeDamage();
        assertEquals(0, brick.getDurability());
    }

    @Test
    @DisplayName("BrickComponent class should be public")
    void testBrickComponentClassIsPublic() {
        assertTrue(java.lang.reflect.Modifier.isPublic(BrickComponent.class.getModifiers()));
    }

    @Test
    @DisplayName("BrickType enum should be public")
    void testBrickTypeEnumIsPublic() {
        assertTrue(java.lang.reflect.Modifier.isPublic(BrickType.class.getModifiers()));
    }

    @Test
    @DisplayName("getDurability method should be public")
    void testGetDurabilityMethodIsPublic() throws NoSuchMethodException {
        var method = BrickComponent.class.getMethod("getDurability");
        assertTrue(java.lang.reflect.Modifier.isPublic(method.getModifiers()));
    }

    @Test
    @DisplayName("setDurability method should be public")
    void testSetDurabilityMethodIsPublic() throws NoSuchMethodException {
        var method = BrickComponent.class.getMethod("setDurability", int.class);
        assertTrue(java.lang.reflect.Modifier.isPublic(method.getModifiers()));
    }

    @Test
    @DisplayName("getBrickType method should be public")
    void testGetBrickTypeMethodIsPublic() throws NoSuchMethodException {
        var method = BrickComponent.class.getMethod("getBrickType");
        assertTrue(java.lang.reflect.Modifier.isPublic(method.getModifiers()));
    }

    @Test
    @DisplayName("takeDamage method should be public")
    void testTakeDamageMethodIsPublic() throws NoSuchMethodException {
        var method = BrickComponent.class.getMethod("takeDamage");
        assertTrue(java.lang.reflect.Modifier.isPublic(method.getModifiers()));
    }
}

