package org.styly.efm.health;

import static org.styly.efm.EFM.LOGGER;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent;
import org.styly.efm.EFM;

@EventBusSubscriber(modid = EFM.MODID, bus = EventBusSubscriber.Bus.GAME)
public class PlayerHitboxHandler {

    // Damage multipliers for different body parts
    private static final float HEAD_DAMAGE_MULTIPLIER = 2.0F; // Critical hits to the head
    private static final float TORSO_DAMAGE_MULTIPLIER = 1.0F; // Standard damage to the torso
    private static final float LEFT_ARM_DAMAGE_MULTIPLIER = 0.7F; // Reduced damage to arms
    private static final float RIGHT_ARM_DAMAGE_MULTIPLIER = 0.7F;
    private static final float LEFT_LEG_DAMAGE_MULTIPLIER = 0.75F; // Reduced damage to legs
    private static final float RIGHT_LEG_DAMAGE_MULTIPLIER = 0.75F;
    private static final float FEET_DAMAGE_MULTIPLIER = 0.5F; // Minimal damage to feet

    // Side enum for differentiating left and right body parts
    private enum BodySide {
        LEFT,
        RIGHT,
    }

    @SubscribeEvent
    public static void onPlayerAttack(LivingDamageEvent.Post event) {
        if (!(event.getEntity() instanceof Player player)) return;
        LOGGER.info(
            "Player " + player.getName().getString() + " was attacked!"
        );
        // Get all player hitboxes
        AABB head = PlayerHitboxes.getHeadHitbox(player);
        AABB torso = PlayerHitboxes.getTorsoHitbox(player);
        AABB legs = PlayerHitboxes.getLegsHitbox(player);

        // Store original damage value
        float originalDamage = event.getOriginalDamage();

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

            // Check for intersection with all hitboxes
            HitResult headHit = rayTraceAABB(attackerPos, endPos, head);
            HitResult torsoHit = rayTraceAABB(attackerPos, endPos, torso);
            HitResult legsHit = rayTraceAABB(attackerPos, endPos, legs);

            // Calculate distances if hits occurred
            double headDist = headHit != null
                ? attackerPos.distanceToSqr(headHit.getLocation())
                : Double.MAX_VALUE;
            double torsoDist = torsoHit != null
                ? attackerPos.distanceToSqr(torsoHit.getLocation())
                : Double.MAX_VALUE;
            double legsDist = legsHit != null
                ? attackerPos.distanceToSqr(legsHit.getLocation())
                : Double.MAX_VALUE;

            // Find the closest hit
            double minDist = Math.min(
                Math.min(headDist, torsoDist),
                Math.min(legsDist, legsDist)
            );

            // Determine which hitbox was hit first (closest to attacker)
            if (headHit != null && headDist == minDist) {
                LOGGER.info("Head hit! Critical damage applied!");
                applyDamageModifier(
                    event,
                    originalDamage,
                    HEAD_DAMAGE_MULTIPLIER
                );
                return;
            } else if (torsoHit != null && torsoDist == minDist) {
                // Check if the hit was on the edge of the torso (arms)
                BodySide armSide = determineArmHitSide(
                    player,
                    torsoHit.getLocation(),
                    torso
                );

                if (armSide == BodySide.LEFT) {
                    LOGGER.info(
                        "Left arm hit! Reduced weapon accuracy applied!"
                    );
                    applyDamageModifier(
                        event,
                        originalDamage,
                        LEFT_ARM_DAMAGE_MULTIPLIER
                    );
                    // Could add left arm specific effects here (e.g., reduced accuracy)
                } else if (armSide == BodySide.RIGHT) {
                    LOGGER.info("Right arm hit! Reduced attack speed applied!");
                    applyDamageModifier(
                        event,
                        originalDamage,
                        RIGHT_ARM_DAMAGE_MULTIPLIER
                    );
                    // Could add right arm specific effects here (e.g., reduced attack speed)
                } else {
                    LOGGER.info("Central torso hit! Standard damage applied!");
                    applyDamageModifier(
                        event,
                        originalDamage,
                        TORSO_DAMAGE_MULTIPLIER
                    );
                }
                return;
            } else if (legsHit != null && legsDist == minDist) {
                // Determine which leg was hit (left or right) based on player orientation and hit position
                BodySide legSide = determineHitSide(
                    player,
                    legsHit.getLocation()
                );

                if (legSide == BodySide.LEFT) {
                    LOGGER.info(
                        "Left leg hit! Apply specific left leg effects!"
                    );
                    applyDamageModifier(
                        event,
                        originalDamage,
                        LEFT_LEG_DAMAGE_MULTIPLIER
                    );
                    // Could add left-specific effects here
                } else {
                    LOGGER.info(
                        "Right leg hit! Apply specific right leg effects!"
                    );
                    applyDamageModifier(
                        event,
                        originalDamage,
                        RIGHT_LEG_DAMAGE_MULTIPLIER
                    );
                    // Could add right-specific effects here
                }
                // Could also add a slowness effect here
                return;
            } else {
                LOGGER.info("Hit outside custom hitboxes");
            }
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

    /**
     * Determines which side (left or right) was hit based on the hit position and player orientation.
     * This uses mathematical transformations to create the effect of separate left/right hitboxes
     * without actually splitting the AABB, which cannot be rotated.
     *
     * @param player The player that was hit
     * @param hitPosition The position where the ray hit the hitbox
     * @return LEFT or RIGHT enum value indicating which side was hit
     */
    private static BodySide determineHitSide(Player player, Vec3 hitPosition) {
        // Get player rotation (yaw) in radians
        float playerYaw = (float) Math.toRadians(player.getYRot());

        // Get player position (center point)
        Vec3 playerPos = player.position().add(0, player.getBbHeight() / 2, 0);

        // Calculate the relative hit position from player center
        Vec3 relativeHitPos = hitPosition.subtract(playerPos);

        // Rotate the hit position based on player's orientation to get position in player's local coordinate system
        // This effectively "unrotates" the hit position as if the player was facing positive Z
        double rotatedX =
            relativeHitPos.x * Math.cos(playerYaw) +
            relativeHitPos.z * Math.sin(playerYaw);

        // Minecraft uses a left-handed coordinate system where:
        // - Player facing positive Z: positive X is to the left, negative X is to the right
        // - This is because yaw=0 means looking at positive Z, and yaw increases counter-clockwise
        // - So after our rotation transformation, negative rotatedX is RIGHT side, positive is LEFT side
        if (rotatedX < 0) {
            LOGGER.debug("Hit on right side: rotatedX = " + rotatedX);
            return BodySide.RIGHT;
        } else {
            LOGGER.debug("Hit on left side: rotatedX = " + rotatedX);
            return BodySide.LEFT;
        }
    }

    /**
     * Determines if a hit on the torso should be considered an arm hit,
     * and if so, which arm (left or right) was hit.
     *
     * This creates virtual arm hitboxes by considering hits near the edges
     * of the torso hitbox as arm hits, based on the player's orientation.
     *
     * @param player The player that was hit
     * @param hitPosition The position where the ray hit the torso hitbox
     * @param torsoHitbox The torso hitbox AABB
     * @return LEFT or RIGHT enum value if arm was hit, null if central torso
     */
    private static BodySide determineArmHitSide(
        Player player,
        Vec3 hitPosition,
        AABB torsoHitbox
    ) {
        // Get player rotation (yaw) in radians
        float playerYaw = (float) Math.toRadians(player.getYRot());

        // Get player position (center point)
        Vec3 playerPos = player.position();

        // Calculate relative position of the hit within the torso hitbox (0-1 range)
        double relX =
            (hitPosition.x - torsoHitbox.minX) /
            (torsoHitbox.maxX - torsoHitbox.minX);
        double relZ =
            (hitPosition.z - torsoHitbox.minZ) /
            (torsoHitbox.maxZ - torsoHitbox.minZ);

        // Convert to -0.5 to 0.5 range (center of hitbox is 0,0)
        relX = relX - 0.5;
        relZ = relZ - 0.5;

        // Rotate the hit position based on player's orientation to get position in player's local coordinate system
        double rotatedX =
            relX * Math.cos(playerYaw) + relZ * Math.sin(playerYaw);

        // Use relative coordinates within the hitbox for better precision
        double armThreshold = 0.35; // Anything beyond 35% from center (each direction) is considered arm

        // Debug info for hit location
        LOGGER.debug(
            "Torso hit relative position: [" +
            relX +
            ", " +
            relZ +
            "], rotated: " +
            rotatedX
        );

        // Check if the hit is on the edge of the torso (arms)
        if (rotatedX < -armThreshold) {
            LOGGER.debug("Hit on right arm: rotatedX = " + rotatedX);
            return BodySide.RIGHT;
        } else if (rotatedX > armThreshold) {
            LOGGER.debug("Hit on left arm: rotatedX = " + rotatedX);
            return BodySide.LEFT;
        } else {
            LOGGER.debug("Hit on central torso: rotatedX = " + rotatedX);
            return null; // Central torso hit, not an arm
        }
    }

    /**
     * Gets a string representation of a hit location for debugging purposes
     *
     * @param hitPosition The position of the hit
     * @param player The player entity
     * @return A string describing the hit position relative to the player
     */
    private static String getHitLocationString(
        Vec3 hitPosition,
        Player player
    ) {
        Vec3 playerPos = player.position();
        Vec3 relativePos = hitPosition.subtract(playerPos);

        return String.format(
            "Hit at [%.2f, %.2f, %.2f] relative to player at [%.2f, %.2f, %.2f] (relative: [%.2f, %.2f, %.2f])",
            hitPosition.x,
            hitPosition.y,
            hitPosition.z,
            playerPos.x,
            playerPos.y,
            playerPos.z,
            relativePos.x,
            relativePos.y,
            relativePos.z
        );
    }

    /**
     * Applies a damage modifier based on which body part was hit
     *
     * @param event The damage event
     * @param originalDamage The original damage amount
     * @param multiplier The multiplier to apply to the damage
     */
    private static void applyDamageModifier(
        LivingDamageEvent.Post event,
        float originalDamage,
        float multiplier
    ) {
        float modifiedDamage = originalDamage * multiplier;
        LOGGER.debug(
            "Original damage: " +
            originalDamage +
            ", Modified damage: " +
            modifiedDamage +
            " (x" +
            multiplier +
            ")"
        );

        // In Post event we can't modify the damage directly, but we can apply additional effects
        // In a PreEvent this would be: event.setAmount(modifiedDamage);

        // For demonstration, we'll log the modified damage that would be applied
        if (multiplier > 1.0F) {
            LOGGER.info(
                "Critical hit! Damage would be increased to: " + modifiedDamage
            );
        } else if (multiplier < 1.0F) {
            LOGGER.info(
                "Glancing hit! Damage would be reduced to: " + modifiedDamage
            );
        }
    }
}
