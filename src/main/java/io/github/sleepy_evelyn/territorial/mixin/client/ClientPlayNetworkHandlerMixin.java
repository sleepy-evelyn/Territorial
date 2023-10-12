package io.github.sleepy_evelyn.territorial.mixin.client;

import io.github.sleepy_evelyn.territorial.init.TerritorialStatusEffects;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.network.packet.s2c.play.WorldTimeUpdateS2CPacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPlayNetworkHandler.class)
public class ClientPlayNetworkHandlerMixin {

    @Inject(method="onWorldTimeUpdate", at=@At("HEAD"), cancellable = true)
    void onWorldTimeUpdate(WorldTimeUpdateS2CPacket packet, CallbackInfo ci) {
        var player = MinecraftClient.getInstance().player;

        // Stops the server syncing world time information when the eclipse effect is applied
        if(player != null && player.hasStatusEffect(TerritorialStatusEffects.ECLIPSE_EFFECT))
            ci.cancel();
    }
}
