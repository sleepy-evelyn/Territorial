package io.github.lunathelemon.territorial.integration.emi;

import dev.emi.emi.api.EmiPlugin;
import dev.emi.emi.api.EmiRegistry;
import dev.emi.emi.api.recipe.EmiRecipeCategory;
import dev.emi.emi.api.stack.EmiStack;
import io.github.lunathelemon.territorial.Territorial;
import net.minecraft.block.Blocks;
import net.minecraft.util.Identifier;

import static io.github.lunathelemon.territorial.api.recipe.PortalCrafting.getInfusionRecipes;

public class TerritorialEmiPlugin implements EmiPlugin {
    public static final EmiRecipeCategory PORTAL = new EmiRecipeCategory(new Identifier(Territorial.MOD_ID, "portal"), EmiStack.of(Blocks.END_PORTAL_FRAME));

    @Override
    public void register(EmiRegistry registry) {
        registry.addCategory(PORTAL);
        registry.addWorkstation(PORTAL, EmiStack.of(Blocks.END_PORTAL_FRAME));
        getInfusionRecipes().forEachRemaining(infusionRecipe -> registry.addRecipe(new PortalEmiRecipe(infusionRecipe)));
    }
}
