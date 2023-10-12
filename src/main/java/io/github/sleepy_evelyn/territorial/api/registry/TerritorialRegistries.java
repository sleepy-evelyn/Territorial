package io.github.sleepy_evelyn.territorial.api.registry;

import io.github.sleepy_evelyn.territorial.api.recipe.FakePortalRecipe;
import net.fabricmc.fabric.api.event.registry.FabricRegistryBuilder;
import net.minecraft.registry.Registry;

import static io.github.sleepy_evelyn.territorial.api.registry.TerritorialRegistryKeys.FAKE_PORTAL_RECIPE_REGISTRY_KEY;

public final class TerritorialRegistries {

	public static final Registry<FakePortalRecipe> FAKE_PORTAL_RECIPE_REGISTRY
		= FabricRegistryBuilder.createSimple(FAKE_PORTAL_RECIPE_REGISTRY_KEY).buildAndRegister();
}
