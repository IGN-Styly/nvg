package org.styly.efm.rederer;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.resources.ResourceLocation;
import team.lodestar.lodestone.systems.postprocess.PostProcessor;

import static org.styly.efm.EFM.id;

public class WPNVG_POST extends PostProcessor {
    public static final WPNVG_POST INSTANCE = new WPNVG_POST();

    static {
        INSTANCE.setActive(false);
    }

    @Override
    public ResourceLocation getPostChainLocation() {
        return id("nvg-wp");
    }

    @Override
    public void beforeProcess(PoseStack viewModelStack) {

    }

    @Override
    public void afterProcess() {

    }
}
