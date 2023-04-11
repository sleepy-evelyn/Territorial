package io.github.lunathelemon.territorial.recipe;

import io.github.lunathelemon.territorial.item.TerritorialItems;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.item.DyeItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.SpecialCraftingRecipe;
import net.minecraft.recipe.book.CraftingRecipeCategory;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.util.DyeColor;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;

public class LensRecipe extends SpecialCraftingRecipe {

    private static final Ingredient SPARKLE_MODIFIER;
    private static final Ingredient RAINBOW_MODIFIER;
    private static final Ingredient HIGHLIGHT_MODIFIER;
    private static final Ingredient DEATH_MODIFIER;
    private static final Ingredient LIGHT_MODIFIER;
    private static final Ingredient SRENGTH_MODIFIER;

    public LensRecipe(Identifier id) {
        super(id, CraftingRecipeCategory.MISC);
    }

    @Override
    public boolean matches(CraftingInventory inventory, World world) {
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
                if(SPARKLE_MODIFIER.test(itemStack)) {
                    if(sparkleMod) return false;
                    sparkleMod = true;
                }
                else if(RAINBOW_MODIFIER.test(itemStack)) {
                    if(rainbowMod) return false;
                    rainbowMod = true;
                }
                else if(HIGHLIGHT_MODIFIER.test(itemStack)) {
                    if(highlightMod) return false;
                    highlightMod = true;
                }
                else if(DEATH_MODIFIER.test(itemStack)) {
                    if(deathMod) return false;
                    deathMod = true;
                }
                else if(LIGHT_MODIFIER.test(itemStack)) {
                    if(lightMod) return false;
                    lightMod = true;
                }
                else if(SRENGTH_MODIFIER.test(itemStack)) {
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
    public ItemStack craft(CraftingInventory inventory, DynamicRegistryManager manager) {
        ItemStack itemStack = new ItemStack(TerritorialItems.LENS);
        NbtCompound compound = itemStack.getOrCreateSubNbt("beam");
        int colourId = DyeColor.WHITE.getId();
        int strengthMod = 0;

        for(int i=0; i < inventory.size(); i++) {
            ItemStack invItemStack = inventory.getStack(i);
            if(!invItemStack.isEmpty()) {
                if(SPARKLE_MODIFIER.test(invItemStack)) {
                    compound.putBoolean("sparkle", true);
                }
                else if(RAINBOW_MODIFIER.test(invItemStack)) {
                    compound.putBoolean("rainbow", true);
                }
                else if(HIGHLIGHT_MODIFIER.test(invItemStack)) {
                    compound.putBoolean("highlight", true);
                }
                else if(DEATH_MODIFIER.test(invItemStack)) {
                    compound.putBoolean("death", true);
                }
                else if(LIGHT_MODIFIER.test(invItemStack)) {
                    compound.putBoolean("light", true);
                }
                else if(SRENGTH_MODIFIER.test(invItemStack)) {
                    strengthMod++;
                } else if (invItemStack.getItem() instanceof DyeItem) {
                    colourId = ((DyeItem) invItemStack.getItem()).getColor().getId();
                }
            }
        }
        compound.putInt("colour", colourId);
        if(strengthMod > 0) compound.putByte("strength", (byte) (strengthMod - 1));
        return itemStack;
    }

    @Override
    public boolean fits(int width, int height) {
        return width * height >= 2;
    }

    @Override
    public ItemStack getOutput(DynamicRegistryManager manager) {
        return new ItemStack(TerritorialItems.LENS);
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return TerritorialRecipes.LENS_RECIPE_SERIALIZER;
    }

    static {
        SPARKLE_MODIFIER = Ingredient.ofItems(Items.EMERALD);
        RAINBOW_MODIFIER = Ingredient.ofItems(Items.DIAMOND);
        HIGHLIGHT_MODIFIER = Ingredient.ofItems(Items.GLOW_INK_SAC);
        DEATH_MODIFIER = Ingredient.ofItems(Items.NETHER_STAR);
        LIGHT_MODIFIER = Ingredient.ofItems(Items.GLOW_BERRIES);
        SRENGTH_MODIFIER = Ingredient.ofItems(Items.AMETHYST_SHARD);
    }
}
