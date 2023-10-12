package io.github.sleepy_evelyn.territorial.api.recipe;

import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.Ingredient;

/**
 * Creates a fake portal recipe that depicts throwing items into a portal and getting other items back in EMI / REI
 *
 * @param portalTextureBlock Texture for the portal to be displayed in EMI or REI
 * @param output List of recipe outputs
 * @param ingredients Collection of ingredients
 */
public record FakePortalRecipe(Block portalTextureBlock, ItemStack[] output, Ingredient... ingredients) {}
