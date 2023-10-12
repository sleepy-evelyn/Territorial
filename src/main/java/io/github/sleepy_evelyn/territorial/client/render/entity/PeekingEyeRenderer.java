package io.github.sleepy_evelyn.territorial.client.render.entity;

import io.github.sleepy_evelyn.territorial.event.template.RenderEvents;
import io.github.sleepy_evelyn.territorial.util.MovementUtils;
import io.github.sleepy_evelyn.territorial.util.RenderUtils;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderContext;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.model.json.ModelTransformationMode;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.item.Items;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.util.math.Axis;

public class PeekingEyeRenderer {

    private static final int EXIT_MESSAGE_TICK_DURATION = 40;

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
            /*

			var peekingCapability = player.getComponent(TerritorialComponents.PEEKING_EYE);
            if(peekingCapability.isPeeking()) {
                if(!RenderUtils.Shader.isLoaded()) {
                    try {
                        RenderUtils.Shader.load(Territorial.id("shaders/post/" + "peeking_eye" + ".json"));
                    } catch(IOException ignored) {}
                }
                RenderUtils.Shader.render(context.tickDelta());
            }
            */
        }
    }

    private static void handleExitTick(ClientWorld world) {
        var player = MinecraftClient.getInstance().player;
        if(player != null) {
			/*

            var peekingComponent = player.getComponent(TerritorialComponents.PEEKING_EYE);
            if(peekingComponent.isPeeking()) {
                if(Screen.hasAltDown()) {
                    RenderUtils.Shader.close();
                    new CancelPeekingPacket().send();
                }
                if(peekingComponent.getTicksPeeking() < exitMessageTickDuration)
                    player.sendMessage(Text.translatable("text.territorial.overlay.exit_peeking_mode"), true);
            }
            */
        }
    }

    private static boolean beforeRenderPlayer(AbstractClientPlayerEntity player, MatrixStack matrices, VertexConsumerProvider vertexConsumer, float tickDelta, int light) {
       /*
        var peekingComponent = player.getComponent(TerritorialComponents.PEEKING_EYE);
        if(peekingComponent.isPeeking()) {
            render(player, matrices, vertexConsumer, tickDelta, light);
            return true;
        } else
            return false;
        */
		return false;
    }

    private static boolean beforeRenderPlayerArms(MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, AbstractClientPlayerEntity player, ModelPart arm, ModelPart sleeve) {
       return false;
        //return player.getComponent(TerritorialComponents.PEEKING_EYE).isPeeking();
    }

    private static void onGameResized(int width, int height) {
        if(RenderUtils.Shader.shader != null) {
            var client = MinecraftClient.getInstance();
            RenderUtils.Shader.shader.setupDimensions(client.getWindow().getFramebufferWidth(), client.getWindow().getFramebufferHeight());
        }
    }

    private static void render(AbstractClientPlayerEntity player, MatrixStack matrices, VertexConsumerProvider vertexConsumer, float tickDelta, int light) {
        matrices.push();

        matrices.multiply(Axis.Y_NEGATIVE.rotationDegrees(player.getYaw(tickDelta)));
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
                    player.getWorld().addParticle(ParticleTypes.BUBBLE, velX - velocity.x * 0.25, velY - velocity.y * 0.25, velZ - velocity.z * 0.25, velocity.x, velocity.y, velocity.z);
            else
                player.getWorld().addParticle(ParticleTypes.PORTAL, velX - velocity.x * 0.25 + random.nextDouble() * 0.6 - 0.3, velY - velocity.y * 0.25 - 0.5, velZ - velocity.z * 0.25 + random.nextDouble() * 0.6 - 0.3, velocity.x, velocity.y, velocity.z);
        }
    }
}
