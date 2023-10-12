package io.github.sleepy_evelyn.territorial.networking.c2s;

import io.github.sleepy_evelyn.territorial.Territorial;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;

public class CancelPeekingPacket extends C2SPacket {

    public static final Identifier ID = Territorial.id("cancel_peeking_packet");

    @Override
    public void write(PacketByteBuf buf) {}

    @Override
    public void read(PacketByteBuf buf) {}

    @Override
    public Identifier getId() {
        return ID;
    }

    @Override
    void execute(MinecraftServer server, ServerPlayerEntity player, ServerPlayNetworkHandler handler, PacketByteBuf buf, PacketSender responseSender) {
        /*
		var peekingComponent = player.getComponent(TerritorialComponents.PEEKING_EYE);
        var be = peekingComponent.getBoundBlockEntity();

        peekingComponent.stopPeeking();
        if(be != null)
            NetworkingUtils.markDirtyAndSync(be, player.getServerWorld());
            */
    }
}
