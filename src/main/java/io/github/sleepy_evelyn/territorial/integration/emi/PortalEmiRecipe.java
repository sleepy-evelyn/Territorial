package io.github.sleepy_evelyn.territorial.integration.emi;

import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;
import dev.emi.emi.api.widget.Bounds;
import dev.emi.emi.api.widget.Widget;
import dev.emi.emi.api.widget.WidgetHolder;
import io.github.sleepy_evelyn.territorial.Territorial;
import io.github.sleepy_evelyn.territorial.api.recipe.FakePortalRecipe;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.registry.Registries;
import net.minecraft.screen.PlayerScreenHandler;
import net.minecraft.util.Identifier;
import org.joml.Matrix3f;
import org.joml.Matrix4f;

import java.util.Arrays;


/**
 * Modified version of the EMI support for the Elven Trade Recipe from Botania
 *
 * @author Vazkii (creator)
 * @link <a href="https://github.com/VazkiiMods/Botania/blob/1.19.x/Fabric/src/main/java/vazkii/botania/fabric/integration/emi/ElvenTradeEmiRecipe.java">ElvenTradeEmiRecipe.java</a>
 */
public class PortalEmiRecipe extends TerritorialEmiRecipe {

    private static final Identifier TEXTURE = Territorial.id("textures/gui/portal_recipe_overlay.png");
    private static final String GROUP = "portal";
    private static final Identifier ID = Territorial.id("/" + GROUP);
    private final Identifier portalTextureId;

    public PortalEmiRecipe(FakePortalRecipe infusionRecipe) {
        super(TerritorialEmiPlugin.PORTAL, ID, GROUP);
        this.input = Arrays.stream(infusionRecipe.ingredients()).map(EmiIngredient::of).toList();
        this.output = Arrays.stream(infusionRecipe.output()).map(EmiStack::of).toList();

		Identifier portalTextureId = Registries.BLOCK.getId(infusionRecipe.portalTextureBlock());
		this.portalTextureId = new Identifier(portalTextureId.getNamespace(), "block/" + portalTextureId.getPath());
    }

    @Override
    public int getDisplayHeight() {
		return 90;
	}

    @Override
    public int getDisplayWidth() {
        return 120;
    }

    @Override
    public void addWidgets(WidgetHolder widgets) {
        widgets.add(new BlendTextureWidget(TEXTURE, 10, 5, 71, 75, 20, 19));
        widgets.add(new PortalTradeWidget(12, 22, portalTextureId));
        int sx = 35;
        for (EmiIngredient ing : input) {
            widgets.addSlot(ing, sx, 0).drawBack(false);
            sx += 18;
        }
        sx = 83;
        for (EmiStack stack : output) {
            widgets.addSlot(stack, sx, 40).drawBack(false).recipeContext(this);
            sx += 18;
        }
    }

    private static class PortalTradeWidget extends Widget {
        private final int x, y;
        private final Identifier portalTexture;

        public PortalTradeWidget(int x, int y, Identifier portalTexture) {
            this.x = x;
            this.y = y;
            this.portalTexture = portalTexture;
        }

        @Override
        public Bounds getBounds() {
            return new Bounds(x, y, 0, 0);
        }

        @Override
        public void render(GuiGraphics graphics, int mouseX, int mouseY, float delta) {
			var client = MinecraftClient.getInstance();
            var sprite = client.getSpriteAtlas(PlayerScreenHandler.BLOCK_ATLAS_TEXTURE).apply(portalTexture);
            var immediate = client.getBufferBuilders().getEntityVertexConsumers();
            var consumer = immediate.getBuffer(RenderLayer.getSolid());
            int startX = x, startY = y, stopX = x + 48, stopY = y + 48;

            Matrix4f model = graphics.getMatrices().peek().getModel();
            Matrix3f norm = graphics.getMatrices().peek().getNormal();

            consumer.vertex(model, startX, startY, 0).color(1f, 1f, 1f, 1f).uv(sprite.getMinU(), sprite.getMinV()).light(0xF000F0).normal(norm, 1, 0, 0).next();
            consumer.vertex(model, startX, stopY, 0).color(1f, 1f, 1f, 1f).uv(sprite.getMinU(), sprite.getMaxV()).light(0xF000F0).normal(norm, 1, 0, 0).next();
            consumer.vertex(model, stopX, stopY, 0).color(1f, 1f, 1f, 1f).uv(sprite.getMaxU(), sprite.getMaxV()).light(0xF000F0).normal(norm, 1, 0, 0).next();
            consumer.vertex(model, stopX, startY, 0).color(1f, 1f, 1f, 1f).uv(sprite.getMaxU(), sprite.getMinV()).light(0xF000F0).normal(norm, 1, 0, 0).next();
            immediate.draw();
        }

    }
}
