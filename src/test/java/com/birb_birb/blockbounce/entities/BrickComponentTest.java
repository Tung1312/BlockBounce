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


}

