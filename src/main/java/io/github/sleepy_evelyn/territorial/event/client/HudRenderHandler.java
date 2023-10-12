package io.github.sleepy_evelyn.territorial.event.client;

import io.github.sleepy_evelyn.territorial.Territorial;
import io.github.sleepy_evelyn.territorial.util.TickCounter;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.util.Identifier;

public class HudRenderHandler implements HudRenderCallback {

    private static final TickCounter enderTicker = new TickCounter(100);
    private static final Identifier POWDER_SNOW_OUTLINE = Territorial.id("textures/misc/ender_effect_dark.png");

    public static void initialize() {}

	@Override
	public void onHudRender(GuiGraphics drawContext, float tickDelta) {
		/*if(enderTicker.test()) {
            var inGameHudInvoker = (InGameHudInvoker) MinecraftClient.getInstance().inGameHud;
            inGameHudInvoker.renderOverlay(matrixStack, POWDER_SNOW_OUTLINE, 0.6F);
        }
        enderTicker.increment();*/
	}
}
