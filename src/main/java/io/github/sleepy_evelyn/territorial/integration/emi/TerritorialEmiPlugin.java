package io.github.sleepy_evelyn.territorial.integration.emi;

import dev.emi.emi.api.EmiPlugin;
import dev.emi.emi.api.EmiRegistry;
import dev.emi.emi.api.recipe.EmiRecipeCategory;
import dev.emi.emi.api.render.EmiTexture;
import dev.emi.emi.api.stack.EmiStack;
import io.github.sleepy_evelyn.territorial.Territorial;
import io.github.sleepy_evelyn.territorial.api.registry.TerritorialRegistries;
import net.minecraft.block.Blocks;

public class TerritorialEmiPlugin implements EmiPlugin {

	private static final EmiTexture NETHER_PORTAL_EMI_TEXTURE = new EmiTexture(
		Territorial.id("textures/gui/emi/mini_portal.png"),
		0, 0, 16, 16, 16, 16, 16, 16);

    public static final EmiRecipeCategory PORTAL = new EmiRecipeCategory(
		Territorial.id("portal"), NETHER_PORTAL_EMI_TEXTURE);

    @Override
    public void register(EmiRegistry registry) {
        registry.addCategory(PORTAL);
        registry.addWorkstation(PORTAL, EmiStack.of(Blocks.NETHER_PORTAL));
		TerritorialRegistries.FAKE_PORTAL_RECIPE_REGISTRY.forEach(
			fakePortalRecipe -> registry.addRecipe(new PortalEmiRecipe(fakePortalRecipe))
		);
    }
}
