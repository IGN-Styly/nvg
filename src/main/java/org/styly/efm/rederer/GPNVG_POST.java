package org.styly.efm.rederer;

import net.minecraft.resources.ResourceLocation;
import org.joml.Matrix4f;
import team.lodestar.lodestone.systems.postprocess.PostProcessor;

import static org.styly.efm.EFM.id;

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
    public void beforeProcess(Matrix4f viewModelMatrix) {

    }


    @Override
    public void afterProcess() {

    }
}
