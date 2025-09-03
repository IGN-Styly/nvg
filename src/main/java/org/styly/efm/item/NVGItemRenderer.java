package org.styly.efm.item;

import org.styly.efm.model.GPNVGITEM;
import software.bernie.geckolib.renderer.GeoItemRenderer;

public class NVGItemRenderer extends GeoItemRenderer<gpnvg> {
    public NVGItemRenderer() {
        super(new GPNVGITEM());
    }
}
