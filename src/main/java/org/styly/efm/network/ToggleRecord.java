package org.styly.efm.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;
import org.styly.efm.EFM;

public record ToggleRecord(int typeN, int pressdms) implements CustomPacketPayload {
    public static final CustomPacketPayload.Type<ToggleRecord> TYPE = new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath(EFM.MODID, "nvg"));
    // Each pair of elements defines the stream codec of the element to encode/decode and the getter for the element to encode
    // 'name' will be encoded and decoded as a string
    // 'age' will be encoded and decoded as an integer
    // The final parameter takes in the previous parameters in the order they are provided to construct the payload object
    public static final StreamCodec<ByteBuf, ToggleRecord> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.VAR_INT,
            ToggleRecord::typeN,
            ByteBufCodecs.VAR_INT,
            ToggleRecord::pressdms,
            ToggleRecord::new
    );

    @Override
    public CustomPacketPayload.@NotNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
