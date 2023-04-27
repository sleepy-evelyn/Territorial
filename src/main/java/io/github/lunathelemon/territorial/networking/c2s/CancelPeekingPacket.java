package io.github.lunathelemon.territorial.networking.c2s;

import io.github.lunathelemon.territorial.Territorial;
import io.github.lunathelemon.territorial.component.TerritorialComponents;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;

public class CancelPeekingPacket extends C2SPacket {

    public static final Identifier ID = new Identifier(Territorial.MOD_ID, "cancel_peeking_packet");

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
        var peekingComponent = player.getComponent(TerritorialComponents.PEEKING_EYE);
        var boundBlockEntity = peekingComponent.getBoundBlockEntity();

        if(boundBlockEntity != null)
            boundBlockEntity.removeBoundEntity(player);
        else
            peekingComponent.stopPeeking();
    }
}
