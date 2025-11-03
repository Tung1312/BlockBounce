package com.birb_birb.blockbounce.utils.saveload;

import java.io.Serial;
import java.io.Serializable;

/**
 * Data structure for individual block state
 */
public class BlockData implements Serializable {
    @Serial
    private static final long serialVersionUID = 2L; // Incremented for compatibility

    private double x;
    private double y;
    private String color; // Color as string (e.g., "RED", "BLUE", "GREEN")
    private int hits; // Number of hits remaining (deprecated, use durability instead)
    private String brickType; // "WOOD", "STONE", "NETHERACK", "NETHERBRICK", "ENDSTONE", "OBSIDIAN", "LUCKY"
    private int durability; // Current durability of the brick

    public BlockData() {}

    public BlockData(double x, double y, String color, int hits) {
        this.x = x;
        this.y = y;
        this.color = color;
        this.hits = hits;
        this.brickType = "WOOD"; // Default for backward compatibility
        this.durability = hits;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public int getHits() {
        return hits;
    }

    public void setHits(int hits) {
        this.hits = hits;
    }

    public String getBrickType() {
        return brickType;
    }

    public void setBrickType(String brickType) {
        this.brickType = brickType;
    }

    public int getDurability() {
        return durability;
    }

    public void setDurability(int durability) {
        this.durability = durability;
    }
}
