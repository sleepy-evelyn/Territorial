package io.github.lunathelemon.territorial.api.recipe;

import io.github.lunathelemon.territorial.api.TerritorialAPI;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.recipe.Ingredient;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class PortalCrafting {

    public static final InfusionRecipe CRYING_OBSIDIAN_INFUSION_RECIPE;
    private static final Map<Identifier, InfusionRecipe> infusionRecipeRegistry = new HashMap<>();

    public static InfusionRecipe register(Identifier id, InfusionRecipe infusionRecipe) {
        return infusionRecipeRegistry.put(id, infusionRecipe);
    }

    public static @Nullable InfusionRecipe getInfusionRecipe(Identifier id) {
        return infusionRecipeRegistry.get(id);
    }
    public static Iterator<InfusionRecipe> getInfusionRecipes() {
        return infusionRecipeRegistry.values().iterator();
    }

    public record InfusionRecipe(Identifier portalTextureId, ItemStack[] output, Ingredient... ingredients) {}

    static {
        CRYING_OBSIDIAN_INFUSION_RECIPE = register(new Identifier(TerritorialAPI.MOD_ID, "territorial"),
                new InfusionRecipe(new Identifier("block/nether_portal"), new ItemStack[]{ Items.CRYING_OBSIDIAN.getDefaultStack() },
                        Ingredient.ofItems(Items.OBSIDIAN), Ingredient.ofItems(Items.GHAST_TEAR)));

    }
}
