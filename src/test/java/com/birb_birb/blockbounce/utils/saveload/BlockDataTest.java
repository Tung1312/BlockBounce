package com.birb_birb.blockbounce.utils.saveload;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.BeforeEach;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for BlockData class
 * Tests block data structure and serialization capabilities
 */
@DisplayName("Block Data Tests")
class BlockDataTest {

    private BlockData blockData;

    @BeforeEach
    void setUp() {
        blockData = new BlockData();
    }

    @Test
    @DisplayName("Default constructor should create instance successfully")
    void testDefaultConstructor() {
        assertNotNull(blockData);
    }

    @Test
    @DisplayName("Constructor with parameters should set all values correctly")
    void testParameterizedConstructor() {
        BlockData block = new BlockData(100.0, 200.0, "RED", 2);

        assertEquals(100.0, block.getX(), 0.001);
        assertEquals(200.0, block.getY(), 0.001);
        assertEquals("RED", block.getColor());
        assertEquals(2, block.getHits());
        assertEquals(2, block.getDurability());
        assertEquals("WOOD", block.getBrickType());
    }

    @Test
    @DisplayName("setX and getX should work correctly")
    void testXGetterSetter() {
        blockData.setX(150.5);
        assertEquals(150.5, blockData.getX(), 0.001);
    }

    @Test
    @DisplayName("setY and getY should work correctly")
    void testYGetterSetter() {
        blockData.setY(250.75);
        assertEquals(250.75, blockData.getY(), 0.001);
    }

    @Test
    @DisplayName("setColor and getColor should work correctly")
    void testColorGetterSetter() {
        blockData.setColor("BLUE");
        assertEquals("BLUE", blockData.getColor());
    }

    @Test
    @DisplayName("setHits and getHits should work correctly")
    void testHitsGetterSetter() {
        blockData.setHits(3);
        assertEquals(3, blockData.getHits());
    }

    @Test
    @DisplayName("setBrickType and getBrickType should work correctly")
    void testBrickTypeGetterSetter() {
        blockData.setBrickType("STONE");
        assertEquals("STONE", blockData.getBrickType());
    }

    @Test
    @DisplayName("setDurability and getDurability should work correctly")
    void testDurabilityGetterSetter() {
        blockData.setDurability(5);
        assertEquals(5, blockData.getDurability());
    }

    @Test
    @DisplayName("X coordinate should accept negative values")
    void testXAcceptsNegativeValues() {
        blockData.setX(-50.0);
        assertEquals(-50.0, blockData.getX(), 0.001);
    }

    @Test
    @DisplayName("Y coordinate should accept negative values")
    void testYAcceptsNegativeValues() {
        blockData.setY(-100.0);
        assertEquals(-100.0, blockData.getY(), 0.001);
    }

    @Test
    @DisplayName("Color should accept any string value")
    void testColorAcceptsAnyString() {
        blockData.setColor("CUSTOM_COLOR");
        assertEquals("CUSTOM_COLOR", blockData.getColor());

        blockData.setColor("");
        assertEquals("", blockData.getColor());
    }

    @Test
    @DisplayName("Hits should accept zero value")
    void testHitsAcceptsZero() {
        blockData.setHits(0);
        assertEquals(0, blockData.getHits());
    }

    @Test
    @DisplayName("Hits should accept negative values")
    void testHitsAcceptsNegative() {
        blockData.setHits(-1);
        assertEquals(-1, blockData.getHits());
    }

    @Test
    @DisplayName("Durability should accept zero value")
    void testDurabilityAcceptsZero() {
        blockData.setDurability(0);
        assertEquals(0, blockData.getDurability());
    }

    @Test
    @DisplayName("Durability should accept negative values for unbreakable blocks")
    void testDurabilityAcceptsNegative() {
        blockData.setDurability(-1);
        assertEquals(-1, blockData.getDurability());
    }

    @Test
    @DisplayName("BrickType should support all game brick types")
    void testBrickTypeSupportAllTypes() {
        String[] brickTypes = {"WOOD", "STONE", "NETHERACK", "NETHERBRICK", "ENDSTONE", "OBSIDIAN", "LUCKY"};

        for (String type : brickTypes) {
            blockData.setBrickType(type);
            assertEquals(type, blockData.getBrickType());
        }
    }

    @Test
    @DisplayName("BlockData should implement Serializable")
    void testBlockDataImplementsSerializable() {
        assertTrue(java.io.Serializable.class.isAssignableFrom(BlockData.class));
    }

    @Test
    @DisplayName("Multiple properties can be set independently")
    void testMultiplePropertiesIndependent() {
        blockData.setX(10.0);
        blockData.setY(20.0);
        blockData.setColor("GREEN");
        blockData.setHits(2);
        blockData.setBrickType("STONE");
        blockData.setDurability(2);

        assertEquals(10.0, blockData.getX(), 0.001);
        assertEquals(20.0, blockData.getY(), 0.001);
        assertEquals("GREEN", blockData.getColor());
        assertEquals(2, blockData.getHits());
        assertEquals("STONE", blockData.getBrickType());
        assertEquals(2, blockData.getDurability());
    }

    @Test
    @DisplayName("BlockData class should be public")
    void testBlockDataClassIsPublic() {
        assertTrue(java.lang.reflect.Modifier.isPublic(BlockData.class.getModifiers()));
    }

    @Test
    @DisplayName("getX method should be public")
    void testGetXMethodIsPublic() throws NoSuchMethodException {
        var method = BlockData.class.getMethod("getX");
        assertTrue(java.lang.reflect.Modifier.isPublic(method.getModifiers()));
    }

    @Test
    @DisplayName("setX method should be public")
    void testSetXMethodIsPublic() throws NoSuchMethodException {
        var method = BlockData.class.getMethod("setX", double.class);
        assertTrue(java.lang.reflect.Modifier.isPublic(method.getModifiers()));
    }

    @Test
    @DisplayName("getY method should be public")
    void testGetYMethodIsPublic() throws NoSuchMethodException {
        var method = BlockData.class.getMethod("getY");
        assertTrue(java.lang.reflect.Modifier.isPublic(method.getModifiers()));
    }

    @Test
    @DisplayName("setY method should be public")
    void testSetYMethodIsPublic() throws NoSuchMethodException {
        var method = BlockData.class.getMethod("setY", double.class);
        assertTrue(java.lang.reflect.Modifier.isPublic(method.getModifiers()));
    }

    @Test
    @DisplayName("getColor method should be public")
    void testGetColorMethodIsPublic() throws NoSuchMethodException {
        var method = BlockData.class.getMethod("getColor");
        assertTrue(java.lang.reflect.Modifier.isPublic(method.getModifiers()));
    }

    @Test
    @DisplayName("setColor method should be public")
    void testSetColorMethodIsPublic() throws NoSuchMethodException {
        var method = BlockData.class.getMethod("setColor", String.class);
        assertTrue(java.lang.reflect.Modifier.isPublic(method.getModifiers()));
    }

    @Test
    @DisplayName("getBrickType method should be public")
    void testGetBrickTypeMethodIsPublic() throws NoSuchMethodException {
        var method = BlockData.class.getMethod("getBrickType");
        assertTrue(java.lang.reflect.Modifier.isPublic(method.getModifiers()));
    }

    @Test
    @DisplayName("setBrickType method should be public")
    void testSetBrickTypeMethodIsPublic() throws NoSuchMethodException {
        var method = BlockData.class.getMethod("setBrickType", String.class);
        assertTrue(java.lang.reflect.Modifier.isPublic(method.getModifiers()));
    }

    @Test
    @DisplayName("getDurability method should be public")
    void testGetDurabilityMethodIsPublic() throws NoSuchMethodException {
        var method = BlockData.class.getMethod("getDurability");
        assertTrue(java.lang.reflect.Modifier.isPublic(method.getModifiers()));
    }

    @Test
    @DisplayName("setDurability method should be public")
    void testSetDurabilityMethodIsPublic() throws NoSuchMethodException {
        var method = BlockData.class.getMethod("setDurability", int.class);
        assertTrue(java.lang.reflect.Modifier.isPublic(method.getModifiers()));
    }

    @Test
    @DisplayName("getX should return double type")
    void testGetXReturnType() throws NoSuchMethodException {
        var method = BlockData.class.getMethod("getX");
        assertEquals(double.class, method.getReturnType());
    }

    @Test
    @DisplayName("getY should return double type")
    void testGetYReturnType() throws NoSuchMethodException {
        var method = BlockData.class.getMethod("getY");
        assertEquals(double.class, method.getReturnType());
    }

    @Test
    @DisplayName("getColor should return String type")
    void testGetColorReturnType() throws NoSuchMethodException {
        var method = BlockData.class.getMethod("getColor");
        assertEquals(String.class, method.getReturnType());
    }

    @Test
    @DisplayName("getBrickType should return String type")
    void testGetBrickTypeReturnType() throws NoSuchMethodException {
        var method = BlockData.class.getMethod("getBrickType");
        assertEquals(String.class, method.getReturnType());
    }

    @Test
    @DisplayName("getDurability should return int type")
    void testGetDurabilityReturnType() throws NoSuchMethodException {
        var method = BlockData.class.getMethod("getDurability");
        assertEquals(int.class, method.getReturnType());
    }

    @Test
    @DisplayName("BlockData should have public no-arg constructor")
    void testBlockDataHasPublicNoArgConstructor() {
        var constructors = BlockData.class.getConstructors();

        boolean hasPublicNoArgConstructor = false;
        for (var constructor : constructors) {
            if (java.lang.reflect.Modifier.isPublic(constructor.getModifiers())
                && constructor.getParameterCount() == 0) {
                hasPublicNoArgConstructor = true;
                break;
            }
        }
        assertTrue(hasPublicNoArgConstructor, "Should have public no-arg constructor");
    }

    @Test
    @DisplayName("BlockData should have public parameterized constructor")
    void testBlockDataHasPublicParameterizedConstructor() {
        var constructors = BlockData.class.getConstructors();

        boolean hasPublicParameterizedConstructor = false;
        for (var constructor : constructors) {
            if (java.lang.reflect.Modifier.isPublic(constructor.getModifiers())
                && constructor.getParameterCount() == 4) {
                hasPublicParameterizedConstructor = true;
                break;
            }
        }
        assertTrue(hasPublicParameterizedConstructor, "Should have public parameterized constructor");
    }

    @Test
    @DisplayName("Parameterized constructor should set brick type to WOOD by default")
    void testParameterizedConstructorDefaultBrickType() {
        BlockData block = new BlockData(0, 0, "RED", 1);
        assertEquals("WOOD", block.getBrickType());
    }

    @Test
    @DisplayName("Coordinates should support precise decimal values")
    void testCoordinatesPrecision() {
        blockData.setX(123.456789);
        blockData.setY(987.654321);

        assertEquals(123.456789, blockData.getX(), 0.000001);
        assertEquals(987.654321, blockData.getY(), 0.000001);
    }

    @Test
    @DisplayName("Color can be null")
    void testColorCanBeNull() {
        blockData.setColor(null);
        assertNull(blockData.getColor());
    }

    @Test
    @DisplayName("BrickType can be null")
    void testBrickTypeCanBeNull() {
        blockData.setBrickType(null);
        assertNull(blockData.getBrickType());
    }
}

