package org.styly.nvg.registries;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.styly.nvg.Nvg;
import org.styly.nvg.components.nvgtoggle;

public class DataCompReg {
    // Using ExampleRecord(int, boolean)
// Only one Codec and/or StreamCodec should be used below
// Multiple are provided for an example

    // Basic codec
    public static final Codec<nvgtoggle> BASIC_CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    Codec.BOOL.fieldOf("toggle").forGetter(nvgtoggle::toggle)
            ).apply(instance, nvgtoggle::new)
    );
    public static final StreamCodec<ByteBuf, nvgtoggle> BASIC_STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.BOOL, nvgtoggle::toggle,
            nvgtoggle::new
    );


    // In another class
// The specialized DeferredRegister.DataComponents simplifies data component registration and avoids some generic inference issues with the `DataComponentType.Builder` within a `Supplier`
    public static final DeferredRegister.DataComponents REGISTRAR = DeferredRegister.createDataComponents(Nvg.MODID);

    public static final DeferredHolder<DataComponentType<?>, DataComponentType<nvgtoggle>> NVG_TOGGLE = REGISTRAR.registerComponentType(
            "nvg_toggle",
            builder -> builder
                    // The codec to read/write the data to disk
                    .persistent(BASIC_CODEC)
                    // The codec to read/write the data across the network
                    .networkSynchronized(BASIC_STREAM_CODEC)
    );

    public static void register(IEventBus eventBus) {
        REGISTRAR.register(eventBus);
    }

}
