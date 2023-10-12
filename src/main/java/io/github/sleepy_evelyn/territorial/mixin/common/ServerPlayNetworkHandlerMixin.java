package io.github.sleepy_evelyn.territorial.mixin.common;

import io.github.sleepy_evelyn.territorial.screen.CorruptedBeaconScreenHandler;
import net.minecraft.network.packet.c2s.play.BeaconUpdateC2SPacket;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerPlayNetworkHandler.class)
public abstract class ServerPlayNetworkHandlerMixin {
    @Shadow
    public ServerPlayerEntity player;

    @Inject(method="onBeaconUpdate", at = @At("TAIL"))
    public void onUpdateBeacon(BeaconUpdateC2SPacket packet, CallbackInfo ci) {
        if(this.player.currentScreenHandler instanceof CorruptedBeaconScreenHandler)
            ((CorruptedBeaconScreenHandler) this.player.currentScreenHandler).setEffects(packet.getPrimaryEffectId(), packet.getSecondaryEffectId());
    }
}
