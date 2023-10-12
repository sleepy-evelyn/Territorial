package io.github.sleepy_evelyn.territorial.mixin.client;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(InGameHud.class)
public interface InGameHudInvoker {
    /*@Invoker("renderOverlay")
    void renderOverlay(GuiGraphics gui, Identifier texture, float opacity);*/
}
