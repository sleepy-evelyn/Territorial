package io.github.lunathelemon.territorial.event.template;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.math.MatrixStack;

public final class RenderEvents {

    public static final Event<BeforeRenderPlayer> BEFORE_RENDER_PLAYER = EventFactory.createArrayBacked(
            BeforeRenderPlayer.class, (callbacks) -> (player, matrices, vertexConsumer, tickDelta, light) -> {
                for(BeforeRenderPlayer callback : callbacks)
                    return callback.beforeRenderPlayer(player, matrices, vertexConsumer, tickDelta, light);
                return false;
            });

    public static final Event<BeforeRenderPlayerArms> BEFORE_RENDER_PLAYER_ARMS = EventFactory.createArrayBacked(
            BeforeRenderPlayerArms.class, (callbacks) -> (matrices, vertexConsumers, light, player, arm, sleeve) -> {
                for(BeforeRenderPlayerArms callback : callbacks)
                    return callback.beforeRenderPlayerArms(matrices, vertexConsumers, light, player, arm, sleeve);
                return false;
            });

    public static final Event<GameResized> GAME_RESIZED = EventFactory.createArrayBacked(
            GameResized.class, (callbacks) -> (width, height) -> {
                for(GameResized callback : callbacks)
                    callback.onGameResized(width, height);
            });

    @FunctionalInterface
    public interface BeforeRenderPlayer {
        /**
         * Called before a player is rendered
         * @return Whether to cancel rendering the rest of the player
         */
        boolean beforeRenderPlayer(AbstractClientPlayerEntity player, MatrixStack matrices, VertexConsumerProvider vertexConsumer, float tickDelta, int light);
    }

    @FunctionalInterface
    public interface BeforeRenderPlayerArms {
        /**
         * Called before a players arm is rendered
         * @return Whether to cancel rendering the rest of the players arm
         */
        boolean beforeRenderPlayerArms(MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, AbstractClientPlayerEntity player, ModelPart arm, ModelPart sleeve);
    }

    @FunctionalInterface
    public interface GameResized {
        void onGameResized(int width, int height);
    }

    private RenderEvents() {}
}
