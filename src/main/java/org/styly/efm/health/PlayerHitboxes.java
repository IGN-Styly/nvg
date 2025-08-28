package org.styly.efm.health;

import java.util.EnumMap;
import java.util.Map;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.phys.AABB;
import org.styly.efm.AABBAlignedBoundingBox;

public class PlayerHitboxes {

    public static AABB getHeadHitbox(Entity player) {
        return new AABBAlignedBoundingBox(0D, 0.8D, 0D, 1D, 1D, 1D).createAABB(
            player.getBoundingBox()
        );
    }

    public static AABB getTorsoHitbox(Entity player) {
        return new AABBAlignedBoundingBox(
            0D,
            0.45D,
            0D,
            1D,
            0.8D,
            1D
        ).createAABB(player.getBoundingBox());
    }

    public static AABB getLegsHitbox(Entity player) {
        return new AABBAlignedBoundingBox(
            0D,
            0.0D,
            0D,
            1D,
            0.45D,
            1D
        ).createAABB(player.getBoundingBox());
    }



}
