package com.birb_birb.blockbounce.entities;

import com.almasb.fxgl.entity.component.Component;
import com.almasb.fxgl.texture.Texture;
import com.birb_birb.blockbounce.constants.BrickType;
import com.birb_birb.blockbounce.constants.GameConstants;
import com.birb_birb.blockbounce.utils.TextureManager;

import static com.almasb.fxgl.dsl.FXGL.getAssetLoader;

public class BrickComponent extends Component {

    private int durability;
    private final BrickType brickType;

    public BrickComponent() {
        this(BrickType.WOOD);
    }

    public BrickComponent(BrickType type) {
        this.brickType = type;
        this.durability = type.getDurability();
    }

    public void takeDamage() {
        // skip unbreakable
        if (brickType.getDurability() == -1) {
            return;
        }

        durability--;
        if (durability == 1) {
            if (brickType == BrickType.STONE || brickType == BrickType.NETHERBRICK || brickType == BrickType.ENDSTONE) {
                // Only update texture if component is attached to an entity
                if (entity != null) {
                    updateTextureToCracked();
                }
            }
        }
    }

    private void updateTextureToCracked() {
        String crackedTexturePath;
        switch (brickType) {
            case STONE:
                crackedTexturePath = GameConstants.STONE_CRACKED_TEXTURE;
                break;
            case NETHERBRICK:
                crackedTexturePath = GameConstants.NETHERBRICK_CRACKED_TEXTURE;
                break;
            case ENDSTONE:
                crackedTexturePath = GameConstants.ENDSTONE_CRACKED_TEXTURE;
                break;
            default:
                return;
        }

        Texture crackedTexture = TextureManager.loadTextureCopy(
            getAssetLoader().loadTexture(crackedTexturePath),
            GameConstants.BRICK_SIZE,
            GameConstants.BRICK_SIZE
        );
        entity.getViewComponent().clearChildren();
        entity.getViewComponent().addChild(crackedTexture);
    }

    public int getDurability() {
        return durability;
    }

    public void setDurability(int durability) {
        this.durability = durability;
    }

    public BrickType getBrickType() {
        return brickType;
    }

    public void destroy() {
        entity.removeFromWorld();
    }
}
