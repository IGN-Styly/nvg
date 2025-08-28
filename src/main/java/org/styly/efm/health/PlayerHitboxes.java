package org.styly.efm.health;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.AABB;
import org.styly.efm.AABBAlignedBoundingBox;

public class PlayerHitboxes {
    public static AABB getTorsoHitbox(Entity player) {
        return player.getBoundingBox().deflate(0.2, 0.5, 0.2);
    }

    public static AABB getHeadHitbox(Entity player) {
        return new AABBAlignedBoundingBox(0D, 0.8D, 0D, 1D, 1D, 1D).createAABB(player.getBoundingBox());
    }
}
