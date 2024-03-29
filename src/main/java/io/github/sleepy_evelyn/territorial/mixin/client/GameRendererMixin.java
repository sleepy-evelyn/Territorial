package io.github.sleepy_evelyn.territorial.mixin.client;

import io.github.sleepy_evelyn.territorial.event.template.RenderEvents;
import net.minecraft.client.render.GameRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GameRenderer.class)
public abstract class GameRendererMixin {

    @Inject(method = "onResized", at = @At("HEAD"))
    void onGameResized(int width, int height, CallbackInfo ci) {
        RenderEvents.GAME_RESIZED.invoker().onGameResized(width, height);
    }
}
