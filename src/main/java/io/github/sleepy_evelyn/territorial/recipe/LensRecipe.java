package io.github.sleepy_evelyn.territorial.recipe;

import io.github.sleepy_evelyn.territorial.init.TerritorialItems;
import io.github.sleepy_evelyn.territorial.init.TerritorialRecipes;
import net.minecraft.inventory.RecipeInputInventory;
import net.minecraft.item.DyeItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.recipe.CraftingCategory;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.SpecialCraftingRecipe;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.util.DyeColor;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;

public class LensRecipe extends SpecialCraftingRecipe {

    private static final Ingredient sparkleModifier;
    private static final Ingredient rainbowModifier;
    private static final Ingredient highlightModifier;
    private static final Ingredient deathModifier;
    private static final Ingredient lightModifier;
    private static final Ingredient strengthModifier;

    public LensRecipe(Identifier id) {
        super(id, CraftingCategory.MISC);
    }

    @Override
    public boolean matches(RecipeInputInventory inventory, World world) {
        boolean colourMod = false;
        boolean sparkleMod = false;
        boolean rainbowMod = false;
        boolean highlightMod = false;
        boolean deathMod = false;
        boolean lightMod = false;
        int strengthMod = 0;

        for(int i = 0; i < inventory.size(); i++) {
            ItemStack itemStack = inventory.getStack(i);

            if(!itemStack.isEmpty()) {
                if(sparkleModifier.test(itemStack)) {
                    if(sparkleMod) return false;
                    sparkleMod = true;
                }
                else if(rainbowModifier.test(itemStack)) {
                    if(rainbowMod) return false;
                    rainbowMod = true;
                }
                else if(highlightModifier.test(itemStack)) {
                    if(highlightMod) return false;
                    highlightMod = true;
                }
                else if(deathModifier.test(itemStack)) {
                    if(deathMod) return false;
                    deathMod = true;
                }
                else if(lightModifier.test(itemStack)) {
                    if(lightMod) return false;
                    lightMod = true;
                }
                else if(strengthModifier.test(itemStack)) {
                    if(strengthMod > 3) return false;
                    strengthMod++;
                }
                else if(itemStack.getItem() instanceof DyeItem) {
                    if(colourMod) return false;
                    colourMod = true;
                }
            }
        }
        return strengthMod > 0 && colourMod;
    }

    @Override
    public ItemStack craft(RecipeInputInventory inventory, DynamicRegistryManager manager) {
        var lensStack = new ItemStack(TerritorialItems.LENS);
        var beamNbt = lensStack.getOrCreateSubNbt("beam");
        int colourId = DyeColor.WHITE.getId();
        int strengthMod = 0;

		for(var invItemStack : inventory.getIngredients()) {
			if (!invItemStack.isEmpty()) {
				if (sparkleModifier.test(invItemStack)) {
					beamNbt.putBoolean("sparkle", true);
				} else if (rainbowModifier.test(invItemStack)) {
					beamNbt.putBoolean("rainbow", true);
				} else if (highlightModifier.test(invItemStack)) {
					beamNbt.putBoolean("highlight", true);
				} else if (deathModifier.test(invItemStack)) {
					beamNbt.putBoolean("death", true);
				} else if (lightModifier.test(invItemStack)) {
					beamNbt.putBoolean("light", true);
				} else if (strengthModifier.test(invItemStack)) {
					strengthMod++;
				} else if (invItemStack.getItem() instanceof DyeItem) {
					colourId = ((DyeItem) invItemStack.getItem()).getColor().getId();
				}
			}
		}
        beamNbt.putInt("colour", colourId);
        if(strengthMod > 0)
			beamNbt.putByte("strength", (byte) (strengthMod - 1));
        return lensStack;
    }

	@Override
    public boolean fits(int width, int height) {
        return width * height >= 2;
    }

    @Override
    public ItemStack getResult(DynamicRegistryManager manager) {
        return new ItemStack(TerritorialItems.LENS);
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return TerritorialRecipes.LENS_RECIPE_SERIALIZER;
    }

    static {
        sparkleModifier = Ingredient.ofItems(Items.EMERALD);
        rainbowModifier = Ingredient.ofItems(Items.DIAMOND);
        highlightModifier = Ingredient.ofItems(Items.GLOW_INK_SAC);
        deathModifier = Ingredient.ofItems(Items.NETHER_STAR);
        lightModifier = Ingredient.ofItems(Items.GLOW_BERRIES);
        strengthModifier = Ingredient.ofItems(Items.AMETHYST_SHARD);
    }
}
