package org.styly.efm.datagen;

import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.client.model.generators.BlockStateProvider;
import net.neoforged.neoforge.client.model.generators.ModelFile;
import net.neoforged.neoforge.client.model.generators.loaders.ObjModelBuilder;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import org.styly.efm.EFM;

public class ModBlockStateProvider extends BlockStateProvider {

    public ModBlockStateProvider(PackOutput output, ExistingFileHelper exFileHelper) {
        super(output, EFM.MODID, exFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {
        //ModelFile LONG_SMALL_CRATE = ObjModelBuilder.begin();
    }
}
