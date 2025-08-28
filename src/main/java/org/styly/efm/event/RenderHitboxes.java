package org.styly.efm.event;

import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import java.util.List;
import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.debug.DebugRenderer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RenderLevelStageEvent;
import org.styly.efm.EFM;
import org.styly.efm.health.PlayerHitboxes;

@EventBusSubscriber(
    modid = EFM.MODID,
    bus = EventBusSubscriber.Bus.GAME,
    value = Dist.CLIENT
)
public class RenderHitboxes {

    @SubscribeEvent
    public static void onRenderLevelStage(RenderLevelStageEvent event) {
        if (
            event.getStage() !=
            RenderLevelStageEvent.Stage.AFTER_TRANSLUCENT_BLOCKS
        ) return;

        PoseStack poseStack = event.getPoseStack();
        MultiBufferSource.BufferSource bufferSource = Minecraft.getInstance()
            .renderBuffers()
            .bufferSource();
        Minecraft mc = Minecraft.getInstance();
        LocalPlayer player = mc.player;

        assert player != null;
        Entity entity = event.getCamera().getEntity();

        // Get camera position for coordinate adjustment
        double camX = event.getCamera().getPosition().x;
        double camY = event.getCamera().getPosition().y;
        double camZ = event.getCamera().getPosition().z;

        // Save current matrix state
        poseStack.pushPose();

        VertexConsumer vertexconsumer = bufferSource.getBuffer(
            RenderType.lines()
        );

        // Render head hitbox (red)
        AABB headHitbox = PlayerHitboxes.getHeadHitbox(entity);
        AABB adjustedHeadHitbox = headHitbox.move(-camX, -camY, -camZ);
        LevelRenderer.renderLineBox(
            poseStack,
            vertexconsumer,
            adjustedHeadHitbox,
            1.0F, // Red
            0.0F,
            0.0F,
            1.0F
        );

        // Render torso hitbox (green)
        AABB torsoHitbox = PlayerHitboxes.getTorsoHitbox(entity);
        AABB adjustedTorsoHitbox = torsoHitbox.move(-camX, -camY, -camZ);
        LevelRenderer.renderLineBox(
            poseStack,
            vertexconsumer,
            adjustedTorsoHitbox,
            0.0F,
            1.0F, // Green
            0.0F,
            1.0F
        );

        // Render legs hitbox (blue)
        AABB legsHitbox = PlayerHitboxes.getLegsHitbox(entity);
        AABB adjustedLegsHitbox = legsHitbox.move(-camX, -camY, -camZ);
        LevelRenderer.renderLineBox(
            poseStack,
            vertexconsumer,
            adjustedLegsHitbox,
            0.0F,
            0.0F,
            1.0F, // Blue
            1.0F
        );

        // Render feet hitbox (yellow)

        // Restore matrix state
        poseStack.popPose();

        bufferSource.endBatch(RenderType.lines());
    }
}
