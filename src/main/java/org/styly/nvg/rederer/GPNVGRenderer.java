package org.styly.nvg.rederer;

import org.styly.nvg.item.gpnvg;
import org.styly.nvg.model.GPNVG;
import software.bernie.geckolib.renderer.GeoArmorRenderer;

public class GPNVGRenderer extends GeoArmorRenderer<gpnvg> {
    private static final org.styly.nvg.model.GPNVG GPNVG = new GPNVG();

    public GPNVGRenderer() {
        super(GPNVG);
    }
}
