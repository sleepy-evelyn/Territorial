package io.github.lunathelemon.territorial.recipe;

import io.github.lunathelemon.territorial.Territorial;
import io.github.lunathelemon.territorial.handler.PortalInfusionRecipeHandler;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.SpecialRecipeSerializer;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public final class TerritorialRecipes {

    public static final SpecialRecipeSerializer<ConditionalRecipes.OmniscientObsidian> OMNISCIENT_OBSIDIAN_RECIPE_SERIALIZER
            = new SpecialRecipeSerializer<>((id, category) -> new ConditionalRecipes.OmniscientObsidian(id));
    public static final SpecialRecipeSerializer<LensRecipe> LENS_RECIPE_SERIALIZER
            = new SpecialRecipeSerializer<>((id, category) -> new LensRecipe(id));

    public static void registerAll() {
        if(Territorial.getConfig().omniscientObsidianRecipe())
            register( "crafting_omniscient_obsidian", OMNISCIENT_OBSIDIAN_RECIPE_SERIALIZER);
        register("crafting_special_lens", LENS_RECIPE_SERIALIZER);
    }

    private static void register(String id, RecipeSerializer<?> recipeSerializer) {
        Registry.register(Registries.RECIPE_SERIALIZER, new Identifier(Territorial.MOD_ID, id), recipeSerializer);
    }
}