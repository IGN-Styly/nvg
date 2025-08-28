package org.styly.efm;

import net.minecraft.world.phys.AABB;

public class AABBAlignedBoundingBox {
    private final double minX;
    private final double minY;
    private final double minZ;
    private final double maxX;
    private final double maxY;
    private final double maxZ;

    public AABBAlignedBoundingBox(double minX, double minY, double minZ, double maxX, double maxY, double maxZ) {
        this.minX = minX;
        this.minY = minY;
        this.minZ = minZ;
        this.maxX = maxX;
        this.maxY = maxY;
        this.maxZ = maxZ;
    }

    public AABB createAABB(AABB original) {
        double sizeX = original.getXsize();
        double sizeY = original.getYsize();
        double sizeZ = original.getZsize();
        double newMinX = original.minX + (sizeX * minX);
        double newMinY = original.minY + (sizeY * minY);
        double newMinZ = original.minZ + (sizeZ * minZ);
        double newMaxX = original.minX + (sizeX * maxX);
        double newMaxY = original.minY + (sizeY * maxY);
        double newMaxZ = original.minZ + (sizeZ * maxZ);
        return new AABB(newMinX, newMinY, newMinZ, newMaxX, newMaxY, newMaxZ);
    }
}
