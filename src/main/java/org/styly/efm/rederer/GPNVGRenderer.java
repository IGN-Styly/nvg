package org.styly.efm.rederer;

import org.styly.efm.item.gpnvg;
import org.styly.efm.model.GPNVG;
import software.bernie.geckolib.renderer.GeoArmorRenderer;

public class GPNVGRenderer extends GeoArmorRenderer<gpnvg> {
    private static final org.styly.efm.model.GPNVG GPNVG = new GPNVG();

    public GPNVGRenderer() {
        super(GPNVG);
    }
}
