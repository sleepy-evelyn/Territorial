package io.github.lunathelemon.territorial.client.render.entity;

import com.mojang.blaze3d.systems.RenderSystem;
import io.github.lunathelemon.territorial.Territorial;
import io.github.lunathelemon.territorial.component.TerritorialComponents;
import io.github.lunathelemon.territorial.event.template.RenderEvents;
import io.github.lunathelemon.territorial.networking.c2s.CancelPeekingPacket;
import io.github.lunathelemon.territorial.util.MovementUtils;
import io.github.lunathelemon.territorial.util.RenderUtils;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderContext;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.WindowEventHandler;
import net.minecraft.client.gl.PostEffectProcessor;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.model.json.ModelTransformationMode;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.item.Items;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.RotationAxis;

import java.io.IOException;

public class PeekingEyeRenderer {

    private static final int exitMessageTickDuration = 40;

    public static void registerEvents() {
        WorldRenderEvents.END.register(PeekingEyeRenderer::renderShaderOverlay);
        ClientTickEvents.START_WORLD_TICK.register(PeekingEyeRenderer::handleExitTick);
        RenderEvents.BEFORE_RENDER_PLAYER.register(PeekingEyeRenderer::beforeRenderPlayer);
        RenderEvents.BEFORE_RENDER_PLAYER_ARMS.register(PeekingEyeRenderer::beforeRenderPlayerArms);
        RenderEvents.GAME_RESIZED.register(PeekingEyeRenderer::onGameResized);
    }

    private static void renderShaderOverlay(WorldRenderContext context) {
        var player = MinecraftClient.getInstance().player;
        if(player != null) {
            var peekingCapability = player.getComponent(TerritorialComponents.PEEKING_EYE);
            if(peekingCapability.isPeeking()) {
                if(!RenderUtils.Shader.isLoaded()) {
                    try {
                        RenderUtils.Shader.load(Territorial.getID("shaders/post/" + "peeking_eye" + ".json"));
                    } catch(IOException ignored) {}
                }
                RenderUtils.Shader.render(context.tickDelta());
            }
        }
    }

    private static void handleExitTick(ClientWorld world) {
        var player = MinecraftClient.getInstance().player;
        if(player != null) {
            var peekingComponent = player.getComponent(TerritorialComponents.PEEKING_EYE);
            if(peekingComponent.isPeeking()) {
                if(Screen.hasAltDown()) {
                    RenderUtils.Shader.close();
                    new CancelPeekingPacket().send();
                }
                if(peekingComponent.getTicksPeeking() < exitMessageTickDuration)
                    player.sendMessage(Text.translatable("text.territorial.overlay.exit_peeking_mode"), true);
            }
        }
    }

    private static boolean beforeRenderPlayer(AbstractClientPlayerEntity player, MatrixStack matrices, VertexConsumerProvider vertexConsumer, float tickDelta, int light) {
        var peekingComponent = player.getComponent(TerritorialComponents.PEEKING_EYE);
        if(peekingComponent.isPeeking()) {
            render(player, matrices, vertexConsumer, tickDelta, light);
            return true;
        } else
            return false;
    }

    private static boolean beforeRenderPlayerArms(MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, AbstractClientPlayerEntity player, ModelPart arm, ModelPart sleeve) {
        return player.getComponent(TerritorialComponents.PEEKING_EYE).isPeeking();
    }

    private static void onGameResized(int width, int height) {
        if(RenderUtils.Shader.shader != null) {
            var client = MinecraftClient.getInstance();
            RenderUtils.Shader.shader.setupDimensions(client.getWindow().getFramebufferWidth(), client.getWindow().getFramebufferHeight());
        }
    }

    private static void render(AbstractClientPlayerEntity player, MatrixStack matrices, VertexConsumerProvider vertexConsumer, float tickDelta, int light) {
        matrices.push();

        matrices.multiply(RotationAxis.NEGATIVE_Y.rotationDegrees(player.getYaw(tickDelta)));
        matrices.scale(0.5f, 0.5f, 0.5f);
        matrices.translate(0, 1.5f, -0.5);
        renderTrail(player);
        MinecraftClient.getInstance().getItemRenderer().renderItem(Items.ENDER_EYE.getDefaultStack(), ModelTransformationMode.HEAD, light, OverlayTexture.DEFAULT_UV, matrices, vertexConsumer, player.getWorld(), 0);

        matrices.pop();
    }

    private static void renderTrail(AbstractClientPlayerEntity player) {
        var velocity = player.getVelocity();
        if (MovementUtils.isMoving(player)) {
            var random = player.getRandom();
            double velX = player.getX() + velocity.x;
            double velY = player.getY() + velocity.y;
            double velZ = player.getZ() + velocity.z;

            if (player.isTouchingWater())
                for (int p = 0; p < 4; ++p)
                    player.world.addParticle(ParticleTypes.BUBBLE, velX - velocity.x * 0.25, velY - velocity.y * 0.25, velZ - velocity.z * 0.25, velocity.x, velocity.y, velocity.z);
            else
                player.world.addParticle(ParticleTypes.PORTAL, velX - velocity.x * 0.25 + random.nextDouble() * 0.6 - 0.3, velY - velocity.y * 0.25 - 0.5, velZ - velocity.z * 0.25 + random.nextDouble() * 0.6 - 0.3, velocity.x, velocity.y, velocity.z);
        }
    }
}
