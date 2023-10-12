package io.github.sleepy_evelyn.territorial.init;

import io.github.sleepy_evelyn.territorial.Territorial;
import io.github.sleepy_evelyn.territorial.api.recipe.FakePortalRecipe;
import io.github.sleepy_evelyn.territorial.api.registry.TerritorialRegistries;
import io.github.sleepy_evelyn.territorial.recipe.ConditionalRecipes;
import io.github.sleepy_evelyn.territorial.recipe.LensRecipe;
import net.minecraft.block.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.SpecialRecipeSerializer;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;

public final class TerritorialRecipes {

    public static final SpecialRecipeSerializer<ConditionalRecipes.OmniscientObsidian> OMNISCIENT_OBSIDIAN_RECIPE_SERIALIZER
            = new SpecialRecipeSerializer<>((id, category) -> new ConditionalRecipes.OmniscientObsidian(id));
    public static final SpecialRecipeSerializer<LensRecipe> LENS_RECIPE_SERIALIZER
            = register("special_lens", new SpecialRecipeSerializer<>((id, category) -> new LensRecipe(id)));

	public static final FakePortalRecipe CRYING_OBSIDIAN_PORTAL_RECIPE
            = registerFakePortalRecipe("crying_obsidian", new FakePortalRecipe(
		        Blocks.NETHER_PORTAL,
		        new ItemStack[]{ Items.CRYING_OBSIDIAN.getDefaultStack() },
		        Ingredient.ofItems(Items.OBSIDIAN), Ingredient.ofItems(Items.GHAST_TEAR)
            )
	);

    public static void initialize() {
        if(Territorial.getConfig().omniscientObsidianRecipe())
            register( "crafting_omniscient_obsidian", OMNISCIENT_OBSIDIAN_RECIPE_SERIALIZER);
    }

    private static <T extends RecipeSerializer<?>> T register(String id, T recipeSerializer) {
        return Registry.register(Registries.RECIPE_SERIALIZER, Territorial.id(id), recipeSerializer);
    }

	private static <T extends FakePortalRecipe> T registerFakePortalRecipe(String id, T recipe) {
		return Registry.register(TerritorialRegistries.FAKE_PORTAL_RECIPE_REGISTRY, Territorial.id(id), recipe);
	}
}
