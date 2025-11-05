package com.birb_birb.blockbounce.constants;

public enum BrickType {
    WOOD(1),
    NETHERACK(1),
    STONE(2),
    NETHERBRICK(2),
    ENDSTONE(2),
    OBSIDIAN(-1),
    LUCKY(1);

    private final int durability;

    BrickType(int durability) {
        this.durability = durability;
    }

    public int getDurability() {
        return durability;
    }
}
