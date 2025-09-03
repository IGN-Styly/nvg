package org.styly.efm.rederer;

import net.minecraft.resources.ResourceLocation;
import org.joml.Matrix4f;
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
    public void beforeProcess(Matrix4f viewModelMatrix) {

    }


    @Override
    public void afterProcess() {

    }
}
