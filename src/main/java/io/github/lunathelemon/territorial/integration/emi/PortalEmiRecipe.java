package io.github.lunathelemon.territorial.integration.emi;

import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;
import dev.emi.emi.api.widget.Bounds;
import dev.emi.emi.api.widget.Widget;
import dev.emi.emi.api.widget.WidgetHolder;
import io.github.lunathelemon.territorial.Territorial;
import io.github.lunathelemon.territorial.api.recipe.PortalCrafting;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.screen.PlayerScreenHandler;
import net.minecraft.util.Identifier;
import org.joml.Matrix3f;
import org.joml.Matrix4f;

import java.util.Arrays;


/**
 * Modified version of the Elven Trade Recipe from Botania
 * @author artemisSystem, williewillus, emilyploszaj
 * @link <a href="https://github.com/VazkiiMods/Botania/blob/1.19.x/Fabric/src/main/java/vazkii/botania/fabric/integration/emi/ElvenTradeEmiRecipe.java">ElvenTradeEmiRecipe.java</a>
 */
public class PortalEmiRecipe extends TerritorialEmiRecipe {

    private static final Identifier TEXTURE = new Identifier(Territorial.MOD_ID, "textures/gui/portal_recipe_overlay.png");
    private static final String GROUP = "portal";
    private static final Identifier ID = new Identifier(Territorial.MOD_ID, "/" + GROUP);
    private final Identifier portalTexture;

    public PortalEmiRecipe(PortalCrafting.InfusionRecipe infusionRecipe) {
        super(TerritorialEmiPlugin.PORTAL, ID, GROUP);
        this.input = Arrays.stream(infusionRecipe.ingredients()).map(EmiIngredient::of).toList();
        this.output = Arrays.stream(infusionRecipe.output()).map(EmiStack::of).toList();
        this.portalTexture = infusionRecipe.portalTextureId();
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
        widgets.add(new PortalTradeWidget(12, 22, portalTexture));
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
        public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
            var sprite = MinecraftClient.getInstance().getSpriteAtlas(PlayerScreenHandler.BLOCK_ATLAS_TEXTURE).apply(portalTexture);
            var immediate = MinecraftClient.getInstance().getBufferBuilders().getEntityVertexConsumers();
            var consumer = immediate.getBuffer(RenderLayer.getSolid());
            int startX = x, startY = y, stopX = x + 48, stopY = y + 48;

            Matrix4f pose = matrices.peek().getPositionMatrix();
            Matrix3f norm = matrices.peek().getNormalMatrix();

            consumer.vertex(pose, startX, startY, 0).color(1f, 1f, 1f, 1f).texture(sprite.getMinU(), sprite.getMinV()).light(0xF000F0).normal(norm, 1, 0, 0).next();
            consumer.vertex(pose, startX, stopY, 0).color(1f, 1f, 1f, 1f).texture(sprite.getMinU(), sprite.getMaxV()).light(0xF000F0).normal(norm, 1, 0, 0).next();
            consumer.vertex(pose, stopX, stopY, 0).color(1f, 1f, 1f, 1f).texture(sprite.getMaxU(), sprite.getMaxV()).light(0xF000F0).normal(norm, 1, 0, 0).next();
            consumer.vertex(pose, stopX, startY, 0).color(1f, 1f, 1f, 1f).texture(sprite.getMaxU(), sprite.getMinV()).light(0xF000F0).normal(norm, 1, 0, 0).next();
            immediate.draw();
        }

    }
}