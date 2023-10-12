package io.github.sleepy_evelyn.territorial.api.registry;

import io.github.sleepy_evelyn.territorial.api.TerritorialAPI;
import io.github.sleepy_evelyn.territorial.api.recipe.FakePortalRecipe;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;

public final class TerritorialRegistryKeys {

	public static final RegistryKey<Registry<FakePortalRecipe>> FAKE_PORTAL_RECIPE_REGISTRY_KEY
		= RegistryKey.ofRegistry(TerritorialAPI.id("fake_portal_recipe"));
}
