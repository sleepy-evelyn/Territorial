package io.github.lunathelemon.territorial.event.client;

import io.github.lunathelemon.territorial.Territorial;
import io.github.lunathelemon.territorial.util.TickCounter;
import net.fabricmc.fabric.api.client.message.v1.ClientReceiveMessageEvents;
import net.fabricmc.fabric.api.client.message.v1.ClientSendMessageEvents;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.fabricmc.fabric.api.message.v1.ServerMessageDecoratorEvent;
import net.fabricmc.fabric.api.message.v1.ServerMessageEvents;
import net.minecraft.client.report.log.ReceivedMessage;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.LiteralTextContent;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.text.TextContent;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import static net.minecraft.client.gui.DrawableHelper.drawTexture;

public class HudRenderHandler implements HudRenderCallback {

    private static final TickCounter enderTicker = new TickCounter(100);

    private static final Identifier POWDER_SNOW_OUTLINE = new Identifier(Territorial.MOD_ID, "textures/misc/ender_effect_dark.png");

    public static void init() {

    }

    @Override
    public void onHudRender(MatrixStack matrixStack, float tickDelta) {
        /*if(enderTicker.test()) {
            var inGameHudInvoker = (InGameHudInvoker) MinecraftClient.getInstance().inGameHud;
            inGameHudInvoker.renderOverlay(matrixStack, POWDER_SNOW_OUTLINE, 0.6F);
        }
        enderTicker.increment();*/
    }
}
