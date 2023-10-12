package io.github.sleepy_evelyn.territorial.networking.c2s;

import io.github.sleepy_evelyn.territorial.Territorial;
import io.github.sleepy_evelyn.territorial.init.TerritorialStatusEffects;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;

public final class AddEclipseEffectPacket extends C2SPacket {

    public static final Identifier ID = Territorial.id("add_eclipse_effect_packet");

    private int effectDuration;

    public AddEclipseEffectPacket() {}

    public AddEclipseEffectPacket(int effectDuration) {
        this.effectDuration = effectDuration;
    }

    @Override
    public void execute(MinecraftServer server, ServerPlayerEntity player, ServerPlayNetworkHandler handler, PacketByteBuf buf, PacketSender responseSender) {
        player.addStatusEffect(new StatusEffectInstance(TerritorialStatusEffects.ECLIPSE_EFFECT, effectDuration));
    }

    @Override
    public void write(PacketByteBuf buf) {
        buf.writeInt(effectDuration);
    }

    @Override
    public void read(PacketByteBuf buf) {
        effectDuration = buf.readInt();
    }

    @Override
    public Identifier getId() {
        return ID;
    }
}
