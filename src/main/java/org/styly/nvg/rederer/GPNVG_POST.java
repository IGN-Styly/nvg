package org.styly.nvg.rederer;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.resources.ResourceLocation;
import team.lodestar.lodestone.systems.postprocess.PostProcessor;

import static org.styly.nvg.Nvg.id;

public class GPNVG_POST extends PostProcessor {
    public static final GPNVG_POST INSTANCE = new GPNVG_POST();

    static {
        INSTANCE.setActive(false);
    }

    @Override
    public ResourceLocation getPostChainLocation() {
        return id("nvg-gp");
    }

    @Override
    public void beforeProcess(PoseStack viewModelStack) {

    }

    @Override
    public void afterProcess() {

    }
}
