package org.styly.efm.rederer;

import org.styly.efm.item.gpnvg;
import org.styly.efm.model.GPNVG;
import org.styly.efm.model.GPNVG_ST;
import software.bernie.geckolib.renderer.GeoArmorRenderer;

public class GPNVG_Renderer_ST extends  GeoArmorRenderer<gpnvg> {


        private static final org.styly.efm.model.GPNVG_ST GPNVG_ST= new GPNVG_ST();

        public GPNVG_Renderer_ST() {
            super(GPNVG_ST);
        }

}
