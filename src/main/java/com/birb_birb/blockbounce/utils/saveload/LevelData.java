package com.birb_birb.blockbounce.utils.saveload;

import com.birb_birb.blockbounce.constants.GameConstants;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Data structure for level configuration loaded from JSON files.
 * Uses a grid-based approach for easy level creation.
 *
 * Letter codes in grid:
 * W = WOOD, S = STONE, R = NETHERACK (note: changed from N to R)
 * N = NETHERBRICK, E = ENDSTONE, O = OBSIDIAN
 * . or space = Empty space (no brick)
 */
public class LevelData {
    private int levelId;
    private double startX;
    private double startY;
    private List<String> grid;

    private static final Map<Character, String> BRICK_TYPE_MAP = new HashMap<>();
    private static final Map<String, Integer> DURABILITY_MAP = new HashMap<>();

    static {
        BRICK_TYPE_MAP.put('W', "WOOD");
        BRICK_TYPE_MAP.put('S', "STONE");
        BRICK_TYPE_MAP.put('R', "NETHERACK");
        BRICK_TYPE_MAP.put('N', "NETHERBRICK");
        BRICK_TYPE_MAP.put('E', "ENDSTONE");
        BRICK_TYPE_MAP.put('O', "OBSIDIAN");
        BRICK_TYPE_MAP.put('L', "LUCKY");

        DURABILITY_MAP.put("WOOD", 1);
        DURABILITY_MAP.put("STONE", 2);
        DURABILITY_MAP.put("NETHERACK", 1);
        DURABILITY_MAP.put("NETHERBRICK", 2);
        DURABILITY_MAP.put("ENDSTONE", 2);
        DURABILITY_MAP.put("OBSIDIAN", -1);
        DURABILITY_MAP.put("LUCKY", 1);
    }

    /**
     * Convert the grid pattern into a list of BlockData objects
     * @return List of blocks with positions and types
     */
    public List<BlockData> getBricks() {
        List<BlockData> bricks = new ArrayList<>();

        for (int row = 0; row < grid.size(); row++) {
            String line = grid.get(row);
            for (int col = 0; col < line.length(); col++) {
                char code = line.charAt(col);
                if (code != '.' && code != ' ') {
                    String brickType = BRICK_TYPE_MAP.get(code);
                    if (brickType != null) {
                        BlockData brick = new BlockData();
                        brick.setX(startX + col * GameConstants.BRICK_SIZE);
                        brick.setY(startY + row * GameConstants.BRICK_SIZE);
                        brick.setBrickType(brickType);
                        brick.setDurability(DURABILITY_MAP.get(brickType));
                        brick.setColor(getColorForType(brickType));
                        bricks.add(brick);
                    }
                }
            }
        }

        return bricks;
    }

    /**
     * Get a default color name for each brick type
     * @param brickType The brick type
     * @return Color name as string
     */
    private String getColorForType(String brickType) {
        return switch (brickType) {
            case "WOOD" -> "BROWN";
            case "STONE" -> "GRAY";
            case "NETHERACK" -> "RED";
            case "NETHERBRICK" -> "DARK_RED";
            case "ENDSTONE" -> "YELLOW";
            case "OBSIDIAN" -> "BLACK";
            case "LUCKY" -> "GOLD";
            default -> "WHITE";
        };
    }

    // Getters and setters
    public int getLevelId() {
        return levelId;
    }

    public void setLevelId(int levelId) {
        this.levelId = levelId;
    }

    public double getStartX() {
        return startX;
    }

    public void setStartX(double startX) {
        this.startX = startX;
    }

    public double getStartY() {
        return startY;
    }

    public void setStartY(double startY) {
        this.startY = startY;
    }

    public List<String> getGrid() {
        return grid;
    }

    public void setGrid(List<String> grid) {
        this.grid = grid;
    }
}

