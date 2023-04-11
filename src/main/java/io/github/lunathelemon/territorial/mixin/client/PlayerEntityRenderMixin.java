package io.github.lunathelemon.territorial.mixin.client;

import io.github.lunathelemon.territorial.event.template.RenderEvents;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.PlayerEntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerEntityRenderer.class)
public abstract class PlayerEntityRenderMixin {

    @Inject(method="render(Lnet/minecraft/client/network/AbstractClientPlayerEntity;FFLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;I)V", at=@At("HEAD"), cancellable = true)
    public void beforeRender(AbstractClientPlayerEntity player, float f, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumer, int light, CallbackInfo ci) {
        boolean cancelRender = RenderEvents.BEFORE_RENDER_PLAYER.invoker().beforeRenderPlayer(player, matrices, vertexConsumer, tickDelta, light);
        if(cancelRender) ci.cancel();
    }

    @Inject(method="renderArm", at=@At("HEAD"), cancellable = true)
    public void beforeRenderArm(MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, AbstractClientPlayerEntity player, ModelPart arm, ModelPart sleeve, CallbackInfo ci) {
        boolean cancelRender = RenderEvents.BEFORE_RENDER_PLAYER_ARMS.invoker().beforeRenderPlayerArms(matrices, vertexConsumers, light, player, arm, sleeve);
        if(cancelRender) ci.cancel();
    }

}
