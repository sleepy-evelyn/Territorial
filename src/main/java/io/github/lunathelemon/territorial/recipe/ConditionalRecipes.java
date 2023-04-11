package io.github.lunathelemon.territorial.recipe;

import io.github.lunathelemon.territorial.block.TerritorialBlocks;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.ShapelessRecipe;
import net.minecraft.recipe.book.CraftingRecipeCategory;
import net.minecraft.util.Identifier;
import net.minecraft.util.collection.DefaultedList;

public class ConditionalRecipes {
    public static class OmniscientObsidian extends ShapelessRecipe {
        private static final DefaultedList<Ingredient> input;
        private static final ItemStack output = new ItemStack(TerritorialBlocks.OMNISCIENT_OBSIDIAN);

        public OmniscientObsidian(Identifier id) {
            super(id, "null", CraftingRecipeCategory.MISC, output, input);
        }

        static {
            input = DefaultedList.of();
            input.add(Ingredient.ofItems(Items.CRYING_OBSIDIAN));
            input.add(Ingredient.ofItems(Items.ENDER_EYE));
            input.add(Ingredient.ofItems(Items.NETHERITE_SWORD));
        }
    }
}
