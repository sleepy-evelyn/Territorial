package io.github.lunathelemon.territorial.integration.emi;

import com.mojang.blaze3d.systems.RenderSystem;
import dev.emi.emi.api.widget.TextureWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;

public class BlendTextureWidget extends TextureWidget {

    public BlendTextureWidget(Identifier texture, int x, int y, int width, int height, int u, int v) {
        super(texture, x, y, width, height, u, v);
    }

    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        RenderSystem.enableBlend();
        super.render(matrices, mouseX, mouseY, delta);
        RenderSystem.disableBlend();
    }
}
