package org.styly.nvg.model;

import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.animatable.GeoAnimatable;
import software.bernie.geckolib.model.GeoModel;

import static org.styly.nvg.Nvg.id;

public class GPNVG extends GeoModel {
    @Override
    public ResourceLocation getModelResource(GeoAnimatable geoAnimatable) {
        return id("geo/gpnvg.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(GeoAnimatable geoAnimatable) {
        return id("textures/model/armor/gpnvg_black.png");
    }

    @Override
    public ResourceLocation getAnimationResource(GeoAnimatable geoAnimatable) {
        return id("animations/gpnvg.animation.json");
    }
}
