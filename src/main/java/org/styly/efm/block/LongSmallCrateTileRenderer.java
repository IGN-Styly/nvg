package org.styly.efm.block;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.neoforged.neoforge.common.NeoForge;
import org.styly.efm.registries.OBJModelRegistry;

public class LongSmallCrateTileRenderer implements BlockEntityRenderer<LongSmallCrateTile> {
    public LongSmallCrateTileRenderer(BlockEntityRendererProvider.Context ctx){}

    @Override
    public void render(LongSmallCrateTile pBlockEntity, float pPartialTick, PoseStack pPoseStack, MultiBufferSource pBufferSource, int pPackedLight, int pPackedOverlay) {
        pPoseStack.pushPose();
        //pPoseStack.scale(0.5f,0.5f,0.5f);
        //pPoseStack.translate(0f,1f,0f);

        OBJModelRegistry.longSmallCrate.render(pPoseStack, pBufferSource.getBuffer(RenderType.solid()), RenderType.solid());
    pPoseStack.popPose();
    }
}
