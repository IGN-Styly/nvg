package org.styly.efm.health;

import static org.styly.efm.EFM.LOGGER;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent;
import net.neoforged.neoforge.event.entity.living.LivingEvent;
import net.neoforged.neoforge.event.entity.player.AttackEntityEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import org.styly.efm.EFM;

@EventBusSubscriber(modid = EFM.MODID, bus = EventBusSubscriber.Bus.GAME)
public class PlayerHitboxHandler {

    @SubscribeEvent
    public static void onPlayerAttack(LivingDamageEvent.Post event) {
        if (!(event.getEntity() instanceof Player player)) return;
        LOGGER.info("Attacked");
        // Get extra hitboxes
        AABB torso = PlayerHitboxes.getTorsoHitbox(player);
        AABB head = PlayerHitboxes.getHeadHitbox(player);

        if (event.getSource().getEntity() instanceof LivingEntity attacker) {
            // Get attacker's position (eye position for proper raycast origin)
            Vec3 attackerPos = attacker.getEyePosition();
            // Get attack direction (look vector)
            Vec3 lookVec = attacker.getLookAngle();
            // Determine reasonable range for the attack
            double attackRange = 5.0; // Can be adjusted based on weapon/reach
            // Calculate the end point of the ray
            Vec3 endPos = attackerPos.add(
                lookVec.multiply(attackRange, attackRange, attackRange)
            );

            // Check for intersection with head (check head first as it's usually prioritized)
            HitResult headHit = rayTraceAABB(attackerPos, endPos, head);
            // Check for intersection with torso
            HitResult torsoHit = rayTraceAABB(attackerPos, endPos, torso);

            // Calculate distances if hits occurred
            double headDist = headHit != null
                ? attackerPos.distanceToSqr(headHit.getLocation())
                : Double.MAX_VALUE;
            double torsoDist = torsoHit != null
                ? attackerPos.distanceToSqr(torsoHit.getLocation())
                : Double.MAX_VALUE;

            // Determine which hitbox was hit first (closest to attacker)
            if (
                headHit != null && (torsoHit == null || headDist <= torsoDist)
            ) {
                System.out.println("Head hit!");
                return;
            } else if (torsoHit != null) {
                System.out.println("Torso hit!");
                return;
            }

            System.out.println("Hit outside custom hitboxes");
        }
    }

    /**
     * Performs a ray trace against an AABB
     * @param start Starting position of the ray
     * @param end Ending position of the ray
     * @param aabb The bounding box to test against
     * @return HitResult if the ray intersects the AABB, null otherwise
     */
    private static HitResult rayTraceAABB(Vec3 start, Vec3 end, AABB aabb) {
        // Check if the ray intersects the AABB
        Vec3 dir = end.subtract(start);
        double length = dir.length();
        dir = dir.normalize();

        // Calculate intersection with each face of the AABB
        double tmin = (aabb.minX - start.x) / (dir.x == 0 ? 0.00001 : dir.x);
        double tmax = (aabb.maxX - start.x) / (dir.x == 0 ? 0.00001 : dir.x);

        if (tmin > tmax) {
            double temp = tmin;
            tmin = tmax;
            tmax = temp;
        }

        double tymin = (aabb.minY - start.y) / (dir.y == 0 ? 0.00001 : dir.y);
        double tymax = (aabb.maxY - start.y) / (dir.y == 0 ? 0.00001 : dir.y);

        if (tymin > tymax) {
            double temp = tymin;
            tymin = tymax;
            tymax = temp;
        }

        if ((tmin > tymax) || (tymin > tmax)) {
            return null;
        }

        if (tymin > tmin) {
            tmin = tymin;
        }

        if (tymax < tmax) {
            tmax = tymax;
        }

        double tzmin = (aabb.minZ - start.z) / (dir.z == 0 ? 0.00001 : dir.z);
        double tzmax = (aabb.maxZ - start.z) / (dir.z == 0 ? 0.00001 : dir.z);

        if (tzmin > tzmax) {
            double temp = tzmin;
            tzmin = tzmax;
            tzmax = temp;
        }

        if ((tmin > tzmax) || (tzmin > tmax)) {
            return null;
        }

        if (tzmin > tmin) {
            tmin = tzmin;
        }

        if (tzmax < tmax) {
            tmax = tzmax;
        }

        // If tmin is negative, the ray starts inside the AABB
        if (tmin < 0) {
            tmin = 0;
        }

        // Check if intersection is within ray length
        if (tmin > length) {
            return null;
        }

        // Calculate hit position
        Vec3 hitPos = start.add(dir.multiply(tmin, tmin, tmin));

        return new HitResult(hitPos) {
            @Override
            public Type getType() {
                return Type.BLOCK;
            }
        };
    }
}
